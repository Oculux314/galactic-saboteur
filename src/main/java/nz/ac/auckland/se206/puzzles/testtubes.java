package nz.ac.auckland.se206.puzzles;

import java.util.Random;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Ellipse;
import nz.ac.auckland.se206.components.AnimatedButton;

public class TestTubes extends Puzzle {

  @FXML private Label instructions;
  @FXML private Ellipse yellowChosen;
  @FXML private Ellipse blueChosen;
  @FXML private Ellipse redChosen;
  @FXML private AnimatedButton yellowOption;
  @FXML private AnimatedButton blueOption;
  @FXML private AnimatedButton redOption;
  @FXML private AnimatedButton btnMix;
  @FXML private Label confirmationMessage;
  @FXML private Label completedMessage;
  @FXML private Group gameComponents;

  private static String colour;
  private boolean yellowSelected;
  private boolean blueSelected;
  private boolean redSelected;

  private static boolean isPuzzleCorrect;

  @FXML
  private void initialize() {

    if (isPuzzleCorrect) {
      completedMessage.setVisible(true);
      gameComponents.setVisible(false);
    } else {
      // generate and store the color only if it hasn't been set before
      if (colour == null) {
        colour = RandomColorSelector();
      }
      instructions.setText("Mix two solutions to create a " + colour + " solution");
      completedMessage.setVisible(false);

      yellowChosen.setVisible(false);
      blueChosen.setVisible(false);
      redChosen.setVisible(false);
    }
  }

  public String RandomColorSelector() {
    // Create an array of colors
    String[] colours = {"purple", "orange", "green"};
    Random random = new Random();

    // Generate a random index to select a color
    int randomIndex = random.nextInt(colours.length);

    String randomColor = colours[randomIndex];
    return randomColor;
  }

  @FXML
  private void TestTubeClicked(MouseEvent event) {

    Ellipse ellipse = null;
    boolean colourSelected;

    // Determine which ellipse was clicked
    if (event.getSource() == blueOption) {
      ellipse = blueChosen;
      colourSelected = blueSelected;
    } else if (event.getSource() == yellowOption) {
      ellipse = yellowChosen;
      colourSelected = yellowSelected;
    } else if (event.getSource() == redOption) {
      ellipse = redChosen;
      colourSelected = redSelected;
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
    if (yellowSelected && blueSelected) {
      if (colour.equals("green")) {
        success();
        confirmationMessage.setText("Correct! You have created a green solution");
      } else {
        isPuzzleCorrect = false;
        confirmationMessage.setText("Incorrect!");
      }
    } else if (yellowSelected && redSelected) {
      if (colour.equals("orange")) {
        success();
        confirmationMessage.setText("Correct! You have created an orange solution");
      } else {
        isPuzzleCorrect = false;
        confirmationMessage.setText("Incorrect!");
      }
    } else if (blueSelected && redSelected) {
      if (colour.equals("purple")) {
        success();
        confirmationMessage.setText("Correct! You have created a purple solution");
      } else {
        isPuzzleCorrect = false;
        confirmationMessage.setText("Incorrect!");
      }
    } else {
      isPuzzleCorrect = false;
      confirmationMessage.setText("You need to select 2 solutions");
    }
  }

  private void success() {
    isPuzzleCorrect = true;
    gameComponents.setDisable(true);
  }
}
