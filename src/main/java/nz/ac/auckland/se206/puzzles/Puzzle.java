package nz.ac.auckland.se206.puzzles;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;
import nz.ac.auckland.se206.GameState;

public class Puzzle {
  
  public boolean isPuzzleSolved;
  private Label solvedLabel =
      new Label("Puzzle Solved!" + System.lineSeparator() + "Clue added to inventory.");
  private Parent root;
  private PuzzleName puzzleName;

  public enum PuzzleName {
    REACTOR_TOOLBOX,
    REACTOR_BUTTONPAD,
    REACTOR_APPLE,
    LABORATORY_TESTTUBES,
    NAVIGATION_COMPUTER;

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
      return ("#btn" + this.toCamelCase());
    }
  }

  public void setPuzzleName(PuzzleName puzzleName) {
    this.puzzleName = puzzleName;
  }

  private static final Label solvedLabel =
      new Label("Puzzle Solved!" + System.lineSeparator() + "Clue added to inventory.");
  private boolean isPuzzleSolved;
  private Parent root;


  public void clearPuzzle(Parent puzzlePane) {
    if (puzzlePane instanceof Pane) {
      ((Pane) puzzlePane).getChildren().clear();
      ((Pane) puzzlePane).getChildren().add(solvedLabel);
      solvedLabel.setLayoutX(0);
      solvedLabel.setLayoutY(230);
      solvedLabel.setPrefWidth(500);
      solvedLabel.setAlignment(Pos.CENTER);
      solvedLabel.setTextAlignment(TextAlignment.CENTER);
    }
  }

  public boolean isSolved() {
    return isPuzzleSolved;
  }

  public void setSolved() {
    isPuzzleSolved = true;
    if (PuzzleLoader.reactorPuzzles.contains(this.puzzleName)) {
      GameState.reactorRoomGameState = GameState.puzzleSolvedMessage;
    } else if (PuzzleLoader.laboratoryPuzzles.contains(this.puzzleName)) {
      GameState.labRoomGameState = GameState.puzzleSolvedMessage;
    } else if (PuzzleLoader.navigationPuzzles.contains(this.puzzleName)) {
      GameState.controlRoomGameState = GameState.puzzleSolvedMessage;
    }
  }

  public Parent getRoot() {
    return root;
  }

  public void setRoot(Parent root) {
    this.root = root;
  }
}