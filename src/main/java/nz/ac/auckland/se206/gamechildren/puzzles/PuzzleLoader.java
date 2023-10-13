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

public class PuzzleLoader {

  public static List<PuzzleName> reactorPuzzles;
  public static List<PuzzleName> laboratoryPuzzles;
  public static List<PuzzleName> navigationPuzzles;

  private Group grpPuzzleButtons;

  private HashMap<PuzzleName, RootPair> puzzleMap;
  private HashMap<AnimatedButton, PuzzleName> buttonToPuzzleMap;

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

  private void loadAllPuzzles() {
    try {
      loadPuzzle(PuzzleName.REACTOR_TOOLBOX);
      loadPuzzle(PuzzleName.REACTOR_BUTTONPAD);
      loadPuzzle(PuzzleName.LABORATORY_TESTTUBES);
      loadPuzzle(PuzzleName.NAVIGATION_COMPUTER);
      loadPuzzle(PuzzleName.REACTOR_HANGMAN);
    } catch (IllegalStateException | IOException e) {
      e.printStackTrace();
    }
  }

  private void choosePuzzles() {
    // select a random puzzle
    GameState.reactorPuzzle = PuzzleName.REACTOR_TOOLBOX;
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
