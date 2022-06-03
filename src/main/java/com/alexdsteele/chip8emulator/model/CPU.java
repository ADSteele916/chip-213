package com.alexdsteele.chip8emulator.model;

public class CPU {

  private final Memory memory;
  private final Registers registers;
  private final Stack stack;
  public final Display display;
  public boolean mustRedraw;

  public CPU() {
    memory = new Memory();
    registers = new Registers();
    stack = new Stack();
    display = new Display();
    mustRedraw = true;
  }

  public void tick() {
    short opcode = fetch();

    int first = (opcode & 0xF000) >>> 12;
    int X = (opcode & 0xF00) >>> 8;
    int Y = (opcode & 0xF0) >>> 4;
    int N = opcode & 0xF;
    int NN = opcode & 0xFF;
    short NNN = (short) (opcode & 0xFFF);

    switch (first) {
      case 0x0:
        switch (NNN) {
          case 0x0E0:
            display.clear();
            break;
          default:
            throw new RuntimeException("Unimplemented instruction reached.");
        }
        break;
      case 0x1:
        registers.pc = NNN;
        break;
      case 0x6:
        registers.v[X] = (byte) NN;
        break;
      case 0x7:
        registers.v[X] += (byte) NN;
        break;
      case 0xA:
        registers.index = NNN;
        break;
      case 0xD:
        int x = registers.v[X] % 64;
        int y = registers.v[Y] % 32;
        drawSprite(x, y, N);
        break;
      default:
        throw new RuntimeException("Unimplemented instruction reached.");
    }
  }

  private short fetch() {
    byte firstByte = memory.getByte(registers.pc++);
    byte secondByte = memory.getByte(registers.pc++);
    return (short) ((firstByte << 8) | (secondByte & 0xFF));
  }

  private void drawSprite(int xpos, int ypos, int n) {
    short I = registers.index;
    registers.v[0xF] = 0;

    for (int y = 0; y < n; y++) {
      if (ypos + y >= Display.HEIGHT) {
        break;
      }
      byte currentByte = memory.getByte(I + y);
      for (int x = 0; x < 8; x++) {
        if (xpos + x >= Display.WIDTH) {
          break;
        }
        boolean on = ((currentByte >> (7 - x)) & 1) == 1;
        display.setPixel(xpos + x, ypos + y, on ^ display.getPixel(xpos + x, ypos + y));
        if (on && !display.getPixel(xpos + x, ypos + y)) {
          registers.v[0xF] = 1;
        }
        if (on) {
          mustRedraw = true;
        }
      }
    }
  }

  public void loadROM(byte[] data) {
    memory.loadProgram(data);
  }

}
