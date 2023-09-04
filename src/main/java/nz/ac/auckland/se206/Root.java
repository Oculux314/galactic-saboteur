package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import nz.ac.auckland.se206.controllers.Controller;

public class Root {

  public enum Name {
    MAIN,
    TITLE,
    EXPOSITION;
  }

  private Parent fxml;
  private Controller controller;

  public Root(FXMLLoader loader) throws IOException {
    fxml = loader.load();
    controller = loader.getController();
  }

  public Parent getFxml() {
    return fxml;
  }

  public Controller getController() {
    return controller;
  }
}
