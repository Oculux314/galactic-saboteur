package nz.ac.auckland.se206.puzzles;

import java.io.IOException;
import java.util.HashMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;



public class PuzzleLoader {

  @FXML private Pane panPuzzle;

  private HashMap<String, Parent> puzzleMap;

  public PuzzleLoader(Pane panPuzzle) {
    this.panPuzzle = panPuzzle;
    puzzleMap = new HashMap<>();
    panPuzzle.setVisible(false);
  }

  public void loadPuzzle(String fxmlFilePath) throws IOException {
    if (puzzleMap.containsKey(fxmlFilePath)) {
      panPuzzle.getChildren().clear();
      panPuzzle.getChildren().add(puzzleMap.get(fxmlFilePath));
      return;
    }
    FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFilePath));
    Parent puzzle = loader.load();
    panPuzzle.getChildren().clear();
    panPuzzle.getChildren().add(puzzle);
    puzzleMap.put(fxmlFilePath, puzzle);
    
  }
}
