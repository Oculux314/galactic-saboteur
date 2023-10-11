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
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import nz.ac.auckland.se206.controllers.MainController;
import nz.ac.auckland.se206.gpt.Assistant;
import nz.ac.auckland.se206.speech.TextToSpeech;

/** The entry point of the JavaFX application, representing the top-level application. */
public class App extends Application {

  private static Stage stage;

  /** A map of all screens in the application (name -> screen) */
  public static Map<Screen.Name, Screen> screens = new HashMap<>();

  private static Set<TaggedThread> threads = new HashSet<>();
  private static TextToSpeech tts = new TextToSpeech();
  public static Assistant scientist;
  public static Assistant mechanic;
  public static Assistant captain;

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
   * Returns the screen with the given name, or null if it does not exist.
   *
   * @param screenName The name of the screen to get.
   * @return The screen with the given name, or null if it does not exist.
   */
  public static Screen getScreen(final Screen.Name screenName) {
    return screens.get(screenName); // Null if does not exist
  }

  /**
   * Sets the screen with the given name as the child of the main pane. Creates the screen if it
   * does not exist. Assumes there is only one screen currently active.
   *
   * @param screenName The name of the screen to set.
   */
  public static void setScreen(final Screen.Name screenName) {
    GameState.currentScreen = screenName;

    while (!screens.containsKey(screenName)) {
      // Wait for screen to be loaded
    }

    ObservableList<Node> activeScreens = getPanMain().getChildren();
    Parent newScreen = getScreen(screenName).getFxml();

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
    //Utils.logTimeTest(

    TaggedThread screenLoader = new TaggedThread(() -> makeScreenWithoutThread(screenName));
    screenLoader.start();
  }

  private static void makeScreenWithoutThread(final Screen.Name screenName) {
    String fxml = screenName.toString().toLowerCase();
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml"));
    screens.put(screenName, new Screen(loader));
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

  public static void restart() {
    killAllThreads();
    GameState.reset();
    resetScreens();
  }

  private static void killAllThreads() {
    for (TaggedThread thread : threads) {
      thread.interrupt(); // Does nothing if thread is already dead
    }

    threads.clear();
  }

  private static void resetScreens() {
    TaggedThread screenLoader =
        new TaggedThread(
            () -> {
              for (Screen.Name screenName : Screen.Name.values()) {
                if (screenName == Screen.Name.MAIN) { // Main screen is persistent
                  continue;
                }

                makeScreen(screenName);
              }
            });

    screenLoader.start();
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

    TaggedThread ttsThread = new TaggedThread(() -> tts.speak(text));
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

    // Stylesheet
    scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

    // Listeners
    ((MainController) screens.get(Screen.Name.MAIN).getController()).addSceneListeners();

    // Properties
    scene.setFill(Color.web("#131d23"));
    stage.setTitle("Galactic Saboteur");
    stage.getIcons().add(new Image("/images/logo.png"));
    stage.setMaximized(true);

    stage.show();
    restart();
  }
}
