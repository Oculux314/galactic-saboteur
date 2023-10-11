package nz.ac.auckland.se206;

public class TaggedThread extends Thread {

  public TaggedThread(Runnable runnable) {
    super(runnable);
  }

  @Override
  public void start() {
    super.start();
    App.addThread(this);
  }
}
