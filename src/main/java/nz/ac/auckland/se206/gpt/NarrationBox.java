package nz.ac.auckland.se206.gpt;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.gamechildren.suspects.SuspectController;

public class NarrationBox {

  public class LoadEffectTask extends Task<Void> {

    private String[] ellipsisAnimation = new String[] {".", "..", "..."};

    @Override
    protected Void call() throws Exception {
      int i = 0;

      // While the response field is disabled, animate the loading effect
      while (textResponse.isDisabled()) {
        int numberOfDots = i;
        Platform.runLater(
            () -> {
              setUserResponse(
                  getWaitingMessage() + " is thinking" + ellipsisAnimation[numberOfDots]);
            });

        // Cycle through the ellipsis animation
        i = (i + 1) % ellipsisAnimation.length;
        // Wait 200ms before updating the text
        Thread.sleep(200);
      }

      // Once the response field is enabled, clear the loading effect
      Platform.runLater(() -> setUserResponse(""));
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
    textResponse.setDisable(true);
    suspectController.disableUserResponse();
  }

  public void enableUserResponse() {
    textResponse.setDisable(false);
    suspectController.enableUserResponse();
  }

  public void showQuestionmarks() {
    suspectController.getThinkingImage().setVisible(true);
  }

  public ImageView getThinkingImage() {
    return suspectController.getThinkingImage();
  }

  public void hideQuestionmarks() {
    suspectController.getThinkingImage().setVisible(false);
  }
}
