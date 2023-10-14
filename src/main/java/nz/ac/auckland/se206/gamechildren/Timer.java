package nz.ac.auckland.se206.gamechildren;

import javafx.application.Platform;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.misc.GameState;
import nz.ac.auckland.se206.misc.TaggedThread;
import nz.ac.auckland.se206.screens.EndController;
import nz.ac.auckland.se206.screens.Screen;
import nz.ac.auckland.se206.gamechildren.NotificationpanelController;
import nz.ac.auckland.se206.screens.GameController;

public class Timer {
  private int secondsLeft;
  private Thread timerThread;
  private Label timerDisplay;

  public Timer(int initialSeconds, Label timerDisplay) {
    this.secondsLeft = initialSeconds;
    this.timerDisplay = timerDisplay;
    this.timerThread = new TaggedThread(() -> run());
  }

  public void start() {
    timerThread.start();
  }

  private void run() {
    while (secondsLeft > 0) {
      if (GameState.isGameover) {
        return; // Returning as interupting this thread doesn't seem to work
      }

      decrementSeconds();
      Platform.runLater(() -> updateTimerDisplay());

      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        return; // Returning as interupting this thread doesn't seem to work
      }
    }

    Platform.runLater(() -> showTimeout());
  }

  private void decrementSeconds() {
    if (secondsLeft == 63) {
      GameController gameController = ((GameController) App.getScreen(Screen.Name.GAME).getController());
      NotificationpanelController notificationpanelController = gameController.getNotificationpanelController();
      notificationpanelController.generateNotification(true, secondsLeft);
    } else if (secondsLeft == 33) {
      GameController gameController = ((GameController) App.getScreen(Screen.Name.GAME).getController());
      NotificationpanelController notificationpanelController = gameController.getNotificationpanelController();
      notificationpanelController.generateNotification(true, secondsLeft);
    }
    secondsLeft--;
  }

  private void showTimeout() {
    EndController endController = ((EndController) App.getScreen(Screen.Name.END).getController());
    endController.showEndOnTimeout();
  }

  private void updateTimerDisplay() {
    Platform.runLater(() -> timerDisplay.setText(getFormattedTime()));
  }

  private String getFormattedTime() {
    int minutes = secondsLeft / 60;
    int seconds = secondsLeft % 60;
    String formattedMinutes = String.format("%02d", minutes);
    String formattedSeconds = String.format("%02d", seconds);
    return formattedMinutes + ":" + formattedSeconds;
  }
}
