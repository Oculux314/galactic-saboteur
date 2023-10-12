package nz.ac.auckland.se206.gamechildren.puzzles;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.components.ComputerTile;
import nz.ac.auckland.se206.components.ComputerTile.Type;
import nz.ac.auckland.se206.components.RotateButton.Orientation;
import nz.ac.auckland.se206.misc.GameState;
import nz.ac.auckland.se206.misc.TaggedThread;

public class NavigationComputerPuzzleController extends Puzzle {

  private enum State {
    UNCLICKED,
    CLICKED,
    COMPLETE,
  }

  private class Coordinate {
    private int row;
    private int col;

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
  private final Coordinate start = new Coordinate(2, 0);
  private final Coordinate end = new Coordinate(0, 3);

  @FXML private Pane panBackground;
  @FXML private Label lblWarning;
  @FXML private Group grpTiles;
  @FXML private ImageView tilStart;
  @FXML private ImageView tilEnd;

  private ComputerTile[][] tiles;
  private ComputerTile.Type[][] tileTypes;
  private List<ComputerTile> activeTiles;
  private State state = State.UNCLICKED;

  public void initialize() {

    tiles = new ComputerTile[NUM_ROWS][NUM_COLS];
    tileTypes = new ComputerTile.Type[NUM_ROWS][NUM_COLS];
    activeTiles = new ArrayList<ComputerTile>();

    randomizeTileTypes();
    chooseSolution();

    for (int i = 0; i < (NUM_ROWS * NUM_COLS); i++) {
      makeTile(i); // Automatically gets row and col
    }

    updateActiveTiles();
    startFlashing();
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
    // Forces a path to exist from start to end
    // Only allow direction right and up
    Coordinate here = start;
    Orientation prevDirection = Orientation.RIGHT;

    while (here.getCol() <= end.getCol()) {
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
    if (here.getRow() == end.getRow()) {
      return Orientation.RIGHT;
    } else if (here.getCol() == end.getCol()) {
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

  public void updateActiveTiles() {
    // Start path from the start tile
    clearActiveTiles();
    ComputerTile tile = tiles[start.getRow()][start.getCol()];
    Orientation prevDirection = Orientation.LEFT;

    // Follow the path until it terminates
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

  private void startFlashing() {
    TaggedThread flashManager =
        new TaggedThread(
            () -> {
              while (state != State.COMPLETE && GameState.isRunning) {
                lblWarning.setVisible(!lblWarning.isVisible()); // Flash warning until solved

                if (state == State.UNCLICKED) {
                  tilStart.setVisible(
                      !tilStart.isVisible()); // Flash start tile until tiles clicked
                }

                try {
                  Thread.sleep(500); // 500ms flash period
                } catch (InterruptedException e) {
                  return;
                }
              }
            });

    flashManager.start();
  }

  @FXML
  private void onTilesClicked() {
    if (state == State.UNCLICKED) {
      state = State.CLICKED;
      tilStart.setVisible(true);
    }

    updateActiveTiles();
  }

  private void checkWinConditions() {
    boolean endTileIsActive =
        activeTiles.get(activeTiles.size() - 1).equals(tiles[end.getRow()][end.getCol()]);
    boolean endTileOrientedCorectly =
        tiles[end.getRow()][end.getCol()].getConnections().get(Orientation.RIGHT);

    if (endTileIsActive && endTileOrientedCorectly) {
      completePuzzle();
    }
  }

  private void completePuzzle() {
    state = State.COMPLETE;
    tilEnd.setImage(new Image("/images/puzzle/connector_end_green.png"));
    grpTiles.setDisable(true);

    lblWarning.setText("LOGS RESTORED");
    lblWarning.setVisible(true);
    lblWarning.setStyle("-fx-text-fill: #58DD94;");

    TaggedThread completeDelay =
        new TaggedThread(
            () -> {
              try {
                Thread.sleep(1500);
              } catch (InterruptedException e) {
                return;
              }

              Platform.runLater(
                  () -> {
                    setSolved();
                    clearPuzzle(panBackground);
                  });
            });

    completeDelay.start(); // Delay before setting solved
  }
}
