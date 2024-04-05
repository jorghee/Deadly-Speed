package gameManager;

import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class Lobby {
  private static Lobby instance;

  private Scene mainLobby;

  public static Lobby getInstance() {
    if(instance == null)
      return new Lobby();

    return instance;
  }

  public void mainLobby(Stage stage) {
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

  }

  private void quickLogIn(Stage stage) {
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

    String playerBlue = inputPlayerBlue.getText();
    String playerRed = inputPlayerRed.getText();
    start.setOnAction(e -> new Game(playerBlue, playerRed, stage));

    Button back = new Button("Back");
    GridPane.setConstraints(back, 0, 2);
    back.setOnAction(e -> stage.setScene(mainLobby));

    // Adding the elements to the GridPane
    grid.getChildren().addAll(troopBlue, inputPlayerBlue, troopRed, inputPlayerRed, back, start);
    Scene quickLogIn = new Scene(grid, 1500, 1000);
    quickLogIn.getStylesheets().add("/styles/login.css");
    stage.setScene(quickLogIn);
  }

  public Scene getMainLobby() { return mainLobby; }
}
