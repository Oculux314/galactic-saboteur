package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.Screen;
import nz.ac.auckland.se206.components.AnimatedButton;

/** Controller class for the title screen. */
public class ExpositionController implements Controller {

  /** Pane that takes up the entire screen. */
  @FXML private Pane panFullScreen;

  @FXML private Pane replayPane;
  @FXML private AnimatedButton btnReplay;
  @FXML private AnimatedButton btnContinue;
  @FXML private ImageView imageView = new ImageView();
  @FXML private ImageView imvWho = new ImageView();

  private int currentImageIndex = 0;
  private Timeline timeline = new Timeline();
  private String[] imagePaths = {
    "/images/expo1.jpg", "/images/expo2.jpg", "/images/expo3.jpg", "/images/expo4.jpg"
  };

  public void startSlideshow() {
    // Runs the slideshow
    showNextImage();
    timeline = new Timeline(new KeyFrame(Duration.seconds(1.5), event -> showNextImage()));
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.play();
  }

  private void showNextImage() {
    if (currentImageIndex < imagePaths.length) {
      String imagePath = imagePaths[currentImageIndex];
      Image image = new Image(getClass().getResourceAsStream(imagePath));
      imageView.setImage(image);
      currentImageIndex++;
    } else {
      // Stop the slideshow and show the replay pane
      timeline.stop();
      currentImageIndex = 0;
      replayPane.setVisible(true);
    }
  }

  @FXML
  private void onReplayClicked() throws IOException {
    replayPane.setVisible(false);
    startSlideshow();
  }

  @FXML
  private void onContinueClicked() throws IOException {
    App.setScreen(Screen.Name.GAME);
  }

  @FXML
  private void onBackgroundClicked() {
    showNextImage();
  }
}
