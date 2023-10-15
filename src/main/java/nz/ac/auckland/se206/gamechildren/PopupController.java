package nz.ac.auckland.se206.gamechildren;

import java.util.HashMap;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.gamechildren.puzzles.Puzzle;
import nz.ac.auckland.se206.gamechildren.puzzles.PuzzleLoader;
import nz.ac.auckland.se206.misc.GameState.HighlightState;
import nz.ac.auckland.se206.misc.RootPair;
import nz.ac.auckland.se206.misc.TaggedThread;
import nz.ac.auckland.se206.screens.GameController;
import nz.ac.auckland.se206.screens.Screen;

public class PopupController implements RootPair.Controller {

  public enum Name {
    RIDDLE,
    SUSPECT,
    PUZZLE_REACTOR,
    PUZZLE_LABORATORY,
    PUZZLE_NAVIGATION
  }

  @FXML private Pane panRoot;
  @FXML private Group grpContent;
  private Map<Name, RootPair> popups = new HashMap<>();
  private PuzzleLoader puzzleLoader;
  private Name currentPopup;

  public void initialise(PuzzleLoader puzzleLoader) {
    this.puzzleLoader = puzzleLoader;
    loadAllPopups();
    hide();
  }

  @Override
  public void onLoad() {
    // Do nothing
  }

  private void loadAllPopups() {
    load(Name.RIDDLE, "/fxml/gamechildren/riddle.fxml");
    load(Name.SUSPECT, "/fxml/gamechildren/suspect.fxml");
    load(Name.PUZZLE_REACTOR, puzzleLoader.getReactorPuzzle());
    load(Name.PUZZLE_LABORATORY, puzzleLoader.getLaboratoryPuzzle());
    load(Name.PUZZLE_NAVIGATION, puzzleLoader.getNavigationPuzzle());
  }

  private void load(Name name, RootPair popup) {
    popups.put(name, popup);
  }

  public void load(Name name, String popupUrl) {
    TaggedThread popupLoader = new TaggedThread(() -> load(name, new RootPair(popupUrl)));
    popupLoader.start();
  }

  private void setRoot(Name rootName) {
    currentPopup = rootName;
    Parent fxml = popups.get(rootName).getFxml();
    grpContent.getChildren().clear();
    grpContent.getChildren().add(fxml);
  }

  public void show(Name name) {
    setRoot(name);
    show();
  }

  private void show() {
    panRoot.setVisible(true);
    getCurrentPopup().getController().onLoad();
  }

  private void hide() {
    panRoot.setVisible(false);
  }

  @FXML
  private void onExitClicked() {
    hide();
    updateHighlightState();
  }

  private void updateHighlightState() {
    GameController gameController =
        ((GameController) App.getScreen(Screen.Name.GAME).getController());
    gameController.progressHighlightStateTo(getNextHighlightState());
  }

  private HighlightState getNextHighlightState() {
    if (currentPopup == null) {
      return null;
    }

    switch (currentPopup) {
      case RIDDLE:
        return HighlightState.SUSPECTS;
      case PUZZLE_REACTOR:
      case PUZZLE_LABORATORY:
      case PUZZLE_NAVIGATION:
        // If all three puzzles solved
        if (((Puzzle) getCurrentPopup().getController()).isAllSolved()) {
          return HighlightState.REACTOR_FINAL;
        }
        return null;
      default:
        return null;
    }
  }

  public RootPair getCurrentPopup() {
    return popups.get(currentPopup);
  }
}
