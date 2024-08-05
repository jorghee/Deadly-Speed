package com.deadlyspeed.gameManager;

import com.deadlyspeed.field.RandomBattle;
import com.deadlyspeed.gameLogic.RandomPlay;
import com.deadlyspeed.connection.ConnectionDB;
import com.deadlyspeed.connection.Player;
import com.deadlyspeed.connection.StatePlayer;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import javafx.application.Platform;

public class Game {
  private Player playerBlue, playerRed;

  public Game(String playerBlue, String playerRed, Stage stage) {
    // We create the random game that we have implemented
    RandomPlay randomPlay = new RandomPlay(playerBlue, playerRed);
    RandomBattle randomBattle = new RandomBattle();
    Scene game = randomPlay.game(randomBattle);
    randomBattle.getExit().setOnAction(e -> stage.setScene(Lobby.getInstance(stage).getMainLobby()));
    game.getStylesheets().add("/styles/game.css");
    stage.setScene(game);

    // Thread to check if any list is empty, if this is the case, then there is a winning player
    Thread gameOver = new Thread(() -> {
      while (!randomPlay.gameOver()) {
        try {
          Thread.sleep(1000);     // default daemon timeout
        } catch(InterruptedException e) {
          e.printStackTrace();
        }
      }

      String winner = randomPlay.getPlayerCurrent();

      // We have to redirect to the specific thread of the GUI.
      Platform.runLater(() -> showVictory(winner, stage));
    });

    gameOver.setDaemon(true);
    gameOver.start();
  }

  public Game(Player playerBlue, Player playerRed, Stage stage) {
    this(playerBlue.getName(), playerRed.getName(), stage);
    this.playerBlue = playerBlue;
    this.playerRed = playerRed;
  }

  private void showVictory(String winner, Stage stage) {
    StackPane background = new StackPane();

    Text win = new Text("You won, " + winner + "!");

    Text isLeague = new Text("Venture into the champions league");
    isLeague.getStyleClass().add("report");

    if(playerBlue != null &&  playerRed != null)
      isLeague.setText(leagueVictory(winner));

    // Tenemos la opcion de jugar otra partida
    Button newGame = new Button("New game");
    newGame.setOnAction(e -> stage.setScene(Lobby.getInstance(stage).getMainLobby()));

    // Tambien podemos abandonar el juego
    Button quit = new Button("Exit game");
    quit.setOnAction(e -> stage.close());

    VBox vbox = new VBox();
    vbox.getChildren().addAll(win, isLeague, newGame, quit);
    vbox.setAlignment(Pos.CENTER);

    // Crear la escena de victoria
    background.getChildren().addAll(vbox);

    // Crear la escena de presentación
    Scene victory = new Scene(background, 1500, 1000);
    victory.getStylesheets().add("/styles/victory.css");
    stage.setScene(victory);
  }

  private String leagueVictory(String winner) {
    StatePlayer state = null;
    if(winner.equals(playerBlue.getName())) {
      state = ConnectionDB.getInstance().saveGame(playerBlue.getId(), playerRed.getId());
      if(state == StatePlayer.SUCCESS)
        return "The game was successfully saved";
      return "Unexpected error occurred while saving the game";
    }

    state = ConnectionDB.getInstance().saveGame(playerRed.getId(), playerBlue.getId());
    if(state == StatePlayer.SUCCESS)
      return "The game was successfully saved";
    return "Unexpected error occurred while saving the game";
  }
}
