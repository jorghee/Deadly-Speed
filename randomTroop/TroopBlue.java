package randomTroop;

import randomTroop.warriors.*;
import javafx.scene.image.Image;
import java.util.List;

public class TroopBlue extends TroopFactory {
  private static Image[] img = {
    new Image(TroopBlue.class.getResource("/randomTroop/img/blue-lord.png").toExternalForm()),
    new Image(TroopBlue.class.getResource("/randomTroop/img/blue-archer.png").toExternalForm()),
    new Image(TroopBlue.class.getResource("/randomTroop/img/blue-swordsman.png").toExternalForm()),
    new Image(TroopBlue.class.getResource("/randomTroop/img/blue-lancer.png").toExternalForm())
  };

  public static List<Warrior> createBlue() {
    return create(img);
  }
}
