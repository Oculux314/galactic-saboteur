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

  public static PuzzleName reactorPuzzle;
  public static PuzzleName laboratoryPuzzle;
  public static PuzzleName navigationPuzzle;

  public static void reset() {
    currentScreen = null;
    reactorPuzzle = null;
    laboratoryPuzzle = null;
    navigationPuzzle = null;
    timeLimit = 2;
  }
}
