package gameLogic;

import randomTroop.TroopFactory;
import randomTroop.fighter.*;
import field.*;

import java.util.List;
import java.util.LinkedList;
import java.util.Random;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.collections.ObservableList;

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

  // Field controling the gain speed and confrontation mode
  private boolean clash;

  // Important field of the current Warrior
  private Fighter<ImageView> selWarrior, victim, attacker;

  // Fields controling the possible answer of the victim warrior
  private enum Answer {ATTACK, ESCAPE, BOTH};
  private Answer answer;

  public RandomPlay(String player1, String player2) {
    this.player1 = player1;
    this.player2 = player2;
  }

  /**
   * @param battle is the type of battle generation, in this case we have implemented a random generation.
   * @return the graphical representation of the battle. In this case is the Scene of the javafx
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
       * GridPane diveded by the legth of the cells.
       * y is the column number calculated couting the number of pixels on the x-axis of the 
       * GridPane diveded by the legth of the cells.
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
   */
  private void selectWarriorOf(int x, int y) {
    if (selWarrior != null)
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
   */
  private void moveSelWarriorTo(int x, int y) {
    if(selWarrior == null) {
      battle.getMessage().setText(playerCurrent + ", no has seleccionado a ningun guerrero");
      return;
    }

    ImageView skin = searchSkin(x, y);

    // Check the target position
    if(skin != null) {
      if(!selWarrior.attackTo(x, y)) {
        battle.getMessage().setText(playerCurrent + ", no tienes suficiente velocidad!");
        return;
      }

      battle.getMessage().setText(playerCurrent + ", estas atacando");

      // Switch to the victim troop to initialize the victim field and report the attack.
      change(); 
      victim = TroopFactory.getWarrior(troopCurrent, x, y);

      if(victim == null) {
        change();
        battle.getMessage().setText(playerCurrent + ", no puedes atacar a uno de tus guerreros, estas loco/a");
        return;
      }

      attacker = selWarrior;
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
   * This method was created with the objective of simulating the players' turn 
   * change. However, it ended up helping to facilitate the change of troops that is 
   * very required in the confrontation mode.
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
   * - If the victim is unable to escape or cope, dies
   * - If the victim and the attacker have the same probability of winning, then they both die
   * - If the above is not met, a confrontation is created in which the victim decides to 
   *   escape and/or confront
   */
  public void optionBeAttacked(int x, int y) {
    double win = victim.probabilityWinning(attacker);
    int speed = victim.getSpeed();

    // In any case the skins are removed
    battle.getMap().getChildren().remove(attacker.getSkin());
    battle.getMap().getChildren().remove(victim.getSkin());

    if(win < 50.00 && speed < 2) {
      troopCurrent.remove(victim);
      battle.getMessage().setText(playerCurrent + ", tu guerrero ha muerto luchando con honor hasta el final");
      battle.getMap().add(attacker.getSkin(), y, x);

      setSkins();
      return;
    }

    if(win == 50.00 && troopCurrent.remove(victim)) {
      troopCurrent.remove(victim);

      change();
      troopCurrent.remove(attacker);
      change();

      battle.getMessage().setText("Batalla epica, murieron con honor, mis respetos.");
      return;
    }

    // We create a temporary HBox that simulates the confrontation
    HBox oneOnOne = new HBox();
    oneOnOne.setAlignment(Pos.CENTER);
    oneOnOne.getChildren().addAll(victim.getSkin(), attacker.getSkin());
    battle.getMap().add(oneOnOne, y, x);

    // We switch to confrontation mode.
    clash = true;
  }
 
  // This method filters the skins and stores them in the skins field
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

  // ## CONFRONTATION MODE
  private void selectVictimOf(int x, int y) {
    HBox oneOnOne = searchClash(x, y);

    if(oneOnOne == null) {
      battle.getMessage().setText(playerCurrent + ", solo puedes mover a tu guerrero que esta siendo atacado");
      return;
    }
    
    double win = victim.probabilityWinning(attacker);
    int speed = victim.getSpeed();

    // Ojo que speed no puede ser menor que 2, pues no se ejecutaria este metodo
    // Entonces, el primer if se sabe que speed es mayor igual que 2
    if(win < 50.00) {
      // Puede solo escapar 
      battle.getMessage().setText("Solo puedes escapar lo mas lejos que puedas, ten cuidado");
      answer = Answer.ESCAPE;
    } else if(speed < 2) {
      // Puede solo atacar
      battle.getMessage().setText("Solo puedes enfrentarlo e incluso eliminarlo, sin miedo al exito");
      answer = Answer.ATTACK;
    } else {
      // puede atacar o escapar
      battle.getMessage().setText("Puedes enfrentarlo o escapar, toma la mejor desicion de tu vida");
      answer = Answer.BOTH;
    }
  }

  private void moveSelVictimTo(int x, int y) {
    int x1 = victim.getPosition()[0];
    int y1 = victim.getPosition()[1];
    HBox oneOnOne = searchClash(x, y);

    switch(answer) {
      case Answer.ESCAPE :
        escapeNow(x1, y1, x, y, oneOnOne); break;
      case Answer.ATTACK :
        instantAttak(x1, y1, x, y, oneOnOne); break;
      default:
        if(x1 != x && y1 != y)
          escapeNow(x1, y1, x, y, oneOnOne);
        else 
          instantAttak(x1, y1, x, y, oneOnOne);
        break;
    }
  }

  private void escapeNow(int x1, int y1, int x, int y, HBox oneOnOne) {
    if(x1 == x && y1 == y) {
      battle.getMessage().setText("Recuerda, no puede enfrentarlo, solo puedes escapar");
      return;
    }

    if(victim.escapeTo(x, y)) {
      battle.getMap().getChildren().remove(oneOnOne);
      battle.getMap().add(victim.getSkin(), y, x);
      battle.getMap().add(attacker.getSkin(), y1, x1);
      victim = attacker = null;
      answer = null;
      clash = false;
      change();
    } else
      battle.getMessage().setText(playerCurrent + ", no tienes suficiente velocidad para escapar ahi");
  }

  private void instantAttak(int x1, int y1, int x, int y, HBox oneOnOne) {
    if(x1 != x && y1 != y) {
      battle.getMessage().setText("Recuerda, no puedes escapar, solo enfrentarlo");
      return;
    }

    battle.getMap().getChildren().remove(oneOnOne);
    // Cambiamos a la tropa del atacante que ahora se convierte en vistima
    // OJO: Con este cambio ya estamos cambiando de turno, turno del atacador
    change();
    if(attacker.getSpeed() < 2) {
      troopCurrent.remove(attacker);
      // No hacemos el cambio de coordenas porque la victima no se mueve
      battle.getMap().add(victim.getSkin(), y, x);
      battle.getMessage().setText(playerCurrent + ", tu guerrero ha muerto, juega con estrategia, analiza el poder de tu contrincante");
    } else {
      // Haremos que escape a donde mejor pueda aleatoriamente
      scapeRandom(x1, y1);
      // Como ha escapado, entonces es turno de la otra tropa
      battle.getMessage().setText(playerCurrent + ", tu guerrero ha escapado desesperadamente a donde mejor pudo, se conciente a quien atacas");
      change();
    }
    
    clash = false;
    victim = attacker = null;
    answer = null;
  }

  private void scapeRandom(int x1, int y1) {
    Random random = new Random();
    int x, y;

    while(true) {
      x = random.nextInt(10) + 1; 
      y = random.nextInt(10) + 1; 
 
      if(searchSkin(x, y) == null && attacker.escapeTo(x, y)) {
        battle.getMap().add(attacker.getSkin(), y, x); 
        battle.getMap().add(victim.getSkin(), y1, x1);
        return;
      }
    }
  }

  private HBox searchClash(int x, int y) {
    for(Node node : battle.getMap().getChildren())
      if(node instanceof HBox && GridPane.getRowIndex(node) == x && GridPane.getColumnIndex(node) == y)
        return (HBox) node;
    return null;
  }

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
