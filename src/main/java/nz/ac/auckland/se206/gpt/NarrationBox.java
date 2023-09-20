package nz.ac.auckland.se206.gpt;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class NarrationBox {

  public class LoadEffectTask extends Task<Void> {

    private String[] ellipsisAnimation = new String[] {".", "..", "..."};

    @Override
    protected Void call() throws Exception {
      int i = 0;

      while (textResponse.isDisabled()) {
        int numberOfDots = i;
        Platform.runLater(() -> textResponse.setText("Loading" + ellipsisAnimation[numberOfDots]));

        i = (i + 1) % ellipsisAnimation.length;
        Thread.sleep(200);
      }

      Platform.runLater(() -> textResponse.setText(""));
      return null;
    }
  }

  private Pane paneNarration;
  private TextArea labelNarration;
  private TextField textResponse;

  public NarrationBox(Pane paneNarration, TextArea labelNarration, TextField textResponse) {
    this.paneNarration = paneNarration;
    this.labelNarration = labelNarration;
    this.textResponse = textResponse;
  }

  public void hidePane() {
    paneNarration.setVisible(false);
  }

  public void showPane() {
    paneNarration.setVisible(true);
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
