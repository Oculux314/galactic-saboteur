package nz.ac.auckland.se206.components;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import nz.ac.auckland.se206.Utils;

/**
 * This is an ImageView-based button that displays an image and has nice animations on hover and
 * click. Optional separate hover image.
 */
public class AnimatedButton extends ImageView {

  private Image normalImage;
  private Image hoverImage;
  protected boolean isHovering;

  /** Creates a new animated button. */
  public AnimatedButton() {
    super();
    attachEventListeners();
  }

  private void attachEventListeners() {
    setOnMouseEntered((event) -> onMouseEntered(event));
    setOnMouseExited((event) -> onMouseExited(event));
    setOnMousePressed((event) -> onMousePressed(event));
    setOnMouseReleased((event) -> onMouseReleased(event));
  }

  private void loadImages() {
    normalImage = getImage();
    hoverImage = Utils.getImageWithSuffix(normalImage, "_hover");
  }

  /**
   * Called when the mouse enters the button. Scales the button up to hover size and changes the
   * image to the hover image.
   *
   * @param event The mouse event.
   */
  private void onMouseEntered(MouseEvent event) {
    if (normalImage == null) {
      loadImages();
    }

    animateScale(1.1);
    changeImageToHover();
    isHovering = true;
  }

  /**
   * Called when the mouse exits the button. Scales the button back down to normal size and changes
   * the image to the normal image.
   *
   * @param event The mouse event.
   */
  private void onMouseExited(MouseEvent event) {
    animateScale(1);
    changeImageToNormal();
    isHovering = false;
  }

  protected void updateImage() {
    if (isHovering) {
      changeImageToHover();
    } else {
      changeImageToNormal();
    }
  }

  protected void changeImageToHover() {
    setImage(hoverImage);
  }

  protected void changeImageToNormal() {
    setImage(normalImage);
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
