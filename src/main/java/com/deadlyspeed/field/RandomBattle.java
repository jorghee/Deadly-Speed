package com.deadlyspeed.field;

import java.util.List;

import com.deadlyspeed.randomTroop.*;
import com.deadlyspeed.randomTroop.fighter.*;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;

public class RandomBattle implements Battle<RandomBattle> {
  // Troops to be engaged
  private List<Fighter<ImageView>> troopBlue;
  private List<Fighter<ImageView>> troopRed;

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
  private Button exit = new Button("Exit game");

  public RandomBattle() {
    state.getChildren().addAll(
                          name, skin,
                          new Label("Hp"), hp,
                          new Label("Speed:"), speed, 
                          new Label("Attack"), attack,
                          new Label("Defense"), defense);
    state.getStyleClass().add("state");

    assistance.getChildren().addAll(message);
    assistance.getStyleClass().add("assistance");

    exit.getStyleClass().add("button");

    overview.getChildren().addAll(state, assistance, exit);
    overview.setAlignment(Pos.CENTER);
    overview.getStyleClass().add("overview");

    map.setAlignment(Pos.CENTER);
    container.getChildren().addAll(map, overview);
    container.setAlignment(Pos.CENTER);
  }

  /**
   * The method adds a Rectangle in every cell of the GridPane to set the 
   * space of every warrior.
   */
  public RandomBattle createField() {
    map.setHgap(1); map.setVgap(1);

    for (int x = 0; x < 10; x++)
      for (int y = 0; y < 10; y++)
        map.add(new Rectangle(100, 100, Color.rgb(0, 0, 0, 0.5)), y, x);

    return this;
  }

  public RandomBattle putFighters() {
    troopBlue = TroopBlue.createBlue();
    troopRed = TroopRed.createRed();

    put(troopBlue);
    put(troopRed);

    return this;
  }

  private void put(List<Fighter<ImageView>> troop) {
    ImageView img = null;

    /**
     * Enhanced for loop is god in Linked Lists, because it implements the iterator()
     * method of the Iterable interface. This enhanced for loop is O(N), it not uses 
     * the get() method of the LinkedList class that at a iteration usinng traditional 
     * loop is O(N^2).
     */
    for(Fighter<ImageView> warrior : troop) {
      img = warrior.getSkin();
      img.setFitWidth(50); img.setFitHeight(50);
      map.add(img, warrior.getPosition()[1], warrior.getPosition()[0]);
      GridPane.setHalignment(img, HPos.CENTER); GridPane.setValignment(img, VPos.CENTER);
    }
  }

  // Setters and Getters methods
  public List<Fighter<ImageView>> getTroopBlue() { return troopBlue; }

  public List<Fighter<ImageView>> getTroopRed() { return troopRed; }

  public HBox getContainer() { return container; }

  public GridPane getMap() { return map; }

  public Label  getNameLabel() { return name; }

  public ImageView getSkinView() { return skin; }

  public ProgressBar getHpBar() { return hp; }

  public ProgressBar getSpeedBar() { return speed; }

  public ProgressBar getAttackBar() { return attack; }

  public ProgressBar getDefenseBar() { return defense; }

  public Label getMessage() { return message; }

  public Button getExit() { return exit; }
}
