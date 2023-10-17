package nz.ac.auckland.se206.gpt;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.misc.GameState;
import nz.ac.auckland.se206.misc.TaggedThread;
import nz.ac.auckland.se206.screens.GameController;
import nz.ac.auckland.se206.screens.Screen;

/**
 * The Assistant class provides an interface for interacting with the GPT-3.5 model API and managing
 * chat messages.
 */
public class Assistant {

  /**
   * A specialized Task for handling API calls in the Assistant class. It manages the request, UI
   * effects, and API call execution.
   */
  public class ApiCallTask extends Task<Void> {
    private ChatCompletionRequest request;

    /**
     * Constructs an ApiCallTask with the provided request.
     *
     * @param request The request for the API call.
     */
    public ApiCallTask(ChatCompletionRequest request) {
      this.request = request;
    }

    /**
     * Handles the process of the API call, including managing UI effects and responses while the
     * call is executed.
     *
     * @return Returns null when the task completes successfully.
     * @throws Exception Throws an exception if an error occurs during the API call process.
     */
    @Override
    protected Void call() throws Exception {
      // Wait until previous responses are processed
      while (isWaitingForResponse) {
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          return null;
        }
      }

      // Set up UI effects and disable user response during API call
      ImageView imageView = narrationBox.getThinkingImage();
      narrationBox.disableUserResponse();
      narrationBox.showQuestionmarks();
      isWaitingForResponse = true;

      // Start the loading effect thread
      TaggedThread loadEffectThread = new TaggedThread(narrationBox.new LoadEffectTask());
      loadEffectThread.start();

      // Start the fade in and out animation
      AtomicBoolean animationRunning = new AtomicBoolean(true);
      TaggedThread animationThread =
          new TaggedThread(
              () -> {
                while (animationRunning.get()) {
                  Platform.runLater(
                      () -> {
                        FadeTransition fadeInOut =
                            new FadeTransition(Duration.seconds(1), imageView);
                        fadeInOut.setFromValue(1.0);
                        fadeInOut.setToValue(0.0);
                        fadeInOut.setAutoReverse(true);
                        fadeInOut.setCycleCount(FadeTransition.INDEFINITE);
                        fadeInOut.play();
                      });

                  try {
                    Thread.sleep(1000); // Adjust the timing here
                  } catch (InterruptedException e) {
                    break;
                  }
                }
              });

      // Start the animation thread as a daemon thread
      animationThread.setDaemon(true);
      animationThread.start();

      // Execute the API call and manage the response
      try {
        ChatCompletionResult result = request.execute();
        ChatMessage response = result.getChoice(0).getChatMessage();
        addChatMessage(response);
        responseText = response.getContent();
      } catch (ApiProxyException e) {
        responseText = GptPromptEngineering.getInternetErrorMessage();
        addChatMessage("assistant", responseText);
      }

      // Stop the animation thread, enable user response, and hide question marks
      animationRunning.set(false);
      narrationBox.enableUserResponse();
      narrationBox.hideQuestionmarks();
      isWaitingForResponse = false;

      return null;
    }
  }

  private NarrationBox narrationBox;
  private List<ChatMessage> chatMessages = new ArrayList<>();
  private ChatMessage systemMessage = null;
  private boolean onlySystemMessage = false;
  private String responseText = "";
  private boolean isWaitingForResponse = false;
  private String job;

  /**
   * Constructs an Assistant with a provided NarrationBox and job title.
   *
   * @param narrationBox The NarrationBox to be associated with the Assistant.
   * @param title The title of the job associated with the Assistant.
   */
  public Assistant(NarrationBox narrationBox, String title) {
    this.narrationBox = narrationBox;
    this.job = title;
    chatMessages.add(
        new ChatMessage(
            "system", GptPromptEngineering.getMainPrompt(narrationBox.getWaitingMessage())));
  }

  /**
   * Retrieves the job title associated with the Assistant.
   *
   * @return The job title associated with the Assistant.
   */
  public String getJob() {
    return job;
  }

  /**
   * Adds a ChatMessage to the list of chat messages.
   *
   * @param message The ChatMessage to be added.
   */
  public void addChatMessage(ChatMessage message) {
    chatMessages.add(message);
  }

  /**
   * Adds a ChatMessage with the specified role and content to the list of chat messages.
   *
   * @param role The role of the message sender.
   * @param content The content of the message.
   */
  public void addChatMessage(String role, String content) {
    chatMessages.add(new ChatMessage(role, content));
  }

  /**
   * Sets a system message with the provided content, indicating whether it's the only system
   * message.
   *
   * @param content The content of the system message.
   */
  public void setSystemMessage(String content) {
    setSystemMessage(content, true);
  }

  /**
   * Sets a system message with the provided content, indicating whether it's the only system
   * message.
   *
   * @param content The content of the system message.
   * @param onlySystemMessage A boolean indicating whether the system message is the only one to be
   *     displayed.
   */
  public void setSystemMessage(String content, boolean onlySystemMessage) {
    this.onlySystemMessage = onlySystemMessage;
    systemMessage = new ChatMessage("system", content);
  }

  /**
   * Executes an API call to the GPT-3.5 model API with the specified parameters and chat messages.
   *
   * @return The TaggedThread representing the execution of the API call.
   */
  private TaggedThread executeApiCall() {
    // Create a request to the GPT-3.5 model API
    ChatCompletionRequest request =
        new ChatCompletionRequest().setTemperature(0.4).setTopP(0.6).setMaxTokens(80);

    // Add the chat messages to the request
    if (!onlySystemMessage) {
      for (ChatMessage message : chatMessages) {
        request.addMessage(message);
      }
    }
    request.addMessage(systemMessage);

    // Create a task to execute the API call
    Task<Void> apiCallTask = new ApiCallTask(request);

    // Create a thread to run the task
    TaggedThread apiCallThread = new TaggedThread(apiCallTask);

    // Start the thread
    apiCallThread.start();

    // Return the thread
    return apiCallThread;
  }

  /**
   * Executes an API call with the provided callback in a separate thread, ensuring the callback is
   * executed on the JavaFX thread after the API call is completed.
   *
   * @param callback The callback to be executed after the API call is completed.
   */
  private void executeApiCallWithCallback(Runnable callback) {
    // Execute API call in a separate tagged thread for concurrency reasons
    TaggedThread apiCallThread =
        new TaggedThread(
            () -> {
              TaggedThread sendMessageThread = executeApiCall();

              // this thread will join the current thread to make sure the API call is completed
              // before the callback is finished
              try {
                sendMessageThread.join();
              } catch (InterruptedException e) {
                return;
              }

              // Run callback on JavaFX thread
              Platform.runLater(
                  () -> {
                    callback.run();
                  });
            });

    // Start the thread
    apiCallThread.start();
  }

  /** Renders the narration box by setting the response text and enabling user response. */
  private void renderNarrationBox() {
    narrationBox.setText(responseText);
    narrationBox.enableUserResponse();
  }

  /**
   * Retrieves the narration box associated with the Assistant.
   *
   * @return The NarrationBox associated with the Assistant.
   */
  public NarrationBox getNarrationBox() {
    return narrationBox;
  }

  /**
   * Responds to the user by setting the user's response in the narration box and calling the
   * 'respondToUser' method.
   *
   * @param text The text response to be set in the narration box.
   */
  public void respondToUser(String text) {
    narrationBox.setUserResponse(text);
    respondToUser();
  }

  /**
   * Responds to the user by processing the user's response and executing the appropriate actions,
   * including adding the user's message to the chat, executing the API call, and updating the hint
   * text.
   */
  public void respondToUser() {
    // Disable user response while waiting for response
    narrationBox.disableUserResponse();

    // Get user message
    String userMessage = narrationBox.getUserResponse();

    // Clear user response
    narrationBox.clearUserResponse();

    // Add user message to chat messages
    if (userMessage.equals("")) {
      narrationBox.enableUserResponse();
      return;
    }

    // Add user message to chat messages
    setSystemMessage(GptPromptEngineering.getUserInteractionPrompt(job), false);
    addChatMessage("user", userMessage);

    // Execute API call and render narration box, update hint text once finished
    executeApiCallWithCallback(
        () -> {
          renderNarrationBox();
          increaseNumberOfHintsAskedIfHintGiven();
          if (GameState.numberOfHintsAsked > 4 && GameState.difficulty == "medium") {
            System.out.println("changing difficulty");
            GameState.difficulty = "hard";
            getGameController()
                .getPopupController()
                .getSuspectController()
                .getHintBtn()
                .setDisable(true);
          }
          getGameController().updateHintText();
        });
  }

  /** Increases the number of hints asked if the hint is mentioned in the narration box's text. */
  private void increaseNumberOfHintsAskedIfHintGiven() {
    String stringToCheck =
        narrationBox.getText().trim(); // Trim any leading or trailing white spaces

    if (stringToCheck.toLowerCase().contains("hint")
        || stringToCheck.toLowerCase().contains("whiteboard")
        || stringToCheck.toLowerCase().contains("letters")
        || stringToCheck.toLowerCase().contains("calendar")
        || stringToCheck.toLowerCase().contains("keyboard")
        || stringToCheck.toLowerCase().contains("colour")
        || stringToCheck.toLowerCase().contains("glitter")
        || stringToCheck.toLowerCase().contains("tool")
        || stringToCheck.toLowerCase().contains("whiteboard")
        || stringToCheck.toLowerCase().contains("navigation")
        || stringToCheck.toLowerCase().contains("rotate")
        || stringToCheck.toLowerCase().contains("counter")
        || stringToCheck.toLowerCase().contains("test")) { // Convert to lowercase before checking
      GameState.numberOfHintsAsked++;
    }
  }

  /**
   * Retrieves the GameController associated with the current App screen.
   *
   * @return The GameController associated with the current App screen.
   */
  private GameController getGameController() {
    return (GameController) App.getScreen(Screen.Name.GAME).getController();
  }

  /**
   * Provides a welcome message to the user by setting the system message and rendering the
   * narration box.
   */
  public void welcome() {
    setSystemMessage(GptPromptEngineering.getWelcomePrompt(job));
    executeApiCallWithCallback(
        () -> {
          renderNarrationBox();
        });
  }

  /**
   * Retrieves the game state of the puzzle based on the specified job.
   *
   * @param job The job title associated with the assistant.
   * @return The game state corresponding to the specified job.
   */
  public String getGameStateOfPuzzle(String job) {
    String gameState = "";

    // return game state depending on what suspect the user is talking to
    if (job == "Spacey's mechanic") { // reactor puzzle
      gameState = GameState.reactorRoomGameState;
    } else if (job == "Spacey's scientist") { // lab puzzle
      gameState = GameState.labRoomGameState;
    } else if (job == "Spacey's captain") { // bridge puzzle
      gameState = GameState.controlRoomGameState;
    }
    return gameState;
  }
}
