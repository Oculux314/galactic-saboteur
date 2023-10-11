package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.Screen;
import nz.ac.auckland.se206.TaggedThread;
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
    "/images/expo1.jpg", "/images/expo2.png", "/images/expo3.png", "/images/expo4.png", "/images/expo5.png"
  };
  private TaggedThread delayManager;

  public void startSlideshow() {
    showNextImage();
  }

  private void showNextImage() {
    // Check if slideshow is finished
    if (currentImageIndex >= imagePaths.length) {
      showReplayPane();
      return;
    }

    // Show next image
    updateImage();
    delayManager = new TaggedThread(() -> delayAndShowImage());
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

  /**
   * Called when the replay button is clicked. Replays exposition.
   *
   * @param event The mouse event.
   */
  @FXML
  private void onReplayClicked(MouseEvent event) throws IOException {
    replayPane.setVisible(false);
    startSlideshow();
  }

  /**
   * Called when the continue button is clicked. Moves onto game.
   *
   * @param event The mouse event.
   */
  @FXML
  private void onContinueClicked(MouseEvent event) throws IOException {
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
