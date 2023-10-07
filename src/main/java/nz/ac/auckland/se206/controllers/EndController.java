package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.Screen;

public class EndController implements Controller {

  @FXML private Label lblEnd;
  @FXML private ImageView imgEnd;

  private void showEndScreen() {
    App.setScreen(Screen.Name.END);
  }

  private void setEndElements(String imageUrl, String speech, String labelText, String labelColor) {
    imgEnd.setImage(new Image("images/" + imageUrl));
    App.speak(speech);
    lblEnd.setText(labelText);
    lblEnd.setStyle("-fx-text-fill: " + labelColor + ";");
  }

  public void showEndOnWin() {
    showEndScreen();
    setEndElements("expo1.png", "You win!", "You Saved The Ship And Escaped!", "WHITE");
  }

  public void showEndOnLose() {
    showEndScreen();
    setEndElements("gameover.png", "Gameover.", "Gameover.\nWrong Keycard Combination.", "RED");
  }

  public void showEndOnTimeout() {
    showEndScreen();
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @FXML
  private void onReplayClicked() throws IOException {
    App.restart();
  }
}
