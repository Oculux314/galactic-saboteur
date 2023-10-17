package nz.ac.auckland.se206.gamechildren.puzzles;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
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
   * @param puzzle the puzzle to be marked as solved.
   */
  public void completePuzzle(Puzzle puzzle, Pane root) {
    setPuzzleSolved(puzzle);
    disablePuzzle(root);
    displayBanner(createBanner(), root);
  }

  private void disablePuzzle(Pane root) {
    root.setDisable(true); // Just a precaution, veil should block mouse clicks anyway

    // Create black "veil" rectangle
    Rectangle veil = new Rectangle(0, 0, root.getWidth(), root.getHeight());
    veil.setFill(Color.BLACK);
    root.getChildren().add(veil);

    // Fade in veil
    FadeTransition fade = new FadeTransition();
    fade.setNode(veil);
    fade.setDuration(Duration.millis(300));
    fade.setFromValue(0);
    fade.setToValue(0.4);
    fade.play();
  }

  /**
   * Creates a banner to be displayed when a puzzle is solved.
   *
   * @return the banner to be displayed.
   */
  private Label createBanner() {
    Label banner = new Label("Puzzle Complete");

    // Text
    banner.setStyle(banner.getStyle() + "-fx-font-size: 50px; -fx-text-fill: #58DD94;");

    // Text alignment
    banner.setAlignment(Pos.CENTER);
    banner.setTextAlignment(TextAlignment.CENTER);

    // Background
    BackgroundFill fill = new BackgroundFill(Color.web("#000000cc"), null, null);
    banner.setBackground(new Background(fill));
    banner.setOpacity(1);

    // Label dimensions
    banner.setPrefWidth(520);
    banner.setPrefHeight(80);
    banner.setLayoutX(-20);

    return banner;
  }

  /**
   * Displays the banner on the screen.
   *
   * @param banner the banner to be displayed.
   * @param root the root pane of the puzzle.
   */
  private void displayBanner(Label banner, Pane root) {
    // Slide up from bottom of viewport
    TranslateTransition translate = new TranslateTransition();
    translate.setNode(banner);
    translate.setDuration(Duration.millis(200));
    translate.setInterpolator(Interpolator.SPLINE(0,1,0.5,1));
    translate.setFromY(500);
    translate.setToY(140);

    root.getChildren().add(banner);
    banner.setVisible(true);
    translate.play();
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
   *
   * @param puzzle the puzzle to be marked as solved.
   */
  private void setPuzzleSolved(Puzzle puzzle) {
    isPuzzleSolved = true;
    if (PuzzleLoader.reactorPuzzles.contains(puzzle.puzzleName)) {
      // if the user has soleved the reactor puzzle, update the game state
      GameState.reactorRoomGameState = GameState.puzzleSolvedMessage;
      GameState.reactorPuzzleSolved = true;
      GameState.unsolvedRooms.remove("reactor");
    } else if (PuzzleLoader.laboratoryPuzzles.contains(puzzle.puzzleName)) {
      // if the user has soleved the laboratory puzzle, update the game state
      GameState.labRoomGameState = GameState.puzzleSolvedMessage;
      GameState.laboratoryPuzzleSolved = true;
      GameState.unsolvedRooms.remove("laboratory");
    } else if (PuzzleLoader.navigationPuzzles.contains(puzzle.puzzleName)) {
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
