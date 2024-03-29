package nz.ac.auckland.se206.gamechildren.puzzles;

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
import nz.ac.auckland.se206.misc.Audio;
import nz.ac.auckland.se206.misc.GameState;

/** Controller class for the Laboratory Test Tubes puzzle. */
public class TestTubesPuzzleController extends Puzzle {

  private String colour;
  private String glitterColour;
  private int incorrectCount = 0;
  private Map<AnimatedButton, Ellipse> buttonToEllipseMap;
  private Map<AnimatedButton, Boolean> selectedMap;

  private Audio wrongAnswerSound = new Audio("puzzle_wrong.mp3");
  private Audio pickUpSound = new Audio("testtube_up.mp3");
  private Audio putDownSound = new Audio("testtube_down.mp3");

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
  @FXML private Group gameComponents;
  @FXML private Pane puzzlePane;

  /** Initialises the test tubes puzzle controller with what it needs. */
  @FXML
  private void initialize() {
    // generate and store the color only if it hasn't been set before
    if (colour == null) {
      colour = selectRandomColor();
      glitterColour = selectRandomGlitterColor();
    }

    instructions.setText(
        "Select what's needed to create a "
            + colour
            + " solution with "
            + glitterColour
            + " glitter in it!");

    initializeButtonToEllipseMap();
    hideAllChosenCircles();
    initializeSelectedMaps();
    GameState.laboratoryPuzzleInformation[0] = getInstructions();
    GameState.laboratoryPuzzleInformation[1] = colour;
    GameState.laboratoryPuzzleInformation[2] = glitterColour;
  }

  /** Hides all of the ellipses in the puzzle. */
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
    // Create a map of buttons to ellipses
    buttonToEllipseMap = new HashMap<>();
    // Add all test tube buttons and ellipses to the map
    buttonToEllipseMap.put(yellowOption, yellowChosen);
    buttonToEllipseMap.put(blueOption, blueChosen);
    buttonToEllipseMap.put(redOption, redChosen);
    buttonToEllipseMap.put(greenOption, greenChosen);
    buttonToEllipseMap.put(whiteOption, whiteChosen);
    // Add all glitter buttons and ellipses to the map
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

  /** Selects a random colour from the test tubes each being unique. */
  public String selectRandomColor() {
    // Create an array of colors
    String[] colours = {"orange", "turquoise", "violet", "brown", "light blue", "pink"};
    Random random = new Random();

    // Generate a random index to select a color
    int randomIndex = random.nextInt(colours.length);

    String randomColor = colours[randomIndex];
    return randomColor;
  }

  /** Selects a random colour from the glitter provided each being unique. */
  public String selectRandomGlitterColor() {
    // Create an array of glitter colors
    String[] glitterColours = {"yellow", "blue", "black", "pink"};
    Random random = new Random();

    // Generate a random index to select a color
    int randomIndex = random.nextInt(glitterColours.length);

    String randomColor = glitterColours[randomIndex];
    return randomColor;
  }

  @FXML
  private void onTestTubeClicked(MouseEvent event) {
    // Get the testtube or glitter that was clicked
    AnimatedButton clickedButton = (AnimatedButton) event.getSource();
    Ellipse correspondingEllipse = buttonToEllipseMap.get(clickedButton);

    // Toggle the visibility of the corresponding ellipse and update selected map
    if (correspondingEllipse != null) {
      if (correspondingEllipse.isVisible()) {
        // Put down
        correspondingEllipse.setVisible(false);
        selectedMap.put(clickedButton, false);
        putDownSound.play();
      } else {
        // Pick up
        correspondingEllipse.setVisible(true);
        selectedMap.put(clickedButton, true);
        pickUpSound.play();
      }
    }

    int totalSelected = countSelected(selectedMap);
    if (totalSelected == 3) {
      btnMixClicked();
    }
  }

  private void btnMixClicked() {
    // Check if the solution is correct
    boolean isSolutionCorrect = checkSolution();

    // Display messages if the solution is correct
    if (isSolutionCorrect) {
      completePuzzle();
      instructions.setText(
          "Correct! You have created a "
              + colour
              + " solution with "
              + glitterColour
              + " glitter in it!");
    } else {
      // Increment the incorrect count
      incorrectCount++;
      wrongAnswerSound.play();

      // Display different messages depending on the amount of incorrect attempts
      if (incorrectCount % 2 == 1) {
        instructions.setText(
            "Oh no, that's wrong! Select what's needed to create a "
                + colour
                + " solution with "
                + glitterColour
                + " glitter in it!");
      } else {
        instructions.setText(
            "Incorrect! Try again and select what's needed to create a "
                + colour
                + " solution with "
                + glitterColour
                + " glitter in it!");
      }
    }
  }

  private boolean checkSolution() {
    boolean isColourCorrect = false;
    boolean isGlitterColourCorrect = false;

    // check if the total number of selected test tubes and glitter is correct
    int totalSelected = countSelected(selectedMap);
    boolean isTotalSelectedCorrect = (totalSelected == 3);
    if (!isTotalSelectedCorrect) {
      return false;
    }

    // check if the colour is correct
    if (colour.equalsIgnoreCase("Orange")) {
      isColourCorrect = selectedMap.get(yellowOption) && selectedMap.get(redOption);
    } else if (colour.equalsIgnoreCase("Turquoise")) {
      isColourCorrect = selectedMap.get(greenOption) && selectedMap.get(blueOption);
    } else if (colour.equalsIgnoreCase("Violet")) {
      isColourCorrect = selectedMap.get(redOption) && selectedMap.get(blueOption);
    } else if (colour.equalsIgnoreCase("Brown")) {
      isColourCorrect = selectedMap.get(redOption) && selectedMap.get(greenOption);
    } else if (colour.equalsIgnoreCase("Light Blue")) {
      isColourCorrect = selectedMap.get(blueOption) && selectedMap.get(whiteOption);
    } else if (colour.equalsIgnoreCase("Pink")) {
      isColourCorrect = selectedMap.get(redOption) && selectedMap.get(whiteOption);
    }

    // check if the glitter colour is correct
    if (glitterColour.equalsIgnoreCase("Yellow")) {
      isGlitterColourCorrect = selectedMap.get(yellowGlitter);
    } else if (glitterColour.equalsIgnoreCase("Blue")) {
      isGlitterColourCorrect = selectedMap.get(blueGlitter);
    } else if (glitterColour.equalsIgnoreCase("Black")) {
      isGlitterColourCorrect = selectedMap.get(blackGlitter);
    } else if (glitterColour.equalsIgnoreCase("Pink")) {
      isGlitterColourCorrect = selectedMap.get(pinkGlitter);
    }
    return isColourCorrect && isGlitterColourCorrect;
  }

  private int countSelected(Map<AnimatedButton, Boolean> selectedMap) {
    int count = 0;
    for (Boolean isSelected : selectedMap.values()) {
      if (isSelected) {
        count++;
      }
    }
    return count;
  }

  public String getInstructions() {
    return instructions.getText();
  }

  private void completePuzzle() {
    completePuzzle(this, puzzlePane);
  }
}
