package com.alexdsteele.chip8emulator.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MemoryTest {

  private Memory memory;

  @BeforeEach
  void setUp() {
    memory = new Memory();
  }

  @Test
  void testReadWrite() {
    for (int i = 0; i < 0x50; i++) {
      assertEquals(memory.getByte(i), 0);
    }
    for (int i = 0x50; i < 0x9F; i++) {
      assertEquals(memory.getByte(i), (byte) Memory.DEFAULT_FONT[i - 0x50]);
    }
    for (int i = 0x9F; i < 0x1000; i++) {
      assertEquals(memory.getByte(i), 0);
    }

    for (int i = 0; i < 0x1000; i++) {
      memory.setByte(i, (byte) (i % 256));
    }
    for (int i = 0; i < 0x1000; i++) {
      assertEquals(memory.getByte(i), (byte) (i % 256));
    }
  }
}
