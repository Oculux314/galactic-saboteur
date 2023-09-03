package nz.ac.auckland.se206;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import nz.ac.auckland.se206.controllers.MainController;

/**
 * This is the entry point of the JavaFX application, while you can change this class, it should
 * remain as the class that runs the JavaFX application.
 */
public class App extends Application {
  private static Map<Root.Name, Root> roots = new HashMap<>(); // Stores all FXML/Controller pairs

  public static void main(final String[] args) {
    launch();
  }

  public static Root getRoot(final Root.Name rootName) throws IOException {
    return roots.get(rootName); // Null if does not exist
  }

  public static void setRoot(final Root.Name rootName) throws IOException {
    if (!roots.containsKey(rootName)) {
      makeRoot(rootName); // Automatically make root if does not exist
    }

    Parent fxml = roots.get(rootName).getFxml();
    Pane panMain = ((MainController) roots.get(Root.Name.MAIN).getController()).getMainPane();

    // Set root as child of main pane
    if (panMain.getChildren().isEmpty()) {
      panMain.getChildren().add(fxml);
    } else {
      panMain.getChildren().set(0, fxml);
    }

    fxml.requestFocus();
  }

  private static void makeRoot(final Root.Name rootName) throws IOException {
    String fxml = rootName.toString().toLowerCase();
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml"));
    roots.put(rootName, new Root(loader));
  }

  /**
   * This method is invoked when the application starts. It loads and shows the "Canvas" scene.
   *
   * @param stage The primary stage of the application.
   * @throws IOException If "src/main/resources/fxml/canvas.fxml" is not found.
   */
  @Override
  public void start(final Stage stage) throws IOException {
    makeRoot(Root.Name.MAIN);
    Parent root = getRoot(Root.Name.MAIN).getFxml();
    setRoot(Root.Name.TITLE);

    Scene scene = new Scene(root, 600, 470);
    stage.setScene(scene);

    stage.show();
    root.requestFocus();
  }
}
