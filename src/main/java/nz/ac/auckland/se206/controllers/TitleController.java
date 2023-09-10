package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.Screen;
import nz.ac.auckland.se206.components.IconButton;

/** Controller class for the title screen. */
public class TitleController implements Controller {

  @FXML Pane panContainer;
  @FXML Pane panFullScreen;

  @FXML
  private void initialize() {

    IconButton btnSettings = new IconButton("/images/settings.png");
    btnSettings.setLayoutX(300);
    btnSettings.setLayoutY(420);
    panContainer.getChildren().add(btnSettings);

    IconButton btnPlay = new IconButton("/images/play.png");
    btnPlay.setLayoutX(420);
    btnPlay.setLayoutY(420);
    panContainer.getChildren().add(btnPlay);
  }

  /**
   * Called when the mouse is clicked on the full screen pane. Moves control to the exposition
   * screen.
   *
   * @param event The mouse event.
   * @throws IOException If the exposition.fxml file is not found.
   */
  @FXML
  public void onMouseClicked(MouseEvent event) throws IOException {
    App.setScreen(Screen.Name.EXPOSITION);
  }
}
