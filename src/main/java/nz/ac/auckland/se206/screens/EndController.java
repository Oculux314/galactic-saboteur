package nz.ac.auckland.se206.screens;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.misc.Audio;
import nz.ac.auckland.se206.misc.GameState;

/**
 * The EndController class controls the end screen of the game, displaying the outcome to the
 * player. It handles events related to winning, losing, and timing out in the game.
 */
public class EndController implements Screen {

  @FXML private Label lblEnd;
  @FXML private ImageView imgEnd;

  private Audio winSound = new Audio("end_win.mp3");
  private Audio loseSound = new Audio("end_lose.mp3");

  /**
   * Executes any necessary actions upon loading the end screen. This method does nothing in the
   * context of the end screen.
   */
  @Override
  public void onLoad() {
    // Do nothing
  }

  /** Sets the screen to the end screen and marks the game as over. */
  private void showEndScreen() {
    App.setScreen(Screen.Name.END);
    App.stopTts();
    GameState.isGameover = true;
  }

  /**
   * Configures the elements on the end screen with the provided parameters.
   *
   * @param imageUrl The URL of the image to be displayed.
   * @param speech The spoken message related to the end screen.
   * @param labelText The text to be displayed on the screen.
   * @param labelColor The color to be applied to the text on the screen.
   */
  private void setEndElements(String imageUrl, String speech, String labelText, String labelColor) {
    imgEnd.setImage(new Image("images/" + imageUrl));
    lblEnd.setText(labelText);
    lblEnd.setStyle(lblEnd.getStyle() + "-fx-text-fill: " + labelColor + ";");
  }

  /**
   * Displays the end screen with a victory message when the player successfully escapes the ship.
   */
  public void showEndOnWin() {
    showEndScreen();
    setEndElements(
        "expo1.jpg", "You win!", "CONGRATULATIONS\nYou Saved The Ship And Escaped!", "#58DD94");
    winSound.play();
  }

  /**
   * Displays the end screen with a loss message when the player provides the wrong keycard
   * combination.
   */
  public void showEndOnLose() {
    showEndScreen();
    setEndElements("gameover.png", "Gameover.", "GAMEOVER\nWrong Keycard Combination.", "RED");
    loseSound.play();
  }

  /** Displays the end screen when the reactor meltdown is complete due to a timeout. */
  public void showEndOnTimeout() {
    showEndScreen();
    setEndElements("gameover.png", "Gameover.", "GAMEOVER\nReactor meltdown complete.", "RED");
    loseSound.play();
  }

  /**
   * Handles the event when the "Replay" button is clicked, allowing the player to restart the game.
   *
   * @throws IOException If an I/O error occurs during the restart process.
   */
  @FXML
  private void onReplayClicked() throws IOException {
    App.restart();
  }
}
