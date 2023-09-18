package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.HashMap;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.Screen;
import nz.ac.auckland.se206.components.AnimatedButton;
import nz.ac.auckland.se206.puzzles.Puzzle.puzzle;
import nz.ac.auckland.se206.puzzles.PuzzleLoader;

/** Controller class for the game screens. */
public class GameController implements Controller {

  @FXML private Pane panSpaceship;
  @FXML private Group grpPanZoom;
  @FXML private Rectangle recTest;
  @FXML private Button btnSettings;
  @FXML private Polyline btnPanelHide;
  @FXML private Group panelContainer;
  @FXML private Label timer;
  @FXML private AnimatedButton btnToolbox;
  @FXML private Pane panPuzzle;
  @FXML private AnimatedButton btnExit;
  @FXML private Group grpPuzzleCommons;

  private ZoomAndPanHandler zoomAndPanHandler;
  private PuzzleLoader puzzleLoader;
  private HashMap<String, puzzle> buttonToPuzzleMap;

  private Timeline countdownTimer;

  public void initialize() {
    buttonToPuzzleMap = new HashMap<>();
    buttonToPuzzleMap.put("btnToolbox", puzzle.reactortoolbox);

    panPuzzle.setVisible(false);
    grpPuzzleCommons.setVisible(false);

    puzzleLoader = new PuzzleLoader(panPuzzle);
    zoomAndPanHandler = new ZoomAndPanHandler(grpPanZoom, panSpaceship);

    startTimer();
  }

  public class TimerData {
    private int initialSeconds;

    public TimerData(int initialSeconds) {
      this.initialSeconds = initialSeconds;
    }

    public int getInitialSeconds() {
      return initialSeconds;
    }

    public void decrement() {
      initialSeconds--;
    }
  }

  public void startTimer() {
    String timeState =
        ((SettingsController) App.getScreen(Screen.Name.SETTINGS).getController()).getTimeState();
    int initialMinutes = extractMinutes(timeState);

    TimerData timerData = new TimerData(initialMinutes * 60 + 1);

    countdownTimer =
        new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                event -> {
                  timerData.decrement();
                  if (timerData.getInitialSeconds() <= 0) {
                    countdownTimer.stop();
                  }
                  updateTimerDisplay(timerData.getInitialSeconds());
                }));

    countdownTimer.setCycleCount(Timeline.INDEFINITE);
    countdownTimer.play();
  }

  public static int extractMinutes(String input) {
    if (input.contains("min")) {
      input = input.replace("min", "").trim();
    }

    // parse the remaining string to an int
    try {
      return Integer.parseInt(input);
    } catch (NumberFormatException e) {
      return -1;
    }
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
  private void settingsClicked() {
    System.out.println("Settings button clicked");
  }

  @FXML
  private void recClicked() {
    System.out.println("Rectangle clicked");
  }

  @FXML
  private void btnPanelHidePressed() {
    if (panelContainer.getLayoutX() == 0) {
      panelContainer.setLayoutX(-180);
      btnPanelHide.setRotate(180);
      btnPanelHide.setLayoutX(100);
    } else {
      panelContainer.setLayoutX(0);
      btnPanelHide.setRotate(0);
      btnPanelHide.setLayoutX(267);
    }
  }

  @FXML
  private void onExitClicked() {
    panPuzzle.setVisible(false);
    grpPuzzleCommons.setVisible(false);
  }

  @FXML
  private void onPuzzleButtonClicked(MouseEvent event) throws IOException {

    // Get the specific puzzle button that was clicked
    AnimatedButton clickedButton = (AnimatedButton) event.getSource();
    String buttonId = clickedButton.getId();

    if (buttonToPuzzleMap.containsKey(buttonId)) {
      // Load the specific puzzle
      puzzle puzzleName = buttonToPuzzleMap.get(buttonId);
      puzzleLoader.loadPuzzle("/fxml/" + puzzleName + ".fxml");

      grpPuzzleCommons.setVisible(true);
      panPuzzle.setVisible(true);
    }
  }
}
