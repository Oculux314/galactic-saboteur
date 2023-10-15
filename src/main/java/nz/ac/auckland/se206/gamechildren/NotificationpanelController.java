package nz.ac.auckland.se206.gamechildren;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.misc.TaggedThread;

public class NotificationpanelController {

  @FXML private TextArea gptTextArea;
  @FXML private Rectangle recHide;

  private String notification;
  private String gamestate;
  private RiddleController riddleController;
  private ChatCompletionRequest chatCompletionRequest;
  private TranslateTransition slideInTransition;
  private PauseTransition pauseTransition;
  private TranslateTransition slideOutTransition;
  private Queue<String> notificationQueue = new LinkedList<>();
  private boolean isTransitioning = false;
  private int notificationsgiven = 0;

  public void initialize() throws ApiProxyException, IOException {
    chatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(0.4).setTopP(0.6).setMaxTokens(50);
    riddleController = new RiddleController();
    recHide.setVisible(false);
  }

  /**
   * Called when wanting to generate a notification.
   *
   * @param timeWarning whether the notification is a time warning
   * @param timeLeft the time left
   * @return
   */
  public void generateNotification(Boolean timeWarning, Integer timeLeft) {
    String newNotification;

    // If the notification is a time warning, get the time warning message
    if (timeWarning) {
      newNotification = GptPromptEngineering.getTimeWarning(timeLeft);
    } else {
      newNotification = GptPromptEngineering.getNotification();
    }

    notificationQueue.add(newNotification);

    // If there is no notification in progress, process the next notification
    if (!isTransitioning) {
      processNextNotification();
    }
    notificationsgiven++;
  }

  /**
   * Called when wanting to generate a notification that is not a time warning
   *
   * @param
   * @return
   */
  public void generateNotification() {
    generateNotification(false, null);
  }

  /**
   * Called when wanting to generate a notification with a specific message
   *
   * @param notification The notification message.
   * @return
   */
  public void generateNotification(String notification) {
    notificationQueue.add(notification);

    if (!isTransitioning) {
      processNextNotification();
    }
    notificationsgiven++;
  }

  /**
   * Processes the queue of notifications. Called when a notification is finished.
   *
   * @param
   */
  private void processNextNotification() {
    if (!notificationQueue.isEmpty()) {
      String nextNotification = notificationQueue.poll();
      ChatMessage msg = new ChatMessage("user", nextNotification);

      // Create a thread to run the GPT-3.5 model
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

  /**
   * Builds the text for the notification.
   *
   * @param response The response from GPT-3.5.
   * @return
   */
  private void buildText(String response) {
    gptTextArea.setText(response);
    transition();
  }

  /**
   * Transitions the notification in and out.
   *
   * @param
   * @return
   */
  private void transition() {
    recHide.setVisible(true);
    // Create the slide-in animation
    slideInTransition = new TranslateTransition(Duration.seconds(1), gptTextArea);
    slideInTransition.setFromX(0); // Start off-screen
    slideInTransition.setToX(gptTextArea.getLayoutBounds().getWidth() + 100);

    // Create a pause transition for 5 seconds
    pauseTransition = new PauseTransition(Duration.seconds(5));
    pauseTransition.setOnFinished(
        event -> {
          slideOutTransition = new TranslateTransition(Duration.seconds(1), gptTextArea);
          slideOutTransition.setFromX(gptTextArea.getLayoutBounds().getWidth() + 100);
          slideOutTransition.setToX(0); // Move off-screen to the left
          slideOutTransition.setOnFinished(
              event2 -> {
                gptTextArea.setTranslateX(0); // Hide it off-screen
                isTransitioning = false;
                processNextNotification();
                recHide.setVisible(false);
              });
          slideOutTransition.play();
        });

    // Start the slide-in animation
    slideInTransition.play();
    pauseTransition.play();
  }

  /**
   * Returns whether a notification is in progress.
   *
   * @param
   * @return
   */
  public boolean isNotificationInProgress() {
    return isTransitioning;
  }

  /**
   * Called when wanting to generate a notification that is time dependent
   *
   * @param initialSeconds The initial seconds.
   * @param secondsLeft The seconds left.
   * @return
   */
  public void generateTimeDependentNotification(Integer initialSeconds, Integer secondsLeft) {
    if (secondsLeft == initialSeconds / 2 || secondsLeft == 60 || secondsLeft == 15 || secondsLeft == 5) {
      generateNotification(true, secondsLeft);
    } else if (secondsLeft == initialSeconds - 1) {
      generateNotification();
    } else if (secondsLeft == 3 * initialSeconds / 4 || secondsLeft == initialSeconds / 4) {
      if (!isNotificationInProgress()) {
        selectGeneralNotification();
      }
    }
  }

  /**
   * Selects a general notification.
   *
   * @param
   * @return
   */
  private void selectGeneralNotification() {
    int random = (int) (Math.random() * 3);
    switch (random) {
      case 0:
        generateNotification("Tell the user each room has one problem to solve.");
        break;
      case 1:
        generateNotification("Tell the user they can pan and zoom on their helmet overlay.");
        break;
      case 2:
        generateNotification("Tell the user they can talk to the astronauts for help.");
        break;
    }
  }
}
