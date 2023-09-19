package nz.ac.auckland.se206.puzzles;

import java.util.Random;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Ellipse;
import nz.ac.auckland.se206.components.AnimatedButton;

public class TestTubes {

  @FXML private Label instructions;
  @FXML private Ellipse yellowChosen;
  @FXML private Ellipse blueChosen;
  @FXML private Ellipse redChosen;
  @FXML private AnimatedButton yellowOption;
  @FXML private AnimatedButton blueOption;
  @FXML private AnimatedButton redOption;

  private static String colour;
  private boolean yellowSelected;
  private boolean blueSelected;
  private boolean redSelected;

  // private static boolean isPuzzleCorrect;

  @FXML
  private void initialize() {
    // generate and store the color only if it hasn't been set before
    if (colour == null) {
      colour = RandomColorSelector();
    }
    instructions.setText("Mix two solutions to create a " + colour + " solution");

    yellowChosen.setVisible(false);
    blueChosen.setVisible(false);
    redChosen.setVisible(false);

    yellowSelected = false;
    blueSelected = false;
    redSelected = false;
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
}
