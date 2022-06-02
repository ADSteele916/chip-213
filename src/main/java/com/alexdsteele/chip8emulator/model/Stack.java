package com.alexdsteele.chip8emulator.model;

public class Stack {

  private final short[] stack;
  private int sp;

  public Stack() {
    stack = new short[16];
    sp = -1;
  }

  public void push(short data) {
    stack[++sp] = data;
  }

  public short pop() {
    return stack[sp--];
  }

}
