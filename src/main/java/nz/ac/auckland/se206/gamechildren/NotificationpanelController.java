package nz.ac.auckland.se206.gamechildren;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.misc.GameState;
import nz.ac.auckland.se206.misc.TaggedThread;
import javafx.animation.TranslateTransition;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import java.util.Queue;
import java.util.LinkedList;


public class NotificationpanelController {

  @FXML private TextArea gptTextArea;

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
    generateNotification(false, null);
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

      TaggedThread runThread = new TaggedThread(() -> {
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
    String indentedResponse = "   " + response.replace("\n", "\n   ");
    gptTextArea.setText(response);
    transition();
  }
  
  private void transition() {
    // Create the slide-in animation
    slideInTransition = new TranslateTransition(Duration.seconds(1), gptTextArea);
    slideInTransition.setFromX(-gptTextArea.getLayoutBounds().getWidth()); // Start off-screen
    slideInTransition.setToX(0);

    // Create a pause transition for 5 seconds
    pauseTransition = new PauseTransition(Duration.seconds(5));
    pauseTransition.setOnFinished(event -> {
      slideOutTransition = new TranslateTransition(Duration.seconds(1), gptTextArea);
      slideOutTransition.setToX(-gptTextArea.getLayoutBounds().getWidth()); // Move off-screen to the left
      slideOutTransition.setOnFinished(event2 -> {
        gptTextArea.setTranslateX(-gptTextArea.getLayoutBounds().getWidth()); // Hide it off-screen
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
