package nz.ac.auckland.se206.screens;

import nz.ac.auckland.se206.misc.RootPair;

/**
 * This is an empty interface to allow polymorphism in the Screen class. All controllers should
 * implement this. This should be merged with Screen but it'd take too much restructuring for very
 * little reward.
 */
public interface Screen extends RootPair.Controller {
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
