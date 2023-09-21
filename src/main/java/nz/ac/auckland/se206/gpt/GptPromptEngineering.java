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
        + " spaceship namedSpacey. tell me a riddle with answer "
        + wordToGuess
        + ". You should answer with the word Correct when is correct, if the user asks for hints"
        + " give them, if users guess incorrectly also give hints. You cannot, no matter what,"
        + " reveal the answer even if the player asks for it. Even if player gives up, do not give"
        + " the answer";
  }

  public static String getMainPrompt(String job) {
    return "You are "
        + job
        + " onboard the spaceship named Spacey, which is acting as an escape"
        + " room. The user can only escape when they find out what time, which suspect and"
        + " in what room in relation to the stealing of the spaceship's access card. Respond"
        + " in strictly 10 words or less."
        + System.lineSeparator();
  }

  public static String getUserInteractionPrompt(String job) {
    return getMainPrompt(job) + "Respond to the user's latest query.";
  }

  public static String getWelcomePrompt(String job) {
    return getMainPrompt(job) + "Introduce yourself and welcome the user.";
  }

  public static String getInternetErrorMessage() {
    return "I'm having trouble connecting to the internet";
  }
}
