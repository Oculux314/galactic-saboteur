package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.Screen;

/** Controller class for the title screen. */
public class ExpositionController implements Controller {

  /** Pane that takes up the entire screen. */
  @FXML Pane panFullScreen;

  /**
   * Called when the mouse is clicked on the full screen pane. Moves control to the game screen.
   *
   * @param event The mouse event.
   * @throws IOException If the game.fxml file is not found.
   */
  @FXML
  public void onMouseClicked(MouseEvent event) throws IOException {

    App.setScreen(Screen.Name.GAME);

    // create an instance of the GameController associated with the game screen
    GameController gameController =
        (GameController) App.getScreen(Screen.Name.GAME).getController();
    // start the timer in the GameController
    gameController.startTimer();
  }
}
