package nz.ac.auckland.se206.misc;

import nz.ac.auckland.se206.gamechildren.puzzles.Puzzle.PuzzleName;
import nz.ac.auckland.se206.screens.Screen;
import nz.ac.auckland.se206.screens.Screen.Name;

/** Represents the state of the game. */
public class GameState {
  public enum HighlightState {
    PAN_ARROWS,
    REACTOR_INITAL,
    SUSPECTS,
    PUZZLES,
    REACTOR_FINAL
  }

  public static boolean isRunning = true;
  public static String difficulty = "easy";
  public static int timeLimit = 2; // In minutes
  public static boolean ttsEnabled = false;

  public static boolean isGameover;
  public static Screen.Name currentScreen;
  public static int numberOfHintsAsked;
  public static boolean cluesFound;
  public static HighlightState highlightState;

  public static String correctSuspect;
  public static String correctRoom;
  public static String correctTime;

  public static String reactorRoomGameState;
  public static String labRoomGameState;
  public static String controlRoomGameState;
  public static String puzzleSolvedMessage;
  public static String puzzleOpenedMessage;

  public static PuzzleName reactorPuzzle;
  public static PuzzleName laboratoryPuzzle;
  public static PuzzleName navigationPuzzle;

  public static void reset() {
    isGameover = false;
    currentScreen = null;
    numberOfHintsAsked = 0;
    cluesFound = false;
    highlightState = HighlightState.PAN_ARROWS;

    correctSuspect = null;
    correctRoom = null;
    correctTime = null;

    reactorPuzzle = null;
    laboratoryPuzzle = null;
    navigationPuzzle = null;

    initialiseRoomStates();
  }

  private static void initialiseRoomStates() {
    reactorRoomGameState = "User hasn't found where the puzzle is yet";
    labRoomGameState = "User hasn't found where the puzzle is yet";
    controlRoomGameState = "User hasn't found where the puzzle is yet";
    puzzleSolvedMessage =
        "User has solved the puzzle in this room already. They have got a clue which they can get"
            + " more information about if the hover over the clues in the bottom bar";
    puzzleOpenedMessage =
        "User has opened the puzzle, but is having difficulty knowing how to complete the puzzle.";
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
    // If the number of hints remaining is negative, set it to 0 so it appears correctly on screen
    if (num < 0) {
      num = 0;
    }
    return num;
  }
}
