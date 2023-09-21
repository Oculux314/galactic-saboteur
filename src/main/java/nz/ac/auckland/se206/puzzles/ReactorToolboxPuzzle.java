package nz.ac.auckland.se206.puzzles;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.components.AnimatedButton;

public class ReactorToolboxPuzzle extends Puzzle {

  @FXML private ImageView imvHammer;
  @FXML private ImageView imvBottle;
  @FXML private ImageView imvTorch;
  @FXML private AnimatedButton btnExit;
  @FXML private Pane panReactorToolbox;

  @FXML
  public void initialize() {}

  @FXML
  private void onSolved() {
    setSolved();
    clearPuzzle(panReactorToolbox);
    GameState.reactorRoomGameState =
        "User has solved the puzzle in this room already. They have got a clue.";
  }

  @FXML
  private void onOpen() {
    GameState.reactorRoomGameState = "User has opened the puzzle, but has not completed it yet.";
  }
}

