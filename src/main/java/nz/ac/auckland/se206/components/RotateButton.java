package nz.ac.auckland.se206.components;

import javafx.scene.image.Image;

/** This is a StateButton that has 1 image that rotates on click. */
public class RotateButton extends StateButton {

  protected void init() {
    super.init();
    setStates();
  }

  private void setStates() {
    Image image = currentState.normalImage;
    states.clear();

    for (int i = 0; i < 4; i++) {
      addState(String.valueOf(i), image);
    }
  }

  @Override
  protected void onClick() {
    super.onClick();
    this.setRotate(this.getRotate() + 90);
  }
}
