package com.alexdsteele.chip8emulator.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StackTest {

  private Stack stack;

  @BeforeEach
  void setUp() {
    stack = new Stack();
  }

  @Test
  void testPushPop() {
    for (short i = 0; i < 16; i++) {
      stack.push(i);
    }
    for (short i = 15; i >= 0; i--) {
      assertEquals(stack.pop(), i);
    }
  }
}
