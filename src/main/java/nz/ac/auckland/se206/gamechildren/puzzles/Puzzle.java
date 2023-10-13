package nz.ac.auckland.se206.gamechildren.puzzles;

import java.util.HashSet;
import java.util.Set;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.misc.GameState;
import nz.ac.auckland.se206.misc.RootPair;
import nz.ac.auckland.se206.screens.GameController;
import nz.ac.auckland.se206.screens.Screen;

public class Puzzle implements RootPair.Controller {

  public enum PuzzleName {
    REACTOR_TOOLBOX,
    REACTOR_BUTTONPAD,
    REACTOR_HANGMAN,
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

  private boolean isPuzzleSolved;
  private Label solvedLabel =
      new Label("Puzzle Solved!" + System.lineSeparator() + "Clue added to inventory.");
  private Parent root;
  private PuzzleName puzzleName;
  private Set<Puzzle> solvedPuzzles = new HashSet<>();

  public void setPuzzleName(PuzzleName puzzleName) {
    this.puzzleName = puzzleName;
  }

  @Override
  public void onLoad() {
    // Do nothing
  }

  /**
   * Called when a puzzle is solved. Clears puzzle.
   *
   * @param puzzlePane. The pane containing the puzzle.
   */
  public void clearPuzzle(Parent puzzlePane) {
    // clear the puzzle content and display the solved label
    if (puzzlePane instanceof Pane) {
      ((Pane) puzzlePane).getChildren().clear();
      ((Pane) puzzlePane).getChildren().add(solvedLabel);
      solvedLabel.setLayoutX(0);
      solvedLabel.setLayoutY(230);
      solvedLabel.setPrefWidth(500);

      // center the label
      solvedLabel.setAlignment(Pos.CENTER);
      solvedLabel.setTextAlignment(TextAlignment.CENTER);
    }
  }

  public boolean isSolved() {
    return isPuzzleSolved;
  }

  public boolean isAllSolved() {
    return solvedPuzzles.size() == 3;
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

    GameController gameController =
        (GameController) App.getScreen(Screen.Name.GAME).getController();

    gameController.giveRandomClue();
    solvedPuzzles.add(this);
  }

  public Parent getRoot() {
    return root;
  }

  public void setRoot(Parent root) {
    this.root = root;
  }
}
