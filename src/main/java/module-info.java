module com.alexdsteele.chip8emulator {
  requires javafx.controls;

  exports com.alexdsteele.chip8emulator.model;
  opens com.alexdsteele.chip8emulator.model to javafx.fxml;
}
