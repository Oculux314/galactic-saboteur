package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.App;

/** Controller class for the room view. */
public class MainController implements Controller {

  private static final int DEFAULT_WIDTH = 800; // Should match scenebuilder
  private static final int DEFAULT_HEIGHT = 600;

  @FXML private Pane panTranslate;
  @FXML private Pane panScale;
  @FXML private Pane panMain;

  public Pane getMainPane() {
    return panMain;
  }

  public void addSceneListeners() {
    Scene scene = App.getScene();
    scene
        .heightProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              updateOuterPaneSize(scene.getWidth(), newVal.doubleValue());
            });
    scene
        .widthProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              updateOuterPaneSize(newVal.doubleValue(), scene.getHeight());
            });
  }

  private void updateOuterPaneSize(double sceneWidth, double sceneHeight) {
    // Height to set outer pane to be (maintain aspect ratio)
    double aspectRatio = (double) DEFAULT_WIDTH / DEFAULT_HEIGHT;
    double requiredPaneWidth = Math.min(sceneWidth, sceneHeight * aspectRatio);

    // Set outer pane size
    double zoom = requiredPaneWidth / DEFAULT_WIDTH;
    panScale.setScaleX(zoom);
    panScale.setScaleY(zoom);

    // Shifts
    double xShift = -(DEFAULT_WIDTH - sceneWidth) / 2;
    double yShift = -(DEFAULT_HEIGHT - sceneHeight) / 2;

    // Center content (workaround)
    panTranslate.setLayoutX(xShift);
    panTranslate.setLayoutY(yShift);
  }
}
