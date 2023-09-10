package nz.ac.auckland.se206.components;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class AnimatedButton extends ImageView {

  /**
   * Creates a new icon button with an image given by a URL.
   *
   * @param url The URL of the image for the button to display.
   */
  public AnimatedButton(String url) {
    this(new Image(url));
  }

  /**
   * Creates a new icon button with an image.
   *
   * @param image The image for the button to display.
   */
  public AnimatedButton(Image image) {
    super(image);

    // Fixed size for all buttons
    setFitHeight(80);
    setFitWidth(80);

    // Event listeners
    setOnMouseEntered((event) -> onMouseEntered(event));
    setOnMouseExited((event) -> onMouseExited(event));
    setOnMousePressed((event) -> onMousePressed(event));
    setOnMouseReleased((event) -> onMouseReleased(event));
  }

  /**
   * Called when the mouse enters the button. Scales the button up to hover size.
   *
   * @param event The mouse event.
   */
  public void onMouseEntered(MouseEvent event) {
    setScaleX(1.1);
    setScaleY(1.1);
  }

  /**
   * Called when the mouse exits the button. Scales the button back down to normal size.
   *
   * @param event The mouse event.
   */
  private void onMouseExited(MouseEvent event) {
    setScaleX(1);
    setScaleY(1);
  }

  /**
   * Called when the mouse is pressed down. Scales the button to normal size and reduces opacity.
   *
   * @param event The mouse event.
   */
  private void onMousePressed(MouseEvent event) {
    setScaleX(1);
    setScaleY(1);
    setOpacity(0.6);
  }

  /**
   * Called when the mouse is released. Scales the button to hover size and restores opacity.
   *
   * @param event The mouse event.
   */
  private void onMouseReleased(MouseEvent event) {
    setScaleX(1.1);
    setScaleY(1.1);
    setOpacity(1);
  }
}
