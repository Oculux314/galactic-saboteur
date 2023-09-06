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

  /** A map of all roots in the application (name -> root) */
  private static Map<Root.Name, Root> roots = new HashMap<>(); // Stores all FXML/Controller pairs

  /**
   * The entry point of the application.
   *
   * @param args Command line arguments.
   */
  public static void main(final String[] args) {
    launch();
  }

  /**
   * Returns the root with the given name, or null if it does not exist.
   *
   * @param rootName The name of the root to get.
   * @return The root with the given name, or null if it does not exist.
   */
  public static Root getRoot(final Root.Name rootName) {
    return roots.get(rootName); // Null if does not exist
  }

  /**
   * Sets the root with the given name as the child of the main pane. Creates the root if it does
   * not exist.
   *
   * @param rootName The name of the root to set.
   * @throws IOException If the root does not exist and the FXML file for the root is not found.
   */
  public static void setRoot(final Root.Name rootName) throws IOException {
    if (!roots.containsKey(rootName)) {
      makeRoot(rootName); // Automatically make root if does not exist
    }

    Parent fxml = getRoot(rootName).getFxml();
    Pane panMain = ((MainController) roots.get(Root.Name.MAIN).getController()).getMainPane();

    // Set root as child of main pane
    panMain.getChildren().clear();
    panMain.getChildren().add(fxml);

    fxml.requestFocus();
  }

  /**
   * Creates a root with the given name, and stores it in the roots map.
   *
   * @param rootName The name of the root to create.
   * @throws IOException If the FXML file for the root is not found.
   */
  private static void makeRoot(final Root.Name rootName) throws IOException {
    String fxml = rootName.toString().toLowerCase();
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml"));
    roots.put(rootName, new Root(loader));
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
   *   <li>Set up root graph
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

    // Set up root graph
    makeRoot(Root.Name.MAIN);
    Parent root = getRoot(Root.Name.MAIN).getFxml();
    setRoot(Root.Name.TITLE);

    // Link stage/scene/root graph
    Scene scene = new Scene(root, 800, 600);
    stage.setScene(scene);

    // Stylesheet
    scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

    // Listeners
    ((MainController) roots.get(Root.Name.MAIN).getController()).addSceneListeners();

    // Properties
    scene.setFill(Color.BLACK);
    stage.setTitle("Galactic Saboteur");
    stage.getIcons().add(new Image("/images/logo.png"));
    stage.setMaximized(true);

    stage.show();
    root.requestFocus();
  }
}
