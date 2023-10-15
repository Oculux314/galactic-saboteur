package nz.ac.auckland.se206.gamechildren;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.misc.TaggedThread;

public class NotificationpanelController {

  @FXML private Label gptTextLabel = new Label();
  @FXML private Rectangle recHide;
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
  private int notificationsgiven = 0;
  private boolean holdNotification = false;
  private Timeline holdTimeline;

  public void initialize() throws ApiProxyException, IOException {
    chatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(0.4).setTopP(0.6).setMaxTokens(50);
    riddleController = new RiddleController();
    recHide.setVisible(false);
  }

  /**
   * Called when logo is being hovered.
   *
   * @param event the mouse event
   * @return
   */
  public void onMouseEntered() {
    System.out.println("Mouse entered");
    holdNotification = true;
  }

  /**
   * Called when logo is exited.
   *
   * @param event the mouse event
   * @return
   */
  public void onMouseExited() {
    System.out.println("Mouse exited");
    holdNotification = false;
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
    Platform.runLater(
        () -> {
          // Set label text as response
          gptTextLabel.setText(response);
          transition();
        });
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
    slideInTransition = new TranslateTransition(Duration.seconds(1), grpTextArea);
    slideInTransition.setFromX(0);
    slideInTransition.setToX(grpTextArea.getLayoutBounds().getWidth() + 85);

    // Create a pause transition for 5 seconds
    pauseTransition = new PauseTransition(Duration.seconds(5));
    pauseTransition.setOnFinished(
        event -> {
          if (holdNotification) {
            setupHoldTimeline();
          } else {
            performSlideOutTransition();
          }
        });

    // Start the slide-in animation
    slideInTransition.setOnFinished(
        event -> {
          recHide.setVisible(false); // Hide recHide when slide-in is complete
        });

    // Start the slide-in animation
    slideInTransition.play();
    pauseTransition.play();
  }

  /**
   * Holds the notification
   *
   * @param
   * @return
   */
  private void setupHoldTimeline() {
    holdTimeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                event -> {
                  if (!holdNotification) {
                    holdTimeline.stop();
                    performSlideOutTransition();
                  }
                }));
    holdTimeline.setCycleCount(Timeline.INDEFINITE);
    holdTimeline.play();
  }

  /**
   * Performs the slide out transition.
   *
   * @param
   * @return
   */
  private void performSlideOutTransition() {
    recHide.setVisible(true);
    slideOutTransition = new TranslateTransition(Duration.seconds(1), grpTextArea);
    slideOutTransition.setFromX(grpTextArea.getLayoutBounds().getWidth() + 85);
    slideOutTransition.setToX(0);
    slideOutTransition.setOnFinished(
        event2 -> {
          // Reset notification parameters
          grpTextArea.setTranslateX(0);
          isTransitioning = false;
          processNextNotification();
          recHide.setVisible(false);
        });
    slideOutTransition.play();
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
    if (secondsLeft == initialSeconds / 2
        || secondsLeft == 60
        || secondsLeft == 15
        || secondsLeft == 5) {
      generateNotification(true, secondsLeft);
    } else if (secondsLeft == initialSeconds - 1 || secondsLeft == initialSeconds - 2) {
      generateNotification();
    } else if (secondsLeft == initialSeconds / 4 || secondsLeft == 3 * initialSeconds / 4) {
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
        generateNotification("Tell me each room has one problem to solve.");
        break;
      case 1:
        generateNotification("Tell me the clues I recieve will unlock the reactor.");
        break;
      case 2:
        generateNotification("Tell me I can talk to the astronauts for help.");
        break;
    }
  }
}
