package nz.ac.auckland.se206.gamechildren;

import java.util.HashMap;
import java.util.Map;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.gamechildren.puzzles.Puzzle;
import nz.ac.auckland.se206.gamechildren.puzzles.PuzzleLoader;
import nz.ac.auckland.se206.misc.Audio;
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

  /** An enumeration representing different popup names in the game. */
  public enum Name {
    RIDDLE,
    SUSPECT,
    PUZZLE_REACTOR,
    PUZZLE_LABORATORY,
    PUZZLE_NAVIGATION
  }

  private static final int TRANSITION_DURATION_BIG = 300;
  private static final int TRANSITION_DURATION_SMALL = 300;
  private static final int TRANSITION_DISTANCE_BIG = 750;
  private static final int TRANSITION_DISTANCE_SMALL = 50;

  @FXML private Rectangle recBackground;
  @FXML private Group grpPopup;
  @FXML private Group grpContent;

  private Map<Name, RootPair> popups = new HashMap<>();
  private PuzzleLoader puzzleLoader;
  private Name currentPopup;

  private Audio popupOpen = new Audio("popup_open.mp3");
  private Audio popupClose = new Audio("popup_close.mp3");

  /**
   * Initializes the PopupController with the provided PuzzleLoader and loads all popups
   * accordingly.
   *
   * @param puzzleLoader the PuzzleLoader object to load different puzzles.
   */
  public void initialise(PuzzleLoader puzzleLoader) {
    this.puzzleLoader = puzzleLoader;
    loadAllPopups();
    grpPopup.setVisible(false);
    recBackground.setVisible(false);
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
  public void maximise(Name name) {
    setRoot(name);
    maximise();
    GameState.suspectsFound = true;
  }

  /** Displays and shows the animation transition for opening a pop-up. */
  private void maximise() {
    getCurrentPopup().getController().onLoad();
    popupOpen.play();

    // Animation
    grpPopup.setVisible(true);
    recBackground.setVisible(true);
    fadeBackgroundOpacityTo(0.4);
    performMaximiseTransition();
  }

  /** Minimises the current popup. Also plays a sound and performs a transition. */
  private void minimise() {
    fadeBackgroundOpacityTo(0);
    performMinimiseTransition();
    popupClose.play();
  }

  /**
   * Fades the background opacity to the given value.
   *
   * @param opacity the double representing the opacity value to fade to.
   */
  private void fadeBackgroundOpacityTo(double opacity) {
    FadeTransition fade = new FadeTransition();
    fade.setNode(recBackground);
    fade.setDuration(Duration.millis(300));
    fade.setToValue(opacity);

    // Allow main game screen to be clicked
    fade.setOnFinished(
        event -> {
          if (opacity == 0) {
            recBackground.setVisible(false);
          }
        });

    fade.play();
  }

  /** Performs the popup maximise transition. */
  private void performMaximiseTransition() {
    // Big UP motion
    TranslateTransition bigMovement = new TranslateTransition();
    bigMovement.setNode(grpPopup);
    bigMovement.setDuration(Duration.millis(TRANSITION_DURATION_BIG));
    bigMovement.setByY(-TRANSITION_DISTANCE_BIG);
    bigMovement.setInterpolator(Interpolator.SPLINE(0, 1, 0.5, 1));

    // Small DOWN motion
    TranslateTransition smallMovement = new TranslateTransition();
    smallMovement.setNode(grpPopup);
    smallMovement.setDuration(Duration.millis(TRANSITION_DURATION_SMALL));
    smallMovement.setByY(TRANSITION_DISTANCE_SMALL);
    smallMovement.setInterpolator(Interpolator.SPLINE(0.5, 0, 0.5, 1));

    bigMovement.setFromY(TRANSITION_DISTANCE_BIG - TRANSITION_DISTANCE_SMALL); // Below viewport
    SequentialTransition transitionSequence = new SequentialTransition(bigMovement, smallMovement);
    transitionSequence.play();
  }

  /** Performs the popup maximise transition. */
  private void performMinimiseTransition() {
    // Small UP motion
    TranslateTransition smallMovement = new TranslateTransition();
    smallMovement.setNode(grpPopup);
    smallMovement.setDuration(Duration.millis(TRANSITION_DURATION_SMALL));
    smallMovement.setByY(-TRANSITION_DISTANCE_SMALL);
    smallMovement.setInterpolator(Interpolator.SPLINE(0.5, 0, 0.5, 1));

    // Big DOWN motion
    TranslateTransition bigMovement = new TranslateTransition();
    bigMovement.setNode(grpPopup);
    bigMovement.setDuration(Duration.millis(TRANSITION_DURATION_BIG));
    bigMovement.setByY(TRANSITION_DISTANCE_BIG);
    bigMovement.setInterpolator(Interpolator.SPLINE(0.5, 0, 1, 0));

    SequentialTransition transitionSequence = new SequentialTransition(smallMovement, bigMovement);
    transitionSequence.play();
  }

  /** Handles the action when the exit button of the popup is clicked. */
  @FXML
  private void onExitClicked() {
    minimise();
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
