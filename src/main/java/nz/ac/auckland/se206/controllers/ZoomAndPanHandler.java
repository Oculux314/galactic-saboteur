package nz.ac.auckland.se206.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.Screen;

public class ZoomAndPanHandler {

  private final Group grpPanZoom;
  private final Scale scale;

  private double pressedX, pressedY;
  private double deltaX, deltaY;
  private double newX, newY;
  private double zoomFactor = 1.25;
  private double minScaleFactor = 1.0;
  private double maxScaleFactor = 2.0;
  private double scaleFactor = 1.0;
  private double margin = 0.0;

  private double imageWidth;
  private double imageHeight;

  public ZoomAndPanHandler(Group grpPanZoom, Pane panSpaceship) {
    this.grpPanZoom = grpPanZoom;
    this.scale = new Scale();
    this.scale.setPivotX(0);
    this.scale.setPivotY(0);
    imageWidth = panSpaceship.getPrefWidth();
    imageHeight = panSpaceship.getPrefHeight();
    grpPanZoom.getTransforms().add(scale);
  }

  public void onPress(MouseEvent event) {
    pressedX = event.getSceneX();
    pressedY = event.getSceneY();
  }

  public void onDrag(MouseEvent event) {
    deltaX = event.getSceneX() - pressedX;
    deltaY = event.getSceneY() - pressedY;

    newX = grpPanZoom.getTranslateX() + deltaX / getScreenZoom();
    newY = grpPanZoom.getTranslateY() + deltaY / getScreenZoom();
    
    // Calculate the image boundaries for the current zoom level
    double minX = -(scale.getX() - 1) * imageWidth - margin;
    double minY = -(scale.getY() - 1) * imageHeight - margin;
    double maxX = margin;
    double maxY = margin;

    // Restrict panning within image boundaries
    newX = clamp(newX, minX, maxX);
    newY = clamp(newY, minY, maxY);

    grpPanZoom.setTranslateX(newX);
    grpPanZoom.setTranslateY(newY);

    pressedX = event.getSceneX();
    pressedY = event.getSceneY();
  }

  public void onScroll(ScrollEvent event) {
    scaleFactor = (event.getDeltaY() < 0) ? 1 / zoomFactor : zoomFactor;
    double currentScaleFactor = scale.getX();
    double newScaleFactor = currentScaleFactor * scaleFactor;
    newScaleFactor = clamp(newScaleFactor, minScaleFactor, maxScaleFactor);
    double scaleChange = newScaleFactor / currentScaleFactor - 1;

    // Calculate the translation adjustments
    double pivotX = event.getX();
    double pivotY = event.getY();
    double translateX =
        grpPanZoom.getTranslateX()
            - scaleChange * (pivotX - grpPanZoom.getBoundsInParent().getMinX());
    double translateY =
        grpPanZoom.getTranslateY()
            - scaleChange * (pivotY - grpPanZoom.getBoundsInParent().getMinY());

    // Calculate the image boundaries for the new zoom level
    double minX = -(newScaleFactor - 1) * imageWidth - margin;
    double minY = -(newScaleFactor - 1) * imageHeight - margin;
    double maxX = margin;
    double maxY = margin;

    // Adjust translation to stay within image boundaries
    translateX = clamp(translateX, minX, maxX);
    translateY = clamp(translateY, minY, maxY);

    // Smoothly animate the zoom
    Timeline timeline =
        new Timeline(
            new KeyFrame(
                Duration.millis(50),
                new KeyValue(scale.xProperty(), newScaleFactor),
                new KeyValue(scale.yProperty(), newScaleFactor),
                new KeyValue(grpPanZoom.translateXProperty(), translateX),
                new KeyValue(grpPanZoom.translateYProperty(), translateY)));

    timeline.play();
    event.consume();
  }

  private double clamp(double value, double min, double max) {
    return Math.min(Math.max(value, min), max);
  }

  /**
   * Returns the zoom of the screen due to window size. This does not include game zoom.
   *
   * @return The screen zoom. 1.0 corresponds to the default screen size 600px x 800px.
   */
  private double getScreenZoom() {
    return ((MainController) App.getScreen(Screen.Name.MAIN).getController()).getScreenZoom();
  }
}