package nz.ac.auckland.se206.puzzles;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class ReactorApplePuzzleController extends Puzzle {

  @FXML private Button btnSolve;
  @FXML private Pane panReactorApple;

  @FXML
  private void onSolved() {
    setSolved();
    clearPuzzle(panReactorApple);
  }
}
