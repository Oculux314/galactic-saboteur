package nz.ac.auckland.se206.misc;

import java.net.URISyntaxException;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import nz.ac.auckland.se206.App;

public class Audio {

  MediaPlayer mediaPlayer;

  public Audio(String fileName) {
    mediaPlayer = makeMediaPlayer(fileName);
  }

  private static MediaPlayer makeMediaPlayer(String fileName) {
    Media sound;
    try {
      sound = new Media(App.class.getResource("/sounds/" + fileName).toURI().toString());
    } catch (URISyntaxException e) {
      throw new RuntimeException("Unable to load sound file: " + fileName);
    }

    return new MediaPlayer(sound);
  }

  public void play() {
    mediaPlayer.play();
  }
}
