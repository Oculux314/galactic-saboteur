package nz.ac.auckland.se206.puzzles;

import javafx.fxml.FXML;
import javafx.scene.Group;
import nz.ac.auckland.se206.components.ComputerTile;

public class NavigationComputerPuzzle extends Puzzle {

  @FXML private Group grpTiles;
  ComputerTile[][] tiles = new ComputerTile[3][4];

  public void initialize() {
    for (int i = 0; i < 12; i++) {
      makeTile(i);
    }
  }

  private void makeTile(int n) {
    ComputerTile tile = new ComputerTile();
    grpTiles.getChildren().add(tile);
    tile.addState("temp", "placeholder.png");

    // Dimensions
    tile.setFitWidth(60);
    tile.setFitHeight(60);

    // Layout
    int row = n / 4;
    int col = n % 4;
    tiles[row][col] = tile;
    tile.setLayoutX(60 * col);
    tile.setLayoutY(60 * row);
  }
}
