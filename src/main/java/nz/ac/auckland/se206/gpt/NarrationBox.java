package nz.ac.auckland.se206.gpt;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.gamechildren.suspects.SuspectController;

/**
 * The NarrationBox class provides functionalities for managing the narration and user responses in
 * the game interface.
 */
public class NarrationBox {

  /**
   * The LoadEffectTask class implements a loading animation while waiting for the response field to
   * be enabled.
   */
  public class LoadEffectTask extends Task<Void> {

    private String[] ellipsisAnimation = new String[] {".", "..", "..."};

    /**
     * Executes the loading animation while the response field is disabled.
     *
     * @return Returns null upon completion of the loading animation.
     * @throws Exception if an error occurs during the execution of the loading animation.
     */
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

  /**
   * Constructs a new NarrationBox with the specified parameters.
   *
   * @param labelNarration The text area for displaying narration.
   * @param textResponse The text field for user responses.
   * @param waitingMessage The message to display while waiting for a response.
   * @param suspectController The controller for managing the suspect.
   */
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

  /**
   * Retrieves the waiting message of the specific suspect.
   *
   * @return The waiting message currently stored in the NarrationBox.
   */
  public String getWaitingMessage() {
    return waitingMessage;
  }

  /**
   * Retrieves the text from the narration area.
   *
   * @return The text currently displayed in the narration area.
   */
  public String getText() {
    String text = labelNarration.getText();

    if (text == null) {
      return "";
    } else {
      return text;
    }
  }

  /**
   * Sets the text in the narration area.
   *
   * @param text The text to be set in the narration area.
   */
  public void setText(String text) {
    labelNarration.setText(text);
    displayNarration();
  }

  /**
   * Retrieves the user's response from the text field.
   *
   * @return The text currently entered by the user in the response field.
   */
  public String getUserResponse() {
    return textResponse.getText();
  }

  /**
   * Sets the user's response in the text field.
   *
   * @param text The text to be set as the user's response.
   */
  public void setUserResponse(String text) {
    textResponse.setText(text);
    displayResponse();
  }

  /** Clears the user's response from the text field. */
  public void clearUserResponse() {
    textResponse.clear();
    displayResponse();
  }

  /** Displays the narration in the suspect controller. */
  private void displayNarration() {
    suspectController.updateNarration(labelNarration.getText());
  }

  /** Displays the user's response in the suspect controller. */
  private void displayResponse() {
    suspectController.updateResponse(textResponse.getText());
  }

  /** Disables the user's ability to respond in the text field. */
  public void disableUserResponse() {
    textResponse.setDisable(true);
    suspectController.disableUserResponse();
  }

  /** Enables the user's ability to respond in the text field. */
  public void enableUserResponse() {
    textResponse.setDisable(false);
    suspectController.enableUserResponse();
  }

  /** Displays question marks in the suspect controller. */
  public void showQuestionmarks() {
    suspectController.getThinkingImage().setVisible(true);
  }

  /**
   * Retrieves the image view representing the thinking image from the suspect controller.
   *
   * @return The image view representing the thinking image.
   */
  public ImageView getThinkingImage() {
    return suspectController.getThinkingImage();
  }

  /** Hides the question marks from the suspect controller. */
  public void hideQuestionmarks() {
    suspectController.getThinkingImage().setVisible(false);
  }
}
