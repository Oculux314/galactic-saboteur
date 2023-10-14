package nz.ac.auckland.se206.gamechildren;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.misc.GameState;
import nz.ac.auckland.se206.misc.TaggedThread;

public class NotificationpanelController {

  @FXML private TextArea gptTextArea;

  private String notification;
  private String gamestate;
  private RiddleController riddleController;
  private ChatCompletionRequest chatCompletionRequest;

  public void initialize() throws ApiProxyException, IOException {
    chatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(0.4).setTopP(0.6).setMaxTokens(50);
    riddleController = new RiddleController();
    generateNotification(false, null);
  }

  public void generateNotification(Boolean timeWarning, Integer timeLeft) {
    if (timeWarning) {
      notification = GptPromptEngineering.getTimeWarning(timeLeft);
    } else {
      notification = GptPromptEngineering.getNotification();
    }
    ChatMessage msg = new ChatMessage("user", notification);

    TaggedThread runThread =
        new TaggedThread(
            () -> {
              try {
                ChatMessage response = riddleController.runGpt(msg, chatCompletionRequest);
                buildText(response.getContent());
              } catch (ApiProxyException e) {
                e.printStackTrace();
              }
            });

    runThread.start();
  }

  private void buildText(String response) {
    String[] words = response.split(" ");
    StringBuilder output = new StringBuilder();

    int wordCount = 0;

    for (String word : words) {
      output.append(word).append(" ");
      wordCount++;
      if (wordCount >= 10) {
        output.append("\n     ");
        wordCount = 0;
      }
    }
    gptTextArea.setText("   " + output.toString());
    System.out.println(GameState.cluesFound);
  }
  
}
