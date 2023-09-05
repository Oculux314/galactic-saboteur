package nz.ac.auckland.se206.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.util.Duration;

/** Controller class for the room view. */
public class ExpositionController implements Controller {

  @FXML private Pane panSpaceship;
  @FXML private Group panzoomgroup;
  @FXML private Rectangle recTest;
  @FXML private Button btnSettings;

  private double pressedX, pressedY;
  private double deltaX, deltaY;
  private double zoomFactor = 1.25;
  private double minScaleFactor = 0.9;
  private double maxScaleFactor = 2.0;
  private Scale scale = new Scale();

  public void initialize() {
    // Add zoom functionality to panzoomgroup
    panzoomgroup.getTransforms().add(scale);
  }

  @FXML
  private void onPress(MouseEvent event) {
    // Store the initial position when the mouse is pressed
    pressedX = event.getSceneX();
    pressedY = event.getSceneY();
    System.out.println("Mouse pressed at " + pressedX + ", " + pressedY);
  }

  @FXML
  private void onDrag(MouseEvent event) {
    deltaX = event.getSceneX() - pressedX;
    deltaY = event.getSceneY() - pressedY;

    // Calculate the new position after panning
    double newX = panzoomgroup.getTranslateX() + deltaX;
    double newY = panzoomgroup.getTranslateY() + deltaY;

    // Define the boundaries for panning (adjust these values as needed)
    // double minX = -100 * scaleFactor;
    // double minY = -100 * scaleFactor;
    // double maxX = 100 * scaleFactor;
    // double maxY = 100 * scaleFactor;

    // newX = Math.min(Math.max(newX, minX), maxX);
    // newY = Math.min(Math.max(newY, minY), maxY);

    // Apply the translation without restricting boundaries
    panzoomgroup.setTranslateX(newX);
    panzoomgroup.setTranslateY(newY);

    pressedX = event.getSceneX();
    pressedY = event.getSceneY();
    System.out.println("Mouse dragged to " + pressedX + ", " + pressedY);
  }

  @FXML
  private void onScroll(ScrollEvent event) {

    // Zoom in or out
    double scaleFactor = (event.getDeltaY() < 0) ? 1 / zoomFactor : zoomFactor;

    double currentScaleFactor = scale.getX();
    double newScaleFactor = currentScaleFactor * scaleFactor;
    newScaleFactor = clamp(newScaleFactor, minScaleFactor, maxScaleFactor);
    double pivotX = event.getX();
    double pivotY = event.getY();
    double scaleChange = newScaleFactor / currentScaleFactor - 1;

    // Calculate the translation adjustments
    double translateX =
        panzoomgroup.getTranslateX()
            - scaleChange * (pivotX - panzoomgroup.getBoundsInParent().getMinX());
    double translateY =
        panzoomgroup.getTranslateY()
            - scaleChange * (pivotY - panzoomgroup.getBoundsInParent().getMinY());

    // Smoothly animate the zoom
    Timeline timeline =
        new Timeline(
            new KeyFrame(
                Duration.millis(50),
                new KeyValue(scale.xProperty(), newScaleFactor),
                new KeyValue(scale.yProperty(), newScaleFactor),
                new KeyValue(panzoomgroup.translateXProperty(), translateX),
                new KeyValue(panzoomgroup.translateYProperty(), translateY)));

    timeline.play();

    event.consume();
  }

  private double clamp(double value, double min, double max) {
    return Math.min(Math.max(value, min), max);
  }

  @FXML
  private void settingsClicked() {
    System.out.println("Settings button clicked");
  }

  @FXML
  private void recClicked() {
    System.out.println("Rectangle clicked");
  }
}
