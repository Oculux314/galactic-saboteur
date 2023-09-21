package nz.ac.auckland.se206.riddle;

import javafx.fxml.FXML;
import nz.ac.auckland.se206.components.StateButton;
import nz.ac.auckland.se206.components.AnimatedButton;
import nz.ac.auckland.se206.controllers.Controller;
import nz.ac.auckland.se206.GameState;

public class RiddleController implements Controller {

    @FXML private StateButton btnWho = new StateButton();
    @FXML private StateButton btnWhere = new StateButton();
    @FXML private StateButton btnWhen = new StateButton();
    @FXML private AnimatedButton btnAnswer;

    @FXML
    private void initialize() {
        btnWho.addState("Scientist", "suspects/suspect1.jpg");
        btnWho.addState("Captain", "suspects/suspect2.jpg");
        btnWho.addState("Mechanic", "suspects/suspect3.png");

        btnWhere.addState("Navigation", "rooms/room1.jpg");
        btnWhere.addState("Laboratory", "rooms/room2.jpg");
        btnWhere.addState("Reactor Room", "rooms/room3.jpg");

        btnWhen.addState("Morning", "times/time1.jpg");
        btnWhen.addState("Afternoon", "times/time2.jpg");
        btnWhen.addState("Night", "times/time3.jpg");
    }

    @FXML
    private void answerClicked() {
        if (btnWho.getState().equals(GameState.correctSuspect)
                && btnWhere.getState().equals(GameState.correctRoom)
                && btnWhen.getState().equals(GameState.correctTime)) {
                    System.out.println("Correct!");
        } else {
            System.out.println("Incorrect!");
        }
    }
}