package nz.ac.auckland.se206.puzzles;

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

  @FXML
  private void onMousePressed(MouseEvent event) {
    if (event.isPrimaryButtonDown()) {
      Node source = (Node) event.getSource();

      if (source.equals(imvBottle) || source.equals(imvAxe) || source.equals(imvTorch)) {
        selectedNode = source;
        pressedX = event.getSceneX();
        pressedY = event.getSceneY();
      }
    }
  }

  @FXML
  private void onMouseDragged(MouseEvent event) {
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

  @FXML
  private void onMouseReleased(MouseEvent event) {
    selectedNode = null;
  }

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

    if (allToolsInRectangles) {
      setSolved();
      clearPuzzle(panReactorToolbox);
    } else {
      lblVerdict.setText("Incorrect");
      labelThread.start();
    }
  }

  private boolean isToolInRectangle(ImageView tool, Rectangle rectangle) {

    // Calculate the position of the tool

    double toolX = tool.getTranslateX() + tool.getLayoutX();
    double toolY = tool.getTranslateY() + tool.getLayoutY();

    // Calculate the dimensions of the rectangle
    double rectX = rectangle.getLayoutX();
    double rectY = rectangle.getLayoutY();
    double rectWidth = rectangle.getWidth();
    double rectHeight = rectangle.getHeight();

    return toolX >= rectX
        && toolX + tool.getFitWidth() <= rectX + rectWidth
        && toolY >= rectY
        && toolY + tool.getFitHeight() <= rectY + rectHeight;
  }

  private double getScreenZoom() {
    return ((MainController) App.getScreen(Screen.Name.MAIN).getController()).getScreenZoom();
  }

  private double clamp(double value, double min, double max) {
    return Math.min(Math.max(value, min), max);
  }

  @FXML
  private void onSolved() {
    setSolved();
    clearPuzzle(panReactorToolbox);
  }
}
