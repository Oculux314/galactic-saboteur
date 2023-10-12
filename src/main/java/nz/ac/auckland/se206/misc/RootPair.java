package nz.ac.auckland.se206.misc;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

/** Represents a root in the application, consisting of an FXML file / controller pair. */
public class RootPair {
  public interface Controller {
    void onLoad();
  }

  /**
   * The FXML file for the screen. More precisely, a reference to the screen node of the FXML file.
   */
  private Parent fxml;

  /** The controller for the screen. */
  private Controller controller;

  public RootPair(String fxmlUrl) {
    this(new FXMLLoader(RootPair.class.getResource(fxmlUrl)));
  }

  /**
   * Constructs a new screen with the FXML file and controller stored within the given loader.
   *
   * @param loader The loader containing the FXML file and controller.
   * @throws IOException If the FXML file is not found.
   */
  private RootPair(FXMLLoader loader) {
    Utils.startTimeTest(); // TODO: remove time tests

    try {
      fxml = loader.load();
      controller = loader.getController();
    } catch (IllegalStateException | IOException e) {
      fxml = getDefaultParent();
      controller = null;
    }

    Utils.logTimeTest("Loaded " + getFxml().toString() + " popup", 100); // TODO: remove time tests
  }

  private Parent getDefaultParent() {
    return (Parent) new Pane();
  }

  /**
   * Returns the FXML file for the screen. More precisely, a reference to the screen node of the
   * FXML file.
   *
   * @return The screen node of the FXML file.
   */
  public Parent getFxml() {
    return fxml;
  }

  /**
   * Returns the controller for the screen.
   *
   * @return The controller for the screen.
   */
  public Controller getController() {
    return controller;
  }
}
