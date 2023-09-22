package nz.ac.auckland.se206.riddle;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.components.StateButton;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

public class RiddleController extends StateButton {

  @FXML private StateButton btnWho = new StateButton();
  @FXML private StateButton btnWhere = new StateButton();
  @FXML private StateButton btnWhen = new StateButton();
  @FXML private Button btnAnswer;
  @FXML private TextArea gptTextArea = new TextArea();

  private ChatCompletionRequest chatCompletionRequest;

  @FXML
  private void initialize() throws ApiProxyException, IOException {
    btnWho.addState("Scientist", "suspects/suspect1.jpg");
    btnWho.addState("Captain", "suspects/suspect2.jpg");
    btnWho.addState("Mechanic", "suspects/suspect3.png");

    btnWhere.addState("Navigation", "rooms/room1.jpg");
    btnWhere.addState("Laboratory", "rooms/room2.jpg");
    btnWhere.addState("Reactor Room", "rooms/room3.jpg");

    btnWhen.addState("Morning", "times/time1.jpg");
    btnWhen.addState("Afternoon", "times/time2.jpg");
    btnWhen.addState("Night", "times/time3.jpg");

    chatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(0.4).setTopP(0.6).setMaxTokens(150);

    generateRiddle();
  }

  @FXML
  private void answerClicked() {
    if (btnWho.getState().equals(GameState.correctSuspect)
        && btnWhere.getState().equals(GameState.correctRoom)
        && btnWhen.getState().equals(GameState.correctTime)) {
      System.out.println("Correct!");
    } else {
      System.out.println("Incorrect!");
    }
  }

  private ChatMessage runGpt(ChatMessage msg) throws ApiProxyException {
    chatCompletionRequest.addMessage(msg);
    ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
    Choice result = chatCompletionResult.getChoices().iterator().next();
    chatCompletionRequest.addMessage(result.getChatMessage());
    return result.getChatMessage();
  }

  private void generateRiddle() throws ApiProxyException, IOException {
    String riddle = GptPromptEngineering.getRiddle();
    ChatMessage msg = new ChatMessage("user", riddle);

    Thread runThread =
        new Thread(
            () -> {
              try {
                ChatMessage response = runGpt(msg);
                gptTextArea.appendText(response.getContent());
              } catch (ApiProxyException e) {
                e.printStackTrace();
              }
            });
    runThread.setDaemon(true);
    runThread.start();
  }

  public void disableButton() {
    btnAnswer.setDisable(false);
  }

  public void enableButton() {
    btnAnswer.setDisable(true);
  }
}