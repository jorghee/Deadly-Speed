package gameManager;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Main {
  private Stage stage;

  public static void main(String[] args) {
    launch(args);
  }

  public void start(Stage stage) {
    this.stage = stage;
      
    String file = "/music/cats.mp3";
    Media sound = new Media(DeadlySpeed.class.getResource(file).toString());
    MediaPlayer mediaPlayer = new MediaPlayer(sound);
    mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(ZERO));
    mediaPlayer.play();

    stage.setTitle("Deadly Speed");

    Lobby launch = Lobby.getInstance();
    launch.mainLobby(stage);

    stage.show();
  }
}
