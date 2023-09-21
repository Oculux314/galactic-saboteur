package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.HashMap;
import javafx.event.ActionEvent;
import java.util.HashSet;
import java.util.Set;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.components.AnimatedButton;
import nz.ac.auckland.se206.components.StateButton;
import nz.ac.auckland.se206.gpt.Assistant;
import nz.ac.auckland.se206.gpt.NarrationBox;
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
  @FXML private StackPane fullSidePanel;
  @FXML private SidepanelController fullSidePanelController;

  private HashMap<String, puzzle> buttonToPuzzleMap;
  private ZoomAndPanHandler zoomAndPanHandler;
  private PuzzleLoader puzzleLoader;
  private Puzzle lastClickedPuzzle;
  private String lastClickedId;
  private Set<String> solvedPuzzles = new HashSet<>();

  private boolean captainWelcomeShown = false;
  private boolean scientistWelcomeShown = false;
  private boolean mechanicWelcomeShown = false;

  @FXML
  private void initialize() {
    buttonToPuzzleMap = new HashMap<>();
    buttonToPuzzleMap.put("btnToolbox", puzzle.reactortoolbox);
    buttonToPuzzleMap.put("btnButtonpad", puzzle.reactorbuttonpad);
    buttonToPuzzleMap.put("btnApple", puzzle.reactorapple);

    puzzleLoader = new PuzzleLoader(panPuzzle, grpPuzzleCommons);
    zoomAndPanHandler = new ZoomAndPanHandler(grpPanZoom, panSpaceship);

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

    grpGptScientist.setVisible(false);
    grpGptMechanic.setVisible(false);
    grpGptCaptain.setVisible(false);

    hintsScientist.addState("nohint", "btnhint.png");
    hintsScientist.addState("hint", "yeshint.png");

    hintsMechanic.addState("nohint", "btnhint.png");
    hintsMechanic.addState("hint", "yeshint.png");

    hintsCaptain.addState("nohint", "btnhint.png");
    hintsCaptain.addState("hint", "yeshint.png");
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
  private void onExitClicked(MouseEvent event) {

    if (event.getSource() == btnExit) {
      minimisePuzzleWindow();
    } else if (event.getSource() == btnGptExitScientist) {
      grpGptScientist.setVisible(false);
    } else if (event.getSource() == btnGptExitMechanic) {
      grpGptMechanic.setVisible(false);
    } else if (event.getSource() == btnGptExitCaptain) {
      grpGptCaptain.setVisible(false);
    }
     // If puzzle was solved, get the clue
    if (lastClickedPuzzle.isSolved() && !solvedPuzzles.contains(lastClickedId)) {
      fullSidePanelController.getClue();
      solvedPuzzles.add(lastClickedId);
    }
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
      restorePuzzleWindow();
    }
    lastClickedId = buttonId;
    lastClickedPuzzle = puzzleLoader.getCurrentPuzzle();
  }

  @FXML
  private void gptStart(MouseEvent event) {
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
    System.out.println(event.getSource());
    if (event.getSource() == textResponseScientist) {
      App.scientist.respondToUser();
    } else if (event.getSource() == textResponseCaptain) {
      App.captain.respondToUser();
    } else if (event.getSource() == textResponseMechanic) {
      App.mechanic.respondToUser();
    }
  }
}
