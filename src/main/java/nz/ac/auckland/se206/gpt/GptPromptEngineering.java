package nz.ac.auckland.se206.gpt;

import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.puzzles.Puzzle.PuzzleName;

/** Utility class for generating GPT prompt engineering strings. */
public class GptPromptEngineering {

  public static String getMainPrompt(String job) {
    String prompt = "";
    boolean isUserAllowedHints = !GameState.isHintLimitReached();

    String suspectInformation = getSuspectInformation(job);
    String puzzleInformation = getPuzzleInformation(job);

    if (isUserAllowedHints) {
      prompt =
          suspectInformation
              + " Assist users in finding and solving the puzzle discreetly through hints. The user"
              + " can only escape when they find out what time, which suspect, and in what room the"
              + " sabotage occurred. "
              + puzzleInformation
              + " Please respond in 10 words or fewer. Provide hints only if users mention 'hint'."
              + " If you give a hint, start your response with 'A hint is:'.";
    } else {
      prompt =
          suspectInformation
              + "Support users in finding and solving the puzzle but don't give any information"
              + " they don't know. The user can only escape when they find out what time, which"
              + " suspect, and in what room the sabotage occurred. "
              + puzzleInformation
              + " Respond in 10 words or fewer and must not include new hints of any form. Do not,"
              + " for any reason, give the user any new hints.";
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

  public static String getSuspectInformation(String job) {
    String suspectInformation = "";
    if (job == "Spacey's mechanic") {
      suspectInformation =
          "Your role: Mechanic on the Brain-e Explorer spaceship escape room adventure.";
    } else if (job == "Spacey's scientist") {
      suspectInformation =
          "Your role: Scientist on the Brain-e Explorer spaceship escape room adventure.";
    } else if (job == "Spacey's captain") {
      suspectInformation =
          "Your role: Captain on the Brain-e Explorer spaceship escape room adventure.";
    }
    return suspectInformation;
  }

  public static String getPuzzleInformation(String job) {
    String puzzleInformation = "";
    if (job == "Spacey's mechanic") { // reactor puzzle
      puzzleInformation = getReactorPuzzleInformation();
    } else if (job == "Spacey's scientist") { // lab puzzle
      puzzleInformation = getLaboratoryPuzzleInformation();
    } else if (job == "Spacey's captain") { // navigation puzzle
      puzzleInformation = getControlRoomPuzzleInformation();
    }
    return puzzleInformation;
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
               + " spaceship named Spacey. Tell me a five line riddle in the style of a space"
               + " themed poem in a modern tone. This riddle should only tell the user to select"
               + " the correct key card combination,tell the user to answer correctly to save the"
               + " ship and tell the user to solve the puzzles to get the clues. Keep the riddle"
               + " short and simple (you have a maximum token size of 60).";
  }
}
