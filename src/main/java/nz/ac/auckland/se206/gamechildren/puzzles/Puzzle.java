package nz.ac.auckland.se206.gamechildren.puzzles;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.gamechildren.NotificationpanelController;
import nz.ac.auckland.se206.misc.GameState;
import nz.ac.auckland.se206.misc.RootPair;
import nz.ac.auckland.se206.screens.GameController;
import nz.ac.auckland.se206.screens.Screen;

/**
 * The Puzzle class represents a puzzle in the game. It implements the RootPair.Controller
 * interface, providing methods for managing and interacting with puzzles in the game.
 */
public class Puzzle implements RootPair.Controller {

  /** An enumeration representing different puzzle names in the game. */
  public enum PuzzleName {
    REACTOR_TOOLBOX,
    REACTOR_BUTTONPAD,
    REACTOR_HANGMAN,
    LABORATORY_TESTTUBES,
    NAVIGATION_COMPUTER;

    /**
     * Converts the enum value to camel case.
     *
     * @return the string in camel case format.
     */
    private String toCamelCase() {
      String[] words = this.toString().split("_");
      StringBuilder camelCase = new StringBuilder();
      for (String word : words) {
        camelCase.append(word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase());
      }
      return camelCase.toString();
    }

    /**
     * Generates the relative path to the corresponding FXML file.
     *
     * @return the relative path to the FXML file associated with this PuzzleName.
     */
    public String toFxmlUrl() {
      return ("/fxml/puzzles/" + this.toString().toLowerCase().replace("_", "") + ".fxml");
    }

    /**
     * Generates the FXML button ID for the corresponding PuzzleName.
     *
     * @return the FXML button ID associated with this PuzzleName.
     */
    public String toFxmlButtonId() {
      return ("#btn" + this.toCamelCase());
    }
  }

  private boolean isPuzzleSolved;
  private Label solvedLabel =
      new Label("Puzzle Solved!" + System.lineSeparator() + "Clue added to inventory.");
  private Parent root;
  private PuzzleName puzzleName;

  /**
   * Sets the PuzzleName for this Puzzle.
   *
   * @param puzzleName the PuzzleName to be set for this Puzzle.
   */
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

  /**
   * Checks whether the puzzle is marked as solved.
   *
   * @return true if the puzzle is solved, false otherwise.
   */
  public boolean isSolved() {
    return isPuzzleSolved;
  }

  /**
   * Checks whether all puzzles in the game are solved.
   *
   * @return true if the number of solved puzzles is equal to the total number of puzzles in the
   *     game, false otherwise.
   */
  public boolean isAllSolved() {
    return GameState.solvedPuzzles == 3;
  }

  /**
   * Marks the puzzle as solved, updates the game state, and triggers various game events.
   * Additionally, updates the status of the relevant rooms and generates notifications and hints
   * accordingly.
   */
  public void setSolved() {
    isPuzzleSolved = true;
    if (PuzzleLoader.reactorPuzzles.contains(this.puzzleName)) {
      // if the user has soleved the reactor puzzle, update the game state
      GameState.reactorRoomGameState = GameState.puzzleSolvedMessage;
      GameState.reactorPuzzleSolved = true;
      GameState.unsolvedRooms.remove("reactor");
    } else if (PuzzleLoader.laboratoryPuzzles.contains(this.puzzleName)) {
      // if the user has soleved the laboratory puzzle, update the game state
      GameState.labRoomGameState = GameState.puzzleSolvedMessage;
      GameState.laboratoryPuzzleSolved = true;
      GameState.unsolvedRooms.remove("laboratory");
    } else if (PuzzleLoader.navigationPuzzles.contains(this.puzzleName)) {
      // if the user has soleved the navigation puzzle, update the game state
      GameState.controlRoomGameState = GameState.puzzleSolvedMessage;
      GameState.navigationPuzzleSolved = true;
      GameState.unsolvedRooms.remove("navigation");
    }

    GameState.solvedPuzzles++;
    if (isAllSolved()) {
      GameState.cluesFound = true;
    }

    // get references for game controller and notification panel controller
    GameController gameController =
        (GameController) App.getScreen(Screen.Name.GAME).getController();
    NotificationpanelController notificationpanelcontroller =
        gameController.getNotificationpanelController();

    // If there is no notification in progress, generate a congratulatory notification
    if (!notificationpanelcontroller.isNotificationInProgress()) {
      notificationpanelcontroller.generateNotification();
    }

    // update the hint text
    gameController.giveRandomClue();
  }

  /**
   * Retrieves the root Parent of the Puzzle.
   *
   * @return the root Parent of the Puzzle.
   */
  public Parent getRoot() {
    return root;
  }

  /**
   * Sets the root Parent of the Puzzle.
   *
   * @param root the Parent to be set as the root of the Puzzle.
   */
  public void setRoot(Parent root) {
    this.root = root;
  }
}
