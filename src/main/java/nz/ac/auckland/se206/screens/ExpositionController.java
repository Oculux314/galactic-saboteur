package nz.ac.auckland.se206.screens;

import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.components.AnimatedButton;
import nz.ac.auckland.se206.misc.TaggedThread;

/** Controller class for the title screen. */
public class ExpositionController implements Screen {

  private static final int DELAY_MILLIS = 1000;
  private static final int FADE_MILLIS = 500;

  /** Pane that takes up the entire screen. */
  @FXML private Pane panFullScreen;

  @FXML private AnimatedButton btnContinue;

  @FXML private ImageView fadeInImage;
  @FXML private ImageView fadeOutImage;

  private int currentImageIndex = 0;
  private String[] imagePaths = {
    "/images/expo1.jpg",
    "/images/expo2.png",
    "/images/expo3.png",
    "/images/expo4.png",
    "/images/expo5.png",
    "/images/expo6.png"
  };
  private TaggedThread delayManager;

  @Override
  public void onLoad() {
    // Do nothing
  }

  public void startSlideshow() {
    btnContinue.setVisible(false); // Make button unclickable
    btnContinue.setOpacity(0);
    showNextImage();
  }

  private void showNextImage() {
    // Check if slideshow is finished
    if (currentImageIndex >= imagePaths.length) {
      showContinueButton();
      return;
    }

    // Show next image
    updateImage();
    delayManager = new TaggedThread(() -> delayAndShowImage());
    delayManager.start();
  }

  private void showContinueButton() {
    currentImageIndex = 0;
    btnContinue.setVisible(true); // Allow button to be clicked
    
    Timeline btnFadeTransition =
        new Timeline(
            new KeyFrame(
                Duration.millis(FADE_MILLIS), new KeyValue(btnContinue.opacityProperty(), 1)));
    btnFadeTransition.play();
  }

  private void updateImage() {
    // Load image
    String imagePath = imagePaths[currentImageIndex];
    Image nextImage = new Image(getClass().getResourceAsStream(imagePath));

    fadeOutImage.setImage(fadeInImage.getImage());
    fadeOutImage.setOpacity(1);
    fadeInImage.setImage(nextImage);
    fadeInImage.setOpacity(0);

    currentImageIndex++;
  }

  private void delayAndShowImage() {
    // Fade
    Timeline fadeTransition =
        new Timeline(
            new KeyFrame(
                Duration.millis(FADE_MILLIS),
                new KeyValue(fadeOutImage.opacityProperty(), 0),
                new KeyValue(fadeInImage.opacityProperty(), 1)));
    fadeTransition.play();

    // Wait
    try {
      Thread.sleep(FADE_MILLIS + DELAY_MILLIS);
    } catch (InterruptedException e) {
      return;
    }

    showNextImage();
  }

  /**
   * Called when the continue button is clicked. Moves onto game.
   *
   * @param event The mouse event.
   */
  @FXML
  private void onContinueClicked(MouseEvent event) throws IOException {
    // These need to be called upon screen transition, not when the screen is initialised
    GameController gameController =
        ((GameController) App.getScreen(Screen.Name.GAME).getController());
    gameController.initialiseTimer();
    gameController.updateHintText();

    App.setScreen(Screen.Name.GAME);
  }

  @FXML
  private void onBackgroundClicked() {
    if (delayManager != null) {
      delayManager.interrupt(); // Cancel current automatic image change
    }

    showNextImage();
  }
}
