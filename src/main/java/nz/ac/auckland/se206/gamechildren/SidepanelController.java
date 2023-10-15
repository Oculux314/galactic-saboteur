package nz.ac.auckland.se206.gamechildren;

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
import nz.ac.auckland.se206.misc.GameState;
import nz.ac.auckland.se206.misc.RootPair;

/**
 * Controller for the side panel displaying information related to clues, suspects, rooms, and time.
 */
public class SidepanelController implements RootPair.Controller {

  @FXML private Rectangle timeClueInformationRectangle;
  @FXML private Rectangle whereClueInformationRectangle;
  @FXML private Rectangle whoClueInformationRectangle;
  @FXML private ImageView timeClue;
  @FXML private ImageView whereClue;
  @FXML private ImageView whoClue;
  @FXML private Label timeClueInformationLabel;
  @FXML private Label whereClueInformationLabel;
  @FXML private Label whoClueInformationLabel;
  @FXML private Label lblTimer;
  @FXML private Label lblHints;

  private String[] suspects = {
    "/images/suspects/suspect1.png",
    "/images/suspects/suspect2.png",
    "/images/suspects/suspect3.png"
  };
  private String[] rooms = {
    "/images/rooms/room1.png", "/images/rooms/room2.jpg", "/images/rooms/room3.png"
  };
  private String[] times = {
    "/images/times/time1.jpg", "/images/times/time2.jpg", "/images/times/time3.jpg"
  };

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

  @Override
  public void onLoad() {
    // Do nothing
  }

  @FXML
  private void informationShow(MouseEvent event) {
    Rectangle informationRectangle = null;
    Label informationLabel = null;

    // Set the correct information rectangle and label
    if (event.getSource() == timeClue) {
      informationRectangle = timeClueInformationRectangle;
      informationLabel = timeClueInformationLabel;
      informationLabel.setText("Time: Unknown");
    } else if (event.getSource() == whereClue) {
      informationRectangle = whereClueInformationRectangle;
      informationLabel = whereClueInformationLabel;
      informationLabel.setText("Location: Unknown");
    } else if (event.getSource() == whoClue) {
      informationRectangle = whoClueInformationRectangle;
      informationLabel = whoClueInformationLabel;
      informationLabel.setText("Culprit: Unknown");
    }

    // If clue has been found, display the information otherwise encourage the user to find the clue
    if (event.getSource() instanceof ImageView) {
      // Get image
      ImageView imageView = (ImageView) event.getSource();
      Image image = imageView.getImage();
      if (image == suspect) {
        informationLabel.setText("Culprit: " + suspectName);
      } else if (image == room) {
        informationLabel.setText("Location: " + roomName);
      } else if (image == time) {
        informationLabel.setText("Time: " + timeName);
      }
    }
    informationRectangle.setVisible(true);
  }

  @FXML
  private void informationHide(MouseEvent event) {
    Rectangle informationRectangle = null;
    Label informationLabel = null;

    // Set the correct information rectangle and label
    if (event.getSource() == timeClue) {
      informationRectangle = timeClueInformationRectangle;
      informationLabel = timeClueInformationLabel;
    } else if (event.getSource() == whereClue) {
      informationRectangle = whereClueInformationRectangle;
      informationLabel = whereClueInformationLabel;
    } else if (event.getSource() == whoClue) {
      informationRectangle = whoClueInformationRectangle;
      informationLabel = whoClueInformationLabel;
    }

    // Hide the information
    informationRectangle.setVisible(false);
    informationLabel.setText("");
  }

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

  /** Retrieves a random clue from the available list of clues and displays it on the side panel. */
  public void getRandomClue() {
    // ensure that the clues picked are random
    int random = (int) (Math.random()) * clues.size();
    Image clue = (Image) (clues.toArray()[random]);

    displayClue(clue);
    clues.remove(clue);
  }

  private void displayClue(Image clue) {
    // Change the image in the side panel to represent the clue
    if (clue == suspect) {
      whoClue.setImage(clue);
    } else if (clue == room) {
      whereClue.setImage(clue);
    } else if (clue == time) {
      timeClue.setImage(clue);
    }
    clues.remove(clue);
  }

  /**
   * Retrieves the name of a clue based on the provided file path.
   *
   * @param filePath The file path of the clue.
   * @return The name of the clue.
   */
  private String getClueName(String filePath) {
    return clueNameMap.get(filePath);
  }

  /** Initializes the clueNameMap with file paths as keys and corresponding clue names as values. */
  private void setClueNameMap() {
    // Suspects
    clueNameMap.put("/images/suspects/suspect1.png", "Scientist");
    clueNameMap.put("/images/suspects/suspect2.png", "Captain");
    clueNameMap.put("/images/suspects/suspect3.png", "Mechanic");

    // Rooms
    clueNameMap.put("/images/rooms/room1.png", "Navigation");
    clueNameMap.put("/images/rooms/room2.jpg", "Laboratory");
    clueNameMap.put("/images/rooms/room3.png", "Reactor Room");

    // Times
    clueNameMap.put("/images/times/time1.jpg", "Morning");
    clueNameMap.put("/images/times/time2.jpg", "Afternoon");
    clueNameMap.put("/images/times/time3.jpg", "Night");
  }

  public Label getTimerLabel() {
    return lblTimer;
  }

  public void setHintText(String hints) {
    lblHints.setText(hints);
  }
}
