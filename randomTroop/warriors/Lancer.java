package randomTroop.warriors;

public class Lancer extends Warrior {
  int spearLen;

  public Lancer(String name, int x, int y) { 
    super(name, random.nextInt(4) + 5, 5, 10, x, y);
  }
}
