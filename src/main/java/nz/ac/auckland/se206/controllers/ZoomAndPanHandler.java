package nz.ac.auckland.se206.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Scale;
import javafx.util.Duration;

public class ZoomAndPanHandler {

  private final Group panzoomgroup;
  private final Scale scale;

  private double pressedX, pressedY;
  private double deltaX, deltaY;
  private double zoomFactor = 1.25;
  private double minScaleFactor = 0.9;
  private double maxScaleFactor = 2.0;
  private double scaleFactor = 1.0;

  public ZoomAndPanHandler(Group panzoomgroup) {
    this.panzoomgroup = panzoomgroup;
    this.scale = new Scale();
    this.scale.setPivotX(0);
    this.scale.setPivotY(0);
    panzoomgroup.getTransforms().add(scale);
  }

  public void onPress(MouseEvent event) {
    // Record the current mouse X and Y position on Node
    pressedX = event.getSceneX();
    pressedY = event.getSceneY();
    System.out.println("Mouse pressed at " + pressedX + ", " + pressedY);
  }

  public void onDrag(MouseEvent event) {
    deltaX = event.getSceneX() - pressedX;
    deltaY = event.getSceneY() - pressedY;

    // Calculate the new position after panning
    double newX = panzoomgroup.getTranslateX() + deltaX;
    double newY = panzoomgroup.getTranslateY() + deltaY;

    // Apply the translation without restricting boundaries
    panzoomgroup.setTranslateX(newX);
    panzoomgroup.setTranslateY(newY);

    pressedX = event.getSceneX();
    pressedY = event.getSceneY();
    System.out.println("Mouse dragged to " + pressedX + ", " + pressedY);
  }

  public void onScroll(ScrollEvent event) {

    scaleFactor = (event.getDeltaY() < 0) ? 1 / zoomFactor : zoomFactor;
    double currentScaleFactor = scale.getX();
    double newScaleFactor = currentScaleFactor * scaleFactor;
    scaleFactor = newScaleFactor;
    newScaleFactor = clamp(newScaleFactor, minScaleFactor, maxScaleFactor);
    double pivotX = event.getX();
    double pivotY = event.getY();
    double scaleChange = newScaleFactor / currentScaleFactor - 1;

    // Calculate the translation adjustments
    double translateX =
        panzoomgroup.getTranslateX()
            - scaleChange * (pivotX - panzoomgroup.getBoundsInParent().getMinX());
    double translateY =
        panzoomgroup.getTranslateY()
            - scaleChange * (pivotY - panzoomgroup.getBoundsInParent().getMinY());

    // Smoothly animate the zoom
    Timeline timeline =
        new Timeline(
            new KeyFrame(
                Duration.millis(50),
                new KeyValue(scale.xProperty(), newScaleFactor),
                new KeyValue(scale.yProperty(), newScaleFactor),
                new KeyValue(panzoomgroup.translateXProperty(), translateX),
                new KeyValue(panzoomgroup.translateYProperty(), translateY)));

    timeline.play();

    event.consume();
  }
  ;

  private double clamp(double value, double min, double max) {
    return Math.min(Math.max(value, min), max);
  }
}
