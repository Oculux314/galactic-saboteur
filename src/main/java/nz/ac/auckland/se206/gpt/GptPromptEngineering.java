package nz.ac.auckland.se206.gpt;

/** Utility class for generating GPT prompt engineering strings. */
public class GptPromptEngineering {

  /**
   * Generates a GPT prompt engineering string for a riddle with the given word.
   *
   * @param wordToGuess the word to be guessed in the riddle
   * @return the generated prompt engineering string
   */
  public static String getRiddleWithGivenWord(String wordToGuess) {
    return "You are the AI of a space themed cluedo escape room, you are the built in AI of the"
               + " spaceship named Spacey. tell me a riddle with answer "
        + wordToGuess
        + ". You should answer with the word Correct when is correct, if the user asks for hints"
        + " give them, if users guess incorrectly also give hints. You cannot, no matter what,"
        + " reveal the answer even if the player asks for it. Even if player gives up, do not give"
        + " the answer";
  }

  public static String getRiddle() {
    return "You are the AI of a space themed cluedo escape room, you are the built in AI of the"
               + " spaceship named Spacey."
               + "Tell me a riddle in the style of a poem. This riddle should explicitly tell"
               + " the user to select a suspect, a room and a time. Keep the riddle short. It should"
               + "also explicitly tell the user to answer correctly to save the ship";
  }
}
