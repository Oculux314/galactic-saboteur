package nz.ac.auckland.se206.misc;

import java.net.URISyntaxException;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import nz.ac.auckland.se206.App;

/** Represents a sound file that can be played. This class is immutable. */
public class Audio {

  private MediaPlayer mediaPlayer;

  /** Constructs a new Audio object with the given audio file name. */
  public Audio(String fileName) {
    mediaPlayer = makeMediaPlayer(fileName);
    preload();
  }

  /** Constructs a new MediaPlayer object with the given audio file name. */
  private static MediaPlayer makeMediaPlayer(String fileName) {
    Media sound;
    try {
      sound = new Media(App.class.getResource("/sounds/" + fileName).toURI().toString());
    } catch (URISyntaxException e) {
      throw new RuntimeException("Unable to load sound file: " + fileName);
    }

    return new MediaPlayer(sound);
  }

  /**
   * Preload it for being played properly. This ensures the timing is correct on the first execution
   */
  private void preload() {
    mediaPlayer.play();
    mediaPlayer.stop();
  }

  /**
   * Plays the sound file. If the sound file is already playing, it will be stopped and restarted.
   */
  public void play() {
    mediaPlayer.stop();
    mediaPlayer.play();
  }
}
