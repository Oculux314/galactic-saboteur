package nz.ac.auckland.se206.gamechildren.suspects;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import nz.ac.auckland.se206.gpt.Assistant;
import nz.ac.auckland.se206.gpt.NarrationBox;

/**
 * The Suspect class represents a character in the game that the player can interact with. Each
 * Suspect has a title, an image, and a narration box for text input and output.
 */
public class Suspect {

  /** Enum representing the names of different suspects. */
  public enum Name {
    SCIENTIST,
    MECHANIC,
    CAPTAIN;
  }

  private NarrationBox narrationBox; // The narration box for text input and output
  private Assistant assistant; // The assistant for helping with interactions
  private String title; // The title of the suspect
  private Image image; // The image representing the suspect

  /**
   * Constructs a Suspect with the specified title, image, and SuspectController.
   *
   * @param title the title of the suspect
   * @param image the image representing the suspect
   * @param suspectController the controller for handling suspect-related actions
   */
  public Suspect(String title, Image image, SuspectController suspectController) {
    this.title = title;
    this.image = image;
    this.narrationBox = generateNarrationBox(suspectController);
    assistant = new Assistant(narrationBox, title);
  }

  private NarrationBox generateNarrationBox(SuspectController suspectController) {
    TextArea textNarration = new TextArea(); // The text area for displaying the narration
    TextField textResponse = new TextField(); // The text field for user responses

    return new NarrationBox(textNarration, textResponse, title, suspectController);
  }

  /**
   * Returns the image representing the suspect.
   *
   * @return the image representing the suspect
   */
  public Image getImage() {
    return image;
  }

  /**
   * Returns the text from the narration box.
   *
   * @return the text from the narration box
   */
  public String getNarration() {
    return narrationBox.getText();
  }

  /**
   * Returns the assistant associated with the suspect.
   *
   * @return the assistant associated with the suspect
   */
  public Assistant getAssistant() {
    return assistant;
  }
}
