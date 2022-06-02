package com.alexdsteele.chip8emulator.model;

public class Display {

  public static final int WIDTH = 64;
  public static final int HEIGHT = 32;

  private final boolean[][] image;

  public Display() {
    image = new boolean[HEIGHT][WIDTH];
  }

  public boolean getPixel(int x, int y) {
    return image[y][x];
  }

  public void setPixel(int x, int y, boolean on) {
    image[y][x] = on;
  }

  public void clear() {
    for (int y = 0; y < HEIGHT; y++) {
      for (int x = 0; x < WIDTH; x++) {
        image[y][x] = false;
      }
    }
  }

}
