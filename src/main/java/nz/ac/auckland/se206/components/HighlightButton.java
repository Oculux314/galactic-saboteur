package nz.ac.auckland.se206.components;

import javafx.scene.image.Image;
import nz.ac.auckland.se206.misc.Utils;

/**
 * The HighlightButton class represents a specialized StateButton designed for highlighting
 * functionality. It allows for highlighting and unhighlighting of the button, altering its state
 * between normal and highlighted states.
 */
public class HighlightButton extends StateButton {

  /**
   * Initializes the HighlightButton by setting up its states. This method sets up the normal and
   * highlight states of the button.
   */
  public void initialise() {
    setupStates();
  }

  /**
   * Sets up the states of the HighlightButton. This method sets up the normal and highlight states
   * of the button by assigning appropriate images.
   */
  private void setupStates() {
    Image normalImage = getImage();
    Image highlightImage = Utils.getImageWithSuffix(normalImage, "_highlight");

    addState("normal", normalImage);
    addState("highlight", highlightImage);
  }

  /**
   * Highlights the HighlightButton. This method changes the state of the button to the highlighted
   * state.
   */
  public void highlight() {
    setState("highlight");
  }

  /**
   * Unhighlights the HighlightButton. This method changes the state of the button to the normal
   * state.
   */
  public void unhighlight() {
    setState("normal");
  }

  /**
   * This method overrides the onClick method of the StateButton class. It does not perform any
   * specific action when the button is clicked.
   */
  @Override
  protected void onClick() {
    // Do nothing
  }
}
