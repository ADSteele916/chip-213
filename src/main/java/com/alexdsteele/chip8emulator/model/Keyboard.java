package com.alexdsteele.chip8emulator.model;

import java.util.Map;
import javafx.scene.input.KeyCode;

public class Keyboard {

  private static final Map<KeyCode, Integer> keyTable = Map.ofEntries(
      Map.entry(KeyCode.DIGIT1, 0),
      Map.entry(KeyCode.DIGIT2, 1),
      Map.entry(KeyCode.DIGIT3, 2),
      Map.entry(KeyCode.DIGIT4, 3),
      Map.entry(KeyCode.Q, 4),
      Map.entry(KeyCode.W, 5),
      Map.entry(KeyCode.E, 6),
      Map.entry(KeyCode.R, 7),
      Map.entry(KeyCode.A, 8),
      Map.entry(KeyCode.S, 9),
      Map.entry(KeyCode.D, 10),
      Map.entry(KeyCode.F, 11),
      Map.entry(KeyCode.Z, 12),
      Map.entry(KeyCode.X, 13),
      Map.entry(KeyCode.C, 14),
      Map.entry(KeyCode.V, 15));

  private final boolean[] keyStates;

  public Keyboard() {
    keyStates = new boolean[16];
  }

  public void pressKey(KeyCode k) {
    if (keyTable.containsKey(k)) {
      keyStates[keyTable.get(k)] = true;
    }
  }

  public void releaseKey(KeyCode k) {
    if (keyTable.containsKey(k)) {
      keyStates[keyTable.get(k)] = false;
    }
  }

  public boolean isPressed(int idx) {
    return keyStates[idx];
  }

}
