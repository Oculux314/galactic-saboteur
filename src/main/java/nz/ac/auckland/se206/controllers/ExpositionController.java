package nz.ac.auckland.se206.controllers;


import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import javafx.scene.control.Button;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;

/** Controller class for the room view. */
public class ExpositionController implements Controller {

  @FXML private Pane panSpaceship;
  @FXML private Group panzoomgroup;
  @FXML private Rectangle recTest;
  @FXML private Button btnSettings;
  
  private double pressedX, pressedY;
  private double deltaX, deltaY;
  private double pivotX, pivotY;
  private double scaleFactor = 1.0;
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
    double minX = -100; 
    double minY = -100;
    double maxX = 100;
    double maxY = 100;

    newX = Math.min(Math.max(newX, minX), maxX);
    newY = Math.min(Math.max(newY, minY), maxY);

    // Apply the translation
    panzoomgroup.setTranslateX(newX);
    panzoomgroup.setTranslateY(newY);

    pressedX = event.getSceneX();
    pressedY = event.getSceneY();
    System.out.println("Mouse dragged to " + pressedX + ", " + pressedY);
}

  
@FXML
private void onScroll(ScrollEvent event) {
    double zoomFactor = 1.1; // You can adjust the zoom factor as needed

    if (event.getDeltaY() < 0) {
        scaleFactor /= zoomFactor;
    } else {
        scaleFactor *= zoomFactor;
    }
    
    double minScaleFactor = 0.9;  // Minimum scale factor allowed
    double maxScaleFactor = 2.0;  // Maximum scale factor allowed

    scaleFactor = Math.min(Math.max(scaleFactor, minScaleFactor), maxScaleFactor);

    // Apply the scale and adjust the translation
    scale.setX(scaleFactor);
    scale.setY(scaleFactor);
    scale.setPivotX(event.getSceneX());
    scale.setPivotY(event.getSceneY());

    event.consume();
}


  @FXML
  private void settingsClicked() {
    System.out.println("Settings button clicked");
  }

  @FXML private void recClicked() {
    System.out.println("Rectangle clicked");
  }

}
