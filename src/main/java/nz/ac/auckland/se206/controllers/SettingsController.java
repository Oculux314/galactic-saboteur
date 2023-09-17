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
  @FXML StateButton btnDifficulty;
  @FXML StateButton btnTime;
  @FXML StateButton btnTts;

  @FXML
  private void initialize() {
    btnDifficulty.addState("easy", "settings_buttons/easy.png");
    btnDifficulty.addState("medium", "settings_buttons/medium.png");
    btnDifficulty.addState("hard", "settings_buttons/hard.png");

    btnTime.addState("2min", "placeholder.png");
    btnTime.addState("4min", "placeholder.png");
    btnTime.addState("6min", "placeholder.png");

    btnTts.addState("off", "placeholder.png");
    btnTts.addState("on", "placeholder.png");
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
