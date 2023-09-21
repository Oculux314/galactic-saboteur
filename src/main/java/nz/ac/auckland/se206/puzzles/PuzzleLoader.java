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
import nz.ac.auckland.se206.puzzles.Puzzle.PuzzleName;

public class PuzzleLoader {

  private Pane panPuzzle;
  private Group grpPuzzleCommons;
  private Group grpMapButtons;

  private HashMap<PuzzleName, Puzzle> puzzleMap;
  private HashMap<String, PuzzleName> buttonToPuzzleMap;

  // TODO: these guys
  private List<PuzzleName> reactorPuzzles;
  private List<PuzzleName> laboratoryPuzzles;
  private List<PuzzleName> navigationPuzzles;

  public PuzzleLoader(Pane panPuzzle, Group grpPuzzleCommons, Group grpMapButtons) {
    this.panPuzzle = panPuzzle;
    this.grpPuzzleCommons = grpPuzzleCommons;
    this.grpMapButtons = grpMapButtons;

    puzzleMap = new HashMap<>();
    buttonToPuzzleMap = new HashMap<>();
    reactorPuzzles = new ArrayList<>();
    laboratoryPuzzles = new ArrayList<>();
    navigationPuzzles = new ArrayList<>();

    grpPuzzleCommons.setVisible(false);
    loadAllPuzzles();
  }

  private void loadAllPuzzles() {
    try {
      setPuzzle(PuzzleName.REACTOR_TOOLBOX);
      setPuzzle(PuzzleName.REACTOR_BUTTONPAD);
      setPuzzle(PuzzleName.REACTOR_APPLE);
      setPuzzle(PuzzleName.LABORATORY_TEST);
      setPuzzle(PuzzleName.NAVIGATION_TEST);
    } catch (Exception e) {
      // TODO: handle exception
    }
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
    FXMLLoader loader = new FXMLLoader(getClass().getResource(name.toFxmlUrl()));
    Parent puzzle = loader.load();
    Puzzle puzzleController = loader.getController();
    puzzleController.setRoot(puzzle);

    puzzleMap.put(name, puzzleController);
    buttonToPuzzleMap.put(name.toFxmlButtonId(), name);
    addToRoom(name);

    return puzzle;
  }

  private void addToRoom(PuzzleName name) {
    String room = name.toString().split("_")[0].toLowerCase();

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

  public HashMap<String, PuzzleName> getButtonToPuzzleMap() {
    return buttonToPuzzleMap;
  }
}
