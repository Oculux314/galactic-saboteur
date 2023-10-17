package nz.ac.auckland.se206.gamechildren;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.components.AnimatedButton;
import nz.ac.auckland.se206.components.StateButton;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;
import nz.ac.auckland.se206.misc.GameState;
import nz.ac.auckland.se206.misc.RootPair;
import nz.ac.auckland.se206.misc.TaggedThread;
import nz.ac.auckland.se206.screens.EndController;
import nz.ac.auckland.se206.screens.Screen;

/**
 * The RiddleController class is responsible for managing the riddle-related functionality in the
 * application. It handles the user interaction with riddle options, triggers GPT for generating
 * riddles, and manages the state of various buttons accordingly.
 */
public class RiddleController implements RootPair.Controller {

  @FXML private StateButton btnWho;
  @FXML private StateButton btnWhere;
  @FXML private StateButton btnWhen;
  @FXML private AnimatedButton btnAnswer;
  @FXML private Label lblRiddle;
  @FXML private Label lblNote;
  @FXML private Group grpAnswer;

  private ChatCompletionRequest riddleChatCompletionRequest;

  /**
   * Initializes the RiddleController by setting up the initial state of the buttons and the riddle
   * chat completion request. It also generates the initial riddle.
   *
   * @throws ApiProxyException if there is an issue with the API proxy during the execution of the
   *     request.
   * @throws IOException if there is an I/O issue during initialization.
   */
  @FXML
  private void initialize() throws ApiProxyException, IOException {
    // set up the suspect option state button
    btnWho.addState("Scientist", "suspects/suspect1.png");
    btnWho.addState("Captain", "suspects/suspect2.png");
    btnWho.addState("Mechanic", "suspects/suspect3.png");

    // set up the room option state button
    btnWhere.addState("Navigation", "rooms/room1.png");
    btnWhere.addState("Laboratory", "rooms/room2.jpg");
    btnWhere.addState("Reactor Room", "rooms/room3.png");

    // set up the time option state button
    btnWhen.addState("Morning", "times/time1.jpg");
    btnWhen.addState("Afternoon", "times/time2.jpg");
    btnWhen.addState("Night", "times/time3.jpg");

    // set up the riddle
    riddleChatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(0.4).setTopP(0.6).setMaxTokens(75);
    generateRiddle();
  }

  /**
   * Triggered upon the loading of the RiddleController. Checks whether the clues have been found
   * and enables or disables the buttons accordingly.
   */
  @Override
  public void onLoad() {
    if (GameState.cluesFound) {
      showGroupAnswer();
    }
  }

  /**
   * Handles the action when the answer button is clicked. It checks the combination of riddle
   * options selected and displays the end screen accordingly.
   */
  @FXML
  private void answerClicked() {
    EndController endController = ((EndController) App.getScreen(Screen.Name.END).getController());

    if (isCorrectRiddleCombination()) {
      endController.showEndOnWin();
    } else {
      endController.showEndOnLose();
    }
  }

  /**
   * Checks whether the current combination of riddle options matches the correct solution.
   *
   * @return true if the combination is correct, otherwise false.
   */
  private boolean isCorrectRiddleCombination() {
    return btnWho.getState().equals(GameState.correctSuspect)
        && btnWhere.getState().equals(GameState.correctRoom)
        && btnWhen.getState().equals(GameState.correctTime);
  }

  /**
   * Runs the GPT to process the chat message and obtain a response.
   *
   * <p>The method takes in a chat message and a chat completion request, adds the provided message
   * to the request, executes the completion request, retrieves the generated response from the GPT,
   * and returns the response message.
   *
   * @param msg the ChatMessage object representing the message to be processed by GPT.
   * @param chatCompletionRequest the ChatCompletionRequest object containing the request for GPT
   *     completion.
   * @return a ChatMessage object representing the generated response from GPT.
   * @throws ApiProxyException if there is an issue with the API proxy during the execution of the
   *     request.
   */
  public ChatMessage runGpt(ChatMessage msg, ChatCompletionRequest chatCompletionRequest)
      throws ApiProxyException {
    // Add the chat messages to the request
    chatCompletionRequest.addMessage(msg);
    ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
    Choice result = chatCompletionResult.getChoices().iterator().next();
    chatCompletionRequest.addMessage(result.getChatMessage());
    // Return the response
    return result.getChatMessage();
  }

  /**
   * Generates the initial riddle by utilizing the GPT model and updates the text area with the
   * generated riddle.
   *
   * @throws ApiProxyException if there is an issue with the API proxy during the execution of the
   *     request.
   * @throws IOException if there is an I/O issue during the process.
   */
  private void generateRiddle() throws ApiProxyException, IOException {
    // Generate the riddle
    String riddle = GptPromptEngineering.getRiddle();
    ChatMessage msg = new ChatMessage("user", riddle);

    // make a thread to run the gpt
    TaggedThread runThread =
        new TaggedThread(
            () -> {
              try {
                // run the gpt
                ChatMessage response = runGpt(msg, riddleChatCompletionRequest);
                lblRiddle.setText(response.getContent());
              } catch (ApiProxyException e) {
                e.printStackTrace();
              }
            });

    // start the thread
    runThread.start();
  }

  /** Shows the group answer once the user has found all of the clues. */
  private void showGroupAnswer() {
    grpAnswer.setOpacity(1);
    lblNote.setVisible(false);
  }
}
