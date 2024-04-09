package com.deadlyspeed.randomTroop.fighter;

public class Swordsman<T> extends Warrior<T> {
  private int swordLen;
  
  public Swordsman(String name, int x, int y) { 
    super(name, random.nextInt(3) + 8, 10, 8, x, y);
  }

  public void createShieldWall() {}
}
