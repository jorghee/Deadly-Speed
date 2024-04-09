package com.deadlyspeed.randomTroop.fighter;

import java.util.Random;

public class Warrior<T> implements Fighter<T> {
  private String name;
  private int hp;
  private int attack;
  private int defense;
  private int speed = 2;    // default 2
  private int[] position = new int[2];
  private T skin;

  protected static final Random random = new Random();
  
  protected Warrior( String name, int hp, int attack, int defense, int x, int y, int speed ) {
    this( name, hp, attack, defense, x, y );
    this.speed = speed;
  }

  protected Warrior( String name, int hp, int attack, int defense, int x, int y ) {
    this.name = name;
    this.hp = hp;
    this.attack = attack;
    this.defense = defense;
    position[0] = x;
    position[1] = y;
  }

  // Setters methods
  public void setName( String name ) { this.name = name; }

  public void setHp( int hp ){ this.hp = hp; }

  public void setAttack( int attack ) { this.attack = attack; } 

  public void setDefense( int defense ) { this.defense = defense; }

  public void setSpeed( int speed ) { this.speed = speed; }
  
  public void setPosition( int x, int y ) { position[0] = x; position[1] = y; }

  public void setSkin( T skin ) { this.skin = skin; }

  // Getters methods
  public String getName() { return name; }

  public int getHp() { return hp; }

  public int getAttack() { return attack; }

  public int getDefense() { return defense; }

  public int getSpeed() { return speed; }
  
  public int[] getPosition() { return position; }

  public T getSkin() { return skin; }

  /**
   * @param x is the row number within of a abstract multidimensional array
   * @param y is the column number within of a abstract multidimensional array
   */
  public void moveTo( int x, int y ) { 
    getPosition()[0] = x;
    getPosition()[1] = y;
  }

  /**
   * The method checks if the warrior will can attack. If the if statement is true then 
   * executes moveTo() method.
   * @param x is the row number of the position of the victim
   * @param y is the column number of the position of the victim
   * @return true if the speed needed to attack is less than or equal to the current 
   * attacker's speed.
   */
  public boolean attackTo( int x, int y ) {
    if( getSpeed() - calculateSpeed( x, y ) >= 0 ) {
      setSpeed( getSpeed() - calculateSpeed( x, y ) );
      moveTo( x, y );
      return true;
    } else return false;
  }

  /**
   * The method performs the speed increase of this warrior that is in 'Gain Speed' mode
   * @param x is the row number of the position where the warrior is going to move to
   * @param y is the column number of the position where the warrior is going to move to
   */
  public void gainSpeed( int x, int y ) {
    if( getSpeed() + calculateSpeed( x, y ) <= 20 )
      setSpeed( getSpeed() + calculateSpeed( x, y ) );
    moveTo( x, y );
  }

  public boolean escapeTo( int x, int y ) {
    if( calculateSpeed( x, y ) * 2 <= getSpeed() ) {
      setSpeed( getSpeed() - calculateSpeed( x, y ) * 2 );
      moveTo( x, y );
      return true;
    } else return false;
  }

  /**
   * The method computes the speed to be gained or lost. The speed is computed by adding
   * the number of cells in rows and columns. Practically the sum is at L.
   * @param x is the row number of the position where the warrior is going to move to
   * @param y is the column number of the position where the warrior is going to move to
   * @return the sum of the speeds at the row and column.
   */
  private int calculateSpeed( int x, int y ) {
    x = Math.absExact( x - position[0] );
    y = Math.absExact( y - position[1] );
    return x + y;
  }

  public String beAttackedBy( Fighter attacker ) {
    return  String.format( "\nTienes %.2f de probabilidad para ganar", probabilityWinning( attacker ) );
  }

  /**
   * This is a special method because it allows the victim to have response options against an attacker.
   * Takes into account hp, attack, and defense fiels of the victim and attacker warrior to obtain a 
   * general average.
   * @param attacker is the attacker Warrior object.
   * @return the percentage probability that the victim wins.
   */
  public double probabilityWinning( Fighter attacker ) {
    double victimPower = getHp() + getAttack() + getDefense();
    double attackerPower = attacker.getHp() + attacker.getAttack() + attacker.getDefense();
    return ( victimPower * 100 ) / ( victimPower + attackerPower );
  }

  public String toString(){
    return ( "\nName: " + getName() + "\nhp: " + getHp() + "\nAttack: " + getAttack() +
        "\nDefense: " + getDefense() + "\nSpeed: " + getSpeed() + "\nRow: " + getPosition()[0] +
        "\nColumn: " + getPosition()[1] + "\n" );
  }
}
