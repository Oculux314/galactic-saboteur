package nz.ac.auckland.se206.components;

import javafx.scene.image.Image;

/** This is a StateButton that has 1 image that rotates on click. */
public class RotateButton extends StateButton {

  public enum Orientation { // Clockwise
    UP,
    RIGHT,
    DOWN,
    LEFT,
  }

  @Override
  protected void init() {
    super.init();
    createStates();
  }

  protected void createStates() {
    recreateStatesWithImage(currentState.normalImage);
  }

  protected void recreateStatesWithImage(Image image) {
    states.clear();

    for (int i = 0; i < 4; i++) {
      String stateName = Orientation.values()[i].toString();
      addState(stateName, image);
    }
  }

  @Override
  protected void onClick() {
    super.onClick();
    this.setRotate(this.getRotate() + 90); // Clockwise
  }

  protected Orientation getOrientation() {
    return Orientation.values()[currentState.index];
  }
}
