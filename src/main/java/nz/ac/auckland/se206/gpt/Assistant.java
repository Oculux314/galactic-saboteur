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

public class Assistant {

  public class ApiCallTask extends Task<Void> {
    private ChatCompletionRequest request;

    public ApiCallTask(ChatCompletionRequest request) {
      this.request = request;
    }

    @Override
    protected Void call() throws Exception {
      while (isWaitingForResponse) {
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          return null;
        }
      }

      ImageView imageView = narrationBox.getThinkingImage();

      narrationBox.disableUserResponse();
      narrationBox.showQuestionmarks();

      isWaitingForResponse = true;

      // Modulate ... loading effect in response field
      TaggedThread loadEffectThread = new TaggedThread(narrationBox.new LoadEffectTask());
      loadEffectThread.start();

      // Fade in and out animation
      AtomicBoolean animationRunning = new AtomicBoolean(true); // Flag to track animation status
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

      animationThread.setDaemon(true);
      animationThread.start();

      // API call
      try {
        ChatCompletionResult result = request.execute();
        ChatMessage response = result.getChoice(0).getChatMessage();
        addChatMessage(response);
        responseText = response.getContent();
      } catch (ApiProxyException e) {
        responseText = GptPromptEngineering.getInternetErrorMessage();
        addChatMessage("assistant", responseText);
      }

      animationRunning.set(false); // Stop the animation thread
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

  public Assistant(NarrationBox narrationBox, String title) {
    this.narrationBox = narrationBox;
    this.job = title;
    chatMessages.add(
        new ChatMessage(
            "system", GptPromptEngineering.getMainPrompt(narrationBox.getWaitingMessage())));
  }

  public String getJob() {
    return job;
  }

  public void addChatMessage(ChatMessage message) {
    chatMessages.add(message);
  }

  public void addChatMessage(String role, String content) {
    chatMessages.add(new ChatMessage(role, content));
  }

  public void setSystemMessage(String content) {
    setSystemMessage(content, true);
  }

  public void setSystemMessage(String content, boolean onlySystemMessage) {
    this.onlySystemMessage = onlySystemMessage;
    systemMessage = new ChatMessage("system", content);
  }

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

  private void renderNarrationBox() {
    narrationBox.setText(responseText);
    narrationBox.enableUserResponse();
  }

  public NarrationBox getNarrationBox() {
    return narrationBox;
  }

  public void respondToUser(String text) {
    narrationBox.setUserResponse(text);
    respondToUser();
  }

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
          getGameController().updateHintText();
        });
  }

  private void increaseNumberOfHintsAskedIfHintGiven() {
    String stringToCheck =
        narrationBox.getText().trim(); // Trim any leading or trailing white spaces

    if (stringToCheck.toLowerCase().contains("hint")) { // Convert to lowercase before checking
      GameState.numberOfHintsAsked++;
    }
  }

  private GameController getGameController() {
    return (GameController) App.getScreen(Screen.Name.GAME).getController();
  }

  public void welcome() {
    setSystemMessage(GptPromptEngineering.getWelcomePrompt(job));
    executeApiCallWithCallback(
        () -> {
          renderNarrationBox();
        });
  }

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
