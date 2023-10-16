package nz.ac.auckland.se206.components;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.misc.Utils;

/**
 * The StateButton class represents an AnimatedButton that has multiple states. Each state has its
 * own name, image (and optional hover image), and onArrive and onLeave actions. The button cycles
 * through states on click.
 */
public class StateButton extends AnimatedButton {

  /**
   * The State class represents a single state of the StateButton. It holds the state's name, normal
   * image, hover image, onArrive action, and onLeave action.
   */
  protected class State {
    private String name;
    private Image normalImage;
    private Image hoverImage;
    private Runnable onArrive;
    private Runnable onLeave;
    private int index;

    State(String name, Image image, Runnable onArrive, Runnable onLeave) {
      this.name = name;
      this.normalImage = image;
      hoverImage = Utils.getImageWithSuffix(image, "_hover");
      this.onArrive = onArrive;
      this.onLeave = onLeave;
      index = states.size();
    }

    protected Image getNormalImage() {
      return normalImage;
    }

    protected int getIndex() {
      return index;
    }
  }

  protected List<State> states;
  protected State currentState;
  protected boolean isInitialized = false;

  /** Constructs a new StateButton with an empty list of states. */
  public StateButton() {
    super();
    states = new ArrayList<State>();
  }

  /**
   * Adds a new state to the StateButton with the provided name and image.
   *
   * @param name The name of the state.
   * @param image The image associated with the state.
   */
  public void addState(String name, String image) {
    addState(name, image, null, null);
  }

  /**
   * Adds a new state to the StateButton with the provided name and image.
   *
   * @param name The name of the state.
   * @param image The image associated with the state.
   */
  public void addState(String name, Image image) {
    addState(name, image, null, null);
  }

  /**
   * Adds a new state to the StateButton with the provided name, image, onArrive, and onLeave
   * actions.
   *
   * @param name The name of the state.
   * @param image The image associated with the state.
   * @param onArrive The action to perform when the state is arrived at.
   * @param onLeave The action to perform when leaving the state.
   */
  public void addState(String name, String image, Runnable onArrive, Runnable onLeave) {
    addState(name, new Image("/images/" + image), onArrive, onLeave);
  }

  /**
   * Adds a new state to the StateButton with the provided name, image, onArrive, and onLeave
   * actions.
   *
   * @param name The name of the state.
   * @param image The image associated with the state.
   * @param onArrive The action to perform when the state is arrived at.
   * @param onLeave The action to perform when leaving the state.
   */
  public void addState(String name, Image image, Runnable onArrive, Runnable onLeave) {
    states.add(new State(name, image, onArrive, onLeave));

    if (!isInitialized) {
      isInitialized = true;
      init();
    }
  }

  /** Initializes the StateButton by setting its current state and adding a click event handler. */
  protected void init() {
    currentState = states.get(0);
    setState(0);
    addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> onClick());
  }

  /**
   * Sets the state of the StateButton based on the provided index.
   *
   * @param index The index of the state to set.
   */
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

  /**
   * Sets the state of the StateButton based on the provided name.
   *
   * @param name The name of the state to set.
   */
  public void setState(String name) {
    for (int i = 0; i < states.size(); i++) {
      if (states.get(i).name.equals(name)) {
        setState(i);
        return;
      }
    }

    throw new IllegalArgumentException("Invalid state name");
  }

  /** Sets the state of the StateButton to the next state in the list. */
  public void nextState() {
    setState((currentState.index + 1) % states.size());
  }

  /** Sets the state of the StateButton to the previous state in the list. */
  public void previousState() {
    setState((currentState.index - 1 + states.size()) % states.size());
  }

  /**
   * Gets the name of the current state of the StateButton.
   *
   * @return The name of the current state.
   */
  public String getState() {
    return currentState.name;
  }

  /**
   * Handles the click action for the StateButton, setting the state to the next state in the list.
   */
  protected void onClick() {
    nextState();
  }

  /** Changes the image of the StateButton to the hover image. */
  @Override
  protected void changeImageToHover() {
    setImage(currentState.hoverImage);
  }

  /** Changes the image of the StateButton to the normal image. */
  @Override
  protected void changeImageToNormal() {
    setImage(currentState.normalImage);
  }
}
