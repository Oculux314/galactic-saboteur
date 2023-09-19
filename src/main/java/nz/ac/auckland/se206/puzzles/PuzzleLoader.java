package nz.ac.auckland.se206.puzzles;

import java.io.IOException;
import java.util.HashMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.Node;

public class PuzzleLoader {

  @FXML private Pane panPuzzle;

  private HashMap<String, Puzzle> puzzleMap;

  public PuzzleLoader(Pane panPuzzle) {
    this.panPuzzle = panPuzzle;
    puzzleMap = new HashMap<>();
    panPuzzle.setVisible(false);
  }

  public void loadPuzzle(String fxmlFilePath) throws IOException {
    if (puzzleMap.containsKey(fxmlFilePath)) {
      Puzzle puzzle = puzzleMap.get(fxmlFilePath);
      panPuzzle.getChildren().clear();
      panPuzzle.getChildren().add(puzzle.getRoot());
      return;
    }

    FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFilePath));
    Parent puzzle = loader.load();
    Puzzle puzzleController = loader.getController();
    puzzleController.setRoot(puzzle);
    panPuzzle.getChildren().clear();
    panPuzzle.getChildren().add(puzzle);
    puzzleMap.put(fxmlFilePath, puzzleController);
  }

  public Puzzle getCurrentPuzzle() {

    if (panPuzzle.getChildren().isEmpty()) {
      return null;
    }

    Node puzzle = panPuzzle.getChildren().get(0);
    for (Puzzle puzzleController : puzzleMap.values()) {
      if (puzzleController.getRoot() == puzzle) {
        return puzzleController;
      }
    }
    return null;
    
  }
}
