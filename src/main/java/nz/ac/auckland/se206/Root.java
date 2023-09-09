package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import nz.ac.auckland.se206.controllers.Controller;

/** Represents a root in the application, consisting of an FXML file / controller pair. */
public class Root {

  /** Represents possible names for a root. */
  public enum Name {
    MAIN,
    TITLE,
    EXPOSITION;
  }

  /** The FXML file for the root. More precisely, a reference to the root node of the FXML file. */
  private Parent fxml;

  /** The controller for the root. */
  private Controller controller;

  /**
   * Constructs a new root with the FXML file and controller stored within the given loader.
   *
   * @param loader The loader containing the FXML file and controller.
   * @throws IOException If the FXML file is not found.
   */
  public Root(FXMLLoader loader) throws IOException {
    fxml = loader.load();
    controller = loader.getController();
  }

  /**
   * Returns the FXML file for the root. More precisely, a reference to the root node of the FXML file.
   * @return The root node of the FXML file.
   */
  public Parent getFxml() {
    return fxml;
  }

  /**
   * Returns the controller for the root.
   * @return The controller for the root.
   */
  public Controller getController() {
    return controller;
  }
}
