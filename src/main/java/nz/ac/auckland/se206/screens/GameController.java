package nz.ac.auckland.se206.screens;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.components.AnimatedButton;
import nz.ac.auckland.se206.components.HighlightButton;
import nz.ac.auckland.se206.components.StateButton;
import nz.ac.auckland.se206.gamechildren.NotificationpanelController;
import nz.ac.auckland.se206.gamechildren.PopupController;
import nz.ac.auckland.se206.gamechildren.SidepanelController;
import nz.ac.auckland.se206.gamechildren.Timer;
import nz.ac.auckland.se206.gamechildren.ZoomAndPanHandler;
import nz.ac.auckland.se206.gamechildren.puzzles.Puzzle.PuzzleName;
import nz.ac.auckland.se206.gamechildren.puzzles.PuzzleLoader;
import nz.ac.auckland.se206.gamechildren.suspects.Suspect;
import nz.ac.auckland.se206.gamechildren.suspects.SuspectController;
import nz.ac.auckland.se206.misc.GameState;
import nz.ac.auckland.se206.misc.GameState.HighlightState;

/** GameController class that manages the main gameplay interactions and controls. */
public class GameController implements Screen {

  @FXML private Pane panSpaceship;
  @FXML private Group grpPanZoom;

  private Map<String, HighlightButton> mapButtons = new HashMap<>();
  @FXML private Group grpSuspectButtons;
  @FXML private Group grpPuzzleButtons;
  @FXML private Group grpOtherButtons;

  private ZoomAndPanHandler zoomAndPanHandler;
  private PuzzleLoader puzzleLoader;
  @FXML private SidepanelController sidePanelController;
  @FXML private SuspectController suspectController;
  @FXML private PopupController popupController;
  @FXML private NotificationpanelController notificationPanelController;

  private Timer countdownTimer;

  @FXML private HighlightButton gptScientist;
  @FXML private HighlightButton gptCaptain;
  @FXML private HighlightButton gptMechanic;
  private Suspect.Name clickedSuspect;

  @FXML private StateButton btnTts;

  /**
   * Initializes the controller with the necessary components and sets up the zoom and pan handler,
   * puzzle loader, and other UI elements.
   */
  @FXML
  private void initialize() {
    zoomAndPanHandler = new ZoomAndPanHandler(grpPanZoom, panSpaceship);
    puzzleLoader = new PuzzleLoader(grpPuzzleButtons);
    popupController.initialise(puzzleLoader);
    intialiseMapButtons();
    progressHighlightStateTo(HighlightState.REACTOR_INITAL);
  }

  /** Represents the method called when the controller is loaded. */
  @Override
  public void onLoad() {
    // Do nothing
  }

  /**
   * Progresses the highlighting state to a new state based on the current game state. If the new
   * state is the expected next state, it updates the highlighting accordingly.
   *
   * @param newState The new highlighting state to progress to.
   */
  public void progressHighlightStateTo(HighlightState newState) {
    if (newState == null) {
      return;
    }

    HighlightState nextState = HighlightState.values()[GameState.highlightState.ordinal() + 1];

    if (newState == nextState) {
      forceHighlightStateTo(newState);
    }
  }

  /**
   * Forces the highlighting state to the specified new state. Updates the game state's highlighting
   * based on the new state provided.
   *
   * @param newState The new state to force the highlighting to.
   */
  private void forceHighlightStateTo(HighlightState newState) {
    GameState.highlightState = newState;

    // Highlight map buttons based on new state
    switch (newState) {
      case PAN_ARROWS:
        break;
      case REACTOR_INITAL:
        highlightReactor();
        break;
      case SUSPECTS:
        highlightAllMapButtonsOfType(grpSuspectButtons);
        break;
      case PUZZLES:
        unhiglightAllMapButtons();
        break;
      case REACTOR_FINAL:
        highlightReactor();
        break;
    }
  }

  /** Unhighlights all map buttons by removing any highlighting effects applied to them. */
  private void unhiglightAllMapButtons() {
    for (HighlightButton button : mapButtons.values()) {
      button.unhighlight();
    }
  }

  /**
   * Retrieves the controller responsible for handling pop-up interactions.
   *
   * @return The controller managing the pop-up functionality.
   */
  public PopupController getPopupController() {
    return popupController;
  }

  /**
   * Highlights the reactor map button by applying a highlighting effect. Unhighlights all other map
   * buttons to ensure only the reactor is highlighted.
   */
  private void highlightReactor() {
    unhiglightAllMapButtons();
    mapButtons.get("btnReactor").highlight();
  }

  /**
   * Highlights all map buttons of a specific type by applying a highlighting effect.
   *
   * @param group The group of buttons to be highlighted.
   */
  private void highlightAllMapButtonsOfType(Group group) {
    unhiglightAllMapButtons();

    for (HighlightButton button : mapButtons.values()) {
      if (button.parentProperty().getValue() == group) {
        button.highlight();
      }
    }
  }

  /**
   * Initializes all map buttons by iterating through the groups and initializing map buttons in
   * each group.
   */
  private void intialiseMapButtons() {
    // Iterate through all the groups and initialise the map buttons in each group
    for (Node node : grpPanZoom.getChildren()) {
      Group mapButtonGroup;

      // If the node is not a group, skip it
      try {
        mapButtonGroup = (Group) node;
      } catch (ClassCastException e) {
        continue;
      }

      intialiseMapButtonsInGroup(mapButtonGroup);
    }
  }

  /**
   * Initializes map buttons in a specific group by iterating through all the nodes in the group.
   *
   * @param mapButtonGroup The group of map buttons to be initialized.
   */
  private void intialiseMapButtonsInGroup(Group mapButtonGroup) {
    // Iterate through all the nodes in the group and initialise the map buttons
    for (Node node : mapButtonGroup.getChildren()) {
      HighlightButton mapButton;

      // If the node is not a map button, skip it
      try {
        mapButton = (HighlightButton) node;
      } catch (ClassCastException e) {
        continue;
      }

      // Add the map button to the map
      addMapButton(mapButton);
    }
  }

  /** Initializes the countdown timer based on the time limit specified in the game state. */
  public void initialiseTimer() {
    int initialSeconds = GameState.timeLimit * 60 + 1;
    countdownTimer = new Timer(initialSeconds, sidePanelController.getTimerLabel());
    countdownTimer.start();
  }

  /** Resets the gameplay tutorial popup controller by loading the specified fxml file. */
  public void resetGpt() {
    popupController.load(PopupController.Name.SUSPECT, "/fxml/gamechildren/suspect.fxml");
  }

  /**
   * Event handler for mouse press events.
   *
   * @param event The mouse event.
   */
  @FXML
  private void onPress(MouseEvent event) {
    zoomAndPanHandler.onPress(event);
  }

  /**
   * Event handler for mouse drag events.
   *
   * @param event The mouse event.
   */
  @FXML
  private void onDrag(MouseEvent event) {
    zoomAndPanHandler.onDrag(event);
  }

  /**
   * Event handler for scroll events.
   *
   * @param event The scroll event.
   */
  @FXML
  private void onScroll(ScrollEvent event) {
    zoomAndPanHandler.onScroll(event);
  }

  /** Provides a random clue to the user through the side panel controller. */
  public void giveRandomClue() {
    sidePanelController.getRandomClue();
  }

  /** Updates the hint text based on the current game state. */
  public void updateHintText() {
    sidePanelController.setHintText(getHintsLeftText());
  }

  /**
   * Retrieves the appropriate hint text based on the current game state's difficulty.
   *
   * @return A string indicating the remaining hints or the hint policy based on the difficulty.
   */
  private String getHintsLeftText() {
    if (GameState.difficulty == "easy") {
      return "You Have Unlimited Hints";
    } else if (GameState.difficulty == "medium") {
      return "You Have " + GameState.getHintLimitRemaining() + " Hints Left";
    } else {
      return "No Hints Are Allowed";
    }
  }

  /**
   * Called when a puzzle button is clicked. Loads puzzle.
   *
   * @param event The mouse event.
   */
  @FXML
  private void onPuzzleButtonClicked(MouseEvent event) throws IOException {
    // Get the specific puzzle button that was clicked
    AnimatedButton clickedButton = (AnimatedButton) event.getSource();

    // If the button is a puzzle button
    if (getButtonToPuzzleMap().containsKey(clickedButton)) {
      // Load the specific puzzle
      PuzzleName puzzleName = getButtonToPuzzleMap().get(clickedButton);

      // Update the game state and show popup
      if (PuzzleLoader.reactorPuzzles.contains(puzzleName)) {
        GameState.reactorRoomGameState = GameState.puzzleOpenedMessage;
        popupController.maximise(PopupController.Name.PUZZLE_REACTOR);
      } else if (PuzzleLoader.laboratoryPuzzles.contains(puzzleName)) {
        GameState.labRoomGameState = GameState.puzzleOpenedMessage;
        popupController.maximise(PopupController.Name.PUZZLE_LABORATORY);
      } else if (PuzzleLoader.navigationPuzzles.contains(puzzleName)) {
        GameState.controlRoomGameState = GameState.puzzleOpenedMessage;
        popupController.maximise(PopupController.Name.PUZZLE_NAVIGATION);
      }
    }
  }

  /**
   * Handles the action when a suspect button is clicked. Sets the visibility of the corresponding
   * group and opens the corresponding popup controller.
   *
   * @param event The mouse event representing the click action on the suspect button.
   */
  @FXML
  private void onSuspectButtonClicked(MouseEvent event) {
    // Set the visibility of the corresponding group
    if (event.getSource() == gptScientist) {
      clickedSuspect = Suspect.Name.SCIENTIST;
    } else if (event.getSource() == gptCaptain) {
      clickedSuspect = Suspect.Name.CAPTAIN;
    } else if (event.getSource() == gptMechanic) {
      clickedSuspect = Suspect.Name.MECHANIC;
    }

    popupController.maximise(PopupController.Name.SUSPECT);
  }

  /**
   * Retrieves the name of the suspect that was clicked.
   *
   * @return The name of the suspect as an enum constant.
   */
  public Suspect.Name getClickedSuspectName() {
    return clickedSuspect;
  }

  /**
   * Retrieves the mapping between the animated buttons and puzzle names from the puzzle loader.
   *
   * @return A HashMap containing the mapping between the animated buttons and puzzle names.
   */
  private HashMap<AnimatedButton, PuzzleName> getButtonToPuzzleMap() {
    return puzzleLoader.getButtonToPuzzleMap();
  }

  /**
   * Opens the popup controller when the riddle is clicked.
   *
   * @throws IOException If an I/O error occurs during the process of displaying the popup
   *     controller.
   */
  @FXML
  private void onRiddleClicked() throws IOException {
    popupController.maximise(PopupController.Name.RIDDLE);
  }

  /**
   * Handles the action when the restart button is clicked. (Temporary development tool.)
   *
   * @throws IOException If an I/O error occurs during the restart process.
   */
  @FXML
  private void onRestartClicked() throws IOException {
    // TODO: TEMPORARY DEV TOOL
    App.restart();
  }

  /**
   * Handles the action when the end button is clicked. (Temporary development tool.)
   *
   * @throws IOException If an I/O error occurs during the process of showing the end screen.
   */
  @FXML
  private void onEndClicked() throws IOException {
    // TODO: TEMPORARY DEV TOOL
    EndController endController = (EndController) App.getScreen(Screen.Name.END).getController();
    endController.showEndOnLose();
  }

  /**
   * Handles the action when the test button is clicked. (Temporary development tool.)
   *
   * @throws IOException If an I/O error occurs during the testing process.
   */
  @FXML
  private void onTestClicked() throws IOException {
    // TODO: TEMPORARY DEV TOOL
    // Bind this to whatever action you want to run
    System.out.println("You are filled with determination!");
  }

  /**
   * Handles the action when the tts button is clicked.
   *
   * @param event The mouse event.
   */
  @FXML
  private void onTtsClicked(MouseEvent event) {
    GameState.ttsInterrupted = notificationPanelController.isNotificationInProgress();
    GameState.ttsEnabled = !GameState.ttsEnabled;
  }

  /**
   * Adds a map button to the map of buttons.
   *
   * @param mapButton The map button to be added.
   */
  public void addMapButton(HighlightButton mapButton) {
    mapButton.initialise();
    mapButtons.put(mapButton.getId(), mapButton);
  }

  /**
   * Retrieves the notification panel controller.
   *
   * @return The notification panel controller associated with this game controller.
   */
  public NotificationpanelController getNotificationpanelController() {
    return notificationPanelController;
  }

  /** Called when game is initialised to set up the tts button. */
  public void setupTtsButton() {
    String ttsEnabled = GameState.ttsEnabled ? "on" : "off";
    // Set state button
    btnTts.addState("off", "settings_buttons/off.png");
    btnTts.addState("on", "settings_buttons/on.png");
    btnTts.setState(ttsEnabled);
  }
}
