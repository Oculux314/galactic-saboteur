package nz.ac.auckland.se206.components;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;

public class StateButton extends AnimatedButton {

  private class State {
    private String name;
    private Image image;
    private Runnable onArrive;
    private Runnable onLeave;
    private int index;

    private State(String name, Image image, Runnable onArrive, Runnable onLeave) {
      this.name = name;
      this.image = image;
      this.onArrive = onArrive;
      this.onLeave = onLeave;
      index = states.size();
    }
  }

  private List<State> states;
  private State currentState;

  public StateButton() {
    super();

    states = new ArrayList<State>();
  }

  public void addState(String name, String image, Runnable onArrive, Runnable onLeave) {
    states.add(new State(name, new Image("/images/" + image), onArrive, onLeave));

    if (states.size() == 1) {
      initialise();
    }
  }

  private void initialise() {
    currentState = states.get(0);
    setState(0);
    setOnMouseClicked((event) -> onClick());
  }

  public void addState(String name, String image) {
    addState(name, image, null, null);
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
    setImage(currentState.image);

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

  private void onClick() {
    nextState();
  }
}
