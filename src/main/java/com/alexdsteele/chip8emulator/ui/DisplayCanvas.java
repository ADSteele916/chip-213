package com.alexdsteele.chip8emulator.ui;

import com.alexdsteele.chip8emulator.model.Display;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DisplayCanvas extends Canvas {

  private static final int SCALE = 16;
  private final GraphicsContext graphicsContext;

  public DisplayCanvas() {
    super(Display.WIDTH * SCALE, Display.HEIGHT * SCALE);

    setFocusTraversable(true);

    graphicsContext = this.getGraphicsContext2D();
    graphicsContext.setFill(Color.BLACK);
    graphicsContext.fillRect(0, 0, getWidth(), getHeight());
  }

  public void render(Display display) {
    for (int y = 0; y < Display.HEIGHT; y++) {
      for (int x = 0; x < Display.WIDTH; x++) {
        graphicsContext.setFill(display.getPixel(x, y) ? Color.WHITE : Color.BLACK);
        graphicsContext.fillRect(x * SCALE, y * SCALE, SCALE, SCALE);
      }
    }
  }

}
