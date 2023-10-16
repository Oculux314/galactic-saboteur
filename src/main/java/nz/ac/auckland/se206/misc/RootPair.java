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

  /**
   * Constructs a RootPair object with the specified FXML URL.
   *
   * @param fxmlUrl The URL of the FXML file.
   */
  public RootPair(String fxmlUrl) {
    this(new FXMLLoader(RootPair.class.getResource(fxmlUrl)), fxmlUrl);
  }

  /** Constructs a default RootPair screen. */
  public RootPair() {
    // Constructs default screen
    fxml = getDefaultParent();
    controller = null;
  }

  /**
   * Constructs a new screen with the FXML file and controller stored within the given loader.
   *
   * @param loader The loader containing the FXML file and controller.
   * @param url The URL of the FXML file.
   * @throws IOException If the FXML file is not found.
   */
  private RootPair(FXMLLoader loader, String url) {
    try {
      fxml = loader.load();
      controller = loader.getController();
    } catch (IllegalStateException | IOException e) {
      checkErrors(url);
      throw new RuntimeException(e); // Fallback error
    }

    checkErrors(url);
  }

  /**
   * Checks for errors in the specified location.
   *
   * @param location The location to check for errors.
   * @throws RuntimeException if the FXML file or the controller fails to load.
   */
  private void checkErrors(String location) {
    if (fxml == null) {
      throw new RuntimeException(location + ": Failed to load fxml file.");
    }

    if (controller == null) {
      throw new RuntimeException(location + ": Failed to load controller.");
    }
  }

  /**
   * Returns the default Parent object.
   *
   * @return A Parent object representing the default pane.
   */
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
