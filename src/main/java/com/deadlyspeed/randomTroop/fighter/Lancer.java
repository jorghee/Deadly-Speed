package com.deadlyspeed.randomTroop.fighter;

public class Lancer<T> extends Warrior<T> {
  int spearLen;

  public Lancer(String name, int x, int y) { 
    super(name, random.nextInt(4) + 5, 5, 10, x, y);
  }
}
