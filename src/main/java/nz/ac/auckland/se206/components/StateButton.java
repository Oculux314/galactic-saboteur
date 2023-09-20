package nz.ac.auckland.se206.components;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;

/**
 * This is an AnimatedButton that has multiple states. Each state has its own name, image (and
 * optional hover image), and onArrive and onLeave actions. The button cycles through states on
 * click.
 */
public class StateButton extends AnimatedButton {

  protected class State {
    String name;
    Image normalImage;
    Image hoverImage;
    Runnable onArrive;
    Runnable onLeave;
    int index;

    State(String name, Image image, Runnable onArrive, Runnable onLeave) {
      this.name = name;
      this.normalImage = image;
      hoverImage = getHoverImage(image);
      this.onArrive = onArrive;
      this.onLeave = onLeave;
      index = states.size();
    }
  }

  protected List<State> states;
  protected State currentState;
  private boolean isInitialized = false;

  public StateButton() {
    super();

    states = new ArrayList<State>();
  }

  public void addState(String name, String image) {
    addState(name, image, null, null);
  }

  public void addState(String name, Image image) {
    addState(name, image, null, null);
  }

  public void addState(String name, String image, Runnable onArrive, Runnable onLeave) {
    addState(name, new Image("/images/" + image), onArrive, onLeave);
  }

  public void addState(String name, Image image, Runnable onArrive, Runnable onLeave) {
    states.add(new State(name, image, onArrive, onLeave));

    if (!isInitialized) {
      isInitialized = true;
      init();
    }
  }

  protected void init() {
    currentState = states.get(0);
    setState(0);
    setOnMouseClicked((event) -> onClick());
  }

  private void setState(int index) {
    if (index < 0 || index >= states.size()) {
      throw new IllegalArgumentException("Invalid state index");
    }

    // Run onLeave action of current state
    if (currentState.onLeave != null) {
      currentState.onLeave.run();
    }

    // Update state
    currentState = states.get(index);
    updateImage();

    // Run onArrive action of new state
    if (currentState.onArrive != null) {
      currentState.onArrive.run();
    }
  }

  public void setState(String name) {
    for (int i = 0; i < states.size(); i++) {
      if (states.get(i).name.equals(name)) {
        setState(i);
        return;
      }
    }

    throw new IllegalArgumentException("Invalid state name");
  }

  public void nextState() {
    setState((currentState.index + 1) % states.size());
  }

  public void previousState() {
    setState((currentState.index - 1 + states.size()) % states.size());
  }

  public String getState() {
    return currentState.name;
  }

  protected void onClick() {
    nextState();
  }

  @Override
  protected void changeImageToHover() {
    setImage(currentState.hoverImage);
  }

  @Override
  protected void changeImageToNormal() {
    setImage(currentState.normalImage);
  }
}
