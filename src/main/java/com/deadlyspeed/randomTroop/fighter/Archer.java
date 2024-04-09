package com.deadlyspeed.randomTroop.fighter;

public class Archer<T> extends Warrior<T> {
  private int nArrow;

  public Archer(String name, int x, int y) { 
    super(name, random.nextInt(3) + 3, 7, 3, x, y);
    nArrow = random.nextInt(10) + 1;
  }

  public void shootArrow() {
    if(nArrow > 0)
      nArrow -= 1;
    else
      System.out.println("No tienes mas flechas, F");
  }
}
