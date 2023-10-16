package nz.ac.auckland.se206.components;

import javafx.scene.image.Image;

/**
 * The RotateButton class represents a specialized StateButton that allows rotation of a single
 * image on click. It enables rotation functionality and manages different orientations of the
 * button.
 */
public class RotateButton extends StateButton {

  /**
   * Enumeration representing the orientations of the RotateButton in a clockwise direction. The
   * orientations are UP, RIGHT, DOWN, and LEFT.
   */
  public enum Orientation {
    UP,
    RIGHT,
    DOWN,
    LEFT,
  }

  /**
   * Initializes the RotateButton by creating its initial states. This method initializes the
   * RotateButton by setting up its initial states.
   */
  @Override
  protected void init() {
    super.init();
    createStates();
  }

  /**
   * Creates the initial states of the RotateButton. This method recreates the states of the button
   * based on the current image associated with it.
   */
  protected void createStates() {
    recreateStatesWithImage(currentState.getNormalImage());
  }

  /**
   * Recreates the states of the RotateButton using the provided image.
   *
   * @param image The image used to recreate the states of the RotateButton.
   */
  protected void recreateStatesWithImage(Image image) {
    states.clear();

    for (int i = 0; i < 4; i++) {
      String stateName = Orientation.values()[i].toString();
      addState(stateName, image);
    }
  }

  /**
   * Handles the click action for the RotateButton. This method is responsible for rotating the
   * button by 90 degrees clockwise on each click.
   */
  @Override
  protected void onClick() {
    super.onClick();
    this.setRotate(this.getRotate() + 90); // Clockwise rotation
  }

  /**
   * Gets the current orientation of the RotateButton.
   *
   * @return The current orientation of the RotateButton.
   */
  protected Orientation getOrientation() {
    return Orientation.values()[currentState.getIndex()];
  }
}
