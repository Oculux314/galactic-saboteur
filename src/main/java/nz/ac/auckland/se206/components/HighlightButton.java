package nz.ac.auckland.se206.components;

import javafx.scene.image.Image;
import nz.ac.auckland.se206.Utils;

public class HighlightButton extends StateButton {

  public HighlightButton() {
    super();
    setupStates();
  }

  private void setupStates() {
    Image normalImage = getImage();
    Image highlightImage = Utils.getImageWithSuffix(normalImage, "_highlighted");

    addState("normal", normalImage);
    addState("highlighted", highlightImage);
  }

  public void highlight() {
    setState("highlighted");
  }

  public void unhighlight() {
    setState("normal");
  }
}
