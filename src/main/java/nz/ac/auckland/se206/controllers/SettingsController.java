package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.Screen;
import nz.ac.auckland.se206.components.IconButton;

/** Controller class for the title screen. */
public class SettingsController implements Controller {

  @FXML Pane panBackground;

  // TEMP: Programmatically add buttons
  @FXML
  private void initialize() {
    IconButton btnBack = new IconButton("/images/back.png");
    btnBack.setLayoutX(100);
    btnBack.setLayoutY(100);
    panBackground.getChildren().add(btnBack);

    btnBack.setOnMouseClicked(
        (event) -> {
          try {
            onBackClicked(event);
          } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        });
  }

  /**
   * Called when the back button is clicked. Moves control to the title screen.
   *
   * @param event The mouse event.
   * @throws IOException If the title.fxml file is not found.
   */
  @FXML
  public void onBackClicked(MouseEvent event) throws IOException {
    App.setScreen(Screen.Name.TITLE);
  }
}
