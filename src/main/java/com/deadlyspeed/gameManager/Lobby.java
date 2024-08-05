package com.deadlyspeed.gameManager;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import java.io.IOException;

public class Lobby {

  private static Lobby instance;

  private Scene mainLobby;

  public static Lobby getInstance() {
    if(instance == null)
      return new Lobby();

    return instance;
  }

  public void mainLobby(Stage stage) {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Lobby.fxml"));
    try {
      Parent root = loader.load();
      mainLobby = new Scene(root, 1500, 1000);
      stage.setScene(mainLobby);
    } catch(IOException e) {
      e.printStackTrace();
    }
  }

  public Scene getMainLobby() {
    return mainLobby;
  }
}
