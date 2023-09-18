package nz.ac.auckland.se206.components;

import javafx.scene.image.Image;

/** This is a StateButton that has 1 image that rotates on click. */
public class RotateButton extends StateButton {

  protected void init() {
    super.init();
    Image image = currentState.normalImage;
    addStates();
  }

  private void addStates() {
    for (int i = 0; i < 4; i++) {
      addState(String.valueOf(i), currentState.normalImage);
    }
  }

  @Override
  protected void onClick() {
    super.onClick();
    this.setRotate(this.getRotate() + 90);
  }
}
