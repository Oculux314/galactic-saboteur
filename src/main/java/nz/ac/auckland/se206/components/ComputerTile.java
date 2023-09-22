package nz.ac.auckland.se206.components;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.image.Image;

/** This is a special RotateButton component intended for use in the computer puzzle only. */
public class ComputerTile extends RotateButton {

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

  public void setType(Type type) {
    this.type = type;
    loadImageColourVersions();
    recreateStates();
    initalizeConnections();
    setActive(false);
    randomiseOrientation();
  }

  private void loadImageColourVersions() {
    String imageName = type.toString().toLowerCase();
    redImage = new Image("/images/puzzle/connector_" + imageName + "_red.png");
    greenImage = new Image("/images/puzzle/connector_" + imageName + "_green.png");
  }

  @Override
  protected void createStates() {
    // Nothing: don't create states with normal image
  }

  private void recreateStates() {
    recreateStatesWithImage(redImage);
  }

  @Override
  protected void onClick() {
    super.onClick();
    cycleConnections();
  }

  @Override
  protected void updateImage() {
    if (isActive) {
      setImage(greenImage);
    } else {
      setImage(redImage);
    }
  }

  @Override
  protected void changeImageToHover() {
    // Nothing: hover image functionality not supported for ComputerTile
  }

  @Override
  protected void changeImageToNormal() {
    // Nothing: hover image functionality not supported for ComputerTile
  }

  private void initalizeConnections() {
    connections = new HashMap<>();

    switch (type) {
      case STRAIGHT:
        setConnections(false, true, false, true);
        break;
      case BEND:
        setConnections(true, false, false, true);
        break;
    }
  }

  private void setConnections(boolean up, boolean right, boolean down, boolean left) {
    connections.put(Orientation.UP, up);
    connections.put(Orientation.RIGHT, right);
    connections.put(Orientation.DOWN, down);
    connections.put(Orientation.LEFT, left);
  }

  private void cycleConnections() {
    boolean up = connections.get(Orientation.UP);
    boolean right = connections.get(Orientation.RIGHT);
    boolean down = connections.get(Orientation.DOWN);
    boolean left = connections.get(Orientation.LEFT);

    // Cycle clockwise
    setConnections(left, up, right, down);
  }

  public Map<Orientation, Boolean> getConnections() {
    return connections;
  }

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

  public void setActive(boolean isActive) {
    this.isActive = isActive;
    updateImage();
  }

  private void randomiseOrientation() {
    int rotations;

    if (row == 2 && col == 0) {
      rotations = 1;
    } else {
      rotations = (int) (Math.random() * 4);
    }

    for (int i = 0; i < rotations; i++) {
      onClick();
    }
  }

  public int getRow() {
    return row;
  }

  public void setRow(int row) {
    this.row = row;
  }

  public int getCol() {
    return col;
  }

  public void setCol(int col) {
    this.col = col;
  }
}