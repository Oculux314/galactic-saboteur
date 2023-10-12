package nz.ac.auckland.se206.puzzles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.components.AnimatedButton;
import nz.ac.auckland.se206.misc.GameState;
import nz.ac.auckland.se206.puzzles.Puzzle.PuzzleName;

public class PuzzleLoader {

  public static List<PuzzleName> reactorPuzzles;
  public static List<PuzzleName> laboratoryPuzzles;
  public static List<PuzzleName> navigationPuzzles;

  private Pane panPuzzle;
  private Group grpPuzzleButtons;

  private HashMap<PuzzleName, Puzzle> puzzleMap;
  private HashMap<AnimatedButton, PuzzleName> buttonToPuzzleMap;

  public PuzzleLoader(Pane panPuzzle, Group grpPuzzleCommons, Group grpPuzzleButtons) {
    this.panPuzzle = panPuzzle;
    this.grpPuzzleButtons = grpPuzzleButtons;

    puzzleMap = new HashMap<>();
    buttonToPuzzleMap = new HashMap<>();
    reactorPuzzles = new ArrayList<>();
    laboratoryPuzzles = new ArrayList<>();
    navigationPuzzles = new ArrayList<>();

    grpPuzzleCommons.setVisible(false);
    loadAllPuzzles();
    choosePuzzles();
    displayChosenPuzzleButtons();
  }

  private void loadAllPuzzles() {
    try {
      setPuzzle(PuzzleName.REACTOR_TOOLBOX);
      setPuzzle(PuzzleName.REACTOR_BUTTONPAD);
      setPuzzle(PuzzleName.LABORATORY_TESTTUBES);
      setPuzzle(PuzzleName.NAVIGATION_COMPUTER);
      setPuzzle(PuzzleName.REACTOR_HANGMAN);
    } catch (IllegalStateException | IOException e) {
      e.printStackTrace();
    }
  }

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

  public void setPuzzle(PuzzleName name) throws IOException {
    Parent puzzleFxml = getFxml(name);
    panPuzzle.getChildren().clear();
    panPuzzle.getChildren().add(puzzleFxml);
  }

  private Parent getFxml(PuzzleName name) throws IOException {
    if (puzzleMap.containsKey(name)) {
      // If the puzzle is already loaded show it
      return puzzleMap.get(name).getRoot();
    } else {
      // Otherwise, load the puzzle into panPuzzle
      return loadPuzzle(name);
    }
  }

  private Parent loadPuzzle(PuzzleName name) throws IOException {
    // get and load puzzle fxml
    FXMLLoader loader = new FXMLLoader(getClass().getResource(name.toFxmlUrl()));
    Parent puzzle = loader.load();
    Puzzle puzzleController = loader.getController();
    puzzleController.setRoot(puzzle);
    puzzleController.setPuzzleName(name);

    // save puzzle fxml
    puzzleMap.put(name, puzzleController);
    buttonToPuzzleMap.put(getButtonFromName(name), name);
    addToRoom(name);

    return puzzle;
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

  public Puzzle getCurrentPuzzle() {
    // If there is no puzzle loaded, return null
    if (panPuzzle.getChildren().isEmpty()) {
      return null;
    }

    // Otherwise, return the puzzle controller for the loaded puzzle
    Node puzzle = panPuzzle.getChildren().get(0);
    for (Puzzle puzzleController : puzzleMap.values()) {
      if (puzzleController.getRoot() == puzzle) {
        return puzzleController;
      }
    }
    return null;
  }

  public HashMap<AnimatedButton, PuzzleName> getButtonToPuzzleMap() {
    return buttonToPuzzleMap;
  }

  private AnimatedButton getButtonFromName(PuzzleName name) {
    return (AnimatedButton) grpPuzzleButtons.lookup(name.toFxmlButtonId());
  }
}
