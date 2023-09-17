package nz.ac.auckland.se206.puzzles;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class PuzzleLoader {

  @FXML private Pane panPuzzle;

  public PuzzleLoader(Pane panPuzzle) {
    this.panPuzzle = panPuzzle;
    panPuzzle.setVisible(false);
    Color grey = Color.web("#BFBFBF");
    BackgroundFill backgroundFill = new BackgroundFill(grey, null, null);
    Background background = new Background(backgroundFill);
    panPuzzle.setBackground(background);
  }

  public void loadPuzzle(String fxmlFilePath) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFilePath));
    Parent puzzle = loader.load();
    panPuzzle.getChildren().clear();
    panPuzzle.getChildren().add(puzzle);
  }
}
