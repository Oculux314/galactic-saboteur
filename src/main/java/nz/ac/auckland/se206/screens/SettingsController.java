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

  @Override
  public void onLoad() {
    // Do nothing
  }

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

  private void updateDifficulty() {
    GameState.difficulty = btnDifficulty.getState();
  }

  private void updateTime() {
    GameState.timeLimit = Integer.parseInt(btnTime.getState());
  }

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

  public String getTimeState() {
    return btnTime.getState();
  }
}