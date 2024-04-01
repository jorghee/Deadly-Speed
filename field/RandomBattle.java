package field;

import java.util.LinkedList;
import java.util.List;

import randomTroop.*;
import randomTroop.warriors.*;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.geometry.HPos;
import javafx.geometry.VPos;

public class RandomBattle implements Battle<RandomBattle> {
  // Troops to be engaged
  private List<Warrior> troop1;
  private List<Warrior> troop2;

  // Game root
  private HBox container = new HBox();    

  // Game space
  private GridPane map = new GridPane();

  // Game information's root
  private VBox overview = new VBox();     

  // Warrior state's information
  private VBox state = new VBox();
  private Label name = new Label();
  private ImageView skin = new ImageView();
  private ProgressBar hp = new ProgressBar();
  private ProgressBar speed = new ProgressBar();
  private ProgressBar attack = new ProgressBar();
  private ProgressBar defense = new ProgressBar();

  // Game assistance, it shows messages
  private VBox assistance = new VBox();
  private Label message = new Label();

  public RandomBattle() {
    state.getChildren().addAll(
                          name, skin,
                          new Label("Hp"), hp,
                          new Label("Speed:"), speed, 
                          new Label("Attack"), attack,
                          new Label("Defense"), defense
                          );
    state.getStyleClass().add("state");

    assistance.getChildren().addAll(message);
    assistance.getStyleClass().add("assistance");

    overview.getChildren().addAll(state, assistance);
    overview.getStyleClass().add("overview");

    container.getChildren().addAll(map, overview);
  }

  /**
   * The method adds a Rectangle in every cell of the GridPane to set the 
   * space of every warrior.
   */
  public RandomBattle createField() {
    map.setHgap(1); map.setVgap(1);

    Rectangle rectangle = new Rectangle(100, 100, Color.rgb(0, 0, 0, 0.5));

    for (int x = 0; x < 10; x++)
      for (int y = 0; y < 10; y++)
        map.add(rectangle, y, x);

    return this;
  }

  public RandomBattle putWarriors() {
    troop1 = TroopBlue.createBlue();
    troop2 = TroopRed.createRed();

    put(troop1);
    put(troop2);

    return this;
  }

  private void put(List<Warrior> troop) {
    ImageView img = null;

    /**
     * Enhanced for loop is god in Linked Lists, because it implements the iterator()
     * method of the Iterable interface. This enhanced for loop is O(N), it not uses 
     * the get() method of the LinkedList class that at a iteration usinng traditional 
     * loop is O(N^2).
     */
    for(Warrior warrior : troop) {
      img = new ImageView(warrior.getSkin());
      img.setFitWidth(50); img.setFitHeight(50);
      map.add(img, warrior.getPosition()[1], warrior.getPosition()[0]);
      GridPane.setHalignment(img, HPos.CENTER); GridPane.setValignment(img, VPos.CENTER);
    }
  }

  // Setters and Getters methods
  public List<Warrior> getTroop1() { return troop1; }

  public List<Warrior> getTroop2() { return troop2; }

  public HBox getContainer() { return container; }

  public GridPane getMap() { return map; }

  public Label  getName() { return name; }

  public ImageView getSkin() { return skin; }

  public ProgressBar getHp() { return hp; }

  public ProgressBar getSpeed() { return speed; }

  public ProgressBar getAttack() { return attack; }

  public ProgressBar getDefense() { return defense; }

  public Label getMessage() { return message; }
}
