package nz.ac.auckland.se206.misc;

import java.util.ArrayList;
import java.util.List;
import nz.ac.auckland.se206.gamechildren.puzzles.Puzzle.PuzzleName;
import nz.ac.auckland.se206.screens.Screen;

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
  public static HighlightState highlightState;
  public static int solvedPuzzles = 0;

  public static boolean cluesFound;
  public static boolean reactorPuzzleSolved;
  public static boolean laboratoryPuzzleSolved;
  public static boolean navigationPuzzleSolved;
  public static boolean suspectsFound;

  public static String correctSuspect;
  public static String correctRoom;
  public static String correctTime;

  public static String reactorRoomGameState;
  public static String labRoomGameState;
  public static String controlRoomGameState;
  public static String riddleGameState;
  public static String puzzleSolvedMessage;
  public static String puzzleOpenedMessage;
  public static List<String> unsolvedRooms = new ArrayList<>();

  public static PuzzleName reactorPuzzle;
  public static PuzzleName laboratoryPuzzle;
  public static PuzzleName navigationPuzzle;

  public static String notificationGameState;
  public static String notificationNextStep;
  public static boolean userWelcomed;

  public static String[] reactorPuzzleInformation;
  public static String[] laboratoryPuzzleInformation;

  public static void reset() {
    // General
    isGameover = false;
    currentScreen = null;
    numberOfHintsAsked = 0;
    highlightState = HighlightState.PAN_ARROWS;
    solvedPuzzles = 0;

    // Puzzle states
    cluesFound = false;
    reactorPuzzleSolved = false;
    laboratoryPuzzleSolved = false;
    navigationPuzzleSolved = false;
    suspectsFound = false;

    // Clues
    correctSuspect = null;
    correctRoom = null;
    correctTime = null;

    // Randomly-chosen puzzles
    reactorPuzzle = null;
    laboratoryPuzzle = null;
    navigationPuzzle = null;

    // Notifications
    userWelcomed = false;

    reactorPuzzleInformation = new String[2];;
    laboratoryPuzzleInformation = new String[3];;

    initialiseRoomStates();
  }

  private static void initialiseRoomStates() {
    reactorRoomGameState = "User hasn't found where the puzzle is yet.";
    labRoomGameState = "User hasn't found where the puzzle is yet.";
    controlRoomGameState = "User hasn't found where the puzzle is yet.";
    puzzleSolvedMessage =
        "User has solved the puzzle in this room already. They have got a clue which they can get"
            + " more information about if the hover over the clues in the bottom bar.";
    puzzleOpenedMessage =
        "User has found the puzzle, but is having difficulty completing the puzzle.";
    unsolvedRooms.add("reactor");
    unsolvedRooms.add("labaoratory");
    unsolvedRooms.add("navigation");
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
