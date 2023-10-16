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
import nz.ac.auckland.se206.misc.GameState;
import nz.ac.auckland.se206.misc.GameState.HighlightState;
import nz.ac.auckland.se206.misc.RootPair;
import nz.ac.auckland.se206.misc.TaggedThread;
import nz.ac.auckland.se206.screens.GameController;
import nz.ac.auckland.se206.screens.Screen;

/**
 * The PopupController class manages various popups in the game, including riddles, suspects, and
 * puzzles.
 */
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

  /**
   * Initializes the PopupController with the provided PuzzleLoader and loads all popups
   * accordingly.
   *
   * @param puzzleLoader the PuzzleLoader object to load different puzzles.
   */
  public void initialise(PuzzleLoader puzzleLoader) {
    this.puzzleLoader = puzzleLoader;
    loadAllPopups();
    hide();
  }

  @Override
  public void onLoad() {
    // Do nothing
  }

  /** Loads all the predefined popups, including riddles, suspects, and various puzzles. */
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

  /**
   * Loads the popup with the given name and FXML file URL.
   *
   * @param name the Name enum representing the popup's type.
   * @param popupUrl the String representing the FXML file URL of the popup.
   */
  public void load(Name name, String popupUrl) {
    TaggedThread popupLoader = new TaggedThread(() -> load(name, new RootPair(popupUrl)));
    popupLoader.start();
  }

  /**
   * Sets the current root popup to the one specified by the provided name.
   *
   * @param rootName the Name enum representing the current root popup.
   */
  private void setRoot(Name rootName) {
    currentPopup = rootName;
    Parent fxml = popups.get(rootName).getFxml();
    grpContent.getChildren().clear();
    grpContent.getChildren().add(fxml);
  }

  /**
   * Shows the popup with the given name.
   *
   * @param name the Name enum representing the popup's type.
   */
  public void show(Name name) {
    setRoot(name);
    show();
    GameState.suspectsFound = true;
  }

  /** Displays the current popup. */
  private void show() {
    panRoot.setVisible(true);
    getCurrentPopup().getController().onLoad();
  }

  private void hide() {
    panRoot.setVisible(false);
  }

  /** Handles the action when the exit button of the popup is clicked. */
  @FXML
  private void onExitClicked() {
    hide();
    updateHighlightState();
  }

  /** Updates the highlight state of the game controller based on the current popup. */
  private void updateHighlightState() {
    GameController gameController =
        ((GameController) App.getScreen(Screen.Name.GAME).getController());
    gameController.progressHighlightStateTo(getNextHighlightState());
  }

  /**
   * Retrieves the next highlight state based on the current popup.
   *
   * @return the HighlightState representing the next highlight state based on the current popup.
   */
  private HighlightState getNextHighlightState() {
    if (currentPopup == null) {
      return null;
    }

    switch (currentPopup) {
      case RIDDLE:
        return HighlightState.SUSPECTS;
      case SUSPECT:
        return HighlightState.PUZZLES;
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

  /**
   * Retrieves the current popup being displayed.
   *
   * @return the RootPair representing the current displayed popup.
   */
  public RootPair getCurrentPopup() {
    return popups.get(currentPopup);
  }
}
