package nz.ac.auckland.se206.puzzles;

import java.util.HashMap;
import nz.ac.auckland.se206.controllers.GameController.RoomGroup;
import nz.ac.auckland.se206.puzzles.Puzzle.PuzzleName;

public class PuzzleHandler {

  HashMap<String, PuzzleName> buttonToPuzzleMap;
  RoomGroup rooms;

  public PuzzleHandler(RoomGroup rooms) {
    buttonToPuzzleMap = new HashMap<>();
    this.rooms = rooms;

    populateButtonToPuzzleMap();
  }

  private void populateButtonToPuzzleMap() {
    buttonToPuzzleMap.put("btnToolbox", PuzzleName.REACTOR_TOOLBOX);
    buttonToPuzzleMap.put("btnButtonpad", PuzzleName.REACTOR_BUTTONPAD);
    buttonToPuzzleMap.put("btnApple", PuzzleName.REACTOR_APPLE);
  }

  public HashMap<String, PuzzleName> getButtonToPuzzleMap() {
    return buttonToPuzzleMap;
  }
}
