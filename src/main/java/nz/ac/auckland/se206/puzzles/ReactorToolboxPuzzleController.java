package nz.ac.auckland.se206.puzzles;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.Screen;
import nz.ac.auckland.se206.components.AnimatedButton;
import nz.ac.auckland.se206.controllers.MainController;
import javafx.scene.control.Label;
import java.util.List;

/** Controller class for the reactor toolbox puzzle. */
public class ReactorToolboxPuzzleController extends Puzzle {

  @FXML private ImageView imvAxe;
  @FXML private ImageView imvBottle;
  @FXML private ImageView imvTorch;
  @FXML private AnimatedButton btnExit;
  @FXML private Pane panReactorToolbox;
  @FXML private Rectangle recAxe;
  @FXML private Rectangle recBottle;
  @FXML private Rectangle recTorch;
  @FXML private Label lblVerdict;

  private double pressedX, pressedY;
  private Node selectedNode;
  private List<ImageView> tools = new ArrayList<>();
  private List<Rectangle> rects = new ArrayList<>();
  private boolean recAxeOccupied = false;
  private boolean recBottleOccupied = false;
  private boolean recTorchOccupied = false;

  @FXML
  private void initialize() {
    tools.add(imvAxe);
    tools.add(imvBottle);
    tools.add(imvTorch);

    rects.add(recAxe);
    rects.add(recBottle);
    rects.add(recTorch);
  }

   /**
   * Called when the mouse is pressed
   *
   * @param event the mouse event
   * @return
   */
  @FXML
  private void onMousePressed(MouseEvent event) {
    if (event.isPrimaryButtonDown()) {
      Node source = (Node) event.getSource();

      if (source.equals(imvBottle) || source.equals(imvAxe) || source.equals(imvTorch)) {
        // Select the node
        selectedNode = source;
        pressedX = event.getSceneX();
        pressedY = event.getSceneY();
       }
    }
  }

  /**
   * Called when the mouse is dragged
   *
   * @param event the mouse event
   * @return
   */
  @FXML
  private void onMouseDragged(MouseEvent event) {

    checkRectanglesClear();
    double offsetX = -selectedNode.getLayoutX();
    double offsetY = -selectedNode.getLayoutY();

    double deltaX = event.getSceneX() - pressedX;
    double deltaY = event.getSceneY() - pressedY;

    double newX = selectedNode.getTranslateX() + deltaX / getScreenZoom();
    double newY = selectedNode.getTranslateY() + deltaY / getScreenZoom();

    // Calculate the bounds within which the image can be dragged
    double minX = offsetX;
    double minY = offsetY;
    double maxX = panReactorToolbox.getWidth() + offsetX - ((ImageView) selectedNode).getFitWidth();
    double maxY =
        panReactorToolbox.getHeight() + offsetY - ((ImageView) selectedNode).getFitHeight();

    // Calculate the new position within the pane
    newX = clamp(newX, minX, maxX);
    newY = clamp(newY, minY, maxY);

    selectedNode.setTranslateX(newX);
    selectedNode.setTranslateY(newY);

    pressedX = event.getSceneX();
    pressedY = event.getSceneY();
  }

  /**
   * Called when the mouse is released
   *
   * @param event the mouse event
   * @return
   */
  @FXML
  private void onMouseReleased(MouseEvent event) {
    selectedNode = null;

    Node source = (Node) event.getSource();
    System.out.println("before Axe: " + recAxeOccupied);
    System.out.println("before Bottle: " + recBottleOccupied);
    System.out.println("before Torch: " + recTorchOccupied);

    snapToPosition(source);

    System.out.println("after Axe: " + recAxeOccupied);
    System.out.println("after Bottle: " + recBottleOccupied);
    System.out.println("after Torch: " + recTorchOccupied);
    
  } 

  /**
   * Called when the submit button is clicked. Checks if the answer is correct.
   *
   * @param event The mouse event.
   */
  @FXML
  private void onSubmitClicked() {
    // Check if all tools are in the correct place
     Thread labelThread =
        new Thread(
            () -> {
              try {
                Thread.sleep(1500);
                Platform.runLater(() -> lblVerdict.setText(""));
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            });

    
    boolean allToolsInRectangles =
        isToolInRectangle(imvAxe, recAxe)
            && isToolInRectangle(imvBottle, recBottle)
            && isToolInRectangle(imvTorch, recTorch);

    System.out.println("Axe: " + isToolInRectangle(imvAxe, recAxe));
    System.out.println("Bottle: " + isToolInRectangle(imvBottle, recBottle));
    System.out.println("Torch: " + isToolInRectangle(imvTorch, recTorch));

    if (allToolsInRectangles) {
      setSolved();
      clearPuzzle(panReactorToolbox);
    } else {
      lblVerdict.setText("Incorrect");
      labelThread.start();
    }
  }

  /**
   * Checks if a tool is in the corresponding rectangle
   *
   * @param tool the tool to check
   * @param rectangle the rectangle to check
   * @return true if the tool is in the rectangle, false otherwise
   */
  private boolean isToolInRectangle(ImageView tool, Rectangle rectangle) {

    double toolX = tool.getTranslateX() + tool.getLayoutX();
    double toolY = tool.getTranslateY() + tool.getLayoutY();

    double rectangleX = rectangle.getLayoutX();
    double rectangleY = rectangle.getLayoutY();
    
    return toolX == rectangleX + 10 && toolY == rectangleY + 5;
  }

  /**
   * get screen zoom
   *
   * @param event The mouse event.
   */
  private double getScreenZoom() {
    return ((MainController) App.getScreen(Screen.Name.MAIN).getController()).getScreenZoom();
  }

  /**
   * Clamps a value between a minimum and maximum value
   *
   * @param value the value to clamp
   * @param min the minimum value
   * @param max the maximum value
   * @return the clamped value
   */
  private double clamp(double value, double min, double max) {
    return Math.min(Math.max(value, min), max);
  }

  /**
   * Called when the puzzle is solved
   *
   * @param event The mouse event.
   */
  @FXML
  private void onSolved() {
    setSolved();
    clearPuzzle(panReactorToolbox);
  }

  private void snapToPosition(Node source) {
    checkCloseToRectangle(source, recAxe);
    checkCloseToRectangle(source, recBottle);
    checkCloseToRectangle(source, recTorch);
  }

  private void checkCloseToRectangle(Node source, Rectangle rect) {
    for (ImageView tool: tools) {
      double toolX = tool.getTranslateX() + tool.getLayoutX();
      double toolY = tool.getTranslateY() + tool.getLayoutY();

      if (toolX == rect.getLayoutX() + 10 && toolY == rect.getLayoutY() + 5) {
        return;
      }
    }

    double sourceX = source.getTranslateX() + source.getLayoutX();
    double sourceY = source.getTranslateY() + source.getLayoutY();
    double sourceWidth = ((ImageView) source).getFitWidth();
    double sourceHeight = ((ImageView) source).getFitHeight();

    double rectX = rect.getLayoutX();
    double rectY = rect.getLayoutY();
    double rectWidth = rect.getWidth();
    double rectHeight = rect.getHeight();

    double distanceX = Math.abs(sourceX - rectX);
    double distanceY = Math.abs(sourceY - rectY);

    if (distanceX < 30 && distanceY < 15) {
        setPosition(source, rectX + 10, rectY + 5);

        // Mark the rectangle as occupied
        if (rect == recAxe) {
            recAxeOccupied = true;
        } else if (rect == recBottle) {
            recBottleOccupied = true;
        } else if (rect == recTorch) {
            recTorchOccupied = true;
        }
    }
  }

  private void setPosition(Node source, double x, double y) {
    source.setTranslateX(x - source.getLayoutX());
    source.setTranslateY(y - source.getLayoutY());
  }

  private void checkRectanglesClear() {
    for (ImageView tool: tools) {
      for (Rectangle rect: rects) {
        double toolX = tool.getTranslateX() + tool.getLayoutX();
        double toolY = tool.getTranslateY() + tool.getLayoutY();

        if (toolX != rect.getLayoutX() + 10 && toolY != rect.getLayoutY() + 5) {
          switch (rect.getId()) {
            case "recAxe":
              recAxeOccupied = false;
              break;
            case "recBottle":
              recBottleOccupied = false;
              break;
            case "recTorch":
              recTorchOccupied = false;
              break;
          }
        }
      }
    }
  }
}
