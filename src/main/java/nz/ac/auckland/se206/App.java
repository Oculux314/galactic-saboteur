package nz.ac.auckland.se206;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import nz.ac.auckland.se206.controllers.MainController;

/** The entry point of the JavaFX application, representing the top-level application. */
public class App extends Application {

  /** The primary stage of the application */
  private static Stage stage;

  /** A map of all screens in the application (name -> screen) */
  private static Map<Screen.Name, Screen> screens = new HashMap<>();

  /**
   * The entry point of the application.
   *
   * @param args Command line arguments.
   */
  public static void main(final String[] args) {
    launch();
  }

  /**
   * Returns the screen with the given name, or null if it does not exist.
   *
   * @param screenName The name of the screen to get.
   * @return The screen with the given name, or null if it does not exist.
   */
  public static Screen getScreen(final Screen.Name screenName) {
    return screens.get(screenName); // Null if does not exist
  }

  /**
   * Sets the screen with the given name as the child of the main pane. Creates the screen if it
   * does not exist.
   *
   * @param screenName The name of the screen to set.
   * @throws IOException If the screen does not exist and the FXML file for the screen is not found.
   */
  public static void setScreen(final Screen.Name screenName) throws IOException {
    if (!screens.containsKey(screenName)) {
      makeScreen(screenName); // Automatically make screen if does not exist
    }

    Parent fxml = getScreen(screenName).getFxml();
    Pane panMain = ((MainController) screens.get(Screen.Name.MAIN).getController()).getMainPane();

    // Set screen as child of main pane
    panMain.getChildren().clear();
    panMain.getChildren().add(fxml);

    fxml.requestFocus();
  }

  /**
   * Creates a screen with the given name, and stores it in the screens map.
   *
   * @param screenName The name of the screen to create.
   * @throws IOException If the FXML file for the screen is not found.
   */
  private static void makeScreen(final Screen.Name screenName) throws IOException {
    String fxml = screenName.toString().toLowerCase();
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml"));
    screens.put(screenName, new Screen(loader));
  }

  /**
   * Returns the primary stage of the application.
   *
   * @return The primary stage of the application.
   */
  public static Stage getStage() {
    return stage;
  }

  /**
   * Returns the scene of the primary stage of the application.
   *
   * @return The scene of the primary stage of the application.
   */
  public static Scene getScene() {
    return stage.getScene();
  }

  /**
   * This method is invoked when the application starts. It performs all neccessary setup,
   * including:
   *
   * <ul>
   *   <li>Initialise stage and scene
   *   <li>Set up screen graph
   *   <li>Link style sheet and listeners
   *   <li>Set stage properties
   *   <li>Show stage
   * </ul>
   *
   * @param newStage The primary stage of the application.
   * @throws IOException If either "main.fxml" or "title.fxml" is not found in
   *     "src/main/resources/fxml/".
   */
  @Override
  public void start(final Stage newStage) throws IOException {
    App.stage = newStage;

    // Set up screen graph
    makeScreen(Screen.Name.MAIN);
    makeScreen(Screen.Name.SETTINGS);
    Parent screen = getScreen(Screen.Name.MAIN).getFxml();
    setScreen(Screen.Name.TITLE);

    // Link stage/scene/screen graph
    Scene scene = new Scene(screen, 800, 600);
    stage.setScene(scene);

    // Stylesheet
    scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

    // Listeners
    ((MainController) screens.get(Screen.Name.MAIN).getController()).addSceneListeners();

    // Properties
    scene.setFill(Color.web("#131d23"));
    stage.setTitle("Galactic Saboteur");
    stage.getIcons().add(new Image("/images/logo.png"));
    stage.setMaximized(true);

    stage.show();
    screen.requestFocus();
  }
}
