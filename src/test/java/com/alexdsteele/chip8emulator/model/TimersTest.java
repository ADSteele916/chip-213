package com.alexdsteele.chip8emulator.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TimersTest {

  Timers timers;

  @BeforeEach
  void setUp() {
    timers = new Timers();
  }

  @Test
  void testTick_delay() {
    timers.setDelay((byte) 0xFF);
    assertEquals((byte) 0xFF, timers.getDelay());
    for (int i = 0; i < 0xFF; i++) {
      timers.tick();
      assertEquals((byte) (0xFF - i - 1), timers.getDelay());
    }
    timers.tick();
    assertEquals(0, timers.getDelay());
  }

  @Test
  void testTick_sound() {
    timers.setSound((byte) 0xFF);
    assertEquals((byte) 0xFF, timers.getSound());
    for (int i = 0; i < 0xFF; i++) {
      timers.tick();
      assertEquals((byte) (0xFF - i - 1), timers.getSound());
    }
    timers.tick();
    assertEquals(0, timers.getSound());
  }

}
