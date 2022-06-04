package com.alexdsteele.chip8emulator.model;

import java.util.Random;

public class CPU {

  private final Memory memory;
  private final Registers registers;
  private final Stack stack;
  private final Timers timers;
  public final Display display;
  public final Keyboard keypad;
  public boolean mustRedraw;

  public CPU() {
    memory = new Memory();
    registers = new Registers();
    stack = new Stack();
    timers = new Timers();
    display = new Display();
    keypad = new Keyboard();
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
          case 0x0EE:
            registers.pc = stack.pop();
            break;
          default:
            throw new RuntimeException("Unimplemented instruction reached.");
        }
        break;
      case 0x1:
        registers.pc = NNN;
        break;
      case 0x2:
        stack.push(registers.pc);
        registers.pc = NNN;
        break;
      case 0x3:
        if (registers.v[X] == (byte) NN) {
          registers.pc += 2;
        }
        break;
      case 0x4:
        if (registers.v[X] != (byte) NN) {
          registers.pc += 2;
        }
        break;
      case 0x5:
        if (registers.v[X] == registers.v[Y]) {
          registers.pc += 2;
        }
        break;
      case 0x6:
        registers.v[X] = (byte) NN;
        break;
      case 0x7:
        registers.v[X] += (byte) NN;
        break;
      case 0x8:
        switch (N) {
          case 0x0:
            registers.v[X] = registers.v[Y];
            break;
          case 0x1:
            registers.v[X] |= registers.v[Y];
            break;
          case 0x2:
            registers.v[X] &= registers.v[Y];
            break;
          case 0x3:
            registers.v[X] ^= registers.v[Y];
            break;
          case 0x4:
            if ((registers.v[X] & 0xFF) + (registers.v[Y] & 0xFF) > 0xFF) {
              registers.v[0xF] = 1;
            } else {
              registers.v[0xF] = 0;
            }
            registers.v[X] += registers.v[Y];
            break;
          case 0x5:
            if ((registers.v[X] & 0xFF) > (registers.v[Y] & 0xFF)) {
              registers.v[0xF] = 1;
            } else {
              registers.v[0xF] = 0;
            }
            registers.v[X] -= registers.v[Y];
            break;
          case 0x6:
            registers.v[0xF] = (byte) (registers.v[X] & 1);
            registers.v[X] >>= 1;
            break;
          case 0x7:
            if ((registers.v[Y] & 0xFF) > (registers.v[X] & 0xFF)) {
              registers.v[0xF] = 1;
            } else {
              registers.v[0xF] = 0;
            }
            registers.v[X] = (byte) (registers.v[Y] - registers.v[X]);
            break;
          case 0xE:
            registers.v[0xF] = (byte) ((registers.v[X] & 0x80) >>> 7);
            registers.v[X] <<= 1;
            break;
          default:
            throw new RuntimeException("Undefined instruction reached.");
        }
        break;
      case 0x9:
        if (registers.v[X] != registers.v[Y]) {
          registers.pc += 2;
        }
        break;
      case 0xA:
        registers.index = NNN;
        break;
      case 0xB:
        registers.pc = (short) ((registers.v[0] & 0xFF) + NNN);
        break;
      case 0xC:
        Random random = new Random();
        byte[] randomByte = new byte[1];
        random.nextBytes(randomByte);
        registers.v[X] = (byte) (randomByte[0] & NN);
        break;
      case 0xD:
        int x = registers.v[X] % 64;
        int y = registers.v[Y] % 32;
        drawSprite(x, y, N);
        break;
      case 0xE:
        switch (NN) {
          case 0x9E:
            if (keypad.isPressed(registers.v[X])) {
              registers.pc += 2;
            }
            break;
          case 0xA1:
            if (!keypad.isPressed(registers.v[X])) {
              registers.pc += 2;
            }
            break;
          default:
            throw new RuntimeException("Undefined instruction reached.");
        }
        break;
      case 0xF:
        switch (NN) {
          case 0x07:
            registers.v[X] = timers.getDelay();
            break;
          case 0x0A:
            registers.pc -= 2;
            for (byte i = 0; i < 0xF; i++) {
              if (keypad.isPressed(i)) {
                registers.pc += 2;
                registers.v[X] = i;
                break;
              }
            }
            break;
          case 0x15:
            timers.setDelay(registers.v[X]);
            break;
          case 0x18:
            timers.setSound(registers.v[X]);
            break;
          case 0x1E:
            registers.index += (registers.v[X] & 0xFF);
            break;
          case 0x29:
            registers.index = (short) (registers.v[X] * 5 + 0x80);
            break;
          case 0x33:
            byte dig1 = (byte) (((registers.v[X] & 0xFF) / 100) % 10);
            memory.setByte(registers.index, dig1);
            byte dig2 = (byte) (((registers.v[X] & 0xFF) / 10) % 10);
            memory.setByte(registers.index + 1, dig2);
            byte dig3 = (byte) ((registers.v[X] & 0xFF) % 10);
            memory.setByte(registers.index + 2, dig3);
            break;
          case 0x55:
            for (int i = 0; i <= X; i++) {
              memory.setByte(registers.index + i, registers.v[i]);
            }
            break;
          case 0x65:
            for (int i = 0; i <= X; i++) {
              registers.v[i] = memory.getByte(registers.index + i);
            }
            break;
          default:
            throw new RuntimeException("Undefined instruction reached.");
        }
        break;
      default:
        throw new RuntimeException("Undefined instruction reached.");
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

  public void updateTimers() {
    timers.tick();
  }

}
