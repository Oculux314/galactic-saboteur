package nz.ac.auckland.se206.puzzles;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class ReactorButtonPuzzle extends Puzzle{

  @FXML private Button btnSolve;
  @FXML private Pane panReactorButtonpad;
    
    @FXML
    private void initialize() {

    }

    @FXML
    private void onSolved() {
        isPuzzleSolved = true;
        clearPuzzle(panReactorButtonpad);
    }
}