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
import nz.ac.auckland.se206.gamechildren.suspects.Suspect.Name;
import nz.ac.auckland.se206.gpt.Assistant;
import nz.ac.auckland.se206.misc.GameState;
import nz.ac.auckland.se206.misc.RootPair;
import nz.ac.auckland.se206.screens.GameController;
import nz.ac.auckland.se206.screens.Screen;

public class SuspectController implements RootPair.Controller {

  @FXML TextArea textNarration;
  @FXML TextField textResponse;
  @FXML ImageView imgSuspect;
  @FXML StateButton btnHints;

  private Map<Name, Suspect> suspects = new HashMap<>();
  private Name currentSuspect;
  private int numberOfHintsAskedToSuspect;

  @FXML
  private void initialize() {
    loadAllSuspects();
    initialiseHintButton();
    numberOfHintsAskedToSuspect = 0;
  }

  @Override
  public void onLoad() {
    updateSuspect(getGameController().getClickedSuspectName());
  }

  private void loadAllSuspects() {
    suspects.put(
        Name.SCIENTIST, new Suspect("Spacey's scientist", loadSuspectImage("scientist.png"), this));
    suspects.put(
        Name.MECHANIC, new Suspect("Spacey's mechanic", loadSuspectImage("mechanic.png"), this));
    suspects.put(
        Name.CAPTAIN, new Suspect("Spacey's captain", loadSuspectImage("captain.png"), this));
  }

  private Image loadSuspectImage(String file) {
    return new Image("/images/suspects/" + file);
  }

  private void initialiseHintButton() {
    btnHints.addState("nohint", "btnhint.png");
    btnHints.addState("hint", "yeshint.png");
  }

  public void updateSuspect(Name name) {
    currentSuspect = name;
    updatePopup(getCurrentSuspect());
  }

  private void updatePopup(Suspect suspect) {
    if (suspect == null) {
      return;
    }

    imgSuspect.setImage(suspect.getImage());
    textNarration.setText(suspect.getNarration());
  }

  private Suspect getCurrentSuspect() {
    return suspects.get(currentSuspect);
  }

  private GameController getGameController() {
    return (GameController) App.getScreen(Screen.Name.GAME).getController();
  }

  @FXML
  private void onUserMessage(ActionEvent event) {
    System.out.println();
    System.out.println();
    System.out.println(event);

    // Respond to the user's message
    Assistant assistant = getCurrentSuspect().getAssistant();
    System.out.println(assistant.getJob());
    assistant.respondToUser(textResponse.getText());

    // reset the accessibility of the hint button
    btnHints.setState("nohint");
    btnHints.setDisable(GameState.isHintLimitReached());

    updateHintsLeft();
  }

  public void updateHintsLeft() {
    getGameController().updateHintText();
  }

  @FXML
  private void onHintButtonClick(MouseEvent event) {
    if (btnHints.getState() == "hint") {
      if (numberOfHintsAskedToSuspect % 2 == 0) {
        textResponse.setText("I want a hint.");
      } else {
        textResponse.setText("Give me another hint.");
      }
      onUserMessage(new ActionEvent());
      numberOfHintsAskedToSuspect++;
    } else {
      textResponse.setText("");
    }
  }

  public void updateNarration(String text) {
    textNarration.setText(text);
  }

  public void updateResponse(String text) {
    textResponse.setText(text);
  }

  public void disableUserResponse() {
    textResponse.setDisable(true);
    btnHints.setDisable(true);
  }

  public void enableUserResponse() {
    textResponse.setDisable(false);
    btnHints.setDisable(false);
  }
}
