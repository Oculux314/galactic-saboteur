package nz.ac.auckland.se206.gpt;

import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.puzzles.Puzzle.PuzzleName;

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

  public static String getMainPrompt(String job) {
    String prompt = "";
    if (job == "Spacey's mechanic" && GameState.numberOfHintsAsked < GameState.getHintLimit()) {
      prompt =
          "Your role: Mechanic on the Spacey spaceship escape room adventure.Assist users in"
              + " finding the reactor room puzzle discreetly through hints.The user can only escape"
              + " when they find out what time, which suspect and in what room the sabotage"
              + " occured. "
              + getReactorPuzzleInformation()
              + " Please respond in 10 words or fewer. Provide hints only if"
              + " users mention 'hint'.";
    } else if (job == "Spacey's mechanic"
        && GameState.numberOfHintsAsked >= GameState.getHintLimit()) {
      prompt =
          "Your role: Mechanic on the Spacey spaceship escape room adventure. Support users in"
              + " finding the reactor room puzzle but don't give any information they don't know"
              + " .The user can only escape when they find out what time, which suspect and in what"
              + " room the sabotage occured. "
              + getReactorPuzzleInformation()
              + " Respond in 10 words or fewer and must not include"
              + " hints of any form. Do not, for any reason give the user any hints.";
    } else if (job == "Spacey's scientist"
        && GameState.numberOfHintsAsked < GameState.getHintLimit()) {
      prompt =
          "Your role: Scientist on the Spacey spaceship escape room adventure. Assist users in"
              + " finding the lab puzzle subtly using hints. Users must deduce the time, suspect,"
              + " and room of sabotage. "
              + getLaboratoryPuzzleInformation()
              + " Keep responses concise (10 words max). Provide hints only if"
              + " users mention 'hint'.";
    } else if (job == "Spacey's scientist"
        && GameState.numberOfHintsAsked >= GameState.getHintLimit()) {
      prompt =
          "Your role: Scientist on the Spacey spaceship escape room adventure. Assist users in"
              + " finding the lab puzzle by giving information they already know. Users must deduce"
              + " the time, suspect, and room of sabotage in the game overall. "
              + getLaboratoryPuzzleInformation()
              + " Respond in 10 words or fewer and must not include hints of"
              + " any form. Do not, for any reason give the user any hints.";
    } else if (job == "Spacey's captain"
        && GameState.numberOfHintsAsked < GameState.getHintLimit()) {
      prompt =
          "Your role: Captain on the Spacey spaceship escape room adventure. Assist users in"
              + " finding the control room puzzle subtly using hints. The user can only escape when"
              + " they find out what time, which suspect and in what room the"
              + " sabotage of Spacey occured. "
              + getControlRoomPuzzleInformation()
              + " Respond in 10 words or fewer and must not include hints of"
              + " any form. Do not, for any reason give the user any hints.";
    } else if (job == "Spacey's captain"
        && GameState.numberOfHintsAsked >= GameState.getHintLimit()) {
      prompt =
          "Your role: Captain on the Spacey spaceship escape room adventure. Assist users in"
              + " finding the control room puzzle by using information the user would already know."
              + " The user can only escape when they find out what time, which suspect and in what"
              + " room in relation to the sabotage of Spacey. "
              + getControlRoomPuzzleInformation()
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

  public static String getReactorPuzzleInformation() {
    String puzzle = "";
    if (GameState.reactorPuzzle == PuzzleName.REACTOR_HANGMAN) {
      puzzle =
          "The reactor puzzle can be found by clicking the apple in the top half of the reactor"
              + " room. Users should solve it trying to guess the correct word. The word is related"
              + " to the solar system, stars and galaxies";
    } else if (GameState.reactorPuzzle == PuzzleName.REACTOR_BUTTONPAD) {
      puzzle =
          "The reactor puzzle can be found by clicking button pad in the top half of the"
              + " reactor room. Users should solve it by clicking the buttons in the right order.";
    } else if (GameState.reactorPuzzle == PuzzleName.REACTOR_TOOLBOX) {
      puzzle =
          "The reactor puzzle can be found by clicking on the toolbox in the bottom half of the"
              + " reactor room. Users should inspect the toolbox and solve it by placing tools"
              + " inside the right spots.";
    }
    return puzzle;
  }

  public static String getLaboratoryPuzzleInformation() {
    String puzzle = "";
    if (GameState.laboratoryPuzzle == PuzzleName.LABORATORY_TESTTUBES) {
      puzzle =
          "The laboratory puzzle can be found by clicking the testtubes on the lab's counter (this"
              + " is in the middle of the room)Users should solve it trying to mix two testtube's"
              + " together to create the right colour.";
    }
    return puzzle;
  }

  public static String getControlRoomPuzzleInformation() {
    String puzzle = "";
    if (GameState.navigationPuzzle == PuzzleName.NAVIGATION_COMPUTER) {
      puzzle =
          "The naviation puzzle can be found by the main screen. Users should solve it by making a"
              + " path from the bottom left side of the screen to the top right side. To do this"
              + " the user must click each part to rotate it to the right spot. The path will turn"
              + " green when the user has solved it.";
    }
    return puzzle;
  }

  public static String getRiddle() {
    return "You are the AI of a space themed cluedo escape room, you are the built in AI of the"
        + " spaceship named Spacey.Tell me a riddle in the style of a space themed poem in a"
        + " modern tone. This riddle should explicitly tell the user to select a suspect, a"
        + " room and a time. Italso has to to tell the user to answer correctly to save the"
        + " ship. It has to tell the user tosolve the puzzles to get the clues.Keep the"
        + " riddle short and simple (under 80 words).";
  }
}
// setPuzzle(PuzzleName.REACTOR_TOOLBOX);
//       setPuzzle(PuzzleName.REACTOR_BUTTONPAD);
//       setPuzzle(PuzzleName.REACTOR_APPLE);
//       setPuzzle(PuzzleName.LABORATORY_TESTTUBES);
//       setPuzzle(PuzzleName.NAVIGATION_COMPUTER);
