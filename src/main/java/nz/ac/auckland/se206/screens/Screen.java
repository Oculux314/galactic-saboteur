package nz.ac.auckland.se206.screens;

/**
 * This is an empty interface to allow polymorphism in the Screen class. All controllers should
 * implement this. This should be merged with Screen but it'd take too much restructuring for very
 * little reward.
 */
public interface Screen {
  /** Represents possible names for a screen. */
  public enum Name {
    DEFAULT,
    MAIN,
    TITLE,
    SETTINGS,
    EXPOSITION,
    GAME,
    END,
  }
}
