package nz.ac.auckland.se206.gpt;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import nz.ac.auckland.se206.gamechildren.suspects.SuspectController;

public class NarrationBox {

  public class LoadEffectTask extends Task<Void> {

    private String[] ellipsisAnimation = new String[] {".", "..", "..."};

    @Override
    protected Void call() throws Exception {
      int i = 0;

      while (textResponse.isDisabled()) {
        int numberOfDots = i;
        Platform.runLater(
            () ->
                textResponse.setText(
                    waitingMessage + " is thinking" + ellipsisAnimation[numberOfDots]));

        i = (i + 1) % ellipsisAnimation.length;
        Thread.sleep(200);
      }

      Platform.runLater(() -> textResponse.setText(""));
      return null;
    }
  }

  private TextArea labelNarration;
  private TextField textResponse;
  private String waitingMessage;
  private SuspectController suspectController;

  public NarrationBox(
      TextArea labelNarration,
      TextField textResponse,
      String waitingMessage,
      SuspectController suspectController) {
    this.labelNarration = labelNarration;
    this.textResponse = textResponse;
    this.waitingMessage = waitingMessage;
    this.suspectController = suspectController;
  }

  public String getWaitingMessage() {
    return waitingMessage;
  }

  public String getText() {
    String text = labelNarration.getText();

    if (text == null) {
      return "";
    } else {
      return text;
    }
  }

  public void setText(String text) {
    labelNarration.setText(text);
    displayNarration();
  }

  public String getUserResponse() {
    return textResponse.getText();
  }

  public void setUserResponse(String text) {
    textResponse.setText(text);
    displayResponse();
  }

  public void clearUserResponse() {
    textResponse.clear();
    displayResponse();
  }

  private void displayNarration() {
    suspectController.updateNarration(labelNarration.getText());
  }

  private void displayResponse() {
    suspectController.updateResponse(textResponse.getText());
  }

  public void disableUserResponse() {
    suspectController.disableUserResponse();
  }

  public void enableUserResponse() {
    suspectController.enableUserResponse();
  }
}
