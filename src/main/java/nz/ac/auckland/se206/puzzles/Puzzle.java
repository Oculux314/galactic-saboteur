package nz.ac.auckland.se206.puzzles;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class Puzzle {

  public boolean isPuzzleSolved;
  private Label solvedLabel = new Label("Puzzle Solved! Go find the other puzzles");

  public enum puzzle {
    reactortoolbox, reactorbuttonpad,
  }

  public void clearPuzzle(Parent puzzlePane) {
    if (puzzlePane instanceof Pane) {
      ((Pane) puzzlePane).getChildren().clear();
      ((Pane) puzzlePane).getChildren().add(solvedLabel);
      solvedLabel.setLayoutX(30);
      solvedLabel.setLayoutY(250);
    }
  }
}
