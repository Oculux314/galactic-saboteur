package nz.ac.auckland.se206.puzzles;

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

  @FXML private Label instructions;
  @FXML private Ellipse yellowChosen;
  @FXML private Ellipse blueChosen;
  @FXML private Ellipse redChosen;
  @FXML private AnimatedButton yellowOption;
  @FXML private AnimatedButton blueOption;
  @FXML private AnimatedButton redOption;
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

    yellowChosen.setVisible(false);
    blueChosen.setVisible(false);
    redChosen.setVisible(false);
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

    Ellipse ellipse = null;
    boolean colourSelected;

    // Determine which ellipse was clicked
    if (event.getSource() == blueOption) {
      ellipse = blueChosen;
    } else if (event.getSource() == yellowOption) {
      ellipse = yellowChosen;
    } else if (event.getSource() == redOption) {
      ellipse = redChosen;
    }

    // Toggle the visibility of the ellipses
    if (ellipse.isVisible()) {
      ellipse.setVisible(false);
      colourSelected = false;
    } else {
      ellipse.setVisible(true);
      colourSelected = true;
    }

    // Update the corresponding color-specific variable
    if (event.getSource() == blueOption) {
      blueSelected = colourSelected;
    } else if (event.getSource() == yellowOption) {
      yellowSelected = colourSelected;
    } else if (event.getSource() == redOption) {
      redSelected = colourSelected;
    }
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
