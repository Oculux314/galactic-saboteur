package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.Root;

public class TitleController implements Controller {

  /** Pane that takes up the entire screen. */
  @FXML Pane panFullScreen;

  /**
   * Called when the mouse is clicked on the full screen pane. Moves game to the exposition screen.
   *
   * @param event The mouse event.
   * @throws IOException If the exposition.fxml file is not found.
   */
  @FXML
  public void onMouseClicked(MouseEvent event) throws IOException {
    App.setRoot(Root.Name.EXPOSITION);
  }
}
