package nz.ac.auckland.se206.components;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.image.Image;

/**
 * This class represents a special RotateButton component designed specifically for use in the
 * computer puzzle.
 */
public class ComputerTile extends RotateButton {

  /** Enumeration representing the types of ComputerTile. */
  public enum Type {
    STRAIGHT,
    BEND,
  }

  private Type type;
  private Map<Orientation, Boolean> connections;
  private int row;
  private int col;

  private boolean isActive;
  private Image redImage;
  private Image greenImage;

  /**
   * Sets the type of the ComputerTile.
   *
   * @param type The type of the ComputerTile.
   */
  public void setType(Type type) {
    this.type = type;
    loadImageColourVersions();
    recreateStates();
    initalizeConnections();
    setActive(false);
    randomiseOrientation();
  }

  /** Loads images for the ComputerTile based on its type. */
  private void loadImageColourVersions() {
    String imageName = type.toString().toLowerCase();
    redImage = new Image("/images/puzzle/connector_" + imageName + "_red.png");
    greenImage = new Image("/images/puzzle/connector_" + imageName + "_green.png");
  }

  /** Recreates the states for the ComputerTile with the new image. */
  private void recreateStates() {
    recreateStatesWithImage(redImage);
  }

  /** Checks if the ComputerTile is active. */
  @Override
  protected void onClick() {
    super.onClick();
    cycleConnections();
  }

  /** Updates the image of the ComputerTile based on its active status. */
  @Override
  protected void updateImage() {
    if (isActive) {
      setImage(greenImage);
    } else {
      setImage(redImage);
    }
  }

  /** This method is not supported for ComputerTile. */
  @Override
  protected void changeImageToHover() {
    // No hover image functionality for ComputerTile
  }

  /** This method is not supported for ComputerTile. */
  @Override
  protected void changeImageToNormal() {
    // No hover image functionality for ComputerTile
  }

  /**
   * Cycles the connections of the ComputerTile in a clockwise direction. This method updates the
   * connections by rotating them clockwise.
   */
  private void cycleConnections() {
    boolean up = connections.get(Orientation.UP);
    boolean right = connections.get(Orientation.RIGHT);
    boolean down = connections.get(Orientation.DOWN);
    boolean left = connections.get(Orientation.LEFT);

    // Cycle connections clockwise
    setConnections(left, up, right, down);
  }

  /**
   * Sets the connections of the ComputerTile.
   *
   * @param up The connection status for the 'up' orientation.
   * @param right The connection status for the 'right' orientation.
   * @param down The connection status for the 'down' orientation.
   * @param left The connection status for the 'left' orientation.
   */
  private void setConnections(boolean up, boolean right, boolean down, boolean left) {
    connections.put(Orientation.UP, up);
    connections.put(Orientation.RIGHT, right);
    connections.put(Orientation.DOWN, down);
    connections.put(Orientation.LEFT, left);
  }

  /**
   * Initializes the connections of the ComputerTile based on its type. This method sets the initial
   * connections based on the type of the ComputerTile.
   */
  private void initalizeConnections() {
    connections = new HashMap<>();

    switch (type) {
      case STRAIGHT:
        setConnections(false, true, false, true); // Initially horizontal
        break;
      case BEND:
        setConnections(true, false, false, true); // Initially second quadrant
        break;
    }
  }

  /**
   * Returns the connections of the ComputerTile.
   *
   * @return A map representing the connections of the ComputerTile.
   */
  public Map<Orientation, Boolean> getConnections() {
    return connections;
  }

  /**
   * Gets the orientation of the connection opposite to the provided orientation.
   *
   * @param baseOrientation The base orientation for which the opposite connection is required.
   * @return The opposite orientation, or null if not found.
   */
  public Orientation getOtherConnection(Orientation baseOrientation) {
    for (Orientation orientation : Orientation.values()) {
      if (orientation == baseOrientation) {
        continue;
      }

      if (connections.get(orientation)) {
        return orientation;
      }
    }

    return null;
  }

  /**
   * Sets the active status of the ComputerTile.
   *
   * @param isActive The new active status of the ComputerTile.
   */
  public void setActive(boolean isActive) {
    this.isActive = isActive;
    updateImage();
  }

  /** Randomizes the orientation of the ComputerTile. */
  private void randomiseOrientation() {
    int rotations;

    if (row == 2 && col == 0) { // Start tile at (2, 0)
      rotations = 1;
    } else {
      rotations = (int) (Math.random() * 4);
    }

    for (int i = 0; i < rotations; i++) {
      onClick();
    }
  }

  /**
   * Returns the row of the ComputerTile.
   *
   * @return The row of the ComputerTile.
   */
  public int getRow() {
    return row;
  }

  /**
   * Sets the row of the ComputerTile.
   *
   * @param row The new row for the ComputerTile.
   */
  public void setRow(int row) {
    this.row = row;
  }

  /**
   * Returns the column of the ComputerTile.
   *
   * @return The column of the ComputerTile.
   */
  public int getCol() {
    return col;
  }

  /**
   * Sets the column of the ComputerTile.
   *
   * @param col The new column for the ComputerTile.
   */
  public void setCol(int col) {
    this.col = col;
  }
}
