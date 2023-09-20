package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
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
  @FXML public ImageView clue1;
  @FXML public ImageView clue2;
  @FXML public ImageView clue3;
  @FXML private ImageView suspect1;
  @FXML private ImageView suspect2;
  @FXML private ImageView suspect3;
  @FXML private Pane panClue1;
  @FXML private Pane panClue2;
  @FXML private Pane panClue3;

  private String[] suspects = {
    "/images/suspects/suspect1.jpg",
    "/images/suspects/suspect2.jpg",
    "/images/suspects/suspect3.png"
  };
  private String[] rooms = {
    "/images/rooms/room1.jpg", "/images/rooms/room2.jpg", "/images/rooms/room3.jpg"
  };
  private String[] times = {
    "/images/times/time1.jpg", "/images/times/time2.jpg", "/images/times/time3.jpg"
  };
  private String defaultInfo = "Clue not found";
  private int random;
  private Image suspect;
  private Image room;
  private Image time;
  private String suspectName;
  private String roomName;
  private String timeName;

  private Set<Image> clues = new HashSet<>();

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

    if (event.getSource() instanceof ImageView) {
      ImageView imageView = (ImageView) event.getSource();
      Image image = imageView.getImage();
      if (image == suspect) {
        informationLabel.setText("Culprit: " + suspectName);
      } else if (image == room) {
        informationLabel.setText("Location: " + roomName);
      } else if (image == time) {
        informationLabel.setText("Time: " + timeName);
      } else {
        informationLabel.setText(defaultInfo);
      }
    }

    if (informationRectangle != null && informationLabel != null) {
      informationRectangle.setVisible(true);
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
    int size = 3;

    random = (int) (Math.random() * size);
    suspect = new Image(getClass().getResourceAsStream(suspects[random]));
    suspectName = getClueName(suspects[random]);
    clues.add(suspect);

    random = (int) (Math.random() * size);
    room = new Image(getClass().getResourceAsStream(rooms[random]));
    roomName = getClueName(rooms[random]);
    clues.add(room);

    random = (int) (Math.random() * size);
    time = new Image(getClass().getResourceAsStream(times[random]));
    timeName = getClueName(times[random]);
    clues.add(time);
  }

  public void getClue() {
    int random = (int) (Math.random()) * clues.size();
    Image clue = (Image) clues.toArray()[random];
    if (clue == suspect) {
      clue1.setImage(clue);
    } else if (clue == room) {
      clue2.setImage(clue);
    } else if (clue == time) {
      clue3.setImage(clue);
    }
    clues.remove(clue);
  }

  private String getClueName(String filePath) {
    if (filePath.contains("suspect")) {
      if (filePath.contains("1")) {
        return "Scientist";
      } else if (filePath.contains("2")) {
        return "Captain";
      } else if (filePath.contains("3")) {
        return "Mechanic";
      } else {
        return null;
      }
    } else if (filePath.contains("room")) {
      if (filePath.contains("1")) {
        return "Navigation";
      } else if (filePath.contains("2")) {
        return "Laboratory";
      } else if (filePath.contains("3")) {
        return "Reactor Room";
      } else {
        return null;
      }
    } else if (filePath.contains("time")) {
      if (filePath.contains("1")) {
        return "Day";
      } else if (filePath.contains("2")) {
        return "Afternoon";
      } else if (filePath.contains("3")) {
        return "Night";
      } else {
        return null;
      }
    } else {
      return null;
    }
  }
}
