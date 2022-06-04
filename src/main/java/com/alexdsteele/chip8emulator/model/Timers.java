package com.alexdsteele.chip8emulator.model;

public class Timers {

  private byte delay;
  private byte sound;

  public Timers() {
    delay = 0;
    sound = 0;
  }

  public byte getDelay() {
    return delay;
  }

  public void setDelay(byte delay) {
    this.delay = delay;
  }

  public byte getSound() {
    return sound;
  }

  public void setSound(byte sound) {
    this.sound = sound;
  }

  public void tick() {
    delay -= delay != 0 ? 1 : 0;
    sound -= sound != 0 ? 1 : 0;
  }
}
