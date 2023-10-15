package nz.ac.auckland.se206.gamechildren;

import java.io.IOException;
import javafx.fxml.FXML;
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

public class RiddleController implements RootPair.Controller {

  @FXML private StateButton btnWho;
  @FXML private StateButton btnWhere;
  @FXML private StateButton btnWhen;
  @FXML private AnimatedButton btnAnswer;
  @FXML private TextArea gptTextArea;

  private ChatCompletionRequest riddleChatCompletionRequest;

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

  @Override
  public void onLoad() {
    if (GameState.cluesFound) {
      enableButton();
    } else {
      disableButton(); // To prevent accidentally submitting
    }
  }

  @FXML
  private void answerClicked() {
    EndController endController = ((EndController) App.getScreen(Screen.Name.END).getController());

    if (isCorrectRiddleCombination()) {
      endController.showEndOnWin();
    } else {
      endController.showEndOnLose();
    }
  }

  private boolean isCorrectRiddleCombination() {
    return btnWho.getState().equals(GameState.correctSuspect)
        && btnWhere.getState().equals(GameState.correctRoom)
        && btnWhen.getState().equals(GameState.correctTime);
  }

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
                gptTextArea.appendText(response.getContent());
              } catch (ApiProxyException e) {
                e.printStackTrace();
              }
            });

    // start the thread
    runThread.start();
  }

  public void disableButton() {
    btnAnswer.setDisable(true);
  }

  public void enableButton() {
    btnAnswer.setDisable(false);
  }
}
