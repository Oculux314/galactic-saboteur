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

  public void setType(Type type) {
    this.type = type;
    initalizeConnections();
    recreateStates();
    setActive(false);
  }

  @Override
  protected void createStates() {
    // Nothing: don't create states with normal image
  }

  private void recreateStates() {
    String imageName = type.toString().toLowerCase();
    Image image = new Image("/images/puzzle/connector_" + imageName + ".png");
    recreateStatesWithImage(image);
  }

  @Override
  protected void onClick() {
    super.onClick();
    cycleConnections();
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
    if (isActive) {
      // TODO
      setOpacity(1);
    } else {
      // TODO
      setOpacity(0.5);
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
