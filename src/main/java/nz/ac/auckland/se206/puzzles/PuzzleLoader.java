package nz.ac.auckland.se206.puzzles;

import java.io.IOException;
import java.util.HashMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.controllers.GameController.RoomGroup;
import nz.ac.auckland.se206.puzzles.Puzzle.PuzzleName;

public class PuzzleLoader {

  @FXML private Pane panPuzzle;

  private HashMap<String, Puzzle> puzzleMap;
  HashMap<String, PuzzleName> buttonToPuzzleMap;
  RoomGroup rooms;

  public PuzzleLoader(Pane panPuzzle, Group grpPuzzleCommons, RoomGroup rooms) {
    this.panPuzzle = panPuzzle;
    puzzleMap = new HashMap<>();
    grpPuzzleCommons.setVisible(false);

    buttonToPuzzleMap = new HashMap<>();
    this.rooms = rooms;

    populateButtonToPuzzleMap();
  }

  public void loadPuzzle(String fxmlFilePath) throws IOException {

    // If the puzzle is already loaded show it
    if (puzzleMap.containsKey(fxmlFilePath)) {
      Puzzle puzzle = puzzleMap.get(fxmlFilePath);
      panPuzzle.getChildren().clear();
      panPuzzle.getChildren().add(puzzle.getRoot());
      return;
    }

    // Otherwise, load the puzzle into panPuzzle
    FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFilePath));
    Parent puzzle = loader.load();
    Puzzle puzzleController = loader.getController();
    puzzleController.setRoot(puzzle);
    panPuzzle.getChildren().clear();
    panPuzzle.getChildren().add(puzzle);
    puzzleMap.put(fxmlFilePath, puzzleController);
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

  private void populateButtonToPuzzleMap() {
    buttonToPuzzleMap.put("btnToolbox", PuzzleName.REACTOR_TOOLBOX);
    buttonToPuzzleMap.put("btnButtonpad", PuzzleName.REACTOR_BUTTONPAD);
    buttonToPuzzleMap.put("btnApple", PuzzleName.REACTOR_APPLE);
  }

  public HashMap<String, PuzzleName> getButtonToPuzzleMap() {
    return buttonToPuzzleMap;
  }
}
