package nz.ac.auckland.se206.puzzles;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

public class PuzzleLoader {

  @FXML private Pane panPuzzle;

  public PuzzleLoader(Pane panPuzzle) {
    this.panPuzzle = panPuzzle;
  }

  public void loadPuzzle(String fxmlFilePath) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFilePath));
    Parent puzzle = loader.load();
    panPuzzle.getChildren().clear();
    panPuzzle.getChildren().add(puzzle);
  }
}
