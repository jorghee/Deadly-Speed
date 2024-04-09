package com.deadlyspeed.connection;

public class Player {
  private String name;
  private int id;
  private StatePlayer statePlayer;

  public Player(String name, int id, StatePlayer statePlayer) {
    this.name = name;
    this.id = id;
    this.statePlayer = statePlayer;
  }

  public Player(StatePlayer statePlayer) { this.statePlayer = statePlayer; }

  public String getName() { return name; }

  public int getId() { return id; }

  public StatePlayer getStatePlayer() { return statePlayer; }
}
