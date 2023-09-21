package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Polyline;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.components.AnimatedButton;
import nz.ac.auckland.se206.puzzles.Puzzle;
import nz.ac.auckland.se206.puzzles.Puzzle.PuzzleName;
import nz.ac.auckland.se206.puzzles.PuzzleLoader;

/** Controller class for the game screens. */
public class GameController implements Controller {

  public class RoomGroup {
    public Group reactorRoom;
    public Group laboratoryRoom;
    public Group navigationRoom;

    public RoomGroup(Group reactorRoom, Group laboratoryRoom, Group navigationRoom) {
      this.reactorRoom = reactorRoom;
      this.laboratoryRoom = laboratoryRoom;
      this.navigationRoom = navigationRoom;
    }
  }

  @FXML private Pane panSpaceship;

  @FXML private Group grpPanZoom;
  @FXML private Group grpMapButtons;

  @FXML private Polyline btnPanelHide;
  @FXML private Group panelContainer;
  @FXML private StackPane fullSidePanel;
  @FXML private SidepanelController fullSidePanelController;

  @FXML private Pane panPuzzle;
  @FXML private AnimatedButton btnExit;
  @FXML private Group grpPuzzleCommons;

  private PuzzleLoader puzzleLoader;
  private ZoomAndPanHandler zoomAndPanHandler;
  private Puzzle lastClickedPuzzle;
  private Set<Puzzle> solvedPuzzles = new HashSet<>();

  @FXML
  private void initialize() {
    puzzleLoader = new PuzzleLoader(panPuzzle, grpPuzzleCommons, grpMapButtons);
    zoomAndPanHandler = new ZoomAndPanHandler(grpPanZoom, panSpaceship);
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

    // If puzzle was solved, get the clue
    if (lastClickedPuzzle.isSolved() && !solvedPuzzles.contains(lastClickedPuzzle)) {
      fullSidePanelController.getClue();
      solvedPuzzles.add(lastClickedPuzzle);
    }
  }

  @FXML
  private void onPuzzleButtonClicked(MouseEvent event) throws IOException {
    // Get the specific puzzle button that was clicked
    AnimatedButton clickedButton = (AnimatedButton) event.getSource();

    if (getButtonToPuzzleMap().containsKey(clickedButton)) {
      // Load the specific puzzle
      PuzzleName puzzleName = getButtonToPuzzleMap().get(clickedButton);
      puzzleLoader.setPuzzle(puzzleName);
      restorePuzzleWindow();
    }

    lastClickedPuzzle = puzzleLoader.getCurrentPuzzle();
  }

  private void minimisePuzzleWindow() {
    grpPuzzleCommons.setVisible(false);
  }

  private void restorePuzzleWindow() {
    grpPuzzleCommons.setVisible(true);
  }

  private HashMap<AnimatedButton, PuzzleName> getButtonToPuzzleMap() {
    return puzzleLoader.getButtonToPuzzleMap();
  }
}
