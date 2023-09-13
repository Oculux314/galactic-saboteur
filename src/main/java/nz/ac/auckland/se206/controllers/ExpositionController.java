package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.Screen;
import javafx.scene.control.Button;

/** Controller class for the title screen. */
public class ExpositionController implements Controller {

  /** Pane that takes up the entire screen. */
  @FXML Pane panFullScreen;
  @FXML Pane replayPane;
  @FXML Button btnReplay;
  @FXML Button btnContinue;
  @FXML ImageView imageView = new ImageView();

  private int currentImageIndex = 0;
  private Timeline timeline = new Timeline();

  private String[] imagePaths = {
    "/images/expo2.jpg",
    "/images/expo3.jpg",
    "/images/expo4.jpg"
  };

  @FXML
  public void initialize() {
    replayPane.setVisible(false);
    startSlideshow();
  }

  /**
   * Called when the mouse is clicked on the full screen pane. Moves control to the game screen.
   *
   * @param event The mouse event.
   * @throws IOException If the game.fxml file is not found.
   */
  @FXML
  public void onMouseClicked(MouseEvent event) throws IOException {
    
  }

  private void startSlideshow() {
    replayPane.setVisible(false);
    Image image = new Image("/images/expo1.jpg");
    imageView.setImage(image);
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
      timeline.stop();
      currentImageIndex = 0;
      replayPane.setVisible(true);
      imageView.setImage(null);
    }
  }

  @FXML
  public void onReplayClicked() throws IOException {
    startSlideshow();
  }

  @FXML
  public void onContinueClicked() throws IOException {
    System.out.println("Continue button clicked");
    App.setScreen(Screen.Name.GAME);
  }
}
