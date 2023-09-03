package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.App;

public class TitleController {

  @FXML Pane fullScreenPane;

  /** Initializes the title view */
  public void initialize() {
    // Initialization code goes here
  }

  @FXML
  public void onMouseClicked(MouseEvent event) throws IOException {
    App.setRoot("exposition");
  }
}
