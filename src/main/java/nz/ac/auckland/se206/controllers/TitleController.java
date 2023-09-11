package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.Screen;
import nz.ac.auckland.se206.components.AnimatedButton;

/** Controller class for the title screen. */
public class TitleController implements Controller {

  @FXML Pane panBackground;
  @FXML Pane panFullScreen;
  @FXML AnimatedButton btnPlay;
  @FXML AnimatedButton btnSettings;

  /**
   * Called when the play button is clicked. Moves control to the exposition screen.
   *
   * @param event The mouse event.
   * @throws IOException If the exposition.fxml file is not found.
   */
  @FXML
  public void onPlayClicked(MouseEvent event) throws IOException {
    App.setScreen(Screen.Name.EXPOSITION);
  }

  /**
   * Called when the settings button is clicked. Moves control to the settings screen.
   *
   * @param event The mouse event.
   * @throws IOException If the settings.fxml file is not found.
   */
  @FXML
  public void onSettingsClicked(MouseEvent event) throws IOException {
    App.setScreen(Screen.Name.SETTINGS);
  }
}
