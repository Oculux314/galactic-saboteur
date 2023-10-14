package nz.ac.auckland.se206.gamechildren;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.misc.TaggedThread;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import javafx.scene.control.TextArea;
import java.io.IOException;
import nz.ac.auckland.se206.misc.RootPair;
import nz.ac.auckland.se206.gamechildren.RiddleController;


public class NotificationpanelController {

  @FXML private TextArea gptTextArea;

  private String notification;
  private String gamestate;
  private RiddleController riddleController;
  private ChatCompletionRequest chatCompletionRequest;

  public void initialize() throws ApiProxyException, IOException{
  System.out.println("NotificationpanelController initialized");
    chatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(0.4).setTopP(0.6).setMaxTokens(50);

    riddleController = new RiddleController();
    generateNotification();
  }

  private void generateNotification() throws ApiProxyException, IOException {
    String notification = GptPromptEngineering.getNotification();
    ChatMessage msg = new ChatMessage("user", notification);

    TaggedThread runThread =
        new TaggedThread(
            () -> {
              try {
                ChatMessage response = riddleController.runGpt(msg, chatCompletionRequest);
                System.out.println(response.getContent());
                gptTextArea.appendText(response.getContent());
              } catch (ApiProxyException e) {
                e.printStackTrace();
              }
            });

    runThread.start();
  }
}
