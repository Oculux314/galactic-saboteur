package nz.ac.auckland.se206.screens;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.Screen;
import nz.ac.auckland.se206.components.AnimatedButton;
import nz.ac.auckland.se206.components.HighlightButton;
import nz.ac.auckland.se206.components.StateButton;
import nz.ac.auckland.se206.gpt.Assistant;
import nz.ac.auckland.se206.gpt.NarrationBox;
import nz.ac.auckland.se206.misc.GameState;
import nz.ac.auckland.se206.misc.TaggedThread;
import nz.ac.auckland.se206.misc.GameState.HighlightState;
import nz.ac.auckland.se206.puzzles.Puzzle;
import nz.ac.auckland.se206.puzzles.Puzzle.PuzzleName;
import nz.ac.auckland.se206.puzzles.PuzzleLoader;
import nz.ac.auckland.se206.riddle.RiddleController;

/** Controller class for the game screens. */
public class GameController implements ScreenParent {

  private class TimerData {
    private int seconds;

    // Constructor of TimerDate class
    public TimerData(int initialSeconds) {
      this.seconds = initialSeconds;
    }

    public int getSeconds() {
      return seconds;
    }

    public void decrementSeconds() {
      seconds--;
    }
  }

  @FXML private Pane panSpaceship;
  @FXML private Group grpPanZoom;

  @FXML private Group grpSuspectButtons;
  @FXML private Group grpPuzzleButtons;
  @FXML private Group grpOtherButtons;
  @FXML private Label timer;
  @FXML private StackPane fullSidePanel;
  @FXML private SidepanelController fullSidePanelController;

  @FXML private Pane panPuzzle;
  @FXML private AnimatedButton btnExit;
  @FXML private Group grpPuzzleCommons;
  @FXML private AnimatedButton gptScientist;
  @FXML private AnimatedButton gptMechanic;
  @FXML private AnimatedButton gptCaptain;
  @FXML private Pane paneNarrationScientist;
  @FXML private Pane paneNarrationMechanic;
  @FXML private Pane paneNarrationCaptain;
  @FXML private TextArea labelNarrationScientist;
  @FXML private TextArea labelNarrationMechanic;
  @FXML private TextArea labelNarrationCaptain;
  @FXML private TextField textResponseScientist;
  @FXML private TextField textResponseMechanic;
  @FXML private TextField textResponseCaptain;
  @FXML private Group grpGptScientist;
  @FXML private Group grpGptMechanic;
  @FXML private Group grpGptCaptain;
  @FXML private StateButton hintsScientist;
  @FXML private StateButton hintsMechanic;
  @FXML private StateButton hintsCaptain;
  @FXML private AnimatedButton btnGptExitCaptain;
  @FXML private AnimatedButton btnGptExitMechanic;
  @FXML private AnimatedButton btnGptExitScientist;
  @FXML private Group grpGpt;
  @FXML private Group grpRiddle;
  @FXML private AnimatedButton btnRiddleExit;
  @FXML private AnimatedButton btnReactor;
  @FXML private RiddleController riddleController;

  @FXML private Pane panEnd;
  @FXML private ImageView imageEnd;
  @FXML private Label lblEnd;

  @FXML private Label labelHintsLeft;

  private PuzzleLoader puzzleLoader;
  private ZoomAndPanHandler zoomAndPanHandler;
  private Puzzle lastClickedPuzzle;
  private Set<Puzzle> solvedPuzzles = new HashSet<>();
  private Map<String, HighlightButton> mapButtons = new HashMap<>();

  private boolean captainWelcomeShown = false;
  private boolean scientistWelcomeShown = false;
  private boolean mechanicWelcomeShown = false;
  private TaggedThread countdownTimer;

  @FXML
  private void initialize() {
    puzzleLoader = new PuzzleLoader(panPuzzle, grpPuzzleCommons, grpPuzzleButtons);
    zoomAndPanHandler = new ZoomAndPanHandler(grpPanZoom, panSpaceship);
    // Set the narration boxes
    NarrationBox narrationBox1 =
        new NarrationBox(
            paneNarrationScientist,
            labelNarrationScientist,
            textResponseScientist,
            "Spacey's scientist");
    App.scientist = new Assistant(narrationBox1);

    NarrationBox narrationBox2 =
        new NarrationBox(
            paneNarrationMechanic,
            labelNarrationMechanic,
            textResponseMechanic,
            "Spacey's mechanic");
    App.mechanic = new Assistant(narrationBox2);

    NarrationBox narrationBox3 =
        new NarrationBox(
            paneNarrationCaptain, labelNarrationCaptain, textResponseCaptain, "Spacey's captain");
    App.captain = new Assistant(narrationBox3);

    // Set the visibility of the corresponding group
    grpGptScientist.setVisible(false);
    grpGptMechanic.setVisible(false);
    grpGptCaptain.setVisible(false);

    hintsScientist.addState("nohint", "btnhint.png");
    hintsScientist.addState("hint", "yeshint.png");

    hintsMechanic.addState("nohint", "btnhint.png");
    hintsMechanic.addState("hint", "yeshint.png");

    hintsCaptain.addState("nohint", "btnhint.png");
    hintsCaptain.addState("hint", "yeshint.png");

    grpRiddle.setVisible(false);

    updateHintsLeft();

    intialiseMapButtons();
    progressHighlightStateTo(HighlightState.REACTOR_INITAL);
  }

  private void progressHighlightStateTo(HighlightState newState) {
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

  public void startTimer() {
    // Create a timer that decrements every second
    countdownTimer =
        new TaggedThread(
            () -> {
              int initialMinutes = GameState.timeLimit;
              TimerData timerData = new TimerData(initialMinutes * 60 + 1);

              while (timerData.getSeconds() > 0) {
                if (GameState.isGameover) {
                  return; // Returning as interupting this thread doesn't seem to work
                }

                timerData.decrementSeconds();
                Platform.runLater(() -> updateTimerDisplay(timerData.getSeconds()));

                try {
                  Thread.sleep(1000);
                } catch (InterruptedException e) {
                  return; // Returning as interupting this thread doesn't seem to work
                }
              }

              Platform.runLater(() -> showTimeout());
            });

    countdownTimer.start();
  }

  private void showTimeout() {
    EndController endController = ((EndController) App.getScreen(Screen.Name.END).getController());
    endController.showEndOnTimeout();
  }

  public void updateTimerDisplay(int initialSeconds) {
    int minutes = initialSeconds / 60;
    int seconds = initialSeconds % 60;

    String formattedMinutes = String.format("%02d", minutes);
    String formattedSeconds = String.format("%02d", seconds);

    timer.setText(formattedMinutes + ":" + formattedSeconds);
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

  @FXML
  private void onExitClicked(MouseEvent event) {
    if (event.getSource() == btnExit) {

      minimisePuzzleWindow();
      if (lastClickedPuzzle.isSolved() && !solvedPuzzles.contains(lastClickedPuzzle)) {
        // If puzzle was solved, get the clue
        fullSidePanelController.getRandomClue();
        solvedPuzzles.add(lastClickedPuzzle);

        // If all puzzles are solved, highlight the reactor
        if (solvedPuzzles.size() == 3) {
          progressHighlightStateTo(HighlightState.REACTOR_FINAL);
        }
      }

    } else if (event.getSource() == btnGptExitScientist) {
      grpGptScientist.setVisible(false);
      progressHighlightStateTo(HighlightState.PUZZLES);
    } else if (event.getSource() == btnGptExitMechanic) {
      grpGptMechanic.setVisible(false);
      progressHighlightStateTo(HighlightState.PUZZLES);
    } else if (event.getSource() == btnGptExitCaptain) {
      grpGptCaptain.setVisible(false);
      progressHighlightStateTo(HighlightState.PUZZLES);
    } else {
      grpRiddle.setVisible(false);
      progressHighlightStateTo(HighlightState.SUSPECTS);
    }
  }

  private void minimisePuzzleWindow() {
    grpPuzzleCommons.setVisible(false);
  }

  private void restorePuzzleWindow() {
    grpPuzzleCommons.setVisible(true);
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
      puzzleLoader.setPuzzle(puzzleName);
      // Show the puzzle
      restorePuzzleWindow();

      // update the game state depending on what puzzle was opened
      if (PuzzleLoader.reactorPuzzles.contains(puzzleName)) {
        GameState.reactorRoomGameState = GameState.puzzleOpenedMessage;
      } else if (PuzzleLoader.laboratoryPuzzles.contains(puzzleName)) {
        GameState.labRoomGameState = GameState.puzzleOpenedMessage;
      } else if (PuzzleLoader.navigationPuzzles.contains(puzzleName)) {
        GameState.controlRoomGameState = GameState.puzzleOpenedMessage;
      }
    }

    lastClickedPuzzle = puzzleLoader.getCurrentPuzzle();
  }

  @FXML
  private void gptStart(MouseEvent event) {
    // Show the welcome message if it hasn't been shown yet
    if (event.getSource() == gptScientist && !scientistWelcomeShown) {
      App.scientist.welcome();
      scientistWelcomeShown = true;
    } else if (event.getSource() == gptCaptain && !captainWelcomeShown) {
      App.captain.welcome();
      captainWelcomeShown = true;
    } else if (event.getSource() == gptMechanic && !mechanicWelcomeShown) {
      App.mechanic.welcome();
      mechanicWelcomeShown = true;
    }

    // Set the visibility of the corresponding group
    if (event.getSource() == gptScientist) {
      grpGptScientist.setVisible(true);
    } else if (event.getSource() == gptCaptain) {
      grpGptCaptain.setVisible(true);
    } else if (event.getSource() == gptMechanic) {
      grpGptMechanic.setVisible(true);
    }
  }

  @FXML
  private void onUserMessage(ActionEvent event) {
    System.out.println(event);

    // Respond to the user's message
    if (event.getSource() == textResponseScientist) {
      App.scientist.respondToUser();
      hintsScientist.setState("nohint");
      hintsScientist.setDisable(GameState.isHintLimitReached());
    } else if (event.getSource() == textResponseCaptain) {
      App.captain.respondToUser();
      hintsCaptain.setState("nohint");
      hintsCaptain.setDisable(GameState.isHintLimitReached());
    } else if (event.getSource() == textResponseMechanic) {
      App.mechanic.respondToUser();
      hintsMechanic.setState("nohint");
      hintsMechanic.setDisable(GameState.isHintLimitReached());
    }
    updateHintsLeft();
  }

  @FXML
  private void hintWanted(MouseEvent event) {
    StateButton sourceStateButton = (StateButton) event.getSource();
    // ActionEvent events =

    if (sourceStateButton == hintsScientist) {
      if (sourceStateButton.getState() == "hint") {
        textResponseScientist.setText("I want a hint.");
        // onUserMessage(event);
      } else {
        textResponseScientist.setText("");
      }
    } else if (sourceStateButton == hintsCaptain) {
      textResponseCaptain.setText("I want a hint.");
    } else if (sourceStateButton == hintsMechanic) {
      textResponseMechanic.setText("I want a hint");
    }
  }

  public void updateHintsLeft() {
    if (GameState.difficulty == "easy") {
      labelHintsLeft.setText("You Have Unlimited Hints");
    } else if (GameState.difficulty == "medium") {
      labelHintsLeft.setText("You Have " + GameState.getHintLimitRemaining() + " Hints Left");
    } else {
      labelHintsLeft.setText("No Hints Are Allowed");
    }
  }

  private HashMap<AnimatedButton, PuzzleName> getButtonToPuzzleMap() {
    return puzzleLoader.getButtonToPuzzleMap();
  }

  @FXML
  private void riddleClicked() throws IOException {
    // Set the visibility of the corresponding group
    grpRiddle.setVisible(true);
    if (GameState.cluesFound == true) {
      riddleController.disableButton();
    } else {
      riddleController.enableButton();
    }
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

  public void addMapButton(HighlightButton mapButton) {
    mapButton.initialise();
    mapButtons.put(mapButton.getId(), mapButton);
  }
}
