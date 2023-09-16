package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;

public class SidepanelController implements Controller {

  @FXML private Polyline shapeClues;
  @FXML private Polyline shapeSuspects;
  @FXML private VBox suspectsContent;
  @FXML private VBox clueContent;
  @FXML private Polyline colShapeSuspects;
  @FXML private Polyline colShapeClues;
  @FXML private Rectangle colPanel;
  @FXML private Rectangle clueInformationRectangle;
  @FXML private Label lblClueInformation;
  @FXML private Rectangle suspectInformationRectangle;
  @FXML private Label lblSuspectInformation;

  @FXML
  private void clueBtnPressed() throws IOException {
    suspectsContent.setVisible(false);
    clueContent.setVisible(true);
    Color blue = Color.web("334855");
    colPanel.setFill(blue);
  }

  @FXML
  private void suspectsBtnPressed() throws IOException {
    clueContent.setVisible(false);
    suspectsContent.setVisible(true);
    Color green = Color.web("42805e");
    colPanel.setFill(green);
  }

  @FXML
  private void clue1InformationShow(MouseEvent event) {
    clueInformationRectangle.setOpacity(1);
    lblClueInformation.setText("Clue 1");
  }

  @FXML
  private void clue1InformationHide(MouseEvent event) {
    clueInformationRectangle.setOpacity(0);
    lblClueInformation.setText("");
  }

  @FXML
  private void clue2InformationShow(MouseEvent event) {
    clueInformationRectangle.setOpacity(1);
    lblClueInformation.setText("Clue 2");
  }

  @FXML
  private void clue2InformationHide(MouseEvent event) {
    clueInformationRectangle.setOpacity(0);
    lblClueInformation.setText("");
  }

  @FXML
  private void clue3InformationShow(MouseEvent event) {
    clueInformationRectangle.setOpacity(1);
    lblClueInformation.setText("Clue 3");
  }

  @FXML
  private void clue3InformationHide(MouseEvent event) {
    clueInformationRectangle.setOpacity(0);
    lblClueInformation.setText("");
  }

  @FXML
  private void suspect1InformationShow(MouseEvent event) {
    suspectInformationRectangle.setOpacity(1);
    lblSuspectInformation.setText("Suspect 1");
  }

  @FXML
  private void suspect1InformationHide(MouseEvent event) {
    suspectInformationRectangle.setOpacity(0);
    lblSuspectInformation.setText("");
  }

  @FXML
  private void suspect2InformationShow(MouseEvent event) {
    suspectInformationRectangle.setOpacity(1);
    lblSuspectInformation.setText("Suspect 2");
  }

  @FXML
  private void suspect2InformationHide(MouseEvent event) {
    suspectInformationRectangle.setOpacity(0);
    lblSuspectInformation.setText("");
  }

  @FXML
  private void suspect3InformationShow(MouseEvent event) {
    suspectInformationRectangle.setOpacity(1);
    lblSuspectInformation.setText("Suspect 3");
  }

  @FXML
  private void suspect3InformationHide(MouseEvent event) {
    suspectInformationRectangle.setOpacity(0);
    lblSuspectInformation.setText("");
  }
}
