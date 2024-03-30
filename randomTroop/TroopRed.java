package randomTroop;

import randomTroop.warriors.*;
import javafx.scene.image.Image;
import java.util.List;

public class TroopRed extends TroopFactory {
  private static Image[] img = {
    new Image(TroopBlue.class.getResource("/randomTroop/img/red-lord.png").toExternalForm()),
    new Image(TroopBlue.class.getResource("/randomTroop/img/red-archer.png").toExternalForm()),
    new Image(TroopBlue.class.getResource("/randomTroop/img/red-swordsman.png").toExternalForm()),
    new Image(TroopBlue.class.getResource("/randomTroop/img/red-lancer.png").toExternalForm())
  };

  public static List<Warrior> createRed() {
    return create(img);
  }
}
