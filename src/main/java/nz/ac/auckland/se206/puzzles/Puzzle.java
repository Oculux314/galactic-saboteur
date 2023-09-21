package nz.ac.auckland.se206.puzzles;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class Puzzle {

  public boolean isPuzzleSolved;
  private Label solvedLabel = new Label("Puzzle Solved! Clue added to inventory");
  private Parent root;

  public enum PuzzleName {
    REACTOR_TOOLBOX,
    REACTOR_BUTTONPAD,
    REACTOR_APPLE;

    private String toCamelCase() {
      String[] words = this.toString().split("_");
      StringBuilder camelCase = new StringBuilder();
      for (String word : words) {
        camelCase.append(word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase());
      }
      return camelCase.toString();
    }

    public String toFxmlUrl() {
      return ("/fxml/puzzles/" + this.toString().toLowerCase().replace("_", "") + ".fxml");
    }

    public String toFxmlButtonId() {
      return ("btn" + this.toCamelCase());
    }
  }

  public void clearPuzzle(Parent puzzlePane) {
    if (puzzlePane instanceof Pane) {
      ((Pane) puzzlePane).getChildren().clear();
      ((Pane) puzzlePane).getChildren().add(solvedLabel);
      solvedLabel.setLayoutX(30);
      solvedLabel.setLayoutY(250);
    }
  }

  public boolean isSolved() {
    return isPuzzleSolved;
  }

  public void setSolved() {
    isPuzzleSolved = true;
  }

  public Parent getRoot() {
    return root;
  }

  public void setRoot(Parent root) {
    this.root = root;
  }
}
