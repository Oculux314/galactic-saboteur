package nz.ac.auckland.se206.puzzles;

import java.util.Map;
import java.util.Random;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.components.AnimatedButton;

public class ReactorButtonPuzzleController extends Puzzle {

  @FXML private Label lblNumberPrompt;
  @FXML private Label lblAnswer;
  @FXML private AnimatedButton btnSubmit;
  @FXML private AnimatedButton btnClear;
  @FXML private AnimatedButton btn1;
  @FXML private AnimatedButton btn2;
  @FXML private AnimatedButton btn3;
  @FXML private AnimatedButton btn4;
  @FXML private AnimatedButton btn5;
  @FXML private AnimatedButton btn6;
  @FXML private AnimatedButton btn7;
  @FXML private AnimatedButton btn8;
  @FXML private AnimatedButton btn9;
  @FXML private AnimatedButton btn0;
  @FXML private Pane panReactorButtonpad;
  @FXML private Label lblVerdict;

  private Map<AnimatedButton, String> buttonToNumberMap;
  private String randomNumber;

  @FXML
  private void initialize() {
    // Map each button to its corresponding number
    buttonToNumberMap =
        Map.of(
            btn1, "1",
            btn2, "2",
            btn3, "3",
            btn4, "4",
            btn5, "5",
            btn6, "6",
            btn7, "7",
            btn8, "8",
            btn9, "9",
            btn0, "0");

    randomNumber = generateRandomNumber();
    String randomSymbol = convertNumberToSymbol(randomNumber);
    lblNumberPrompt.setText(randomSymbol);
  }

  private String generateRandomNumber() {
    Random random = new Random();
    int min = 10000;
    int max = 99999;
    int randomNumber = random.nextInt(max - min + 1) + min;
    return String.valueOf(randomNumber);
  }

  private String convertNumberToSymbol(String number) {
    String symbols = ")!@#$%^&*(";
    StringBuilder result = new StringBuilder();

    for (char digit : number.toCharArray()) {
      int index = digit - '0';
      if (index >= 0 && index < symbols.length()) {
        result.append(symbols.charAt(index));
      } else {
        result.append(digit);
      }
    }

    return result.toString();
  }

  @FXML
  private void onNumberClicked(MouseEvent event) {
    AnimatedButton clickedButton = (AnimatedButton) event.getSource();
    String number = buttonToNumberMap.get(clickedButton);

    // Append the number to the label
    if (lblAnswer.getText() != null) {
      String currentText = lblAnswer.getText();
      String newText = currentText + number;
      lblAnswer.setText(newText);
    } else {
      lblAnswer.setText(number);
    }
  }

  @FXML
  private void onSubmitClicked() {

    Thread labelThread =
        new Thread(
            () -> {
              try {
                Thread.sleep(1500);
                Platform.runLater(() -> lblVerdict.setText(""));
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            });

    // Check if the answer is correct
    if (lblAnswer.getText().equals(randomNumber.toString())) {
      setSolved();
      clearPuzzle(panReactorButtonpad);
    } else {
      lblVerdict.setText("Incorrect");
      labelThread.start();
    }
  }

  @FXML
  private void onClearClicked() {
    lblAnswer.setText("");
  }
}
