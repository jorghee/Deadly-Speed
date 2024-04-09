package com.deadlyspeed.randomTroop.fighter;

public interface Fighter<T> {
  void setName(String name);
  void setHp(int hp);
  void setAttack(int attack);
  void setDefense(int defense);
  void setSpeed(int speed);
  void setPosition(int x, int y);
  void setSkin(T skin);

  String getName();
  int getHp();
  int getAttack();
  int getDefense();
  int getSpeed();
  int[] getPosition();
  T getSkin();

  void moveTo(int x, int y);
  boolean attackTo(int x, int y);
  void gainSpeed(int x, int y);
  boolean escapeTo(int x, int y);
  String beAttackedBy(Fighter attacker);
  double probabilityWinning(Fighter attacker);
}
