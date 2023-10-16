package nz.ac.auckland.se206;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import nz.ac.auckland.se206.misc.GameState;
import nz.ac.auckland.se206.misc.RootPair;
import nz.ac.auckland.se206.misc.TaggedThread;
import nz.ac.auckland.se206.misc.TextToSpeech;
import nz.ac.auckland.se206.misc.TextToSpeech.TextToSpeechException;
import nz.ac.auckland.se206.screens.MainController;
import nz.ac.auckland.se206.screens.Screen;

/** The entry point of the JavaFX application, representing the top-level application. */
public class App extends Application {

  private static Stage stage;

  /**
   * A map of all screens in the application (name -> screen). A value of null indicates a screen
   * has begun loading but is not yet finished loading.
   */
  private static Map<Screen.Name, RootPair> screens = new HashMap<>();

  private static Set<TaggedThread> threads = new HashSet<>();
  private static TextToSpeech tts = new TextToSpeech();

  /**
   * The entry point of the application.
   *
   * @param args Command line arguments.
   */
  public static void main(final String[] args) {
    launch();
  }

  private static Pane getPanMain() {
    return ((MainController) getScreen(Screen.Name.MAIN).getController()).getMainPane();
  }

  /**
   * Returns the screen with the given name.
   *
   * @param screenName The name of the screen to get.
   */
  public static RootPair getScreen(final Screen.Name screenName) {
    // Should not run under normal execution
    if (!screens.containsKey(screenName)) {
      throw new IllegalArgumentException("Screen " + screenName + " does not exist.");
    }

    // Can run under normal execution
    while (screens.get(screenName) == null) { // Null indicates screen is loading in the background
      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {
        throw new RuntimeException("Thread interrupted while waiting for screen to load.");
      }
    }

    return screens.get(screenName);
  }

  /**
   * Sets the screen with the given name as the child of the main pane. Creates the screen if it
   * does not exist. Assumes there is only one screen currently active.
   *
   * @param screenName The name of the screen to set.
   */
  public static void setScreen(final Screen.Name screenName) {
    ObservableList<Node> activeScreens = getPanMain().getChildren();
    Parent newScreen = getScreen(screenName).getFxml();
    GameState.currentScreen = screenName;

    if (activeScreens.size() == 0) {
      makeScreen(Screen.Name.DEFAULT);
      activeScreens.add(getScreen(Screen.Name.DEFAULT).getFxml()); // Black

      setScreen(screenName);
      return;
    }

    Parent oldScreen = (Parent) activeScreens.get(0);

    if (oldScreen == newScreen) {
      return;
    }

    Timeline fadeTransition =
        new Timeline(
            new KeyFrame(Duration.millis(200), new KeyValue(newScreen.opacityProperty(), 1)));

    activeScreens.add(newScreen);
    activeScreens.remove(0);
    newScreen.setOpacity(0);
    fadeTransition.play();

    newScreen.requestFocus();
  }

  /**
   * Creates a screen with the given name, and stores it in the screens map.
   *
   * @param screenName The name of the screen to create.
   */
  private static void makeScreen(final Screen.Name screenName) {
    screens.put(screenName, null); // Set to null to indicate screen is being loaded
    TaggedThread screenLoader = new TaggedThread(() -> makeScreenWithoutThread(screenName));
    screenLoader.start();
  }

  private static void makeScreenWithoutThread(final Screen.Name screenName) {
    if (screenName == Screen.Name.DEFAULT) {
      screens.put(screenName, new RootPair());
      return;
    }

    // Normal screens loaded via fxml
    String fxml = screenName.toString().toLowerCase();
    screens.put(screenName, new RootPair("/fxml/screens/" + fxml + ".fxml"));
  }

  /**
   * Returns the primary stage of the application.
   *
   * @return The primary stage of the application.
   */
  public static Stage getStage() {
    return stage;
  }

  /**
   * Returns the scene of the primary stage of the application.
   *
   * @return The scene of the primary stage of the application.
   */
  public static Scene getScene() {
    return stage.getScene();
  }

  /**
   * Restarts the game by stopping all active threads, resetting the game state, and resetting
   * screens.
   */
  public static void restart() {
    killAllThreads();
    GameState.reset();
    resetScreens();
  }

  /** Kills all active threads and resets the game over state. */
  private static void killAllThreads() {
    GameState.isGameover = true;

    for (TaggedThread thread : threads) {
      thread.interrupt(); // Does nothing if thread is already dead
    }

    threads.clear();
  }

  /**
   * Resets all screens except for the main screen, setting the current screen to the title screen.
   */
  private static void resetScreens() {
    for (Screen.Name screenName : Screen.Name.values()) {
      if (screenName == Screen.Name.MAIN) { // Main screen is persistent
        continue;
      }

      makeScreen(screenName);
    }

    setScreen(Screen.Name.TITLE);
  }

  /**
   * Ansyncronously speaks the given text if tts is enabled in GameState. Returns the same text
   * afterwards so this function can wrap existing strings.
   *
   * @param text The text to speak.
   * @return The same text that was passed in.
   */
  public static String speak(String text) {
    if (text == null) {
      throw new IllegalArgumentException("Text cannot be null.");
    }

    if (!GameState.ttsEnabled) {
      return text;
    }

    TaggedThread ttsThread =
        new TaggedThread(
            () -> {
              try {
                tts.speak(text);
              } catch (TextToSpeechException e) {
                return;
              }
            });
    ttsThread.start();
    return text; // Return original text so this function can wrap existing strings
  }

  public static void addThread(TaggedThread thread) {
    threads.add(thread);
  }

  @Override
  public void stop() {
    GameState.isRunning = false;
    killAllThreads();
    tts.terminate();
  }

  /**
   * This method is invoked when the application starts. It performs all neccessary setup,
   * including:
   *
   * <ul>
   *   <li>Initialise stage and scene
   *   <li>Set up screen graph
   *   <li>Link style sheet and listeners
   *   <li>Set stage properties
   *   <li>Show stage
   * </ul>
   *
   * @param newStage The primary stage of the application.
   */
  @Override
  public void start(final Stage newStage) {
    App.stage = newStage;

    // Set up screen graph
    makeScreenWithoutThread(Screen.Name.MAIN);
    Parent screen = getScreen(Screen.Name.MAIN).getFxml();

    // Link stage/scene/screen graph
    Scene scene = new Scene(screen, 800, 600);
    stage.setScene(scene);

    // Misc
    scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
    ((MainController) screens.get(Screen.Name.MAIN).getController()).addSceneListeners();

    // Properties
    scene.setFill(Color.web("#131d23"));
    stage.setTitle("Galactic Saboteur: AI-Assisted Cognitive Development");
    stage.getIcons().add(new Image("/images/logo.png"));
    stage.setMaximized(true);

    restart();
    stage.show();
  }
}
