package nz.ac.auckland.se206.components;

import javafx.scene.image.Image;

/** This is a special RotateButton component intended for use in the computer puzzle only. */
public class ComputerTile extends RotateButton {

  public enum Type {
    STRAIGHT,
    BEND,
  }

  private class ConnectionData {
    public boolean up;
    public boolean right;
    public boolean down;
    public boolean left;

    public ConnectionData(Type type) {
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
      this.up = up;
      this.right = right;
      this.down = down;
      this.left = left;
    }
  }

  private Type type;
  private ConnectionData connections;

  public void setType(Type type) {
    this.type = type;
    connections = new ConnectionData(type);
    recreateStates();
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

  private void cycleConnections() {
    boolean up = connections.up;
    boolean right = connections.right;
    boolean down = connections.down;
    boolean left = connections.left;

    // Cycle clockwise
    connections.setConnections(left, up, right, down);
  }

  public ConnectionData getConnections() {
    return connections;
  }
}
