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
import nz.ac.auckland.se206.gamechildren.NotificationpanelController;
import nz.ac.auckland.se206.gamechildren.PopupController;
import nz.ac.auckland.se206.gamechildren.SidepanelController;
import nz.ac.auckland.se206.gamechildren.Timer;
import nz.ac.auckland.se206.gamechildren.ZoomAndPanHandler;
import nz.ac.auckland.se206.gamechildren.puzzles.Puzzle.PuzzleName;
import nz.ac.auckland.se206.gamechildren.puzzles.PuzzleLoader;
import nz.ac.auckland.se206.gamechildren.suspects.Suspect;
import nz.ac.auckland.se206.misc.GameState;
import nz.ac.auckland.se206.misc.GameState.HighlightState;

/** Controller class for the game screens. */
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
  @FXML private PopupController popupController;
  @FXML private NotificationpanelController notificationPanelController;
  private Timer countdownTimer;

  @FXML private HighlightButton gptScientist;
  @FXML private HighlightButton gptCaptain;
  @FXML private HighlightButton gptMechanic;
  private Suspect.Name clickedSuspect;

  @FXML
  private void initialize() {
    zoomAndPanHandler = new ZoomAndPanHandler(grpPanZoom, panSpaceship);
    puzzleLoader = new PuzzleLoader(grpPuzzleButtons);
    popupController.initialise(puzzleLoader);
    intialiseMapButtons();
    progressHighlightStateTo(HighlightState.REACTOR_INITAL);
  }

  @Override
  public void onLoad() {
    // Do nothing
  }

  public void progressHighlightStateTo(HighlightState newState) {
    if (newState == null) {
      return;
    }

    HighlightState nextState = HighlightState.values()[GameState.highlightState.ordinal() + 1];

    if (newState == nextState) {
      forceHighlightStateTo(newState);
    }
  }

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

  private void unhiglightAllMapButtons() {
    for (HighlightButton button : mapButtons.values()) {
      button.unhighlight();
    }
  }

  private void highlightReactor() {
    unhiglightAllMapButtons();
    mapButtons.get("btnReactor").highlight();
  }

  private void highlightAllMapButtonsOfType(Group group) {
    unhiglightAllMapButtons();

    for (HighlightButton button : mapButtons.values()) {
      if (button.parentProperty().getValue() == group) {
        button.highlight();
      }
    }
  }

  private void intialiseMapButtons() {
    for (Node node : grpPanZoom.getChildren()) {
      Group mapButtonGroup;

      try {
        mapButtonGroup = (Group) node;
      } catch (ClassCastException e) {
        continue;
      }

      intialiseMapButtonsInGroup(mapButtonGroup);
    }
  }

  private void intialiseMapButtonsInGroup(Group mapButtonGroup) {
    for (Node node : mapButtonGroup.getChildren()) {
      HighlightButton mapButton;

      try {
        mapButton = (HighlightButton) node;
      } catch (ClassCastException e) {
        continue;
      }

      addMapButton(mapButton);
    }
  }

  public void initialiseTimer() {
    int initialSeconds = GameState.timeLimit * 60 + 1;
    countdownTimer = new Timer(initialSeconds, sidePanelController.getTimerLabel());
    countdownTimer.start();
  }

  @FXML
  private void onPress(MouseEvent event) {
    zoomAndPanHandler.onPress(event);
  }

  @FXML
  private void onDrag(MouseEvent event) {
    zoomAndPanHandler.onDrag(event);
  }

  @FXML
  private void onScroll(ScrollEvent event) {
    zoomAndPanHandler.onScroll(event);
  }

  public void giveRandomClue() {
    sidePanelController.getRandomClue();
  }

  public void updateHintText() {
    sidePanelController.setHintText(getHintsLeftText());
  }

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
        popupController.show(PopupController.Name.PUZZLE_REACTOR);
      } else if (PuzzleLoader.laboratoryPuzzles.contains(puzzleName)) {
        GameState.labRoomGameState = GameState.puzzleOpenedMessage;
        popupController.show(PopupController.Name.PUZZLE_LABORATORY);
      } else if (PuzzleLoader.navigationPuzzles.contains(puzzleName)) {
        GameState.controlRoomGameState = GameState.puzzleOpenedMessage;
        popupController.show(PopupController.Name.PUZZLE_NAVIGATION);
      }
    }
  }

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

    popupController.show(PopupController.Name.SUSPECT);
  }

  public Suspect.Name getClickedSuspectName() {
    return clickedSuspect;
  }

  private HashMap<AnimatedButton, PuzzleName> getButtonToPuzzleMap() {
    return puzzleLoader.getButtonToPuzzleMap();
  }

  @FXML
  private void onRiddleClicked() throws IOException {
    popupController.show(PopupController.Name.RIDDLE);
  }

  @FXML
  private void onRestartClicked() throws IOException {
    // TODO: TEMPORARY DEV TOOL
    App.restart();
  }

  @FXML
  private void onEndClicked() throws IOException {
    // TODO: TEMPORARY DEV TOOL
    EndController endController = (EndController) App.getScreen(Screen.Name.END).getController();
    endController.showEndOnLose();
  }

  @FXML
  private void onTestClicked() throws IOException {
    // TODO: TEMPORARY DEV TOOL
    // Bind this to whatever action you want to run
    System.out.println("You are filled with determination!");
  }

  @FXML
  private void onLogoEntered(MouseEvent event) {
    notificationPanelController.onMouseEntered(event);
  }

  @FXML
  private void onLogoExited(MouseEvent event) {
    notificationPanelController.onMouseExited(event);
  }

  public void addMapButton(HighlightButton mapButton) {
    mapButton.initialise();
    mapButtons.put(mapButton.getId(), mapButton);
  }

  public NotificationpanelController getNotificationpanelController() {
    return notificationPanelController;
  }
}
