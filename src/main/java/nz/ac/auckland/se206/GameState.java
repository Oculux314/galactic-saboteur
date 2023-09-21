package nz.ac.auckland.se206;

/** Represents the state of the game. */
public class GameState {

  public static boolean isRunning = true;
  public static Screen.Name currentScreen;
  public static String difficulty = "easy";
  public static int timeLimit = 2; // In minutes
  public static boolean ttsEnabled = false;

  public static void reset() {
    currentScreen = null;
  }
}
