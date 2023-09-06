package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.App;

/**
 * Controller class for the root screen that controls all other screens. The active screen is a
 * child of this screen.
 */
public class MainController implements Controller {

  /** Should match width of screens in SceneBuilder. */
  private static final int DEFAULT_WIDTH = 800;

  /** Should match height of screens in SceneBuilder. */
  private static final int DEFAULT_HEIGHT = 600;

  /** Outermost pane that is translated to compensate for scaling. */
  @FXML private Pane panTranslate;

  /** Middle pane that is scaled. */
  @FXML private Pane panScale;

  /** Innermost pane that stores a reference to the root node of the active screen. */
  @FXML private Pane panMain;

  /**
   * Returns the main pane. This is the pane that stores the root node of the active screen.
   *
   * @return The main pane.
   */
  public Pane getMainPane() {
    return panMain;
  }

  /** Adds listeners to the scene to keep the screen centered and scaled to the scene dimensions. */
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

  /**
   * Updates the screen dimensions to fill the scene. Will scale inner contents. Will maintain
   * original aspect ratio, adding borders if neccessary (screen centered).
   *
   * @param sceneWidth The current width of the scene.
   * @param sceneHeight The current height of the scene.
   */
  private void updateOuterPaneSize(double sceneWidth, double sceneHeight) {
    // Height to set outer pane to be (maintain aspect ratio)
    double aspectRatio = (double) DEFAULT_WIDTH / DEFAULT_HEIGHT;
    double requiredPaneWidth = Math.min(sceneWidth, sceneHeight * aspectRatio);

    // Set outer pane size
    double zoom = requiredPaneWidth / DEFAULT_WIDTH;
    panScale.setScaleX(zoom);
    panScale.setScaleY(zoom);

    // Center content (workaround)
    double xShift = (sceneWidth - DEFAULT_WIDTH) / 2;
    double yShift = (sceneHeight - DEFAULT_HEIGHT) / 2;
    panTranslate.setLayoutX(xShift);
    panTranslate.setLayoutY(yShift);
  }
}
