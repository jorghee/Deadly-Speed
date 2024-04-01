package randomTroop;

import randomTroop.fighter.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.List;

public class TroopRed extends TroopFactory {
  private static Image[] img = {
    new Image(TroopBlue.class.getResource("/randomTroop/img/red-lord.png").toExternalForm()),
    new Image(TroopBlue.class.getResource("/randomTroop/img/red-archer.png").toExternalForm()),
    new Image(TroopBlue.class.getResource("/randomTroop/img/red-swordsman.png").toExternalForm()),
    new Image(TroopBlue.class.getResource("/randomTroop/img/red-lancer.png").toExternalForm())
  };

  public static List<Fighter<ImageView>> createRed() {
    return create(img);
  }
}
