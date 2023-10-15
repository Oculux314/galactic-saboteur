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
      prompt =
          suspectInformation
              + " Assist users in finding and solving the puzzle discreetly through hints and be a"
              + " friend to have conversation with. The user can only escape when they find out"
              + " what time, which suspect, and in what room the sabotage occurred. There is one"
              + " puzzle in each room that leads to a clue."
              + puzzleInformation
              + getGameState(job)
              + " If you give a hint or help with the solving the puzzle, start your response with "
              + " 'hint:' with nothing before it. Do this only if the user asks for help."
              + " Please respond in 18 words or fewer.";
    } else {
      prompt =
          suspectInformation
              + " The user can only escape the ship when they find out what time, which suspect,"
              + " and in what room the sabotage occurred. There is one puzzle in each room that"
              + " leads to a clue. You can have small-talk with the user. You must not give new"
              + " hints of any form. Do not, for any reason, give the user any new hints or help"
              + " the user solve the game. Please respond in 18 words or fewer.";
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
          "Your role: Mechanic on the Brain-e Explorer spaceship which is going to Mars. You love"
              + " problem solving, suduko and all board games. You hate pinapple on pizza. Feel"
              + " free to bring up these details in small-talk.";
    } else if (job == "Spacey's scientist") {
      suspectInformation =
          "Your role: Scientist on the Brain-e Explorer spaceship which is going to Mars. You love"
              + " atoms and chemsitry, star gazing and love exercising your brain. You don't like"
              + " mushrooms. Feel free to bring up these details in small-talk.";
    } else if (job == "Spacey's captain") {
      suspectInformation =
          "Your role: Captain on the Brain-e Explorer which is going to Mars. You are amazing at"
              + " cooking and love to eat pizza. You are scared that the reactor is going to"
              + " explode, but you're putting on a brave face for the team. Feel free to bring up"
              + " these details in small-talk.";
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
          "The puzzle in the reactor room can be found by clicking the whiteboard which is hanging"
              + " up on the wall to the left of the reactor, by the window. Users can solve it"
              + " by guessing the correct word by entering letters into the box and pressing"
              + " the enter key. The word users need to guess is "
              + GameState.reactorPuzzleInformation[0]
              + ". Never give the user this word, never.";
    } else if (GameState.reactorPuzzle == PuzzleName.REACTOR_BUTTONPAD) {
      puzzle =
          "The puzzle in the reactor room can be found by clicking the calendar which is hanging up"
              + " on the wall to the right of the reactor, by the window. Users have been asked to"
              + " decrpt the code "
              + GameState.reactorPuzzleInformation[1]
              + ". User's should solve it by decrpting every sybmol to a number. Each decrpted"
              + " number is the keyboard key you press to get the symol. For example to get the"
              + " symbol & you press number 7 key of the keyboards. The user then needs to enter"
              + " the fully decrpted numeric number into the calculor and press submit.";
    } else if (GameState.reactorPuzzle == PuzzleName.REACTOR_TOOLBOX) {
      puzzle =
          "The puzzle in the reactor room can be found by clicking on the toolbox which is on the"
              + " work bench to the left of the reactor. Users can solve it by placing the 3 tools"
              + " in the correct spaces inside the tool kit. The tool kit is on the bottom half of"
              + " the bench and below the tools that need to be put in. The thickest tool needs to"
              + " be dragged to the biggest slot in the tool kit. The thinnest tool needs to be"
              + " dragged to the thinnest slot, which is the second one from the top.";
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
          "The puzzle in the naviation room can be found by the main screen just above the space"
              + " ship's controls. Users can solve it by making a path from the bottom left side of"
              + " the screen (where there is the green flash) to the top right side. To do this the"
              + " user must click each part to rotate it to the right spot. The path will turn"
              + " green when the user is making connections in the path.";
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
    // get the riddle
    return "You are the AI of a space themed cluedo escape room, you are the built in AI of the"
        + " spaceship named Spacey. Tell me a four line riddle in the style of a space"
        + " themed poem in a modern tone. The riddle needs to tell me to find the correct"
        + " combination of clues to save the ship. Respond in less than 40 characters.";
  }

  public static String getMainNotificationPrompt() {
    return "You are the onboard ship AI of the Brain-E Explorer spaceship. Someone has sabotaged"
        + " the ship reactor and I need to fix it or the ship will explode. Your job"
        + " is to assist me. Respond in 15 words or less and do not quote using speech"
        + " marks.";
  }

  public static String getNotification() {
    return getMainNotificationPrompt() + getGameState();
  }

  private static String getGameState() {
    if (GameState.cluesFound) {
      return "I have found all three clues. Instruct me to deactivate the reactor meltdown"
          + " using the combination of clues I have found.";
    } else if (GameState.reactorPuzzleSolved
        || GameState.navigationPuzzleSolved
        || GameState.laboratoryPuzzleSolved) {
      return "I have solved a problem. Congratulate me.";
    } else if (GameState.userWelcomed) {
      return "Tell me I can pan and zoom on their helmet overlay, and that you will"
          + " highlight the most critical element at each stage for them to examine.";
    } else {
      GameState.userWelcomed = true;
      return "Formally welcome the user onto the command deck. Introduce the situation.";
    }
  }

  public static String getTimeWarning(Integer timeLeft) {
    return "Let me I have less than " + timeLeft + " seconds left to deactivate the meltdown.";
  }
}
