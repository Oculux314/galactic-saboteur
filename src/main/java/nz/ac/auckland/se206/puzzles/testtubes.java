package nz.ac.auckland.se206.puzzles;

import java.util.Random;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class testtubes {

  @FXML private Label instructions;

  private static String colour;

  @FXML
  private void initialize() {
    // generate and store the color only if it hasn't been set before
    if (colour == null) {
      colour = RandomColorSelector();
    }
    instructions.setText("Mix two solutions to create a " + colour + " solution");
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
}
