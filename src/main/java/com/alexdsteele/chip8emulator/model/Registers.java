package com.alexdsteele.chip8emulator.model;

public class Registers {

  public final byte[] v;
  public short pc;
  public short index;

  public Registers() {
    pc = 0x200;
    index = 0;
    v = new byte[16];
  }

}
