package nz.ac.auckland.se206.components;

import javafx.scene.image.Image;

/** This is a special RotateButton component intended for use in the computer puzzle only. */
public class ComputerTile extends RotateButton {

  public enum Connector {
    STRAIGHT,
    BEND,
    RANDOM,
  }

  private Connector type;

  public void setType(Connector type) {
    this.type = processType(type);
    recreateStates();
  }

  private Connector processType(Connector type) {
    if (type == Connector.RANDOM) {
      int random = (int) (Math.random() * (Connector.values().length - 1));
      return Connector.values()[random];
    }

    return type;
  }

  private void recreateStates() {
    String imageName = type.toString().toLowerCase();
    Image image = new Image("/images/puzzle/connector_" + imageName + ".png");
    recreateStatesWithImage(image);
  }
}
