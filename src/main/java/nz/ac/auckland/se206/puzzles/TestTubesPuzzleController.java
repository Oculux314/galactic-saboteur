package nz.ac.auckland.se206.puzzles;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Ellipse;
import nz.ac.auckland.se206.components.AnimatedButton;

public class TestTubesPuzzleController extends Puzzle {

  private static String colour;
  private boolean yellowSelected;
  private boolean blueSelected;
  private boolean redSelected;
  private Map<AnimatedButton, Ellipse> buttonToEllipseMap;
  private Map<AnimatedButton, Boolean> selectedMap;

  @FXML private Label instructions;
  @FXML private Ellipse yellowChosen;
  @FXML private Ellipse blueChosen;
  @FXML private Ellipse redChosen;
  @FXML private Ellipse greenChosen;
  @FXML private Ellipse whiteChosen;
  @FXML private Ellipse yellowGlitterChosen;
  @FXML private Ellipse blueGlitterChosen;
  @FXML private Ellipse blackGlitterChosen;
  @FXML private Ellipse pinkGlitterChosen;
  @FXML private AnimatedButton yellowOption;
  @FXML private AnimatedButton blueOption;
  @FXML private AnimatedButton redOption;
  @FXML private AnimatedButton greenOption;
  @FXML private AnimatedButton whiteOption;
  @FXML private AnimatedButton yellowGlitter;
  @FXML private AnimatedButton blueGlitter;
  @FXML private AnimatedButton pinkGlitter;
  @FXML private AnimatedButton blackGlitter;
  @FXML private AnimatedButton btnMix;
  @FXML private Label confirmationMessage;
  @FXML private Group gameComponents;
  @FXML private Pane puzzlePane;

  @FXML
  private void initialize() {

    // generate and store the color only if it hasn't been set before
    if (colour == null) {
      colour = selectRandomColor();
    }
    instructions.setText("Mix two solutions to create a " + colour + " solution");

    initializeButtonToEllipseMap();
    hideAllChosenCircles();
    initializeSelectedMaps();
  }

  public void hideAllChosenCircles() {

    Ellipse[] ellipses = {
      yellowChosen,
      blueChosen,
      redChosen,
      greenChosen,
      whiteChosen,
      yellowGlitterChosen,
      blueGlitterChosen,
      blackGlitterChosen,
      pinkGlitterChosen
    };

    // Iterate through and hide all Ellipse elements
    for (Ellipse ellipse : ellipses) {
      if (ellipse != null) {
        ellipse.setVisible(false);
      }
    }
  }

  private void initializeButtonToEllipseMap() {
    buttonToEllipseMap = new HashMap<>();
    buttonToEllipseMap.put(yellowOption, yellowChosen);
    buttonToEllipseMap.put(blueOption, blueChosen);
    buttonToEllipseMap.put(redOption, redChosen);
    buttonToEllipseMap.put(greenOption, greenChosen);
    buttonToEllipseMap.put(whiteOption, whiteChosen);
    buttonToEllipseMap.put(yellowGlitter, yellowGlitterChosen);
    buttonToEllipseMap.put(blueGlitter, blueGlitterChosen);
    buttonToEllipseMap.put(pinkGlitter, pinkGlitterChosen);
    buttonToEllipseMap.put(blackGlitter, blackGlitterChosen);
  }

  private void initializeSelectedMaps() {
    selectedMap = new HashMap<>();

    for (AnimatedButton button : buttonToEllipseMap.keySet()) {
      selectedMap.put(button, false);
    }
  }

  public String selectRandomColor() {
    // Create an array of colors
    String[] colours = {"purple", "orange", "green"};
    Random random = new Random();

    // Generate a random index to select a color
    int randomIndex = random.nextInt(colours.length);

    String randomColor = colours[randomIndex];
    return randomColor;
  }

  @FXML
  private void onTestTubeClicked(MouseEvent event) {
    AnimatedButton clickedButton = (AnimatedButton) event.getSource();
    Ellipse correspondingEllipse = buttonToEllipseMap.get(clickedButton);

    if (correspondingEllipse != null) {
      System.out.println("is visible" + correspondingEllipse.isVisible());
      if (correspondingEllipse.isVisible()) {
        correspondingEllipse.setVisible(false);
        selectedMap.put(clickedButton, false); // Button is now deselected
      } else {
        correspondingEllipse.setVisible(true);
        selectedMap.put(clickedButton, true); // Button is now selected
      }
    }
    System.out.println(selectedMap);
  }

  @FXML
  private void btnMixClicked(MouseEvent event) {
    if (yellowSelected && blueSelected) { // Yellow + blue = green
      if (colour.equals("green")) {
        completePuzzle();
        confirmationMessage.setText("Correct! You have created a green solution");
      } else {
        confirmationMessage.setText("Incorrect!");
      }
    } else if (yellowSelected && redSelected) { // Yellow + red = orange
      if (colour.equals("orange")) {
        completePuzzle();
        confirmationMessage.setText("Correct! You have created an orange solution");
      } else {
        confirmationMessage.setText("Incorrect!");
      }
    } else if (blueSelected && redSelected) { // Blue + red = purple
      if (colour.equals("purple")) {
        completePuzzle();
        confirmationMessage.setText("Correct! You have created a purple solution");
      } else {
        confirmationMessage.setText("Incorrect!");
      }
    } else {
      confirmationMessage.setText("You need to select 2 solutions");
    }
  }

  private void completePuzzle() {
    setSolved();
    clearPuzzle(puzzlePane);
  }
}
