package nz.ac.auckland.se206.gpt;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

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

  public NarrationBox(TextArea labelNarration, TextField textResponse, String waitingMessage) {
    this.labelNarration = labelNarration;
    this.textResponse = textResponse;
    this.waitingMessage = waitingMessage;
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
  }

  public String getUserResponse() {
    return textResponse.getText();
  }

  public void setUserResponse(String text) {
    textResponse.setText(text);
  }

  public void clearUserResponse() {
    textResponse.clear();
  }

  public void disableUserResponse() {
    textResponse.setDisable(true);
  }

  public void enableUserResponse() {
    textResponse.setDisable(false);
  }
}
