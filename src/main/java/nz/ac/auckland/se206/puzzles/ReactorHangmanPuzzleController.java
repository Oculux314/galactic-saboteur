package nz.ac.auckland.se206.puzzles;

import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javax.swing.JLabel;

public class ReactorHangmanPuzzleController extends Puzzle {

  @FXML private Label letter1;
  @FXML private Label letter2;
  @FXML private Label letter3;
  @FXML private Label letter4;
  @FXML private Label letter5;
  @FXML private Label lblGuessedLetters1;
  @FXML private Label lblGuessedLetters2;
  @FXML private TextField txtGuessLetter;
  @FXML private Button btnGuess;
  @FXML private Group grpWord;
  @FXML private Pane panHangmanPuzzle;

  private String letter;
  private int lettersguessed = 0;
  private String[] words = {"SPACE", "ORBIT", "STARS", "SOLAR", "COMET", "EARTH"};
  private String word;
  private List<Label> letters = new ArrayList<Label>();

  @FXML
  private void initialize() {
    addLetters();
    selectWord();
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
    if (!checkValidInput(letter)) {
      return;
    }

    // Determine which label to use based on the condition
    Label label;
    if (lettersguessed <= 16) {
      label = lblGuessedLetters1;
    } else {
      label = lblGuessedLetters2;
    }

    // Update the label
    if (label.getText().equals("")) {
      label.setText(letter + ",");
    } else {
      label.setText(label.getText() + " " + letter.toUpperCase());
    }
  }

  private void checkLetter(String letter) {
    for (int i = 0; i < word.length(); i++) {
      if (letter.equals(word.substring(i, i + 1))) {
        setLetter(letter);
      }
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
    for (Label label : letters) {
      System.out.println(label.getText());
      if (label.getText().equals("*")) {
        return;
      }
    }

    setSolved();
    clearPuzzle(panHangmanPuzzle);
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
    letters.add(letter1);
    letters.add(letter2);
    letters.add(letter3);
    letters.add(letter4);
    letters.add(letter5);
  }

  private void selectWord() {
    int random = (int) (Math.random() * words.length);
    word = words[2];
    System.out.println(word);
  }

  private void setLetter(String letter) {
    for (int i = 0; i < word.length(); i++) {
      if (letter.equals(word.substring(i, i + 1))) {
        letters.get(i).setText(letter);
      }
    }
  }
}
