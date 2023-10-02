package nz.ac.auckland.se206;

import nz.ac.auckland.se206.puzzles.Puzzle.PuzzleName;

/** Represents the state of the game. */
public class GameState {

  public static boolean isRunning = true;
  public static boolean cluesFound = false;
  public static Screen.Name currentScreen;
  public static String correctSuspect;
  public static String correctRoom;
  public static String correctTime;
  public static String difficulty = "easy";
  public static int timeLimit = 2; // In minutes
  public static boolean ttsEnabled = false;
  public static String reactorRoomGameState = "User hasn't found where the puzzle is yet";
  public static String labRoomGameState = "User hasn't found where the puzzle is yet";
  public static String controlRoomGameState = "User hasn't found where the puzzle is yet";
  public static String puzzleSolvedMessage =
      "User has solved the puzzle in this room already. They have got a clue which is located in"
          + " the side panel, under clues.";
  public static String puzzleOpenedMessage =
      "User has opened the puzzle, but has not completed it yet.";
  public static int numberOfHintsAsked = 0;

  public static PuzzleName reactorPuzzle;
  public static PuzzleName laboratoryPuzzle;
  public static PuzzleName navigationPuzzle;

  public static void reset() {
    currentScreen = null;
    reactorPuzzle = null;
    laboratoryPuzzle = null;
    navigationPuzzle = null;
    timeLimit = 2;
    numberOfHintsAsked = 0;
  }

  public static int getHintLimit() {
    // Set hint limit based on difficulty
    int hintLimit;
    if (difficulty == "easy") {
      hintLimit = 1000;
    } else if (difficulty == "medium") {
      hintLimit = 5;
    } else {
      hintLimit = 0;
    }
    return hintLimit;
  }

  public static boolean isHintLimitReached() {
    return numberOfHintsAsked >= getHintLimit();
  }

  public static int getHintLimitRemaining() {
    int num = getHintLimit() - numberOfHintsAsked;
    if (num < 0) {
      num = 0;
    }
    return num;
  }
}
