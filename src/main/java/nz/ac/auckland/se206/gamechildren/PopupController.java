package nz.ac.auckland.se206.gamechildren;

import java.util.HashMap;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.gamechildren.puzzles.Puzzle;
import nz.ac.auckland.se206.gamechildren.puzzles.PuzzleLoader;
import nz.ac.auckland.se206.misc.RootPair;
import nz.ac.auckland.se206.misc.TaggedThread;
import nz.ac.auckland.se206.misc.Utils;

public class PopupController implements RootPair.Controller {

  public enum Name {
    RIDDLE,
    GPT_SCIENTIST,
    GPT_ENGINEER,
    GPT_CAPTAIN,
    PUZZLE_REACTOR,
    PUZZLE_LABORATORY,
    PUZZLE_NAVIGATION
  }

  @FXML private Pane panRoot;
  @FXML private Group grpContent;
  private Map<Name, RootPair> popups = new HashMap<>();
  private PuzzleLoader puzzleLoader;

  public void initialise(PuzzleLoader puzzleLoader) {
    this.puzzleLoader = puzzleLoader;
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
    TaggedThread popupLoader = new TaggedThread(() -> load(name, new RootPair(popupUrl)));
    popupLoader.start();
  }

  private void loadAllPopups() {
    // Riddle

    // GPT

    // Puzzles
    load(Name.PUZZLE_REACTOR, puzzleLoader.getReactorPuzzle());
    load(Name.PUZZLE_LABORATORY, puzzleLoader.getLaboratoryPuzzle());
    load(Name.PUZZLE_NAVIGATION, puzzleLoader.getNavigationPuzzle());
  }
}
