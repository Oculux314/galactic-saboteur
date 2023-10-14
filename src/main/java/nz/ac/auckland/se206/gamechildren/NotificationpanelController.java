package nz.ac.auckland.se206.gamechildren;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.TextArea;
import javafx.util.Duration;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.misc.TaggedThread;

public class NotificationpanelController {

  @FXML private TextArea gptTextArea;
  @FXML private Group grpTextArea;

  private String notification;
  private String gamestate;
  private RiddleController riddleController;
  private ChatCompletionRequest chatCompletionRequest;
  private TranslateTransition slideInTransition;
  private PauseTransition pauseTransition;
  private TranslateTransition slideOutTransition;
  private Queue<String> notificationQueue = new LinkedList<>();
  private boolean isTransitioning = false;

  public void initialize() throws ApiProxyException, IOException {
    chatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(0.4).setTopP(0.6).setMaxTokens(50);
    riddleController = new RiddleController();
    gptTextArea.setWrapText(true);
  }

  public void generateNotification(Boolean timeWarning, Integer timeLeft) {
    String newNotification;
    if (timeWarning) {
      newNotification = GptPromptEngineering.getTimeWarning(timeLeft);
    } else {
      newNotification = GptPromptEngineering.getNotification();
    }

    notificationQueue.add(newNotification);

    if (!isTransitioning) {
      processNextNotification();
    }
  }

  private void processNextNotification() {
    if (!notificationQueue.isEmpty()) {
      String nextNotification = notificationQueue.poll();
      ChatMessage msg = new ChatMessage("user", nextNotification);

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

      isTransitioning = true;
      runThread.start();
    }
  }

  private void buildText(String response) {
    gptTextArea.setText(response);
    transition();
  }

  private void transition() {
    // Create the slide-in animation
    slideInTransition = new TranslateTransition(Duration.seconds(1), gptTextArea);
    slideInTransition.setFromX(0); // Start off-screen
    slideInTransition.setToX(gptTextArea.getLayoutBounds().getWidth());

    // Create a pause transition for 5 seconds
    pauseTransition = new PauseTransition(Duration.seconds(5));
    pauseTransition.setOnFinished(
        event -> {
          slideOutTransition = new TranslateTransition(Duration.seconds(1), gptTextArea);
          slideOutTransition.setFromX(gptTextArea.getLayoutBounds().getWidth());
          slideOutTransition.setToX(0); // Move off-screen to the left
          slideOutTransition.setOnFinished(
              event2 -> {
                gptTextArea.setTranslateX(0); // Hide it off-screen
                isTransitioning = false;
                processNextNotification();
              });
          slideOutTransition.play();
        });

    // Start the slide-in animation
    slideInTransition.play();
    pauseTransition.play();
  }
}
