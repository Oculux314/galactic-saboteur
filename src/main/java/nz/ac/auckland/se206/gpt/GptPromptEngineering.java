package nz.ac.auckland.se206.gpt;

import java.util.List;
import nz.ac.auckland.se206.gamechildren.puzzles.Puzzle.PuzzleName;
import nz.ac.auckland.se206.misc.GameState;

/** Utility class for generating GPT prompt engineering strings. */
public class GptPromptEngineering {

  public static String getMainPrompt(String job) {
    String prompt = "";
    boolean isUserAllowedHints = !GameState.isHintLimitReached();

    String suspectInformation = getSuspectInformation(job);
    String puzzleInformation = getPuzzleInformation(job);

    if (isUserAllowedHints) {
      System.out.println("prompt where user is allowed hints " + isUserAllowedHints);
      prompt =
          suspectInformation
              + " Assist users in finding and solving the puzzle discreetly through hints. The user"
              + " can only escape when they find out what time, which suspect, and in what room the"
              + " sabotage occurred. There is one puzzle in each room that leads to a clue."
              + puzzleInformation
              + " Please respond in 12 words or fewer."
              + getGameState(job)
              + " If you give a hint or help with the solving the game, start your response with 'A"
              + " hint is:' with nothing before it. Do this only if the user asks for help.";
    } else {
      System.out.println("prompt where user is not allowed hints " + isUserAllowedHints);
      prompt =
          suspectInformation
              + " The user can only escape the ship when they find out what time,"
              + " which suspect, and in what room the sabotage occurred. There is one puzzle in"
              + " each room that leads to a clue."
              + " Respond in 11 words or fewer. You can have small-talk with the user. You must not"
              + " give new hints of any form. Do not, for any reason, give the user any new"
              + " hints or help the user solve the game.";
    }
    return prompt;
  }

  public static String getUserInteractionPrompt(String job) {
    return getMainPrompt(job) + "Respond to the user's latest message.";
  }

  public static String getWelcomePrompt(String job) {
    return getMainPrompt(job) + "Introduce yourself what your role is and welcome the user.";
  }

  public static String getInternetErrorMessage() {
    return "Sorry, I'm having trouble picking you up! Please check your internet connection.";
  }

  public static String getSuspectInformation(String job) {
    String suspectInformation = "";
    if (job == "Spacey's mechanic") {
      suspectInformation =
          "Your role: Mechanic on the Brain-e Explorer spaceship escape room adventure.";
    } else if (job == "Spacey's scientist") {
      suspectInformation =
          "Your role: Scientist on the Brain-e Explorer spaceship escape room adventure You love"
              + " atoms and chemsitry, sunny days and love exercising your brain.";
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
          "The puzzle in the reactor room can be found by clicking the calendar which is hanging up"
              + " on the wall to the right of the reactor, by the window. Users have been asked to"
              + " decrpt the code "
              + GameState.reactorPuzzleInformation[0]
              + ". User's should solve it by decrpting every sybmol to a number. Each decrpted"
              + " number is the keyboard key you press to get the symol. For example to get the"
              + " symbol & you press number 7 key of the keyboards. The user then needs to enter"
              + " the fully decrpted numeric number into the calculor and press submit.";
    } else if (GameState.reactorPuzzle == PuzzleName.REACTOR_TOOLBOX) {
      puzzle =
          "The puzzle in the reactor room can be found by clicking on the toolbox which is on the"
              + " work bench to the left of the reactor. Users should inspect the"
              + " toolbox and solve it by placing tools inside the right spots.";
    }
    System.out.println("puzzle " + puzzle);
    return puzzle;
  }

  public static String getLaboratoryPuzzleInformation() {
    String puzzle = "";
    if (GameState.laboratoryPuzzle == PuzzleName.LABORATORY_TESTTUBES) {
      puzzle =
          "The laboratory puzzle can be found by clicking the testtubes on the lab's counter (this"
              + " is in the middle of the room). The user has been asked to "
              + GameState.laboratoryPuzzleInformation[0]
              + " Users should solve it by selecting 2 different testtubes (test tube are on the"
              + " left part of the table) to make the colour "
              + GameState.laboratoryPuzzleInformation[1]
              + ", and"
              + " pick the "
              + GameState.laboratoryPuzzleInformation[2]
              + " glitter mixture (glitter is found on the right side of the table).   ";
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

  public static String getGameState(String job) {
    // return game state depending on what suspect the user is talking to
    String gameState = "";
    if (job == "Spacey's mechanic") { // reactor puzzle
      gameState = GameState.reactorRoomGameState;
    } else if (job == "Spacey's scientist") { // lab puzzle
      gameState = GameState.labRoomGameState;
    } else if (job == "Spacey's captain") { // navigation puzzle
      gameState = GameState.controlRoomGameState;
    }

    if (gameState == GameState.puzzleSolvedMessage) {
      gameState = gameState + whatOtherRoomToLookIn();
    }
    return gameState;
  }

  private static String whatOtherRoomToLookIn() {
    // retreieve what rooms are unsolved
    List<String> unsolvedRooms = GameState.unsolvedRooms;

    if (unsolvedRooms.isEmpty()) {
      return "The user has solved all the puzzles, go to the unstable reactor to escape the ship."
          + " Be quick.";
    } else {
      return "The user has solved the puzzle in this room, go to the " + unsolvedRooms.get(0) + ".";
    }
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
