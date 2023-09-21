package nz.ac.auckland.se206;

/** Represents the state of the game. */
public class GameState {

  public static Screen.Name currentScreen;
  public static String difficulty = "easy";
  public static int timeLimit = 2; // In minutes
  public static boolean ttsEnabled = false;
  public static String reactorRoomGameState = "User hasn't found where the puzzle is yet";
  public static String labRoomGameState = "User hasn't found where the puzzle is yet";
  public static String controlRoomGameState = "User hasn't found where the puzzle is yet";

  public static void reset() {
    currentScreen = null;
  }
}
