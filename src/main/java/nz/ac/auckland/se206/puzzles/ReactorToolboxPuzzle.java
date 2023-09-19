package nz.ac.auckland.se206.puzzles;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.components.AnimatedButton;
import javafx.scene.layout.Pane;

public class ReactorToolboxPuzzle extends Puzzle {

  @FXML private ImageView imvHammer;
  @FXML private ImageView imvBottle;
  @FXML private ImageView imvTorch;
  @FXML private AnimatedButton btnExit;
  @FXML private Pane panReactorToolbox;


  public void initialize() {
  }

  @FXML
  private void onSolved() {
    isPuzzleSolved = true;
    clearPuzzle(panReactorToolbox);
  }
}
