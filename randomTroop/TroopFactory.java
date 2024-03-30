package randomTroop;

import java.util.*;
import randomTroop.warriors.*;
import javafx.scene.image.Image;

public class TroopFactory {
  private static boolean[][] position = new boolean[10][10];  
  private static final Random random = new Random();

  public static List<Warrior> create(Image[] img) {
    List<Warrior> troop = new LinkedList<>();
    Warrior warrior = null;

    int total = random.nextInt(10) + 1;
    int count = 0;

    while(count < total) {
      int x = random.nextInt(10);
      int y = random.nextInt(10);
      int type = random.nextInt(4);
      
      if(!position[x][y]) {
        switch(type) {
          case 0 :
            warrior = new Lord("Lord " + count, x, y);
            warrior.setSkin(img[0]);
            break;

          case 1 :
            warrior = new Archer("Archer " + count, x, y);
            warrior.setSkin(img[1]);
            break;

          case 2 :
            warrior = new Swordsman("Swordsman " + count, x, y);
            warrior.setSkin(img[2]);
            break;

          default:
            warrior = new Lancer("Lancer " + count, x, y);
            warrior.setSkin(img[3]);
            break;
        }

        troop.add(warrior);
        position[x][y] = true;
        count++;
      }
    }

    return troop;
  }

  public static Warrior selectWarrior(List<Warrior> troop, int x, int y) {
    for(Warrior i : troop)
      if(i.getPosition()[0] == x && i.getPosition()[1] == y)
        return i;
    return null;
  }
}
