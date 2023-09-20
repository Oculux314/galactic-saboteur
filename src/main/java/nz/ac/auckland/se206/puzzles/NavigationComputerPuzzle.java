package nz.ac.auckland.se206.puzzles;

import javafx.fxml.FXML;
import javafx.scene.Group;
import nz.ac.auckland.se206.components.ComputerTile;
import nz.ac.auckland.se206.components.ComputerTile.Connector;

public class NavigationComputerPuzzle extends Puzzle {

  private static final int NUM_ROWS = 3;
  private static final int NUM_COLS = 4;

  @FXML private Group grpTiles;
  private ComputerTile[][] tiles;
  private ComputerTile.Connector[][] tileTypes;

  public void initialize() {
    tiles = new ComputerTile[NUM_ROWS][NUM_COLS];
    tileTypes = new ComputerTile.Connector[NUM_ROWS][NUM_COLS];

    randomizeTileTypes();
    chooseSolution();

    for (int i = 0; i < (NUM_ROWS * NUM_COLS); i++) {
      makeTile(i);
    }
  }

  private void randomizeTileTypes() {
    for (int i = 0; i < NUM_ROWS; i++) {
      for (int j = 0; j < NUM_COLS; j++) {
        tileTypes[i][j] = Connector.RANDOM;
      }
    }
  }

  private void chooseSolution() {
    // TODO: make randomised choose solution
  }

  private void makeTile(int n) {
    int row = n / NUM_COLS;
    int col = n % NUM_COLS;

    ComputerTile tile = new ComputerTile();
    grpTiles.getChildren().add(tile);
    tile.setType(tileTypes[row][col]);
    tiles[row][col] = tile;

    // Dimensions & position
    tile.setFitWidth(60);
    tile.setFitHeight(60);
    tile.setLayoutX(60 * col);
    tile.setLayoutY(60 * row);
  }
}
