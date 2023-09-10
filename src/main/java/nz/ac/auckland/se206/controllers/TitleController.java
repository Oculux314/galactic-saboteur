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

  // TEMP: Programmatically add buttons
  @FXML
  private void initialize() {
    // TEMP: Play button
    AnimatedButton btnPlay = new AnimatedButton("/images/play.png");
    btnPlay.setLayoutX(420);
    btnPlay.setLayoutY(420);
    panBackground.getChildren().add(btnPlay);

    btnPlay.setOnMouseClicked(
        (event) -> {
          try {
            onPlayClicked(event);
          } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        });

    // TEMP: Settings button
    AnimatedButton btnSettings = new AnimatedButton("/images/settings.png");
    btnSettings.setLayoutX(300);
    btnSettings.setLayoutY(420);
    panBackground.getChildren().add(btnSettings);

    btnSettings.setOnMouseClicked(
        (event) -> {
          try {
            onSettingsClicked(event);
          } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        });
  }

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
