package nz.ac.auckland.se206.gamechildren;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class PopupController {

  @FXML private Pane panRoot;

  @FXML
  private void onExitClicked() {
    hide();
  }

  private void show() {
    panRoot.setVisible(true);
  }

  private void hide() {
    panRoot.setVisible(false);
  }
}
