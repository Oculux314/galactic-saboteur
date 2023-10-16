package nz.ac.auckland.se206.misc;

import nz.ac.auckland.se206.App;

/**
 * The TaggedThread class extends the functionality of the Thread class by allowing threads to be
 * tagged with additional information for identification and management purposes.
 */
public class TaggedThread extends Thread {

  /**
   * Constructs a TaggedThread with the specified Runnable object.
   *
   * @param runnable The Runnable object to be executed by the thread.
   */
  public TaggedThread(Runnable runnable) {
    super(runnable);
  }

  /** Starts the thread and adds it to the application's list of active threads. */
  @Override
  public void start() {
    super.start();
    App.addThread(this);
  }
}
