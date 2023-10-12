package nz.ac.auckland.se206.components;

import javafx.scene.image.Image;
import nz.ac.auckland.se206.misc.Utils;

public class HighlightButton extends StateButton {

  public HighlightButton() {
    super();
  }

  public void initialise() {
    setupStates();
  }

  private void setupStates() {
    Image normalImage = getImage();
    Image highlightImage = Utils.getImageWithSuffix(normalImage, "_highlight");

    addState("normal", normalImage);
    addState("highlight", highlightImage);
  }

  public void highlight() {
    setState("highlight");
  }

  public void unhighlight() {
    setState("normal");
  }

  @Override
  protected void onClick() {
    // Do nothing
  }
}
