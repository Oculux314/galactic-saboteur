package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.Screen;
import nz.ac.auckland.se206.components.AnimatedButton;
import nz.ac.auckland.se206.components.StateButton;

/** Controller class for the title screen. */
public class SettingsController implements Controller {

  @FXML Pane panBackground;
  @FXML AnimatedButton btnBack;
  @FXML StateButton btnTest;

  @FXML
  private void initialize() {
    btnTest.addState("state1", "play.png", () -> System.out.println("Now at state 1"), null);
    btnTest.addState("state2", "settings.png", () -> System.out.println("Now at state 2"), null);
  }

  /**
   * Called when the back button is clicked. Moves control to the title screen.
   *
   * @param event The mouse event.
   * @throws IOException If the title.fxml file is not found.
   */
  @FXML
  private void onBackClicked(MouseEvent event) throws IOException {
    App.setScreen(Screen.Name.TITLE);
  }
}
