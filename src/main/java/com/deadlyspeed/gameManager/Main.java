package com.deadlyspeed.gameManager;

import javafx.application.Application;
import javafx.stage.Stage;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Main extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  public void start(Stage stage) {
    String file = "/music/cats.mp3";
    Media sound = new Media(Main.class.getResource(file).toString());
    MediaPlayer mediaPlayer = new MediaPlayer(sound);
    mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
    mediaPlayer.play();

    stage.setTitle("Deadly Speed");

    Lobby launch = Lobby.getInstance();
    launch.mainLobby(stage);

    stage.show();
  }
}
