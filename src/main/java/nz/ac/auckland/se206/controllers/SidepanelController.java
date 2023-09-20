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
import javafx.scene.image.Image;

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
  @FXML public ImageView clue1;
  @FXML public ImageView clue2;
  @FXML public ImageView clue3;
  @FXML private ImageView suspect1;
  @FXML private ImageView suspect2;
  @FXML private ImageView suspect3;

  private String[] suspects = {"/images/suspects/suspect1.jpg", "/images/suspects/suspect2.jpg", "/images/suspects/suspect3.png"};
  private String[] rooms = {"/images/rooms/room1.jpg", "/images/rooms/room2.jpg", "/images/rooms/room3.jpg"};
  private String[] times = {"/images/times/time1.jpg", "/images/times/time2.jpg", "/images/times/time3.jpg"};
  private String defaultInfo = "Clue not found";
  private Image suspect;
  private Image room;
  private Image time;
  private Image[] clues = {suspect, room, time};
  private int clueIndex = 0;

  

  @FXML
  @FXML
  private void initialize() {
    suspectsContent.setVisible(false);
    clueInformationRectangle.setVisible(false);
    selectClues();
  }

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
    suspectInformationRectangle.setVisible(false);
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
      informationRectangle.setVisible(true);
      if (event.getSource() == clue1) {
        informationLabel.setText(defaultInfo);
      } else if (event.getSource() == clue2) {
        informationLabel.setText(defaultInfo);
      } else if (event.getSource() == clue3) {
        informationLabel.setText(defaultInfo);
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
      informationRectangle.setVisible(false);
      informationLabel.setText("");
    }
  }

  private void selectClues() {
    int random = (int) (Math.random() * 3);
    suspect = new Image(getClass().getResourceAsStream(suspects[random]));

    random = (int) (Math.random() * 3);
    room = new Image(getClass().getResourceAsStream(rooms[random]));

    random = (int) (Math.random() * 3);
    time = new Image(getClass().getResourceAsStream(times[random]));
  }
  
  public void getClue() {
    if (clueIndex == 0) {
      clue1.setImage(suspect);
  } else if (clueIndex == 1) {
      clue2.setImage(room);
  } else if (clueIndex == 2) {
      clue3.setImage(time);
  }

  // Increment the index, and loop back to 0 if it reaches the end
  clueIndex++;
  if (clueIndex >= 3) {
      clueIndex = 0;
  }
  }

}
