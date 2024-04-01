package randomTroop;

import randomTroop.fighter.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.List;

public class TroopBlue extends TroopFactory {
  private static Image[] img = {
    new Image(TroopBlue.class.getResource("/randomTroop/img/blue-lord.png").toExternalForm()),
    new Image(TroopBlue.class.getResource("/randomTroop/img/blue-archer.png").toExternalForm()),
    new Image(TroopBlue.class.getResource("/randomTroop/img/blue-swordsman.png").toExternalForm()),
    new Image(TroopBlue.class.getResource("/randomTroop/img/blue-lancer.png").toExternalForm())
  };

  public static List<Fighter<ImageView>> createBlue() {
    return create(img);
  }
}
