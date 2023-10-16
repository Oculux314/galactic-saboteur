package nz.ac.auckland.se206.screens;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.components.AnimatedButton;
import nz.ac.auckland.se206.components.StateButton;
import nz.ac.auckland.se206.misc.GameState;

/** Controller class for the title screen. */
public class SettingsController implements Screen {

  @FXML private Pane panBackground;
  @FXML private AnimatedButton btnBack;
  @FXML private StateButton btnDifficulty;
  @FXML private StateButton btnTime;
  @FXML private StateButton btnTts;

  /**
   * Initializes the user interface, populating it with the persistent settings stored in the
   * GameState. The method retrieves and sets the stored difficulty, time limit, and text-to-speech
   * settings.
   */
  @FXML
  private void initialize() {
    // Store persistent settings
    String difficulty = GameState.difficulty;
    String timeLimit = Integer.toString(GameState.timeLimit);
    String ttsEnabled = GameState.ttsEnabled ? "on" : "off";

    createButtonStates();

    // Set persistent settings
    btnDifficulty.setState(difficulty);
    btnTime.setState(timeLimit);
    btnTts.setState(ttsEnabled);
  }

  /** Specifies the actions to be performed during the loading process. */
  @Override
  public void onLoad() {
    // Do nothing
  }

  /**
   * Creates button states for the difficulty, time, and text-to-speech settings. Each button state
   * is associated with an image and corresponding update action.
   */
  private void createButtonStates() {
    // Difficulty
    btnDifficulty.addState("easy", "settings_buttons/easy.png", this::updateDifficulty, null);
    btnDifficulty.addState("medium", "settings_buttons/medium.png", this::updateDifficulty, null);
    btnDifficulty.addState("hard", "settings_buttons/hard.png", this::updateDifficulty, null);

    // Time
    btnTime.addState("2", "settings_buttons/2.png", this::updateTime, null);
    btnTime.addState("4", "settings_buttons/4.png", this::updateTime, null);
    btnTime.addState("6", "settings_buttons/6.png", this::updateTime, null);

    // Text-to-speech
    btnTts.addState("off", "settings_buttons/off.png", this::updateTts, null);
    btnTts.addState("on", "settings_buttons/on.png", this::updateTts, null);
  }

  /** Updates the difficulty setting in the GameState based on the selected button state. */
  private void updateDifficulty() {
    GameState.difficulty = btnDifficulty.getState();
  }

  /** Updates the time limit setting in the GameState based on the selected button state. */
  private void updateTime() {
    GameState.timeLimit = Integer.parseInt(btnTime.getState());
  }

  /** Updates the text-to-speech setting in the GameState based on the selected button state. */
  private void updateTts() {
    GameState.ttsEnabled = btnTts.getState().equals("on");
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

  /**
   * Retrieves the current state of the time setting from the button.
   *
   * @return The current state of the time setting.
   */
  public String getTimeState() {
    return btnTime.getState();
  }
}
