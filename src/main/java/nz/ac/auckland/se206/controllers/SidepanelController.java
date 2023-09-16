package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
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
  @FXML private ImageView clue1;
  @FXML private ImageView clue2;
  @FXML private ImageView clue3;
  @FXML private ImageView suspect1;
  @FXML private ImageView suspect2;
  @FXML private ImageView suspect3;

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
  private void itemInformationShow(MouseEvent event) {
    Rectangle informationRectangle = null;
    Label informationLabel = null;

    if (event.getSource() == clue1 || event.getSource() == clue2 || event.getSource() == clue3) {
      informationRectangle = clueInformationRectangle;
      informationLabel = lblClueInformation;
    } else if (event.getSource() == suspect1
        || event.getSource() == suspect2
        || event.getSource() == suspect3) {
      informationRectangle = suspectInformationRectangle;
      informationLabel = lblSuspectInformation;
    }

    if (informationRectangle != null && informationLabel != null) {
      informationRectangle.setOpacity(1);
      if (event.getSource() == clue1) {
        informationLabel.setText("Clue 1");
      } else if (event.getSource() == clue2) {
        informationLabel.setText("Clue 2");
      } else if (event.getSource() == clue3) {
        informationLabel.setText("Clue 3");
      } else if (event.getSource() == suspect1) {
        informationLabel.setText("Suspect 1");
      } else if (event.getSource() == suspect2) {
        informationLabel.setText("Suspect 2");
      } else if (event.getSource() == suspect3) {
        informationLabel.setText("Suspect 3");
      }
    }
  }

  @FXML
  private void itemInformationHide(MouseEvent event) {
    Rectangle informationRectangle = null;
    Label informationLabel = null;

    if (event.getSource() == clue1 || event.getSource() == clue2 || event.getSource() == clue3) {
      informationRectangle = clueInformationRectangle;
      informationLabel = lblClueInformation;
    } else if (event.getSource() == suspect1
        || event.getSource() == suspect2
        || event.getSource() == suspect3) {
      informationRectangle = suspectInformationRectangle;
      informationLabel = lblSuspectInformation;
    }

    if (informationRectangle != null && informationLabel != null) {
      informationRectangle.setOpacity(0);
      informationLabel.setText("");
    }
  }

}
