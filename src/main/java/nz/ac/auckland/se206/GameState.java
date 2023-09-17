package nz.ac.auckland.se206;

/** Represents the state of the game. */
public class GameState {

  public static Screen.Name currentScreen;

  public static void reset() {
    currentScreen = null;
  }
}
