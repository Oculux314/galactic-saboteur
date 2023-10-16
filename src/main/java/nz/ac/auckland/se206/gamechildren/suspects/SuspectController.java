package nz.ac.auckland.se206.gamechildren.suspects;

import java.util.HashMap;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.components.StateButton;
import nz.ac.auckland.se206.gamechildren.PopupController;
import nz.ac.auckland.se206.gamechildren.suspects.Suspect.Name;
import nz.ac.auckland.se206.gpt.Assistant;
import nz.ac.auckland.se206.misc.GameState;
import nz.ac.auckland.se206.misc.RootPair;
import nz.ac.auckland.se206.screens.GameController;
import nz.ac.auckland.se206.screens.Screen;

/** The SuspectController class manages interactions with different suspects in the game. */
public class SuspectController implements RootPair.Controller {

  @FXML private TextArea textNarration;
  @FXML private TextField textResponse;
  @FXML private ImageView imgSuspect;
  @FXML private StateButton btnHints;
  @FXML private ImageView thinkingImage;

  private Map<Name, Suspect> suspects = new HashMap<>();
  private Name currentSuspect;
  private int numberOfHintsAskedToSuspect;

  private boolean captainWelcomeShown = false;
  private boolean scientistWelcomeShown = false;
  private boolean mechanicWelcomeShown = false;

  /** Initializes the SuspectController by loading all suspects and initializing the hint button. */
  @FXML
  private void initialize() {
    loadAllSuspects();
    initialiseHintButton();
    numberOfHintsAskedToSuspect = 0;
  }

  /**
   * Called when the controller is loaded. Updates the current suspect and displays a welcome
   * message if applicable.
   */
  @Override
  public void onLoad() {
    updateSuspect(getGameController().getClickedSuspectName());

    if (currentSuspect == Name.CAPTAIN && !captainWelcomeShown) {
      getCurrentSuspect().getAssistant().welcome();
      captainWelcomeShown = true;
    } else if (currentSuspect == Name.SCIENTIST && !scientistWelcomeShown) {
      getCurrentSuspect().getAssistant().welcome();
      scientistWelcomeShown = true;
    } else if (currentSuspect == Name.MECHANIC && !mechanicWelcomeShown) {
      getCurrentSuspect().getAssistant().welcome();
      mechanicWelcomeShown = true;
    }
  }

  /** Loads all suspects into the suspects map. */
  public void loadAllSuspects() {
    suspects.put(
        Name.SCIENTIST, new Suspect("Spacey's scientist", loadSuspectImage("scientist.png"), this));
    suspects.put(
        Name.MECHANIC, new Suspect("Spacey's mechanic", loadSuspectImage("mechanic.png"), this));
    suspects.put(
        Name.CAPTAIN, new Suspect("Spacey's captain", loadSuspectImage("captain.png"), this));
  }

  /**
   * Loads an image for the suspect from the specified file.
   *
   * @param file the file name of the image
   * @return the loaded Image object
   */
  private Image loadSuspectImage(String file) {
    return new Image("/images/suspects/" + file);
  }

  /** Initializes the hint button by adding different states to it. */
  private void initialiseHintButton() {
    btnHints.addState("nohint", "btnhint.png");
    btnHints.addState("hint", "yeshint.png");
  }

  /**
   * Updates the current suspect to the one with the provided name and updates the popup
   * accordingly.
   *
   * @param name the name of the suspect to update
   */
  public void updateSuspect(Name name) {
    currentSuspect = name;
    updatePopup(getCurrentSuspect());
  }

  /**
   * Updates the popup with information from the specified suspect.
   *
   * @param suspect the suspect to update the popup with
   */
  private void updatePopup(Suspect suspect) {
    if (suspect == null) {
      return;
    }

    imgSuspect.setImage(suspect.getImage());
    textNarration.setText(suspect.getNarration());
  }

  /**
   * Retrieves the current suspect.
   *
   * @return the current suspect
   */
  public Suspect getCurrentSuspect() {
    return suspects.get(currentSuspect);
  }

  /**
   * Retrieves the game controller associated with the current game screen.
   *
   * @return the GameController object associated with the game screen
   */
  private GameController getGameController() {
    return (GameController) App.getScreen(Screen.Name.GAME).getController();
  }

  /**
   * Called when the user sends a message. Responds to the user's message and updates the hint
   * button.
   *
   * @param event the ActionEvent triggered by the user's message
   */
  @FXML
  private void onUserMessage(ActionEvent event) {

    if (GameState.numberOfHintsAsked > 4 && GameState.difficulty == "medium") {
      GameState.difficulty = "hard";
      getGameController()
          .getPopupController()
          .load(PopupController.Name.SUSPECT, "/fxml/gamechildren/suspect.fxml");
    }

    System.out.println();

    // Respond to the user's message
    Assistant assistant = getCurrentSuspect().getAssistant();
    assistant.respondToUser(textResponse.getText());

    // reset the accessibility of the hint button
    btnHints.setState("nohint");
    btnHints.setDisable(GameState.isHintLimitReached());

    updateHintsLeft();
  }

  /** Updates the number of hints left and the accessibility of the hint button. */
  public void updateHintsLeft() {
    getGameController().updateHintText();
  }

  /**
   * Called when the hint button is clicked. Requests a hint from the assistant and updates the
   * response text accordingly.
   *
   * @param event the MouseEvent triggered by clicking the hint button
   */
  @FXML
  private void onHintButtonClick(MouseEvent event) {
    // If hint button is clicked, ask the assistant for a hint
    if (btnHints.getState() == "hint") {
      // If the user has asked for a hint before, change the phrasing of the text to get a better
      // response
      if (numberOfHintsAskedToSuspect % 2 == 0) {
        textResponse.setText("I want a hint.");
      } else {
        textResponse.setText("Give me another hint.");
      }
      onUserMessage(new ActionEvent());
      numberOfHintsAskedToSuspect++;
    } else {
      // If the user is turning the hint button off reset the response text
      textResponse.setText("");
    }
  }

  /**
   * Updates the narration text to the specified text.
   *
   * @param text the text to set as the narration
   */
  public void updateNarration(String text) {
    textNarration.setText(text);
  }

  /**
   * Updates the response text to the specified text.
   *
   * @param text the text to set as the response
   */
  public void updateResponse(String text) {
    textResponse.setText(text);
  }

  /** Disables user response by disabling the response text field and the hint button. */
  public void disableUserResponse() {
    textResponse.setDisable(true);
    btnHints.setDisable(true);
  }

  /** Enables user response by enabling the response text field and the hint button. */
  public void enableUserResponse() {
    textResponse.setDisable(false);
    btnHints.setDisable(false);
  }

  /**
   * Retrieves the thinking image associated with the suspect.
   *
   * @return the ImageView object representing the thinking image
   */
  public ImageView getThinkingImage() {
    return thinkingImage;
  }
}
