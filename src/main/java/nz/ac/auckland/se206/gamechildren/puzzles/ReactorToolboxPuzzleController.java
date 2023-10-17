package nz.ac.auckland.se206.gamechildren.puzzles;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.misc.Audio;
import nz.ac.auckland.se206.misc.TaggedThread;
import nz.ac.auckland.se206.screens.MainController;
import nz.ac.auckland.se206.screens.Screen;

/** Controller class for the reactor toolbox puzzle. */
public class ReactorToolboxPuzzleController extends Puzzle {

  @FXML private ImageView imvTool1;
  @FXML private ImageView imvTool2;
  @FXML private ImageView imvTool3;
  @FXML private Pane panReactorToolbox;
  @FXML private Rectangle rec2;
  @FXML private Rectangle rec3;
  @FXML private Rectangle rec1;
  @FXML private Label lblVerdict;

  private double pressedX;
  private double pressedY;
  private Node selectedNode;
  private List<ImageView> tools = new ArrayList<>();
  private List<Rectangle> rects = new ArrayList<>();

  private int marginX = 10;
  private int marginY = 5;
  private int boundminX = 130;
  private int boundminY = 150;
  private int boundmaxX = 360;
  private int boundmaxY = 390;

  private Audio wrongAnswerSound = new Audio("puzzle_wrong.mp3");
  private Audio pickUpSound = new Audio("toolbox_pickup.mp3");
  private Audio putDownSound = new Audio("toolbox_putdown.mp3");

  @FXML
  private void initialize() {
    addToolsAndRectangles();
  }

  /**
   * Called when the mouse is pressed.
   *
   * @param event the mouse event.
   */
  @FXML
  private void onMousePressed(MouseEvent event) {
    if (event.isPrimaryButtonDown()) {
      Node source = (Node) event.getSource();

      if (tools.contains(source)) {
        // Select the node
        selectedNode = source;
        selectedNode.toFront();
        pressedX = event.getSceneX();
        pressedY = event.getSceneY();
        pickUpSound.play();
      }
    }
  }

  /**
   * Called when the mouse is dragged.
   *
   * @param event the mouse event.
   */
  @FXML
  private void onMouseDragged(MouseEvent event) {

    double offsetX = -selectedNode.getLayoutX();
    double offsetY = -selectedNode.getLayoutY();
    double deltaX = event.getSceneX() - pressedX;
    double deltaY = event.getSceneY() - pressedY;
    double newX = selectedNode.getTranslateX() + deltaX / getScreenZoom();
    double newY = selectedNode.getTranslateY() + deltaY / getScreenZoom();

    // Calculate the bounds within which the image can be dragged
    double minX = boundminX + offsetX;
    double minY = boundminY + offsetY;
    double maxX = boundmaxX + offsetX - ((ImageView) selectedNode).getFitWidth();
    double maxY = boundmaxY + offsetY - ((ImageView) selectedNode).getFitHeight();

    // Calculate the new position within the pane
    newX = clamp(newX, minX, maxX);
    newY = clamp(newY, minY, maxY);

    selectedNode.setTranslateX(newX);
    selectedNode.setTranslateY(newY);

    pressedX = event.getSceneX();
    pressedY = event.getSceneY();
  }

  /**
   * Called when the mouse is released.
   *
   * @param event the mouse event.
   */
  @FXML
  private void onMouseReleased(MouseEvent event) {
    if (selectedNode != null) {
      putDownSound.play();
    }

    selectedNode = null;
    Node source = (Node) event.getSource();
    snapToPosition(source);

    // Check done
    if (isRectanglesFilled()) {
      checkCorrect();
    }
  }

  /** Called when there are three tools in place and the submit button is clicked. */
  private void checkCorrect() {
    // Check if all tools are in the correct place
    // create a thread to clear the label after 1.5 seconds
    TaggedThread labelThread =
        new TaggedThread(
            () -> {
              try {
                Thread.sleep(1500);
                Platform.runLater(() -> lblVerdict.setText(""));
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            });
    boolean allToolsInRectangles = getAllToolsInRectangles();
    // If all tools are in the correct place, set the puzzle as solved
    if (allToolsInRectangles) {
      completePuzzle(this, panReactorToolbox);
    } else {
      // If not, display a message & play sound
      lblVerdict.setText(
          "Incorrect"
              + System.lineSeparator()
              + "combination"
              + System.lineSeparator()
              + "try again");
      labelThread.start();
      wrongAnswerSound.play();
    }
  }

  /**
   * Checks if a tool is in the corresponding rectangle.
   *
   * @param tool the tool to check.
   * @param rectangle the rectangle to check.
   * @return true if the tool is in the rectangle, false otherwise.
   */
  private boolean isToolInRectangle(ImageView tool, Rectangle rectangle) {

    double toolX = tool.getTranslateX() + tool.getLayoutX();
    double toolY = tool.getTranslateY() + tool.getLayoutY();

    double rectangleX = rectangle.getLayoutX();
    double rectangleY = rectangle.getLayoutY();

    return toolX == rectangleX + marginX && toolY == rectangleY + marginY;
  }

  /**
   * This method retrieves the screens zoom level taken from the main pane.
   *
   * @param event The mouse event.
   */
  private double getScreenZoom() {
    return ((MainController) App.getScreen(Screen.Name.MAIN).getController()).getScreenZoom();
  }

  /**
   * Clamps a value between a minimum and maximum value.
   *
   * @param value the value to clamp.
   * @param min the minimum value.
   * @param max the maximum value.
   * @return the clamped value.
   */
  private double clamp(double value, double min, double max) {
    return Math.min(Math.max(value, min), max);
  }

  /**
   * Snaps a tool to a rectangle if it meets conditions.
   *
   * @param source the node to snap.
   */
  private void snapToPosition(Node source) {
    for (Rectangle rect : rects) {
      checkCloseToRectangle(source, rect);
    }
  }

  /**
   * Checks if a tool is close to a rectangle.
   *
   * @param source the node to check.
   * @param rect the rectangle to check.
   */
  private void checkCloseToRectangle(Node source, Rectangle rect) {
    // if the rectangle is not available, do not try and snap to it
    if (!checkRectangleAvailable(rect)) {
      return;
    }

    // get the coordinates of the tool and rectangle
    double sourceX = source.getTranslateX() + source.getLayoutX();
    double sourceY = source.getTranslateY() + source.getLayoutY();

    double rectX = rect.getLayoutX();
    double rectY = rect.getLayoutY();

    // calculate the distance between the tool and rectangle
    double distanceX = Math.abs(sourceX - rectX);
    double distanceY = Math.abs(sourceY - rectY);

    // if the tool is close enough to the rectangle, snap it to the rectangle
    if (distanceX < 30 && distanceY < 15) {
      setPosition(source, rectX + marginX, rectY + marginY);
    }
  }

  /**
   * Sets position of a tool node.
   *
   * @param source the node to set position.
   * @param x the x position to set.
   * @param y the y position to set.
   */
  private void setPosition(Node source, double x, double y) {
    source.setTranslateX(x - source.getLayoutX());
    source.setTranslateY(y - source.getLayoutY());
  }

  /** Adds tools and rectangles to lists. */
  private void addToolsAndRectangles() {
    tools.add(imvTool2);
    tools.add(imvTool3);
    tools.add(imvTool1);

    rects.add(rec2);
    rects.add(rec3);
    rects.add(rec1);
  }

  /**
   * Checks if a rectangle is available to snap to.
   *
   * @param rect the rectangle to check.
   */
  private boolean checkRectangleAvailable(Rectangle rect) {
    for (ImageView tool : tools) {
      double toolX = tool.getTranslateX() + tool.getLayoutX();
      double toolY = tool.getTranslateY() + tool.getLayoutY();

      // If a tool is in the rectangle already don't snap the position
      if (toolX == rect.getLayoutX() + marginX && toolY == rect.getLayoutY() + marginY) {
        return false;
      }
    }
    return true;
  }

  /**
   * Checks if all tools are in the correct place.
   *
   * @return true if all tools are in the correct place, false otherwise.
   */
  private boolean getAllToolsInRectangles() {
    // Check if all tools are in the correct place
    boolean allToolsInRectangles =
        isToolInRectangle(imvTool2, rec2)
            && isToolInRectangle(imvTool3, rec3)
            && isToolInRectangle(imvTool1, rec1);

    return allToolsInRectangles;
  }

  /**
   * Checks if all rectangles are filled so that check can be called.
   *
   * @return true if all rectangles are filled, false otherwise.
   */
  private boolean isRectanglesFilled() {
    return !checkRectangleAvailable(rec1)
        && !checkRectangleAvailable(rec2)
        && !checkRectangleAvailable(rec3);
  }
}
