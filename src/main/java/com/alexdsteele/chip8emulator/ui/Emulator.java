package com.alexdsteele.chip8emulator.ui;

import com.alexdsteele.chip8emulator.model.CPU;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Emulator extends Application {

  Timeline gameTimeline;
  Timeline timerTimeline;
  CPU cpu;

  public static void main(String[] args) {
    launch();
  }

  @Override
  public void start(Stage stage) {
    stage.setTitle("CHIP-213");

    MenuBar menuBar = new MenuBar();

    Menu fileMenu = new Menu("File");
    menuBar.getMenus().add(fileMenu);

    MenuItem loadROM = new MenuItem("Load ROM");
    loadROM.setOnAction(e -> {
      FileChooser f = new FileChooser();
      f.setTitle("Load ROM");
      File file = f.showOpenDialog(stage);

      if (file != null) {
        loadROM(file.getPath());
      }
    });
    fileMenu.getItems().add(loadROM);

    MenuItem exit = new MenuItem("Exit");
    exit.setOnAction(e -> System.exit(0));
    fileMenu.getItems().add(exit);

    DisplayCanvas displayCanvas = new DisplayCanvas();

    VBox vbox = new VBox();
    vbox.getChildren().add(menuBar);
    vbox.getChildren().add(displayCanvas);

    Scene scene = new Scene(vbox);
    stage.setScene(scene);

    cpu = new CPU();

    scene.setOnKeyPressed(e -> cpu.keypad.pressKey(e.getCode()));
    scene.setOnKeyReleased(e -> cpu.keypad.releaseKey(e.getCode()));

    gameTimeline = new Timeline();
    gameTimeline.setCycleCount(Timeline.INDEFINITE);

    KeyFrame gameFrame = new KeyFrame(
        Duration.seconds(1 / 700.0),
        actionEvent -> {
          try {
            cpu.tick();
          } catch (RuntimeException e) {
            e.printStackTrace();
            gameTimeline.stop();
          }

          if (cpu.mustRedraw) {
            displayCanvas.render(cpu.display);
            cpu.mustRedraw = false;
          }
        });

    gameTimeline.getKeyFrames().add(gameFrame);

    timerTimeline = new Timeline();
    timerTimeline.setCycleCount(Timeline.INDEFINITE);

    KeyFrame timerFrame = new KeyFrame(
        Duration.seconds(1 / 60.0),
        actionEvent -> cpu.updateTimers());

    timerTimeline.getKeyFrames().add(timerFrame);

    stage.show();
  }

  private void loadROM(String romPath) {
    gameTimeline.stop();
    timerTimeline.stop();

    cpu = new CPU();

    try {
      FileInputStream inputStream = new FileInputStream(romPath);
      cpu.loadROM(inputStream.readAllBytes());
      inputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }

    gameTimeline.play();
    timerTimeline.play();
  }

}
