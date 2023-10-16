package nz.ac.auckland.se206.misc;

import java.util.ArrayList;
import java.util.List;
import nz.ac.auckland.se206.gamechildren.puzzles.Puzzle.PuzzleName;
import nz.ac.auckland.se206.screens.Screen;

/**
 * The GameState class represents the current state of the game, including puzzle information, user
 * progress, and notifications. It keeps track of various game-related data and provides methods for
 * updating and retrieving this information. This class manages the state of puzzles, user progress,
 * and notifications within the game, ensuring a coherent and consistent experience for the user
 * throughout gameplay.
 */
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
  public static boolean ttsInterrupted = false;
  public static boolean ttsFinished = true;

  public static boolean isGameover;
  public static Screen.Name currentScreen;
  public static int numberOfHintsAsked;
  public static HighlightState highlightState;
  public static int solvedPuzzles;

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

  /**
   * Resets the state of the game to its initial state, ensuring that various game-related
   * variables, puzzles, and notifications are reinitialized. This method is responsible for
   * resetting the game to its starting point, allowing for a fresh gameplay experience.
   */
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

    reactorPuzzleInformation = new String[2];
    ;
    laboratoryPuzzleInformation = new String[3];
    ;

    initialiseRoomStates();
  }

  /** Initializes the room states, puzzle messages, and the list of unsolved rooms in the game. */
  private static void initialiseRoomStates() {
    // Initialise room states
    reactorRoomGameState = "User hasn't found where the puzzle is yet.";
    labRoomGameState = "User hasn't found where the puzzle is yet.";
    controlRoomGameState = "User hasn't found where the puzzle is yet.";

    // initlise puzzle messages
    puzzleSolvedMessage =
        "User has solved the puzzle in this room already. They have got a clue which they can get"
            + " more information about if the hover over the clues in the bottom bar.";
    puzzleOpenedMessage =
        "User has found the puzzle, but is having difficulty completing the puzzle.";

    // add rooms to the unsolved rooms list
    unsolvedRooms.add("reactor");
    unsolvedRooms.add("labaoratory");
    unsolvedRooms.add("navigation");
  }

  /**
   * Gets the hint limit based on the game difficulty.
   *
   * @return The limit for the number of hints based on the difficulty level.
   */
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

  /**
   * Checks if the hint limit has been reached.
   *
   * @return true if the hint limit has been reached, false otherwise.
   */
  public static boolean isHintLimitReached() {
    return numberOfHintsAsked >= getHintLimit();
  }

  /**
   * Gets the remaining number of hints that can be asked based on the hint limit.
   *
   * @return The number of hints remaining before the hint limit is reached.
   */
  public static int getHintLimitRemaining() {
    int num = getHintLimit() - numberOfHintsAsked;
    // If the number of hints remaining is negative, set it to 0 so it appears correctly on screen
    if (num < 0) {
      num = 0;
    }
    return num;
  }
}
