package nz.ac.auckland.se206.puzzles;

import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.Group;
import nz.ac.auckland.se206.components.ComputerTile;
import nz.ac.auckland.se206.components.ComputerTile.Type;
import nz.ac.auckland.se206.components.RotateButton.Orientation;

public class NavigationComputerPuzzle extends Puzzle {

  private static final int NUM_ROWS = 3;
  private static final int NUM_COLS = 4;
  private static final int START_ROW = 2;
  private static final int START_COL = 0;
  private static final int END_ROW = 0;
  private static final int END_COL = 3;

  @FXML private Group grpTiles;
  private ComputerTile[][] tiles;
  private ComputerTile.Type[][] tileTypes;
  private List<ComputerTile> activeTiles;

  public void initialize() {
    tiles = new ComputerTile[NUM_ROWS][NUM_COLS];
    tileTypes = new ComputerTile.Type[NUM_ROWS][NUM_COLS];
    activeTiles = new ArrayList<ComputerTile>();

    randomizeTileTypes();
    chooseSolution();

    for (int i = 0; i < (NUM_ROWS * NUM_COLS); i++) {
      makeTile(i);
    }
  }

  private void randomizeTileTypes() {
    for (int i = 0; i < NUM_ROWS; i++) {
      for (int j = 0; j < NUM_COLS; j++) {
        int random = (int) (Math.random() * Type.values().length);
        tileTypes[i][j] = Type.values()[random];
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

    tiles[row][col] = tile;
    tile.setRow(row);
    tile.setCol(col);
    tile.setType(tileTypes[row][col]);

    // Dimensions & position
    tile.setFitWidth(60);
    tile.setFitHeight(60);
    tile.setLayoutX(60 * col);
    tile.setLayoutY(60 * row);
  }

  @FXML
  private void onTilesClicked() {
    updateActiveTiles();
  }

  public void updateActiveTiles() {
    clearActiveTiles();
    ComputerTile tile = tiles[START_ROW][START_COL];
    Orientation prevDirection = Orientation.LEFT;

    while (tile != null) {
      if (!tile.getConnections().get(prevDirection)) {
        return;
      }

      tile.setActive(true);
      activeTiles.add(tile);

      Orientation nextDirection = tile.getOtherConnection(prevDirection);
      tile = getNextTile(tile, nextDirection);
      prevDirection = getOppositeDirection(nextDirection);
    }
  }

  private void clearActiveTiles() {
    for (ComputerTile tile : activeTiles) {
      tile.setActive(false);
    }

    activeTiles.clear();
  }

  private ComputerTile getNextTile(ComputerTile tile, Orientation direction) {
    int oldRow = tile.getRow();
    int oldCol = tile.getCol();

    int changeInRow = direction == Orientation.DOWN ? 1 : direction == Orientation.UP ? -1 : 0;
    int changeInCol = direction == Orientation.RIGHT ? 1 : direction == Orientation.LEFT ? -1 : 0;

    try {
      return tiles[oldRow + changeInRow][oldCol + changeInCol];
    } catch (ArrayIndexOutOfBoundsException e) {
      return null;
    }
  }

  private Orientation getOppositeDirection(Orientation direction) {
    return Orientation.values()[(direction.ordinal() + 2) % 4];
  }

  private void completePuzzle() {
    // TODO: finish
  }
}
