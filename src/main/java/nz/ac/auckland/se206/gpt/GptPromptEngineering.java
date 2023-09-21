package nz.ac.auckland.se206.gpt;

import nz.ac.auckland.se206.GameState;

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
    String prompt = "";
    if (job == "Spacey's mechanic" && GameState.numberOfHintsAsked < GameState.getHintLimit()) {
      prompt =
          "Your role: Mechanic on the Spacey spaceship escape room adventure.Assist users in"
              + " finding the reactor room puzzle discreetly through hints.The user can only escape"
              + " when they find out what time, which suspect and in what room the sabotage"
              + " occured. To find it, users should inspect the toolbox and solve it by placing"
              + " tools inside.Please respond in 10 words or fewer. Provide hints only if users"
              + " mention 'hint'.";
    } else if (job == "Spacey's mechanic"
        && GameState.numberOfHintsAsked >= GameState.getHintLimit()) {
      prompt =
          "Your role: Mechanic on the Spacey spaceship escape room adventure.Assist users in"
              + " finding the reactor room puzzle discreetly through hints.The user can only escape"
              + " when they find out what time, which suspect and in what room the sabotage"
              + " occured. To find it, users should inspect the toolbox and solve it by placing"
              + " tools inside. Respond in 10 words or fewer and must not include hints of"
              + " any form. Do not, for any reason give the user any hints.";
    } else if (job == "Spacey's scientist"
        && GameState.numberOfHintsAsked < GameState.getHintLimit()) {
      prompt =
          "Your role: Scientist on the Spacey spaceship escape room adventure. Assist users in"
              + " finding the lab puzzle subtly using hints. Users must deduce the time, suspect,"
              + " and room of sabotage. The puzzle involves mixing test tube colors to create green"
              + " (yellow + blue). Keep responses concise (10 words max). Provide hints only if"
              + " users mention 'hint'.";
    } else if (job == "Spacey's scientist"
        && GameState.numberOfHintsAsked >= GameState.getHintLimit()) {
      prompt =
          "Your role: Scientist on the Spacey spaceship escape room adventure. Assist users in"
              + " finding the lab puzzle subtly using hints. Users must deduce the time, suspect,"
              + " and room of sabotage. The puzzle involves mixing test tube colors to create green"
              + " (yellow + blue). Respond in 10 words or fewer and must not include hints of"
              + " any form. Do not, for any reason give the user any hints.";
    } else if (job == "Spacey's captain"
        && GameState.numberOfHintsAsked < GameState.getHintLimit()) {
      prompt =
          "Your role: Captain on the Spacey spaceship escape room adventure. Assist users in"
              + " finding the control room puzzle subtly using hints. The user can only escape when"
              + " they find out what time, which suspect and in what room in relation to the"
              + " sabotage of Spacey. The puzzle can be found at the room's main screen, and can be"
              + " solved by clicking each line to rotate and then to finding the path to the exit."
              + " Respond in 10 words or fewer and must not include hints of"
              + " any form. Do not, for any reason give the user any hints.";
    } else if (job == "Spacey's captain"
        && GameState.numberOfHintsAsked >= GameState.getHintLimit()) {
      prompt =
          "Your role: Captain on the Spacey spaceship escape room adventure. Assist users in"
              + " finding the control room puzzle subtly using hints. The user can only escape when"
              + " they find out what time, which suspect and in what room in relation to the"
              + " sabotage of Spacey. The puzzle can be found at the room's main screen, and can be"
              + " solved by clicking each line to rotate and then to finding the path to the exit."
              + " Keep responses concise (10 words max). Provide hints only if users mention"
              + " 'hint'.";
    }

    return prompt;
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
