package com.deadlyspeed.randomTroop;

import com.deadlyspeed.randomTroop.fighter.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.List;

public class TroopBlue extends TroopFactory {
  private static Image[] img = {
    new Image(TroopBlue.class.getResource("/img/blue-lord.png").toExternalForm()),
    new Image(TroopBlue.class.getResource("/img/blue-archer.png").toExternalForm()),
    new Image(TroopBlue.class.getResource("/img/blue-swordsman.png").toExternalForm()),
    new Image(TroopBlue.class.getResource("/img/blue-lancer.png").toExternalForm())
  };

  public static List<Fighter<ImageView>> createBlue() {
    return create(img);
  }
}
