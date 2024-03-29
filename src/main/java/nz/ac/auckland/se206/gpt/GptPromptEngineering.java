package nz.ac.auckland.se206.gpt;

import java.util.List;
import nz.ac.auckland.se206.gamechildren.puzzles.Puzzle.PuzzleName;
import nz.ac.auckland.se206.misc.GameState;

/** Utility class for generating GPT prompt engineering strings. */
public class GptPromptEngineering {

  /**
   * Generates the main prompt for the GPT API based on the provided job, including information
   * about suspects and puzzles, as well as hint availability.
   *
   * @param job The job title associated with the prompt.
   * @return The main prompt to be used for the GPT API call.
   */
  public static String getMainPrompt(String job) {
    // create an empty string to store the prompt
    String prompt;
    // check if the user is allowed hints
    boolean isUserAllowedHints = !GameState.isHintLimitReached();

    // get the suspect information and puzzle information
    String suspectInformation = getSuspectInformation(job);
    String puzzleInformation = getPuzzleInformation(job);

    if (isUserAllowedHints) {
      // if the user is allowed hints, add the hint information to the prompt and say hints are
      // allowed to the gpt api
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
      // if the user is not allowed hints, don't add the hint information to the prompt and say no
      // hints to the gpt api
      prompt =
          suspectInformation
              + " The user can only escape the ship when they find out what time, which suspect,"
              + " and in what room the sabotage occurred. There is one puzzle in each room that"
              + " leads to a clue. You can have small-talk with the user. You must not give new"
              + " hints of any form. Do not, for any reason, give the user any new hints or help"
              + " the user solve the game. Please respond in 18 words or fewer.";
    }
    // Give the updated prompt
    return prompt;
  }

  /**
   * Generates the user interaction prompt based on the provided job, including the main prompt
   * followed by an instruction to respond to the user's latest message.
   *
   * @param job The job title associated with the prompt.
   * @return The user interaction prompt.
   */
  public static String getUserInteractionPrompt(String job) {
    return getMainPrompt(job) + "Respond to the user's latest message.";
  }

  /**
   * Generates the welcome prompt based on the provided job, including the main prompt followed by
   * an instruction to introduce oneself and welcome the user.
   *
   * @param job The job title associated with the prompt.
   * @return The welcome prompt.
   */
  public static String getWelcomePrompt(String job) {
    return getMainPrompt(job)
        + "Introduce yourself and explain what your role is, then welcome the user.";
  }

  /**
   * Retrieves the internet error message when there are issues with the internet connection.
   *
   * @return The internet error message.
   */
  public static String getInternetErrorMessage() {
    return "Sorry, I'm having trouble connecting! Please check your internet connection.";
  }

  /**
   * Retrieves the suspect information based on the provided job, including details specific to each
   * suspect on the Brain-e Explorer spaceship.
   *
   * @param job The job title associated with the suspect.
   * @return The suspect information associated with the job.
   */
  public static String getSuspectInformation(String job) {
    // get the suspect information depending on what suspect the user is talking to
    String suspectInformation = "";
    if (job == "Spacey's mechanic") {
      // set the suspect information to the mechanic information
      suspectInformation =
          "Your role: Mechanic on the Brain-e Explorer spaceship which is going to Mars. You are"
              + " panicked because the ship's reactor is going to blow up. Use"
              + " rough, simple, colloquial words. You love problem solving, suduko and all board"
              + " games. You hate pinapple on pizza. Feel free to bring up these details in"
              + " small-talk.";
    } else if (job == "Spacey's scientist") {
      // set the suspect information to the scientist information
      suspectInformation =
          "Your role: Scientist on the Brain-e Explorer spaceship. You are panicked becaused the"
              + " ship's reactor  is going to blow up. Use technical and"
              + " proper words. You love atoms and chemsitry, star gazing and love exercising your"
              + " brain. You don't like mushrooms. Feel free to bring up these details in"
              + " small-talk.";
    } else if (job == "Spacey's captain") {
      // set the suspect information to the captain information
      suspectInformation =
          "Your role: Captain on the Brain-e Explorer which is going to Mars but the ship's reactor"
              + " is going to blow up so you're a bit panicked. Your responses should be "
              + " confident. You are amazing at cooking and love to eat pizza. You"
              + " are scared that the reactor is going to explode, but you're putting on a brave"
              + " face for the team. Feel free to bring up these details in small-talk.";
    }
    return suspectInformation;
  }

  /**
   * Retrieves the puzzle information based on the provided job, including details about the
   * specific puzzle associated with each job on the Brain-e Explorer spaceship.
   *
   * @param job The job title associated with the puzzle.
   * @return The puzzle information associated with the job.
   */
  public static String getPuzzleInformation(String job) {
    // get the puzzle information depending on what suspect the user is talking to
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

  /**
   * Retrieves the specific information related to the reactor puzzle, depending on the type of
   * reactor puzzle the user is currently attempting.
   *
   * @return The details and instructions associated with the reactor puzzle currently in progress.
   */
  public static String getReactorPuzzleInformation() {
    // start with an empty string incase this puzzle is not the one the user is doing
    String puzzle = "";
    // check that it's the reactor puzzle the user is doing
    if (GameState.reactorPuzzle == PuzzleName.REACTOR_HANGMAN) {
      // set the puzzle string to the reactor puzzle information if it's the hangman puzzle
      puzzle =
          "The puzzle in the reactor room can be found by clicking the whiteboard which is hanging"
              + " up on the wall to the left of the reactor, by the window. Users can solve it"
              + " by guessing the correct word by entering letters into the box and pressing"
              + " the enter key. The word users need to guess is "
              + GameState.reactorPuzzleInformation[0]
              + ". Never give the user this word, never.";
    } else if (GameState.reactorPuzzle == PuzzleName.REACTOR_BUTTONPAD) {
      // set the puzzle string to the reactor puzzle information if it's the button pad puzzle
      puzzle =
          "The puzzle in the reactor room can be found by clicking the calendar which is hanging up"
              + " on the wall to the right of the reactor, by the window. Users have been asked to"
              + " decrpt the code "
              + GameState.reactorPuzzleInformation[1]
              + ". User's should solve it by decrpting every sybmol to a number. Each decrpted"
              + " number is the keyboard key you press to get the symol. For example to get the"
              + " symbol & you press number 7 key of the keyboards. The user then needs to enter"
              + " the fully decrpted numeric number into the calculator and press submit.";
    } else if (GameState.reactorPuzzle == PuzzleName.REACTOR_TOOLBOX) {
      // set the puzzle string to the reactor puzzle information if it's the toolbox puzzle
      puzzle =
          "The puzzle in the reactor room can be found by clicking on the toolbox which is on the"
              + " work bench to the left of the reactor. Users can solve it by placing the 3 tools"
              + " in the correct spaces inside the tool kit. The tool kit is on the bottom half of"
              + " the bench and below the tools that need to be put in. The thickest tool needs to"
              + " be dragged to the biggest slot in the tool kit. The thinnest tool needs to be"
              + " dragged to the thinnest slot, which is the second one from the top.";
    }
    // return the puzzle string
    return puzzle;
  }

  /**
   * Retrieves the specific information related to the laboratory puzzle, depending on the type of
   * laboratory puzzle the user is currently attempting.
   *
   * @return The details and instructions associated with the laboratory puzzle currently in
   *     progress.
   */
  public static String getLaboratoryPuzzleInformation() {
    // start with an empty string incase this puzzle is not the one the user is doing
    String puzzle = "";
    // check that it's the lab puzzle the user is doing
    if (GameState.laboratoryPuzzle == PuzzleName.LABORATORY_TESTTUBES) {
      // set the puzzle string to the lab puzzle information
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

  /**
   * Retrieves the specific information related to the control room puzzle, depending on the type of
   * control room puzzle the user is currently attempting.
   *
   * @return The details and instructions associated with the control room puzzle currently in
   *     progress.
   */
  public static String getControlRoomPuzzleInformation() {
    // start with an empty string incase this puzzle is not the one the user is doing
    String puzzle = "";
    if (GameState.navigationPuzzle == PuzzleName.NAVIGATION_COMPUTER) {
      // set the puzzle string to the navigation puzzle information
      puzzle =
          "The puzzle in the naviation room can be found by the main screen just above the space"
              + " ship's controls. Users can solve it by making a path from the bottom left side of"
              + " the screen (where there is the green flash) to the top right side. To do this the"
              + " user must click each part to rotate it to the right spot. The path will turn"
              + " green when the user is making connections in the path.";
    }
    return puzzle;
  }

  /**
   * Provides information about the next room the user should investigate based on which rooms
   * remain unsolved.
   *
   * @return Information about the next room the user should investigate.
   */
  private static String whatOtherRoomToLookIn() {
    // get the list of unsolved rooms
    List<String> unsolvedRooms = GameState.unsolvedRooms;

    // check if there are no unsolved rooms
    if (unsolvedRooms.isEmpty()) {
      return "The user has solved all the puzzles, go to the unstable reactor to escape the ship."
          + " Be quick.";
    } else {
      return "The user has solved the puzzle in this room, go to the " + unsolvedRooms.get(0) + ".";
    }
  }

  /**
   * Retrieves the riddle for the AI of the space-themed Cluedo escape room.
   *
   * @return The riddle for the AI, prompting the user to find the correct combination of clues to
   *     save the ship.
   */
  public static String getRiddle() {
    // return the riddle for the AI
    return "You are the AI of a space-themed cluedo escape room, you are the built-in AI of the"
        + " spaceship named Spacey. Tell me a four-line riddle in the style of a"
        + " space-themed poem in a modern tone. The riddle needs to tell me to find the"
        + " correct combination of clues to save the ship. Respond in less than 40"
        + " characters.";
  }

  /**
   * Retrieves the main notification prompt for the onboard ship AI of the Brain-E Explorer
   * spaceship.
   *
   * @return The main notification prompt for the AI, providing instructions for assisting the user
   *     within the specified word limit.
   */
  public static String getMainNotificationPrompt() {
    return "You are the onboard ship AI of the Brain-E Explorer spaceship. Someone has sabotaged"
        + " the ship reactor and I need to fix it or the ship will explode. Your job is to"
        + " assist me. Respond in 15 words or less and do not quote using speech marks.";
  }

  /**
   * Retrieves the main notification prompt for the onboard ship AI of the Brain-E Explorer
   * spaceship, along with the current game state.
   *
   * @return The main notification prompt for the AI, including instructions for assisting the user
   *     and the current game state.
   */
  public static String getNotification() {
    return getMainNotificationPrompt() + getGameState();
  }

  /**
   * Generates a time warning for the AI based on the time left to deactivate the meltdown.
   *
   * @param timeLeft The remaining time in seconds to deactivate the meltdown.
   * @return A time warning for the AI, indicating the remaining time to resolve the situation.
   */
  public static String getTimeWarning(Integer timeLeft) {
    return "Let me I have less than " + timeLeft + " seconds left to deactivate the meltdown.";
  }

  /**
   * Retrieves the current game state based on the suspect the user is interacting with.
   *
   * @param job The role of the suspect the user is currently interacting with.
   * @return The current game state information depending on the suspect's role.
   */
  public static String getGameState(String job) {
    // get the game state depending on what suspect the user is talking to
    String gameState = "";
    if (job == "Spacey's mechanic") { // Reactor puzzle
      gameState = GameState.reactorRoomGameState;
    } else if (job == "Spacey's scientist") { // Lab puzzle
      gameState = GameState.labRoomGameState;
    } else if (job == "Spacey's captain") { // Navigation puzzle
      gameState = GameState.controlRoomGameState;
    }

    // check if the user has solved the puzzle
    if (gameState == GameState.puzzleSolvedMessage) {
      gameState = gameState + whatOtherRoomToLookIn();
    }
    // give the updated value of the game state
    return gameState;
  }

  /**
   * Retrieves the current game state information for the AI, based on the progress and actions of
   * the user.
   *
   * @return The current game state information for the AI to provide suitable instructions to the
   *     user.
   */
  private static String getGameState() {
    if (!GameState.userWelcomed) {
      // if the user hasn't been welcomed, welcome them
      GameState.userWelcomed = true;
      return "Formally welcome the user onto the command deck. Introduce the situation.";
    } else if (GameState.cluesFound) {
      // check if the user has found all three clues
      return "I have found all three clues. Instruct me to deactivate the reactor meltdown using"
          + " the combination of clues I have found.";
    } else if (GameState.reactorPuzzleSolved
        || GameState.navigationPuzzleSolved
        || GameState.laboratoryPuzzleSolved) {
      // check if the user has solved any of the puzzles
      return "I have solved a problem. Congratulate me.";
    } else {
      // check if the user has been welcomed
      return "Tell me I can pan and zoom on their helmet overlay, and that you will highlight the"
          + " most critical element at each stage for them to examine.";
    }
  }
}
