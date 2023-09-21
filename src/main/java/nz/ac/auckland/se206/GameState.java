package nz.ac.auckland.se206;

/** Represents the state of the game. */
public class GameState {

  public static Screen.Name currentScreen;
  public static String correctSuspect;
  public static String correctRoom;
  public static String correctTime;

  public static void reset() {
    currentScreen = null;
  }
}
