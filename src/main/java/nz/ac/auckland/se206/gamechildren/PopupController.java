package nz.ac.auckland.se206.gamechildren;

import java.util.HashMap;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.misc.RootPair;
import nz.ac.auckland.se206.misc.TaggedThread;
import nz.ac.auckland.se206.misc.Utils;

public class PopupController implements RootPair.Controller {

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
  private Map<Name, RootPair> popups = new HashMap<>();

  public PopupController() {
    loadAllPopups();
  }

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

  private void load(Name name, RootPair popup) {
    popups.put(name, popup);
  }

  private void load(Name name, String popupUrl) {
    Utils.startTimeTest();
    TaggedThread popupLoader = new TaggedThread(() -> load(name, new RootPair(popupUrl)));
    popupLoader.start();
    Utils.logTimeTest("Loaded " + name.toString() + " popup", 100);
  }

  private void loadAllPopups() {
    load(Name.RIDDLE, "/fxml/gamechildren/riddle.fxml");

    // GPT
    // load(Name.GPT_SCIENTIST, "/fxml/gamechildren/gptScientist.fxml");
    // load(Name.GPT_ENGINEER, "/fxml/gamechildren/gptEngineer.fxml");
    // load(Name.GPT_CAPTAIN, "/fxml/gamechildren/gptCaptain.fxml");

    // Puzzles
    load(Name.PUZZLE_REACTOR_TOOLBOX, "/fxml/puzzles/reactortoolbox.fxml");
    load(Name.PUZZLE_REACTOR_BUTTONPAD, "/fxml/puzzles/reactorbuttonpad.fxml");
    load(Name.PUZZLE_REACTOR_HANGMAN, "/fxml/puzzles/reactorhangman.fxml");
    load(Name.PUZZLE_LABORATORY_TESTTUBES, "/fxml/puzzles/testtubes.fxml");
    load(Name.PUZZLE_NAVIGATION_COMPUTER, "/fxml/puzzles/navigationcomputer.fxml");
  }
}
