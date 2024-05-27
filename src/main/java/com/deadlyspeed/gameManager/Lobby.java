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
    /* This section will be implemented in fxml 
    StackPane background = new StackPane();
    background.getStyleClass().add("root"); // Apply CSS style

    Text title = new Text("Deadly Speed");

    Button quick = new Button("Quick game");
    quick.setOnAction(e -> quickLogIn(stage));

    Button league = new Button("Champions League");
    league.setOnAction(e -> League.getInstance().getLeagueLobby(stage));

    Button quit  = new Button("Exit game");
    quit.setOnAction(e -> stage.close());

    // Container of the elements
    VBox launch = new VBox();
    launch.getChildren().addAll(title, quick, league, quit);
    launch.setAlignment(Pos.CENTER_RIGHT);

    // We add all the elements into the background
    background.getChildren().addAll(launch);

    // We create the scene and we add style
    mainLobby = new Scene(background, 1500, 1000);
    mainLobby.getStylesheets().add("/styles/lobby.css");
    stage.setScene(mainLobby);
    */
    // Load FXML
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Lobby.fxml"));
    // Show
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
