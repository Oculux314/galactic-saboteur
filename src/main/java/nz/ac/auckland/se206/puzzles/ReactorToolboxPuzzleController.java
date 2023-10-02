package nz.ac.auckland.se206.puzzles;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/** Controller class for the reactor toolbox puzzle. */
public class ReactorToolboxPuzzleController extends Puzzle {

  @FXML private ImageView imvHammer;
  @FXML private ImageView imvBottle;
  @FXML private ImageView imvTorch;
  @FXML private Pane panReactorToolbox;

  @FXML
  public void initialize() {}

  @FXML
  private void onSolved() {
    setSolved();
    clearPuzzle(panReactorToolbox);
  }
}
