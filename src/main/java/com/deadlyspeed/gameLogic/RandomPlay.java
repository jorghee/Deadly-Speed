package com.deadlyspeed.gameLogic;

import com.deadlyspeed.randomTroop.TroopFactory;
import com.deadlyspeed.randomTroop.fighter.*;
import com.deadlyspeed.field.*;

import java.util.List;
import java.util.LinkedList;
import java.util.Random;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;

public class RandomPlay implements Play<RandomBattle, Scene> {
  // Game type current
  private RandomBattle battle;

  // Abstracts the turn of the troops with its corresponding Player
  private List<Fighter<ImageView>> troopCurrent;
  private String playerCurrent;

  // Fields to the assistance field
  private String player1, player2;

  // Field storing the view of the warrior's skins
  private List<ImageView> skins = new LinkedList<>();

  // Field controling the gain speed and clash mode
  private boolean clash;

  // Important field of the current Warrior
  private Fighter<ImageView> selWarrior, victim, attacker;

  // Fields controling the possible answer of the victim warrior
  private enum Answer {COUNTERATTACK, ESCAPE, BOTH};
  private Answer answer;

  public RandomPlay(String player1, String player2) {
    this.player1 = player1;
    this.player2 = player2;
  }

  /**
   * @param battle is the type of battle generation, in this case we have implemented a random generation.
   * @return the graphical representation of the battle. In this case is the javafx Scene
   */
  public Scene game(RandomBattle battle) {
    this.battle = battle.createField().putFighters();

    // Troop Blue and player 1 initiated by default
    troopCurrent = this.battle.getTroopBlue();
    playerCurrent = player1;

    // Search the warrior skins within of the GridPane to initialize the skins field
    setSkins();

    this.battle.getMap().setOnMouseClicked(event -> {
      /**
       * x is the row number calculated couting the number of pixels on the y-axis of the 
       * GridPane diveded by the legth of the even cells.
       * y is the column number calculated couting the number of pixels on the x-axis of the 
       * GridPane diveded by the legth of the even cells.
       * In this case, each cell is square of legth 100px
       */
      int x = (int) (event.getY() / 100);  // compute row
      int y = (int) (event.getX() / 100);  // compute column

      if(!clash) {
        if(event.getButton() == MouseButton.PRIMARY)
          selectWarriorOf(x, y);
        if(event.getButton() == MouseButton.SECONDARY)
          moveSelWarriorTo(x, y);
      } else {
        if(event.getButton() == MouseButton.PRIMARY)
          selectVictimOf(x, y);
        if(event.getButton() == MouseButton.SECONDARY)
          moveSelVictimTo(x, y);
      }
    });

    return new Scene(this.battle.getContainer(), 1500, 1000);
  }

  /**
   * The method is called when the event is left click, selecting a warrior involves:
   * - Make sure that there is not a warrior previously selected
   * - Check that it is a warrior y that it is from the same troop
   * - Report if the above 3 points are not verified
   *
   * @param x is the row number of the warrior to be selected
   * @param y is the column number of the warrior to be selected
   */
  private void selectWarriorOf(int x, int y) {
    if(selWarrior != null)
      selWarrior = null;

    // Search the skin on the skins field that stores to the warriors on the GridPane
    ImageView skin = searchSkin(x, y);

    if(skin != null) {
      // Search to the warrior on its own troop
      selWarrior = TroopFactory.getWarrior(troopCurrent, x, y);

      if(selWarrior == null) {
        change();
        battle.getMessage().setText(playerCurrent + ", no es tu turno");
        change();
        return;
      }

      battle.getMessage().setText(playerCurrent + ", gana velocidad o ataca");

      // Show the information of the warrior
      battle.getNameLabel().setText(selWarrior.getName());
      battle.getSkinView().setImage(selWarrior.getSkin().getImage());
      battle.getHpBar().setProgress(selWarrior.getHp() * 1.0 / 12);
      battle.getSpeedBar().setProgress(selWarrior.getSpeed() * 1.0 / 20);
      battle.getAttackBar().setProgress(selWarrior.getAttack() * 1.0 / 13);
      battle.getDefenseBar().setProgress(selWarrior.getDefense()  * 1.0 / 10);
    } else 
      battle.getMessage().setText(playerCurrent + ", no estas seleccionando a un guerrero");
  }

  /**
   * The method is called when the event is right click, moving a warrior involves:
   * - Make sure that there is a warrior previously selected
   * - Check if it is going to attack or just to gain speed
   * - If it attacks, check that it has sufficient speed and that the victim is an opponent. If the 
   *   attack is allowed, then switch to the victim's troop, report the attack and wait for the 
   *   victim's response.
   * - If it gain speed, perfom the movement, update the positions, and make the shift change
   *
   * @param x is the row number of selected warrior's target position 
   * @param y is the column number of selected warrior's target position
   */
  private void moveSelWarriorTo(int x, int y) {
    if(selWarrior == null) {
      battle.getMessage().setText(playerCurrent + ", no has seleccionado a ninguno de tus guerreros");
      return;
    }

    ImageView skin = searchSkin(x, y);

    // Check the target position
    if(skin != null) {
      // Switch to the victim's troop to initialize the victim field and report the attack.
      change(); 
      victim = TroopFactory.getWarrior(troopCurrent, x, y);
      change();

      if(victim == null) {
        battle.getMessage().setText(playerCurrent + ", no puedes atacar a uno de tus guerreros, estas loco/a");
        return;
      }

      if(!selWarrior.attackTo(x, y)) {
        battle.getMessage().setText(playerCurrent + ", no tienes suficiente velocidad para atacar!");
        return;
      }

      attacker = selWarrior;

      change();
      battle.getMessage().setText(playerCurrent + ", estas siendo atacado/a" + victim.beAttackedBy(attacker));

      // We call this method to define the response of the victim
      optionBeAttacked(x, y);
    } else {
      selWarrior.gainSpeed(x, y);

      // Update the positions and the skins field
      battle.getMap().getChildren().remove(selWarrior.getSkin());
      battle.getMap().add(selWarrior.getSkin(), y, x);
      battle.getMessage().setText(playerCurrent + ", ganaste velocidad");
      setSkins();

      change();
    }

    selWarrior = null;
  }

  /**
   * This method was created with the objective of simulating the players's shift 
   * change. However, it ended up helping to facilitate the change of troops that is 
   * very required in the clash mode, such as report the attack to the victim,
   * report the victim's response to the Attacker, and so on.
   */
  private void change() {
    if(troopCurrent == battle.getTroopBlue()) {
      troopCurrent = battle.getTroopRed();
      playerCurrent = player2;
    } else {
      troopCurrent = battle.getTroopBlue();
      playerCurrent = player1;
    }
  }

  /** 
   * This method is always called upon in the victim's troop. Performs the following:
   * - If the victim is unable to escape or counterattack, dies
   * - If the victim and the attacker have the same probability of winning, then they both die
   * - If the above is not met, a clash is created in which the victim decides to 
   *   escape and/or counterattack
   */
  public void optionBeAttacked(int x, int y) {
    double win = victim.probabilityWinning(attacker);
    int speed = victim.getSpeed();

    // In any case, the representation of the skins are removed
    battle.getMap().getChildren().remove(attacker.getSkin());
    battle.getMap().getChildren().remove(victim.getSkin());

    if(win < 50.00 && speed < 2) {
      troopCurrent.remove(victim);
      battle.getMessage().setText(playerCurrent + ", tu guerrero ha muerto luchando con honor hasta el final");
      battle.getMap().add(attacker.getSkin(), y, x);

      setSkins();
      victim = attacker = null;
      return;
    }

    if(win == 50.00) {
      troopCurrent.remove(victim);

      change();
      troopCurrent.remove(attacker);
      change();

      battle.getMessage().setText("Batalla epica, murieron con honor, mis respetos.");

      setSkins();
      victim = attacker = null;
      return;
    }

    // We create a temporary HBox that simulates the clash
    HBox oneOnOne = new HBox();
    oneOnOne.setAlignment(Pos.CENTER);
    oneOnOne.getChildren().addAll(victim.getSkin(), attacker.getSkin());
    battle.getMap().add(oneOnOne, y, x);

    // We switch to clash mode.
    clash = true;
  }
 
  // This method filters the skins of the GridPane and stores them in the skins field
  private void setSkins() {
    battle.getMap().getChildren()
                  .filtered(node -> node instanceof ImageView)
                  .forEach(node -> skins.add((ImageView) node));
  }

  // Search a skin in the skin field
  private ImageView searchSkin(int x, int y) {
    for(ImageView skin : skins)
      if(GridPane.getRowIndex(skin) == x && GridPane.getColumnIndex(skin) == y)
        return skin;
    return null;
  }

  //  ## METHODS ON THE CLASH MODE ##

  /**
   * As the logic of the game is sequential, the victim must respond to the attack without the 
   * opportunity to move another warrior, because if this were possible, the logic of the game 
   * would be very hard to building. Therefore, this method is exceptional but expensive because it 
   * involves doing the following:
   * - Check that the player selected to his victim, this involves search the clash (expensive)
   * - Set the type of victim's response
   *
   * @param x must be the row number of the victim warrior 
   * @param y must be the column number of the victim warrior 
   * # It's also valid to say that must be the position of the simulated clash or the 
   * position of the attacker.
   */
  private void selectVictimOf(int x, int y) {
    HBox oneOnOne = searchClash(x, y);

    if(oneOnOne == null) {
      battle.getMessage().setText(playerCurrent + ", solo puedes mover a tu guerrero que esta siendo atacado");
      return;
    }
    
    // Show the information of the victim warrior
    battle.getNameLabel().setText(victim.getName());
    battle.getSkinView().setImage(victim.getSkin().getImage());
    battle.getHpBar().setProgress(victim.getHp() * 1.0 / 12);
    battle.getSpeedBar().setProgress(victim.getSpeed() * 1.0 / 20);
    battle.getAttackBar().setProgress(victim.getAttack() * 1.0 / 13);
    battle.getDefenseBar().setProgress(victim.getDefense()  * 1.0 / 10);

    double win = victim.probabilityWinning(attacker);
    int speed = victim.getSpeed();

    if(win < 50.00) {
      // The victim just can escape
      battle.getMessage().setText("Solo puedes escapar lo mas lejos que puedas, ten cuidado");
      answer = Answer.ESCAPE;
    } else if(speed < 2) {
      // The victim just can counterattack
      battle.getMessage().setText("Solo puedes enfrentarlo o contratacar, sin miedo al exito");
      answer = Answer.COUNTERATTACK;
    } else {
      // The victim can escape or counterattack
      battle.getMessage().setText("Puedes enfrentarlo o escapar, toma la mejor desicion de tu vida");
      answer = Answer.BOTH;
    }
  }

  /**
   * The method calls the methods that implement the victim's response action.
   * Becasue this method is called by a event, again search the clash. Futhermore
   * is a method expensive
   *
   * @param x is the row number of the victim's response target position
   * @param y is the column number of the victim's response target position
   */
  private void moveSelVictimTo(int x, int y) {
    if(answer == null) {
      battle.getMessage().setText(playerCurrent + ", selecciona a tu victima");
      return;
    }

    int currentX = victim.getPosition()[0];
    int currentY = victim.getPosition()[1];
    HBox oneOnOne = searchClash(x, y);

    switch(answer) {
      case ESCAPE :
        escapeNow(currentX, currentY, x, y, oneOnOne); break;
      case COUNTERATTACK :
        counterattack(currentX, currentY, x, y, oneOnOne); break;
      default :
        if(currentX != x && currentY != y) escapeNow(currentX, currentY, x, y, oneOnOne);
        else counterattack(currentX, currentY, x, y, oneOnOne); break;
    }
  }

  /**
   * The method performs the following:
   * - Check that the victim cannot counterattack the attacker
   * - Check that the victim has sufficient speed to escape to the target position
   * - If the victim escapes successfully, then it switch the standard mode
   *
   * @param currentX is the row number of the victim, attacker or clash's current position
   * @param currentY is the column number of the victim, attacker or clash's current position 
   * @param oneOnOne is the temporary HBox that simulates the clash
   * Parameters x and y already are known
   */
  private void escapeNow(int currentX, int currentY, int x, int y, HBox oneOnOne) {
    // Thus the action is not allowed or is performed successfully, the answer field again a null
    answer = null;

    if(currentX == x && currentY == y) {
      battle.getMessage().setText("Recuerda, no puedes enfrentarlo, solo puedes escapar");
      return;
    }

    if(victim.escapeTo(x, y)) {
      battle.getMap().getChildren().remove(oneOnOne);
      battle.getMap().add(victim.getSkin(), y, x);
      battle.getMap().add(attacker.getSkin(), currentY, currentX);
      victim = attacker = null;
      clash = false;
      change();
    } else
      battle.getMessage().setText(playerCurrent + ", no tienes suficiente velocidad para escapar a ese lugar");
  }

  /** 
   * This method is funny because the attacker now becomes the victim. The method perfors the following:
   * - Check that the victim cannot escape
   * - Remove the temporary HBox that simulates the clash
   * - Check if the attacker can escape o simply die
   * - In Any case, it switch the standard mode
   *
   * The parameters already are known
   */
  private void counterattack(int currentX, int currentY, int x, int y, HBox oneOnOne) {
    // Thus the action is not allowed or is performed successfully, the answer field again a null
    answer = null;

    if(currentX != x && currentY != y) {
      battle.getMessage().setText("Recuerda, no puedes escapar, solo enfrentarlo");
      return;
    }

    battle.getMap().getChildren().remove(oneOnOne);

    change();
    if(attacker.getSpeed() < 2) {
      troopCurrent.remove(attacker);

      // We do not change the position of the victim because the victim does not move
      battle.getMap().add(victim.getSkin(), currentY, currentX);
      battle.getMessage().setText(playerCurrent + ", tu guerrero ha muerto, juega con estrategia, analiza el poder de tu contrincante");
    } else {
      // We can only create a random escape
      scapeRandom(currentX, currentY);
      battle.getMessage().setText(playerCurrent + ", tu guerrero ha escapado desesperadamente a donde mejor pudo, se conciente a quien atacas");

      // As the attacker has already escaped, then we make the shift change
      change();
    }
    
    clash = false;
    victim = attacker = null;
  }

  private void scapeRandom(int currentX, int currentY) {
    Random random = new Random();
    int x, y;

    while(true) {
      x = random.nextInt(10) + 1; 
      y = random.nextInt(10) + 1; 
 
      // Check that the target position is not a warrior and the attaker's speed
      // is sufficient for escape.
      if(searchSkin(x, y) == null && attacker.escapeTo(x, y)) {
        battle.getMap().add(attacker.getSkin(), y, x); 
        battle.getMap().add(victim.getSkin(), currentY, currentX);
        return;
      }
    }
  }

  /**
   * @return the temporary HBox that simulates the clash, null if not found
   */
  private HBox searchClash(int x, int y) {
    for(Node node : battle.getMap().getChildren())
      if(node instanceof HBox && GridPane.getRowIndex(node) == x && GridPane.getColumnIndex(node) == y)
        return (HBox) node;
    return null;
  }

  /**
   * This method clearly must be execute as a daemon
   * @return true if any troop is out of warriors
   */
  public boolean gameOver() {
    if(battle.getTroopBlue().isEmpty()) {
      playerCurrent = player2;
      return true;
    }

    if(battle.getTroopRed().isEmpty()) {
      playerCurrent = player1;
      return true;
    }

    return false;
  }

  public String getPlayerCurrent() { return playerCurrent; }

  public String getPlayer1() {return player1; }

  public String getPlayer2() {return player2; }
}
