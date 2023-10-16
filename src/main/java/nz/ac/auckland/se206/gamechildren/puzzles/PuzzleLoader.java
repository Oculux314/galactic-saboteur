package nz.ac.auckland.se206.gamechildren.puzzles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.scene.Group;
import nz.ac.auckland.se206.components.AnimatedButton;
import nz.ac.auckland.se206.gamechildren.puzzles.Puzzle.PuzzleName;
import nz.ac.auckland.se206.misc.GameState;
import nz.ac.auckland.se206.misc.RootPair;

/**
 * The PuzzleLoader class is responsible for managing and loading puzzles in the game. It
 * facilitates the loading, choosing, and displaying of puzzles, as well as their associated UI
 * elements.
 */
public class PuzzleLoader {

  /** The list of puzzles associated with the reactor room. */
  public static List<PuzzleName> reactorPuzzles;

  /** The list of puzzles associated with the laboratory room. */
  public static List<PuzzleName> laboratoryPuzzles;

  /** The list of puzzles associated with the navigation room. */
  public static List<PuzzleName> navigationPuzzles;

  private Group grpPuzzleButtons;

  private HashMap<PuzzleName, RootPair> puzzleMap;
  private HashMap<AnimatedButton, PuzzleName> buttonToPuzzleMap;

  /**
   * Constructor for the PuzzleLoader class. Initializes the necessary data structures and loads the
   * puzzles.
   *
   * @param grpPuzzleButtons the group of puzzle buttons to be managed by the PuzzleLoader.
   */
  public PuzzleLoader(Group grpPuzzleButtons) {
    this.grpPuzzleButtons = grpPuzzleButtons;

    puzzleMap = new HashMap<>();
    buttonToPuzzleMap = new HashMap<>();
    reactorPuzzles = new ArrayList<>();
    laboratoryPuzzles = new ArrayList<>();
    navigationPuzzles = new ArrayList<>();

    loadAllPuzzles();
    choosePuzzles();
    displayChosenPuzzleButtons();
  }

  /**
   * Loads all the puzzles associated with the different rooms in the game. It catches exceptions
   * and prints the stack trace if no puzzles are found in the room.
   */
  private void loadAllPuzzles() {
    // load all puzzles
    try {
      loadPuzzle(PuzzleName.REACTOR_TOOLBOX);
      loadPuzzle(PuzzleName.REACTOR_BUTTONPAD);
      loadPuzzle(PuzzleName.LABORATORY_TESTTUBES);
      loadPuzzle(PuzzleName.NAVIGATION_COMPUTER);
      loadPuzzle(PuzzleName.REACTOR_HANGMAN);
    } catch (IllegalStateException | IOException e) {
      // catch exception if no puzzles in room
      e.printStackTrace();
    }
  }

  /**
   * Chooses a random puzzle for each room from the available puzzle lists. Updates the GameState
   * with the chosen puzzles for each room.
   */
  private void choosePuzzles() {
    // select a random puzzle
    GameState.reactorPuzzle = getRandomPuzzle(reactorPuzzles);
    GameState.laboratoryPuzzle = getRandomPuzzle(laboratoryPuzzles);
    GameState.navigationPuzzle = getRandomPuzzle(navigationPuzzles);
  }

  private PuzzleName getRandomPuzzle(List<PuzzleName> room) {
    if (room.isEmpty()) {
      throw new IllegalStateException("No puzzles in room.");
    }

    int random = (int) (Math.random() * room.size());
    return room.get(random);
  }

  private void displayChosenPuzzleButtons() {
    grpPuzzleButtons.getChildren().forEach(button -> button.setVisible(false));

    getButtonFromName(GameState.reactorPuzzle).setVisible(true);
    getButtonFromName(GameState.laboratoryPuzzle).setVisible(true);
    getButtonFromName(GameState.navigationPuzzle).setVisible(true);
  }

  private void loadPuzzle(PuzzleName name) throws IOException {
    // get and load puzzle fxml
    RootPair puzzle = new RootPair(name.toFxmlUrl());
    ((Puzzle) puzzle.getController()).setPuzzleName(name);

    // save puzzle fxml
    puzzleMap.put(name, puzzle);
    buttonToPuzzleMap.put(getButtonFromName(name), name);
    addToRoom(name);
  }

  private void addToRoom(PuzzleName name) {
    String room = name.toString().split("_")[0].toLowerCase(); // parse room from puzzle name

    switch (room) {
      case "reactor":
        reactorPuzzles.add(name);
        break;
      case "laboratory":
        laboratoryPuzzles.add(name);
        break;
      case "navigation":
        navigationPuzzles.add(name);
        break;
      default:
        break;
    }
  }

  public HashMap<AnimatedButton, PuzzleName> getButtonToPuzzleMap() {
    return buttonToPuzzleMap;
  }

  private AnimatedButton getButtonFromName(PuzzleName name) {
    return (AnimatedButton) grpPuzzleButtons.lookup(name.toFxmlButtonId());
  }

  public RootPair getReactorPuzzle() {
    return puzzleMap.get(GameState.reactorPuzzle);
  }

  public RootPair getLaboratoryPuzzle() {
    return puzzleMap.get(GameState.laboratoryPuzzle);
  }

  public RootPair getNavigationPuzzle() {
    return puzzleMap.get(GameState.navigationPuzzle);
  }
}
