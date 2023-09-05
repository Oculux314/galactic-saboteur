package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class ExpositionController implements Controller {

    @FXML private Pane panSpaceship;
    @FXML private Group panzoomgroup;
    @FXML private Rectangle recTest;
    @FXML private Button btnSettings;

    private ZoomAndPanHandler zoomAndPanHandler;

    public void initialize() {
        zoomAndPanHandler = new ZoomAndPanHandler(panzoomgroup);
    }

    @FXML
    private void onPress(MouseEvent event) {
        zoomAndPanHandler.onPress(event);
    }

    @FXML
    private void onDrag(MouseEvent event) {
        zoomAndPanHandler.onDrag(event);
    }

    @FXML
    private void onScroll(ScrollEvent event) {
        zoomAndPanHandler.onScroll(event);
    }

    

  @FXML
  private void settingsClicked() {
    System.out.println("Settings button clicked");
  }

  @FXML
  private void recClicked() {
    System.out.println("Rectangle clicked");
  }
}