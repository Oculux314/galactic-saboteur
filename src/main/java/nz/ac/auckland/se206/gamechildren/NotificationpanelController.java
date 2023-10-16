package nz.ac.auckland.se206.gamechildren;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import org.w3c.dom.events.MouseEvent;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.misc.GameState;
import nz.ac.auckland.se206.misc.TaggedThread;
import nz.ac.auckland.se206.misc.TextToSpeech;

/**
 * The NotificationpanelController class manages the notifications displayed in the game. It handles
 * the generation and display of various types of notifications, including general notifications,
 * time-dependent notifications, and specific message-based notifications.
 */
public class NotificationpanelController {

  @FXML private Label gptTextLabel;
  @FXML private Rectangle recHide;
  @FXML private Group grpTextArea;

  private RiddleController riddleController;
  private ChatCompletionRequest chatCompletionRequest;
  private TranslateTransition slideInTransition;
  private PauseTransition pauseTransition;
  private TranslateTransition slideOutTransition;
  private Queue<String> notificationQueue = new LinkedList<>();
  private boolean isTransitioning = false;
  private boolean holdNotification = false;
  private boolean ttsFinished = false;
  private Timeline holdTimeline;
  private TextToSpeech tts = new TextToSpeech();

  /**
   * Initializes the NotificationpanelController by setting up the chat completion request and the
   * riddle controller, as well as hiding the rectangle used for transitions.
   *
   * @throws ApiProxyException if there is an issue with the API proxy during the execution of the
   *     request.
   * @throws IOException if there is an I/O issue during initialization.
   */
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
    holdNotification = true;
  }

  /**
   * Called when logo is exited.
   *
   * @param event the mouse event
   */
  public void onMouseExited() {
    holdNotification = false;
  }

  /**
   * Called when wanting to generate a notification.
   *
   * @param timeWarning whether the notification is a time warning
   * @param timeLeft the time left
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
  }

  /** Called when wanting to generate a notification that is not a time warning */
  public void generateNotification() {
    generateNotification(false, null);
  }

  /**
   * Called when wanting to generate a notification with a specific message
   *
   * @param notification The notification message.
   */
  public void generateNotification(String notification) {
    notificationQueue.add(notification);

    if (!isTransitioning) {
      processNextNotification();
    }
  }

  /** Processes the queue of notifications. Called when a notification is finished. */
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
   * Builds the text for the notification using the provided response from GPT-3.5. It sets the
   * label text with the response and triggers the transition for the notification panel.
   *
   * @param response The response obtained from the GPT-3.5 model.
   */
  private void buildText(String response) {
    Platform.runLater(
        () -> {
          TaggedThread ttsThread =
              new TaggedThread(
                  () -> {
                    try {
                      ttsFinished = false;
                      tts.speak(response);
                      ttsFinished = true; // Set the flag to true when TTS finishes
                    } catch (Exception e) {
                      e.printStackTrace();
                    }
                  });

          if (GameState.ttsEnabled) {
            ttsThread.start();
          }

          gptTextLabel.setText(response);
          transition();
        });
  }

  /**
   * Transitions the notification panel in and out by setting up various animations, including
   * slide-in and slide-out transitions.
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
          // Hold if needed
          setupHoldTimeline();
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

  /** Holds the notification */
  private void setupHoldTimeline() {
    // Create a timeline over 1 second to check if the notification should be held
    holdTimeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                event -> {
                  // When tts is finished and the notification is not being held, perform the slide out
                  if (GameState.ttsEnabled) {
                    if (!holdNotification && ttsFinished) {
                      holdTimeline.stop();
                      performSlideOutTransition();
                    }
                  } else {
                    if (!holdNotification) {
                      holdTimeline.stop();
                      performSlideOutTransition();
                    }
                  }
                }));
    holdTimeline.setCycleCount(Timeline.INDEFINITE);
    // Start the hold timeline
    holdTimeline.play();
  }

  /** Performs the slide out transition. */
  private void performSlideOutTransition() {
    // Create the base for the slide-out animation
    recHide.setVisible(true);
    slideOutTransition = new TranslateTransition(Duration.seconds(1), grpTextArea);
    slideOutTransition.setFromX(grpTextArea.getLayoutBounds().getWidth() + 85);
    slideOutTransition.setToX(0);

    // create instructions for when the slide-out animation is finished
    slideOutTransition.setOnFinished(
        event2 -> {
          // Reset notification parameters
          grpTextArea.setTranslateX(0);
          isTransitioning = false;
          processNextNotification();
          recHide.setVisible(false);

          if (GameState.ttsInterrupted) {
            GameState.ttsEnabled = !GameState.ttsEnabled;
            GameState.ttsInterrupted = false;
          }
        });

    // Start the slide-out animation
    slideOutTransition.play();
  }

  /** Returns whether a notification is in progress. */
  public boolean isNotificationInProgress() {
    return isTransitioning;
  }

  /**
   * Called when wanting to generate a notification that is time dependent
   *
   * @param initialSeconds The initial seconds.
   * @param secondsLeft The seconds left.
   */
  public void generateTimeDependentNotification(Integer initialSeconds, Integer secondsLeft) {
    // If the seconds left is half of the initial seconds, or 60, or 15, or 5, generate a time
    // warning
    if (secondsLeft == initialSeconds / 2
        || secondsLeft == 60
        || secondsLeft == 15
        || secondsLeft == 5) {
      generateNotification(true, secondsLeft);
    } else if (secondsLeft == initialSeconds - 1 || secondsLeft == initialSeconds - 2) {
      generateNotification();
    } else if (secondsLeft == initialSeconds / 4 || secondsLeft == 3 * initialSeconds / 4) {
      // If the seconds left is a quarter or three quarters of the initial seconds, generate a
      // general notification
      if (!isNotificationInProgress()) {
        selectGeneralNotification();
      }
    }
  }

  /**
   * Selects a general notification randomly from a set of predefined options and generates the
   * selected notification message. It generates a random number between 0 and 2 and selects the
   * notification based on the generated number using a switch statement.
   */
  private void selectGeneralNotification() {
    // Generate a random number between 0 and 2
    int random = (int) (Math.random() * 3);
    switch (random) {
      case 0:
        // room related notification
        generateNotification("Tell me each room has one problem to solve.");
        break;
      case 1:
        // puzzle related notification
        generateNotification("Tell me the clues I recieve will unlock the reactor.");
        break;
      case 2:
        // assistant reminder related notification
        generateNotification("Tell me I can talk to the astronauts for help.");
        break;
    }
  }
}
