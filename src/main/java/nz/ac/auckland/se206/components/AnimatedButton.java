package nz.ac.auckland.se206.components;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class AnimatedButton extends ImageView {

  /**
   * Creates a new animated button. If no image is provided in SceneBuilder, "placeholder.png" is
   * used if possible.
   */
  public AnimatedButton() {
    super();

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
    animateScale(1.1);
  }

  /**
   * Called when the mouse exits the button. Scales the button back down to normal size.
   *
   * @param event The mouse event.
   */
  private void onMouseExited(MouseEvent event) {
    animateScale(1);
  }

  /**
   * Called when the mouse is pressed down. Scales the button to normal size and reduces opacity.
   *
   * @param event The mouse event.
   */
  private void onMousePressed(MouseEvent event) {
    animateScale(1, 1);
    animateOpacity(0.6, 1);
  }

  /**
   * Called when the mouse is released. Scales the button to hover size and restores opacity.
   *
   * @param event The mouse event.
   */
  private void onMouseReleased(MouseEvent event) {
    animateScale(1.1);
    animateOpacity(1);
  }

  /**
   * Smoothly scale the button to the given value over 25 milliseconds.
   *
   * @param scale The scale to transition to.
   */
  private void animateScale(double scale) {
    animateScale(scale, 25);
  }

  /**
   * Smoothly scale the button to the given value over the given time.
   *
   * @param scale The scale to transition to.
   * @param millis The time in milliseconds to transition over.
   */
  private void animateScale(double scale, double millis) {
    KeyFrame keyFrame =
        new KeyFrame(
            Duration.millis(millis),
            new KeyValue(scaleXProperty(), scale),
            new KeyValue(scaleYProperty(), scale));

    animate(keyFrame);
  }

  /**
   * Smoothly transition the opacity of the button to the given value over 25 milliseconds.
   *
   * @param opacity The opacity to transition to.
   */
  private void animateOpacity(double opacity) {
    animateOpacity(opacity, 25);
  }

  /**
   * Smoothly transition the opacity of the button to the given value over the given time.
   *
   * @param opacity The opacity to transition to.
   * @param millis The time in milliseconds to transition over.
   */
  private void animateOpacity(double opacity, double millis) {
    KeyFrame keyFrame =
        new KeyFrame(Duration.millis(millis), new KeyValue(opacityProperty(), opacity));

    animate(keyFrame);
  }

  /**
   * Asynchronously animates a keyframe.
   *
   * @param keyFrame The keyframe to animate.
   */
  private void animate(KeyFrame keyFrame) {
    Timeline timeline = new Timeline(keyFrame);
    timeline.play();
  }
}
