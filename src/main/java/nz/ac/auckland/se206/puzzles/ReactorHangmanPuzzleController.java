package nz.ac.auckland.se206.puzzles;

import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class ReactorHangmanPuzzleController extends Puzzle {

  @FXML private Label letterS;
  @FXML private Label letterP;
  @FXML private Label letterA;
  @FXML private Label letterC;
  @FXML private Label letterE;
  @FXML private Label lblGuessedLetters1;
  @FXML private Label lblGuessedLetters2;
  @FXML private TextField txtGuessLetter;
  @FXML private Button btnGuess;
  @FXML private Group grpWord;
  @FXML private Pane panHangmanPuzzle;

  private String letter;
  private int lettersguessed = 0;
  private List<Label> letters = new ArrayList<Label>();

  @FXML
  private void initialize() {
    letterS.setText(null);
    letterP.setText(null);
    letterA.setText(null);
    letterC.setText(null);
    letterE.setText(null);

    addLetters();
  }

  @FXML
  private void onGuess() throws InterruptedException {
    lettersguessed++;
    letter = txtGuessLetter.getText();
    addLetter(letter);
    checkLetter(letter.toUpperCase());
    txtGuessLetter.clear();
    checkIfSolved();
  }

  private void addLetter(String letter) {

    // Check if input is valid
    if (checkValidInput(letter)) {

      // Add letter to the correct label (top row or bottom row)
      if (lettersguessed <= 16) {
        if (lblGuessedLetters1.getText().equals("")) {
          lblGuessedLetters1.setText(letter + ",");
        } else {
          lblGuessedLetters1.setText(lblGuessedLetters1.getText() + " " + letter.toUpperCase());
        }
      } else if (lettersguessed <= 26) {
        if (lblGuessedLetters2.getText().equals("")) {
          lblGuessedLetters2.setText(letter + ",");
        } else {
          lblGuessedLetters2.setText(lblGuessedLetters2.getText() + " " + letter.toUpperCase());
        }
      }
    } else {
      return;
    }
  }

  private void checkLetter(String letter) {
    if (letter.equals("S")) {
      letterS.setText(letter);
    } else if (letter.equals("P")) {
      letterP.setText(letter);
    } else if (letter.equals("A")) {
      letterA.setText(letter);
    } else if (letter.equals("C")) {
      letterC.setText(letter);
    } else if (letter.equals("E")) {
      letterE.setText(letter);
    }
  }

  private boolean checkValidInput(String letter) {
    if (letter.length() > 1
        || !letter.matches("[a-zA-Z]+")
        || lblGuessedLetters1.getText().contains(letter.toUpperCase())
        || lblGuessedLetters2.getText().contains(letter.toUpperCase())) {
      return false;
    } else {
      return true;
    }
  }

  private void checkIfSolved() throws InterruptedException {
    if (!isEmpty(letters)) {
      setSolved();
      clearPuzzle(panHangmanPuzzle);
    }
  }

  private boolean isEmpty(List<Label> letters) {

    int count = 0;
    for (Label letter : letters) {
      if (letter.getText() != null) {
        count++;
      }
    }

    if (count == 5) {
      return false;
    } else {
      return true;
    }
  }

  private void addLetters() {
    letters.add(letterS);
    letters.add(letterP);
    letters.add(letterA);
    letters.add(letterC);
    letters.add(letterE);
  }
}
