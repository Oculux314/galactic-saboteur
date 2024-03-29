package nz.ac.auckland.se206.screens;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.components.AnimatedButton;
import nz.ac.auckland.se206.gamechildren.PopupController;
import nz.ac.auckland.se206.misc.GameState;

/** Controller class for the title screen. */
public class TitleController implements Screen {

  @FXML private Pane panBackground;
  @FXML private Pane panFullScreen;
  @FXML private AnimatedButton btnPlay;
  @FXML private PopupController popupController;

  /** This method is called when the screen is loaded, performing no specific action. */
  @Override
  public void onLoad() {
    GameState.ttsEnabled = false;
  }

  /**
   * Called when the play button is clicked. Moves control to the exposition screen.
   *
   * @param event The mouse event.
   * @throws IOException If the exposition.fxml file is not found.
   */
  @FXML
  private void onPlayClicked(MouseEvent event) throws IOException {
    // Start the exposition slideshow
    App.setScreen(Screen.Name.EXPOSITION);
    ((ExpositionController) App.getScreen(Screen.Name.EXPOSITION).getController()).startSlideshow();

    // Retrieve the game controller
    GameController gameController =
        ((GameController) App.getScreen(Screen.Name.GAME).getController());
    
    // Reset components which were initialised before the game started
    gameController.resetGpt();
    gameController.setupTtsButton();
  }

  /**
   * Called when the settings button is clicked. Moves control to the settings screen.
   *
   * @param event The mouse event.
   * @throws IOException If the settings.fxml file is not found.
   */
  @FXML
  private void onSettingsClicked(MouseEvent event) throws IOException {
    App.setScreen(Screen.Name.SETTINGS);
  }
}
