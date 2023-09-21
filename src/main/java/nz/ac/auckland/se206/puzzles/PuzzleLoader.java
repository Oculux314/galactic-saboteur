package nz.ac.auckland.se206.puzzles;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.controllers.GameController.RoomGroup;
import nz.ac.auckland.se206.puzzles.Puzzle.PuzzleName;

public class PuzzleLoader {

  private Pane panPuzzle;
  private Group grpPuzzleCommons;
  private RoomGroup rooms;

  private HashMap<String, Puzzle> puzzleMap;
  private HashMap<String, PuzzleName> buttonToPuzzleMap;

  private Set<Puzzle> reactorPuzzles;
  private Set<Puzzle> laboratoryPuzzles;
  private Set<Puzzle> navigationPuzzles;

  public PuzzleLoader(Pane panPuzzle, Group grpPuzzleCommons, RoomGroup rooms) {
    this.panPuzzle = panPuzzle;
    this.grpPuzzleCommons = grpPuzzleCommons;
    this.rooms = rooms;

    puzzleMap = new HashMap<>();
    buttonToPuzzleMap = new HashMap<>();
    reactorPuzzles = new HashSet<>();
    laboratoryPuzzles = new HashSet<>();
    navigationPuzzles = new HashSet<>();

    grpPuzzleCommons.setVisible(false);
    loadAllPuzzles();
    populateButtonToPuzzleMap();
  }

  private void loadAllPuzzles() {
    setPuzzle(PuzzleName.REACTOR_TOOLBOX);
    setPuzzle(PuzzleName.REACTOR_BUTTONPAD);
    setPuzzle(PuzzleName.REACTOR_APPLE);
  }

  private void populateButtonToPuzzleMap() {
    buttonToPuzzleMap.put("btnToolbox", PuzzleName.REACTOR_TOOLBOX);
    buttonToPuzzleMap.put("btnButtonpad", PuzzleName.REACTOR_BUTTONPAD);
    buttonToPuzzleMap.put("btnApple", PuzzleName.REACTOR_APPLE);
  }

  public void setPuzzle(PuzzleName name) {
    try {
      setPuzzle(getFxmlUrl(name));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void setPuzzle(String fxmlFilePath) throws IOException {
    Parent puzzleFxml = getFxml(fxmlFilePath);
    panPuzzle.getChildren().clear();
    panPuzzle.getChildren().add(puzzleFxml);
  }

  private Parent getFxml(String fxmlFilePath) throws IOException {
    if (puzzleMap.containsKey(fxmlFilePath)) {
      // If the puzzle is already loaded show it
      return puzzleMap.get(fxmlFilePath).getRoot();
    } else {
      // Otherwise, load the puzzle into panPuzzle
      return loadPuzzle(fxmlFilePath);
    }
  }

  private Parent loadPuzzle(String fxmlFilePath) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFilePath));
    Parent puzzle = loader.load();
    Puzzle puzzleController = loader.getController();

    puzzleController.setRoot(puzzle);
    puzzleMap.put(fxmlFilePath, puzzleController);
    return puzzle;
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

  private String getFxmlUrl(PuzzleName name) {
    return ("/fxml/" + name.toString() + ".fxml");
  }

  public HashMap<String, PuzzleName> getButtonToPuzzleMap() {
    return buttonToPuzzleMap;
  }
}
