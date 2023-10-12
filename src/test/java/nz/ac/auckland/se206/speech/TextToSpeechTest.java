package nz.ac.auckland.se206.speech;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import nz.ac.auckland.se206.misc.TextToSpeech;
import nz.ac.auckland.se206.misc.TextToSpeech.TextToSpeechException;

class TextToSpeechTest {
  private TextToSpeech textToSpeech;

  @BeforeEach
  void setUp() throws TextToSpeechException {
    textToSpeech = new TextToSpeech();
  }

  @Test
  void textToSpeech() {
    textToSpeech.speak("hello");
  }

  @Test
  void multipleTextToSpeech() {
    textToSpeech.speak("hello", "how are you?");
    textToSpeech.speak("today is a great day");
  }
}
