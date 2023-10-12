package nz.ac.auckland.se206.gamechildren;

import java.util.HashMap;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.screens.Screen;

public class PopupController {

  public enum Name {
    RIDDLE,
    GPT_SCIENTIST,
    GPT_ENGINEER,
    GPT_CAPTAIN,
    PUZZLE_REACTOR_TOOLBOX,
    PUZZLE_REACTOR_BUTTONPAD,
    PUZZLE_REACTOR_HANGMAN,
    PUZZLE_LABORATORY_TESTTUBES,
    PUZZLE_NAVIGATION_COMPUTER
  }

  @FXML private Pane panRoot;
  @FXML private Group grpContent;
  private Map<Name, Screen> popups = new HashMap<>();

  @FXML
  private void onExitClicked() {
    hide();
  }

  private void show(Name name) {
    setRoot(name);
    show();
  }

  private void show() {
    panRoot.setVisible(true);
  }

  private void hide() {
    panRoot.setVisible(false);
  }

  private void setRoot(Name rootName) {
    setRoot(popups.get(rootName).getFxml());
  }

  private void setRoot(Parent root) {
    grpContent.getChildren().clear();
    grpContent.getChildren().add(root);
  }

  private void load(Name name, Screen popup) {
    popups.put(name, popup);
  }
}
