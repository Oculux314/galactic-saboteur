package nz.ac.auckland.se206.puzzles;

import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.components.ComputerTile;
import nz.ac.auckland.se206.components.ComputerTile.Type;
import nz.ac.auckland.se206.components.RotateButton.Orientation;

public class NavigationComputerPuzzle extends Puzzle {

  private class Coordinate {
    int row;
    int col;

    Coordinate(int row, int col) {
      this.row = row;
      this.col = col;
    }

    public int getRow() {
      return row;
    }

    public int getCol() {
      return col;
    }

    @Override
    public boolean equals(Object obj) {
      if (!(obj instanceof Coordinate)) {
        return false;
      }

      Coordinate other = (Coordinate) obj;
      return row == other.row && col == other.col;
    }
  }

  private static final int NUM_ROWS = 3;
  private static final int NUM_COLS = 4;
  private final Coordinate START = new Coordinate(2, 0);
  private final Coordinate END = new Coordinate(0, 3);

  @FXML private Group grpTiles;
  @FXML private ImageView tilStart;
  @FXML private ImageView tilEnd;

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

    updateActiveTiles();
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
    Coordinate here = START;
    Orientation prevDirection = Orientation.RIGHT;

    while (!here.equals(END)) {
      int row = here.getRow();
      int col = here.getCol();
      Orientation nextDirection = chooseDirection(here);

      if (prevDirection == nextDirection) {
        tileTypes[row][col] = Type.STRAIGHT;
      } else {
        tileTypes[row][col] = Type.BEND;
      }

      prevDirection = nextDirection;
      here = getNextCoordinate(new Coordinate(row, col), nextDirection);
    }
  }

  private Orientation chooseDirection(Coordinate here) {
    if (here.getRow() == END.getRow()) {
      return Orientation.RIGHT;
    } else if (here.getCol() == END.getCol()) {
      return Orientation.UP;
    } else {
      return Orientation.values()[(int) (Math.random() * 2)];
    }
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
    ComputerTile tile = tiles[START.getRow()][START.getCol()];
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

    checkWinConditions();
  }

  private void clearActiveTiles() {
    for (ComputerTile tile : activeTiles) {
      tile.setActive(false);
    }

    activeTiles.clear();
  }

  private ComputerTile getNextTile(ComputerTile tile, Orientation direction) {
    Coordinate next = getNextCoordinate(new Coordinate(tile.getRow(), tile.getCol()), direction);

    try {
      return tiles[next.getRow()][next.getCol()];
    } catch (ArrayIndexOutOfBoundsException e) {
      return null;
    }
  }

  private Coordinate getNextCoordinate(Coordinate here, Orientation direction) {
    int oldRow = here.getRow();
    int oldCol = here.getCol();

    int changeInRow = direction == Orientation.DOWN ? 1 : direction == Orientation.UP ? -1 : 0;
    int changeInCol = direction == Orientation.RIGHT ? 1 : direction == Orientation.LEFT ? -1 : 0;

    return new Coordinate(oldRow + changeInRow, oldCol + changeInCol);
  }

  private Orientation getOppositeDirection(Orientation direction) {
    return Orientation.values()[(direction.ordinal() + 2) % 4];
  }

  private void checkWinConditions() {
    boolean endTileIsActive =
        activeTiles.get(activeTiles.size() - 1).equals(tiles[END.getRow()][END.getCol()]);
    boolean endTileOrientedCorectly =
        tiles[END.getRow()][END.getCol()].getConnections().get(Orientation.RIGHT);

    if (endTileIsActive && endTileOrientedCorectly) {
      completePuzzle();
    }
  }

  private void completePuzzle() {
    tilEnd.setImage(new Image("/images/puzzle/connector_end_green.png"));
  }
}
