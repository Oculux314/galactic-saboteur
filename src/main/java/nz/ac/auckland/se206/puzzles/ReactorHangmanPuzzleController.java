package nz.ac.auckland.se206.puzzles;

import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

/** Controller class for the reactor hangman puzzle. */
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
    addLabels();
    selectWord();
  }

  /**
   * Called when the guess button is clicked. Adds valid input into lblGuessedLetters1 or
   * lblGuessedLetters2.
   *
   * @param
   * @return
   */
  @FXML
  private void onGuess() throws InterruptedException {
    letter = txtGuessLetter.getText();
    addLetter(letter);
  }

  /**
   * Adds a letter to the guessed letters text area
   *
   * @param letter the letter to add
   * @return
   */
  private void addLetter(String letter) throws InterruptedException {
    if (!checkValidInput(letter)) {
      txtGuessLetter.clear();
      return;
    }

    lettersguessed++;

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

    checkLetter(letter.toUpperCase());
  }

  /**
   * Checks if the letter is in the word and updates the word accordingly
   *
   * @param letter the letter to check
   * @return
   */
  private void checkLetter(String letter) throws InterruptedException {
    for (int i = 0; i < word.length(); i++) {
      if (letter.equals(word.substring(i, i + 1))) {
        setLetter(letter);
      }
    }

    checkIfSolved();
  }

  /**
   * Checks if the input is valid
   *
   * @param letter the letter to check
   * @return true if the input is valid, false otherwise
   */
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

  /**
   * Checks if the word has been solved
   *
   * @param
   * @return
   */
  private void checkIfSolved() throws InterruptedException {
    txtGuessLetter.clear();
    for (Label label : letters) {
      if (label.getText().equals("*")) {
        return;
      }
    }

    // Puzzle is solved
    setSolved();
    clearPuzzle(panHangmanPuzzle);
  }

  /**
   * Adds the labels to the letters list
   *
   * @param
   * @return
   */
  private void addLabels() {
    letters.add(letter1);
    letters.add(letter2);
    letters.add(letter3);
    letters.add(letter4);
    letters.add(letter5);
  }

  /**
   * Selects a random word from the words array
   *
   * @param
   * @return
   */
  private void selectWord() {
    int random = (int) (Math.random() * words.length);
    word = words[random];
  }

  /**
   * Sets the letter in the word to the letter guessed
   *
   * @param letter the letter to set
   * @return
   */
  private void setLetter(String letter) {
    for (int i = 0; i < word.length(); i++) {
      if (letter.equals(word.substring(i, i + 1))) {
        letters.get(i).setText(letter);
      }
    }
  }
}
