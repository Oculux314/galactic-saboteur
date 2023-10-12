package nz.ac.auckland.se206.gamechildren.suspects;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import nz.ac.auckland.se206.gpt.Assistant;
import nz.ac.auckland.se206.gpt.NarrationBox;

public class Suspect {

  public enum Name {
    SCIENTIST,
    MECHANIC,
    CAPTAIN;
  }

  private NarrationBox narrationBox;
  private Assistant assistant;
  private String title;
  private Image image;

  public Suspect(String title, Image image) {
    this.narrationBox = generateNarrationBox();
    assistant = new Assistant(narrationBox);
    this.image = image;
    assistant.welcome();
  }

  private NarrationBox generateNarrationBox() {
    TextArea textNarration = new TextArea();
    TextField textResponse = new TextField();

    return new NarrationBox(textNarration, textResponse, title);
  }

  public Image getImage() {
    return image;
  }

  public String getNarration() {
    return narrationBox.getText();
  }

  public Assistant getAssistant() {
    return assistant;
  }
}
