package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.components.AnimatedButton;
import nz.ac.auckland.se206.puzzles.Puzzle;
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
  @FXML private StackPane fullSidePanel;
  @FXML private SidepanelController fullSidePanelController;
  @FXML private Group grpRiddle;
  @FXML private AnimatedButton btnRiddleExit;
  @FXML private AnimatedButton btnReactor;

  private HashMap<String, puzzle> buttonToPuzzleMap;
  private ZoomAndPanHandler zoomAndPanHandler;
  private PuzzleLoader puzzleLoader;
  private Puzzle lastClickedPuzzle;
  private String lastClickedId;
  private Set<String> solvedPuzzles = new HashSet<>();

  @FXML
  private void initialize() {
    buttonToPuzzleMap = new HashMap<>();
    buttonToPuzzleMap.put("btnToolbox", puzzle.reactortoolbox);
    buttonToPuzzleMap.put("btnButtonpad", puzzle.reactorbuttonpad);
    buttonToPuzzleMap.put("btnApple", puzzle.reactorapple);

    puzzleLoader = new PuzzleLoader(panPuzzle, grpPuzzleCommons);
    zoomAndPanHandler = new ZoomAndPanHandler(grpPanZoom, panSpaceship);

    grpRiddle.setVisible(false);
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
  private void onExitClicked(MouseEvent event) throws IOException {

    if (event.getSource() == btnExit) {
      
      minimisePuzzleWindow();
      // If puzzle was solved, get the clue
      if (lastClickedPuzzle.isSolved() && !solvedPuzzles.contains(lastClickedId)) {
        fullSidePanelController.getClue();
        solvedPuzzles.add(lastClickedId);
      }
    } else if (event.getSource() == btnRiddleExit) {
      grpRiddle.setVisible(false);
    }
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
      restorePuzzleWindow();
    }

    lastClickedId = buttonId;
    lastClickedPuzzle = puzzleLoader.getCurrentPuzzle();
  }

  private void minimisePuzzleWindow() {
    grpPuzzleCommons.setVisible(false);
  }

  private void restorePuzzleWindow() {
    grpPuzzleCommons.setVisible(true);
  }

  @FXML
  private void riddleClicked() throws IOException {
    grpRiddle.setVisible(true);
  }
}
