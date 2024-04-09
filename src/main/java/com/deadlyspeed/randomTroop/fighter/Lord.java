package com.deadlyspeed.randomTroop.fighter;

public class Lord<T> extends Warrior<T> {
  private enum Armament {SWORD, LANCE}
  private enum State {MOUNTED, DISMOUNTED}

  private Armament armament = Armament.LANCE;
  private State state = State.MOUNTED;

  public Lord(String name, int x, int y) { 
    super(name, random.nextInt(3) + 10, 13, 7, x, y);
  }

  public void mount() {
    if(state == State.DISMOUNTED) { 
      armament = Armament.LANCE;
      state = State.MOUNTED;
    }
  }

  public void dismount() {
    if(state == State.MOUNTED) { 
      armament = Armament.SWORD;
      state = State.DISMOUNTED;
    }
  }

  public void charge() {
    if( state == State.DISMOUNTED )
      setAttack( getAttack() * 2 );
    else
      setAttack( getAttack() * 3 );
  }
}
