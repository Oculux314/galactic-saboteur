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

  public SuspectController() {
    loadAllSuspects();
    initialiseHintButton();
  }

  @Override
  public void onLoad() {
    updateSuspect(getGameController().getClickedSuspectName());
  }

  private void loadAllSuspects() {
    suspects.put(
        Name.SCIENTIST, new Suspect("Spacey's scientist", loadSuspectImage("scientist.png")));
    suspects.put(Name.MECHANIC, new Suspect("Spacey's mechanic", loadSuspectImage("mechanic.png")));
    suspects.put(Name.CAPTAIN, new Suspect("Spacey's captain", loadSuspectImage("captain.png")));
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
    System.out.println(event);

    // Respond to the user's message
    Assistant assistant = getCurrentSuspect().getAssistant();
    assistant.respondToUser(textResponse.getText());

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
      textResponse.setText("I want a hint.");
    } else {
      textResponse.setText("");
    }
  }
}
