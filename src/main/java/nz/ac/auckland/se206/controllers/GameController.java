package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.HashMap;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.components.AnimatedButton;
import nz.ac.auckland.se206.components.StateButton;
import nz.ac.auckland.se206.gpt.Assistant;
import nz.ac.auckland.se206.gpt.NarrationBox;
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
  @FXML private AnimatedButton btnToolbox;
  @FXML private Pane panPuzzle;
  @FXML private AnimatedButton btnExit;
  @FXML private Group grpPuzzleCommons;
  @FXML private AnimatedButton gptScientist;
  @FXML private AnimatedButton gptMechanic;
  @FXML private AnimatedButton gptCaptain;
  @FXML private Pane paneNarration;
  @FXML private TextArea labelNarration;
  @FXML private TextField textResponse;
  @FXML private Group grpGpt;
  @FXML private StateButton hints;

  private ZoomAndPanHandler zoomAndPanHandler;
  private PuzzleLoader puzzleLoader;
  private HashMap<String, puzzle> buttonToPuzzleMap;
  private boolean hintWanted = false;

  @FXML
  private void initialize() {
    buttonToPuzzleMap = new HashMap<>();
    buttonToPuzzleMap.put("btnToolbox", puzzle.reactortoolbox);

    puzzleLoader = new PuzzleLoader(panPuzzle);
    zoomAndPanHandler = new ZoomAndPanHandler(grpPanZoom, panSpaceship);

    NarrationBox narrationBox = new NarrationBox(paneNarration, labelNarration, textResponse);
    App.scientist = new Assistant(narrationBox);
    grpGpt.setVisible(false);
    hints.addState("nohint", "btnhint.png");
    hints.addState("hint", "yeshint.png");
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
  private void restartClicked() throws IOException {
    App.restart();
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
    minimisePuzzleWindow();
  }

  private void minimisePuzzleWindow() {
    grpPuzzleCommons.setVisible(false);
  }

  private void restorePuzzleWindow() {
    grpPuzzleCommons.setVisible(true);
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

      restorePuzzleWindow();
    }
  }

  @FXML
  private void gptStart(MouseEvent event) {

    event.getSource();

    grpGpt.setVisible(true);
    App.scientist.welcome();
    App.scientist.respondToUser();
  }

  @FXML
  private void onUserMessage() {
    App.scientist.respondToUser();
  }

}
