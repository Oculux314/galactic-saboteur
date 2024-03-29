package nz.ac.auckland.se206.gamechildren.puzzles;

import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.components.ComputerTile;
import nz.ac.auckland.se206.components.ComputerTile.Type;
import nz.ac.auckland.se206.components.RotateButton.Orientation;
import nz.ac.auckland.se206.misc.Audio;
import nz.ac.auckland.se206.misc.GameState;
import nz.ac.auckland.se206.misc.TaggedThread;

/**
 * Controller class for the navigation computer puzzle. Handles the interaction and logic of the
 * navigation computer puzzle.
 */
public class NavigationComputerPuzzleController extends Puzzle {

  // Enum representing the various states of the puzzle
  private enum State {
    UNCLICKED,
    CLICKED,
    COMPLETE,
  }

  // Represents a coordinate in the puzzle grid
  private class Coordinate {
    private int row;
    private int col;

    Coordinate(int row, int col) {
      this.row = row;
      this.col = col;
    }

    /**
     * Returns the row of the coordinate.
     *
     * @return The row of the coordinate.
     */
    public int getRow() {
      return row;
    }

    /**
     * Returns the column of the coordinate.
     *
     * @return The column of the coordinate.
     */
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

  private static final double TILE_SIZE = 57.6;
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
  private Audio rotateSound = new Audio("nav_rotate.mp3");

  /** Initializes the components of the navigation computer puzzle. */
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

  /**
   * Randomizes the types of tiles in the puzzle. The types are chosen randomly from the available
   * types.
   */
  private void randomizeTileTypes() {
    for (int i = 0; i < NUM_ROWS; i++) {
      for (int j = 0; j < NUM_COLS; j++) {
        int random = (int) (Math.random() * Type.values().length);
        tileTypes[i][j] = Type.values()[random];
      }
    }
  }

  /**
   * Chooses a solution for the puzzle. The solution is a path from the start tile to the end tile.
   * The path is chosen randomly, but must always exist.
   */
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

  /**
   * Chooses a direction for the path to take at the specified coordinate.
   *
   * @param here The coordinate to choose a direction for.
   */
  private Orientation chooseDirection(Coordinate here) {
    if (here.getRow() == end.getRow()) {
      return Orientation.RIGHT;
    } else if (here.getCol() == end.getCol()) {
      return Orientation.UP;
    } else {
      return Orientation.values()[(int) (Math.random() * 2)];
    }
  }

  /**
   * Creates a tile at the specified index in the puzzle grid.
   *
   * @param n The index of the tile in the puzzle grid.
   */
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
    tile.setFitWidth(TILE_SIZE);
    tile.setFitHeight(TILE_SIZE);
    tile.setLayoutX(TILE_SIZE * col);
    tile.setLayoutY(TILE_SIZE * row);
  }

  /**
   * Updates the active tiles in the puzzle. Starts from the start tile and follows the path until
   * it terminates.
   */
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

  /** Clears the active tiles in the puzzle (adding in extra). */
  private void clearActiveTiles() {
    for (ComputerTile tile : activeTiles) {
      tile.setActive(false);
    }

    activeTiles.clear();
  }

  /**
   * Returns the tile adjacent to the specified tile in the specified direction.
   *
   * @param tile The tile to get the adjacent tile of.
   * @param direction The direction of the adjacent tile.
   * @return The tile adjacent to the specified tile in the specified direction.
   */
  private ComputerTile getNextTile(ComputerTile tile, Orientation direction) {
    Coordinate next = getNextCoordinate(new Coordinate(tile.getRow(), tile.getCol()), direction);

    try {
      return tiles[next.getRow()][next.getCol()];
    } catch (ArrayIndexOutOfBoundsException e) {
      return null;
    }
  }

  /**
   * Returns the coordinate adjacent to the specified coordinate in the specified direction.
   *
   * @param here The coordinate to get the adjacent coordinate of.
   * @param direction The direction of the adjacent coordinate.
   * @return The coordinate adjacent to the specified coordinate in the specified direction.
   */
  private Coordinate getNextCoordinate(Coordinate here, Orientation direction) {
    int oldRow = here.getRow();
    int oldCol = here.getCol();

    int changeInRow = direction == Orientation.DOWN ? 1 : direction == Orientation.UP ? -1 : 0;
    int changeInCol = direction == Orientation.RIGHT ? 1 : direction == Orientation.LEFT ? -1 : 0;

    return new Coordinate(oldRow + changeInRow, oldCol + changeInCol);
  }

  /**
   * Returns the opposite direction of the specified direction.
   *
   * @param direction The direction to get the opposite direction of.
   * @return The opposite direction of the specified direction.
   */
  private Orientation getOppositeDirection(Orientation direction) {
    return Orientation.values()[(direction.ordinal() + 2) % 4];
  }

  /** Starts the flashing of the warning and start tiles (adding in extra). */
  private void startFlashing() {
    TaggedThread flashManager =
        new TaggedThread(
            () -> {
              while (state != State.COMPLETE && GameState.isRunning) {
                lblWarning.setVisible(!lblWarning.isVisible()); // Flash warning & end until solved
                tilEnd.setVisible(!tilEnd.isVisible());

                if (state == State.UNCLICKED) {
                  // Flash start and end until tiles clicked
                  tilStart.setVisible(!tilStart.isVisible());
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

  /**
   * Handles the event of the tiles being clicked. If the tiles have not been clicked before, the
   * start tile is revealed.
   */
  @FXML
  private void onTilesClicked() {
    if (state == State.UNCLICKED) {
      state = State.CLICKED;
      tilStart.setVisible(true);
    }

    rotateSound.play();
    updateActiveTiles();
  }

  /**
   * Handles the event of the start tile being clicked. If the start tile is clicked, the puzzle is
   * solved.
   */
  private void checkWinConditions() {
    boolean endTileIsActive =
        activeTiles.get(activeTiles.size() - 1).equals(tiles[end.getRow()][end.getCol()]);
    boolean endTileOrientedCorectly =
        tiles[end.getRow()][end.getCol()].getConnections().get(Orientation.RIGHT);

    if (endTileIsActive && endTileOrientedCorectly) {
      completePuzzle();
    }
  }

  /**
   * Completes the puzzle. Updates the components to reflect completion and completes the puzzle.
   */
  private void completePuzzle() {
    // Update specific components to reflect completion
    state = State.COMPLETE;
    tilEnd.setImage(new Image("/images/puzzle/connector_end_green.png"));
    grpTiles.setDisable(true);

    // Update the label to reflect completion
    lblWarning.setText("LOGS RESTORED");
    lblWarning.setVisible(true);
    lblWarning.setStyle(lblWarning.getStyle() + "-fx-text-fill: #58DD94;");
    lblWarning.setOpacity(1);

    // Complete the puzzle
    completePuzzle(this, panBackground);
  }
}
