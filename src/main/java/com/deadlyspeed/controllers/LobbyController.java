package com.deadlyspeed.controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import com.deadlyspeed.gameManager.League;
import com.deadlyspeed.gameManager.Game;
import com.deadlyspeed.gameManager.Lobby;



public class LobbyController {
  @FXML
  private StackPane root;

  @FXML
  public void getLeagueLobby() {
    Stage stage = (Stage) this.root.getScene().getWindow();
    League.getInstance().getLeagueLobby(stage);
  }

  @FXML
  public void quitGame(Event event) {
    Stage stage = (Stage) this.root.getScene().getWindow();
    stage.close();
  }

  @FXML
  private void quickLogIn() {
    // Get the stage
    Stage stage = (Stage) this.root.getScene().getWindow();
    // Crear el diseño del formulario
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setPadding(new Insets(10, 10, 10, 10));
    grid.setVgap(20);
    grid.setHgap(20);

    // Login playerBlue
    Label troopBlue = new Label("Troop Blue");
    GridPane.setConstraints(troopBlue, 0, 0);
    troopBlue.getStyleClass().add("label");

    TextField inputPlayerBlue = new TextField();
    inputPlayerBlue.setPromptText("Player 1");
    GridPane.setConstraints(inputPlayerBlue, 0, 1);
    inputPlayerBlue.getStyleClass().add("text-field");

    // Login playerRed
    Label troopRed = new Label("Troop Red:");
    GridPane.setConstraints(troopRed, 1, 0);
    troopRed.getStyleClass().add("label");

    TextField inputPlayerRed = new TextField();
    inputPlayerRed.setPromptText("Player 2");
    GridPane.setConstraints(inputPlayerRed, 1, 1);
    inputPlayerRed.getStyleClass().add("text-field");

    // Start Button
    Button start = new Button("Start game");
    GridPane.setConstraints(start, 1, 2);
    start.getStyleClass().add("button");

    start.setOnAction(e -> new Game(inputPlayerBlue.getText(), inputPlayerRed.getText(), stage));

    Button back = new Button("Back");
    GridPane.setConstraints(back, 0, 2);
    back.setOnAction(e -> stage.setScene(Lobby.getInstance(stage).getMainLobby()));

    // We add the elements to the GridPane
    grid.getChildren().addAll(troopBlue, inputPlayerBlue, troopRed, inputPlayerRed, back, start);
    Scene quickLogIn = new Scene(grid, 1500, 1000);
    quickLogIn.getStylesheets().add("/styles/login.css");
    stage.setScene(quickLogIn);
  }
}
