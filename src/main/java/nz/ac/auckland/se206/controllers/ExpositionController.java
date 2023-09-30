package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.Screen;
import nz.ac.auckland.se206.components.AnimatedButton;

/** Controller class for the title screen. */
public class ExpositionController implements Controller {

  private static final int DELAY_MILLIS = 1000;

  /** Pane that takes up the entire screen. */
  @FXML private Pane panFullScreen;

  @FXML private Pane replayPane;
  @FXML private AnimatedButton btnReplay;
  @FXML private AnimatedButton btnContinue;
  @FXML private ImageView imageView;

  private int currentImageIndex = 0;
  private String[] imagePaths = {
    "/images/expo1.png", "/images/expo2.jpg", "/images/expo3.jpg", "/images/expo4.jpg"
  };
  private Thread delayManager;

  public void startSlideshow() {
    showNextImage();
  }

  private void showNextImage() {
    if (currentImageIndex >= imagePaths.length) {
      showReplayPane();
      return;
    }

    updateImage();

    delayManager = new Thread(() -> delayAndShowImage());
    delayManager.start();
  }

  private void showReplayPane() {
    currentImageIndex = 0;
    replayPane.setVisible(true);
  }

  private void updateImage() {
    // Load image
    String imagePath = imagePaths[currentImageIndex];
    Image image = new Image(getClass().getResourceAsStream(imagePath));
    imageView.setImage(image);
    currentImageIndex++;
  }

  private void delayAndShowImage() {
    try {
      Thread.sleep(DELAY_MILLIS);
    } catch (InterruptedException e) {
      return;
    }

    showNextImage();
  }

  @FXML
  private void onReplayClicked() throws IOException {
    replayPane.setVisible(false);
    startSlideshow();
  }

  @FXML
  private void onContinueClicked() throws IOException {
    // Start timer
    App.setScreen(Screen.Name.GAME);
    GameController gameController =
        ((GameController) App.screens.get(Screen.Name.GAME).getController());
    gameController.startTimer();
  }

  @FXML
  private void onBackgroundClicked() {
    if (delayManager != null) {
      delayManager.interrupt(); // Cancel current automatic image change
    }

    showNextImage();
  }
}
