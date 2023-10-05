package nz.ac.auckland.se206.controllers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.GameState;

public class SidepanelController implements Controller {

  @FXML private Rectangle timeClueInformationRectangle;
  @FXML private Rectangle whereClueInformationRectangle;
  @FXML private Rectangle whoClueInformationRectangle;
  @FXML private ImageView timeClue;
  @FXML private ImageView whereClue;
  @FXML private ImageView whoClue;
  @FXML private Label timeClueInformationLabel;
  @FXML private Label whereClueInformationLabel;
  @FXML private Label whoClueInformationLabel;

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
  private String defaultInfo = "Find the clue!";

  private int random;
  private Image suspect;
  private Image room;
  private Image time;
  private String suspectName;
  private String roomName;
  private String timeName;

  private Set<Image> clues = new HashSet<>();
  private Map<String, String> clueNameMap = new HashMap<>();

  @FXML
  private void initialize() {
    setClueNameMap();
    selectClues();
  }

  @FXML
  private void informationShow(MouseEvent event) {
    Rectangle informationRectangle = null;
    Label informationLabel = null;

    if (event.getSource() == timeClue){
      informationRectangle = timeClueInformationRectangle;
      informationLabel = timeClueInformationLabel;
    } else if (event.getSource() == whereClue){
      System.out.println("chosen");
      informationRectangle = whereClueInformationRectangle;
      informationLabel = whereClueInformationLabel;
    } else if (event.getSource() == whoClue){
      informationRectangle = whoClueInformationRectangle;
      informationLabel = whoClueInformationLabel;
    }
    System.out.println(informationLabel.getId());
    System.out.println(informationRectangle.getId());
    System.out.println(event.getSource());


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
      informationRectangle.setVisible(true);
  }

  @FXML
  private void informationHide(MouseEvent event){
    Rectangle informationRectangle = null;
    Label informationLabel = null;

    if (event.getSource() == timeClue){
      informationRectangle = timeClueInformationRectangle;
      informationLabel = timeClueInformationLabel;
    } else if (event.getSource() == whereClue){
      informationRectangle = whereClueInformationRectangle;
      informationLabel = whereClueInformationLabel;
    } else if (event.getSource() == whoClue){
      informationRectangle = whoClueInformationRectangle;
      informationLabel = whoClueInformationLabel;
    }

      informationRectangle.setVisible(false);
      informationLabel.setText("");
  }

  // @FXML
  // private void itemInformationHide(MouseEvent event) {
  //   Rectangle informationRectangle = null;
  //   Label informationLabel = null;

  //   if (event.getSource() == clue1 || event.getSource() == clue2 || event.getSource() == clue3) {
  //     informationRectangle = clueInformationRectangle;
  //     informationLabel = lblClueInformation;
  //   } else if (event.getSource() == suspect1
  //       || event.getSource() == suspect2
  //       || event.getSource() == suspect3) {
  //     informationRectangle = suspectInformationRectangle;
  //     informationLabel = lblSuspectInformation;
  //   }

  //   if (informationRectangle != null && informationLabel != null) {
  //     informationRectangle.setVisible(false);
  //     informationLabel.setText("");
  //   }
  // }

  private void selectClues() {
    int size = 3;

    // Suspect
    random = (int) (Math.random() * size);
    suspect = new Image(getClass().getResourceAsStream(suspects[random]));
    suspectName = getClueName(suspects[random]);
    GameState.correctSuspect = suspectName;
    clues.add(suspect);

    // Room
    random = (int) (Math.random() * size);
    room = new Image(getClass().getResourceAsStream(rooms[random]));
    roomName = getClueName(rooms[random]);
    GameState.correctRoom = roomName;
    clues.add(room);

    // Time
    random = (int) (Math.random() * size);
    time = new Image(getClass().getResourceAsStream(times[random]));
    timeName = getClueName(times[random]);
    GameState.correctTime = timeName;
    clues.add(time);
  }

  public void getRandomClue() {
    int random = (int) (Math.random()) * clues.size();
    Image clue = (Image) (clues.toArray()[random]);

    displayClue(clue);
    clues.remove(clue);
  }

  private void displayClue(Image clue) {
    if (clue == suspect) {
      // clue1.setImage(clue);
    } else if (clue == room) {
    // ?clue2.setImage(clue);
    } else if (clue == time) {
      // clue3.setImage(clue);
    }
    clues.remove(clue);

    if (clues.size() == 0) {
      GameState.cluesFound = true;
    }
  }

  private String getClueName(String filePath) {
    return clueNameMap.get(filePath);
  }

  private void setClueNameMap() {
    // Suspects
    clueNameMap.put("/images/suspects/suspect1.jpg", "Scientist");
    clueNameMap.put("/images/suspects/suspect2.jpg", "Captain");
    clueNameMap.put("/images/suspects/suspect3.png", "Mechanic");

    // Rooms
    clueNameMap.put("/images/rooms/room1.jpg", "Navigation");
    clueNameMap.put("/images/rooms/room2.jpg", "Laboratory");
    clueNameMap.put("/images/rooms/room3.jpg", "Reactor Room");

    // Times
    clueNameMap.put("/images/times/time1.jpg", "Morning");
    clueNameMap.put("/images/times/time2.jpg", "Afternoon");
    clueNameMap.put("/images/times/time3.jpg", "Night");
  }
}
