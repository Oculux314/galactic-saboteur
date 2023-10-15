package nz.ac.auckland.se206.screens;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.misc.GameState;

public class EndController implements Screen {

  @FXML private Label lblEnd;
  @FXML private ImageView imgEnd;

  @Override
  public void onLoad() {
    // Do nothing
  }

  private void showEndScreen() {
    App.setScreen(Screen.Name.END);
    GameState.isGameover = true;
  }

  private void setEndElements(String imageUrl, String speech, String labelText, String labelColor) {
    imgEnd.setImage(new Image("images/" + imageUrl));
    App.speak(speech);
    lblEnd.setText(labelText);
    lblEnd.setStyle(lblEnd.getStyle() + "-fx-text-fill: " + labelColor + ";");
  }

  public void showEndOnWin() {
    showEndScreen();
    setEndElements(
        "expo1.jpg", "You win!", "CONGRATULATIONS\nYou Saved The Ship And Escaped!", "WHITE");
  }

  public void showEndOnLose() {
    showEndScreen();
    setEndElements("gameover.png", "Gameover.", "GAMEOVER\nWrong Keycard Combination.", "RED");
  }

  public void showEndOnTimeout() {
    showEndScreen();
    setEndElements("gameover.png", "Gameover.", "GAMEOVER\nReactor meltdown complete.", "RED");
  }

  @FXML
  private void onReplayClicked() throws IOException {
    App.restart();
  }
}