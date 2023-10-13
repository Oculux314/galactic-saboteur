package nz.ac.auckland.se206.screens;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.App;

/**
 * Controller class for the root screen that controls all other screens. The active screen is a
 * child of this screen.
 */
public class MainController implements Screen {

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

  private double screenWidth = DEFAULT_WIDTH;
  private double screenHeight = DEFAULT_HEIGHT;
  private double zoom = 1;

  @Override
  public void onLoad() {
    // Do nothing
  }

  /** Adds listeners to the scene to keep the screen centered and scaled to the scene dimensions. */
  public void addSceneListeners() {
    Scene scene = App.getScene();

    // Height change listener
    scene
        .heightProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              updateOuterPaneSize(scene.getWidth(), newVal.doubleValue());
            });

    // Width change listener
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
    screenWidth = Math.min(sceneWidth, sceneHeight * aspectRatio);
    screenHeight = screenWidth / aspectRatio;

    // Set outer pane size
    zoom = screenWidth / DEFAULT_WIDTH;
    panScale.setScaleX(zoom);
    panScale.setScaleY(zoom);

    // Center content (workaround)
    double horizontalShift = (sceneWidth - DEFAULT_WIDTH) / 2;
    double verticalShift = (sceneHeight - DEFAULT_HEIGHT) / 2;
    panTranslate.setLayoutX(horizontalShift);
    panTranslate.setLayoutY(verticalShift);
  }

  /**
   * Returns the main pane. This is the pane that stores the root node of the active screen.
   *
   * @return The main pane.
   */
  public Pane getMainPane() {
    return panMain;
  }

  /**
   * Returns the width of the screen. Guaranteed to conform to the aspect ratio of the screen.
   *
   * @return The width of the screen.
   */
  public double getScreenWidth() {
    return screenWidth;
  }

  /**
   * Returns the height of the screen. Guaranteed to conform to the aspect ratio of the screen.
   *
   * @return The height of the screen.
   */
  public double getScreenHeight() {
    return screenHeight;
  }

  /**
   * Returns the zoom of the screen due to window size. This does not include game zoom.
   *
   * @return The screen zoom. 1.0 corresponds to the default screen size 600px x 800px.
   */
  public double getScreenZoom() {
    return zoom;
  }
}
