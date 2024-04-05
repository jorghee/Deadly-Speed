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
import javafx.scene.control.PasswordField;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import connection.ConnectionDB;
import connection.Player;
import connection.StatePlayer;

public class League {
  private static League instance;

  private Scene leagueLobby;
  
  public static League getInstance() {
    if(instance == null)
      return new League();

    return instance;
  }

  public void getLeagueLobby(Stage stage) {
    // Create GUI
    StackPane background = new StackPane();
    background.getStyleClass().add("root");

    Text title = new Text("Deadly Speed");

    Button logIn = new Button("Log in");
    logIn.setOnAction(e -> leagueLogIn(stage));

    Button signUp = new Button("Sign up");
    signUp.setOnAction(e -> leagueSignUp(stage));

    Button back = new Button("Back");
    back.setOnAction(e -> stage.setScene(Lobby.getInstance().getMainLobby()));

    // Container of the buttons
    VBox start = new VBox();
    start.getChildren().addAll(title, logIn, signUp, back);
    start.setAlignment(Pos.CENTER_RIGHT);

    // Adding all the elements
    background.getChildren().addAll(start);

    leagueLobby = new Scene(background, 1500, 1000);
    leagueLobby.getStylesheets().add("/styles/lobby.css");
    stage.setScene(leagueLobby);
  }
   
  private void leagueLogIn(Stage stage) {
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setPadding(new Insets(10, 10, 10, 10));
    grid.setVgap(20);
    grid.setHgap(20);

    // Sign in blue player
    Label troopBlue = new Label("Troop Blue");
    GridPane.setConstraints(troopBlue, 0, 0);
    troopBlue.getStyleClass().add("label");

    TextField inputPlayerBlue = new TextField();
    inputPlayerBlue.setPromptText("name");
    GridPane.setConstraints(inputPlayerBlue, 0, 1);
    inputPlayerBlue.getStyleClass().add("text-field");

    PasswordField inputPasswordBlue = new PasswordField();
    inputPasswordBlue.setPromptText("password");
    GridPane.setConstraints(inputPasswordBlue, 0, 2);
    inputPasswordBlue.getStyleClass().add("text-field");

    // Sign in red player
    Label troopRed = new Label("Troop Red");
    GridPane.setConstraints(troopRed, 1, 0);
    troopRed.getStyleClass().add("label");

    TextField inputPlayerRed = new TextField();
    inputPlayerRed.setPromptText("name");
    GridPane.setConstraints(inputPlayerRed, 1, 1);
    inputPlayerRed.getStyleClass().add("text-field");

    PasswordField inputPasswordRed = new PasswordField();
    inputPasswordRed.setPromptText("password");
    GridPane.setConstraints(inputPasswordRed, 1, 2);
    inputPlayerRed.getStyleClass().add("text-field");

    Text reportBlue = new Text("");
    GridPane.setConstraints(reportBlue, 0, 3);
    reportBlue.getStyleClass().add("text");

    Text reportRed = new Text("");
    GridPane.setConstraints(reportRed, 1, 3);
    reportRed.getStyleClass().add("text");

    // Start button
    Button start = new Button("Start game");
    GridPane.setConstraints(start, 1, 4);
    start.getStyleClass().add("button");

    start.setOnAction(e -> {
      // Aun falta mejorar esta logica, en proceso de creacion
      Player playerBlue = ConnectionDB.getInstance().getPlayer(inputPlayerBlue.getText(),
                                                              inputPasswordBlue.getText());
      Player playerRed = ConnectionDB.getInstance().getPlayer(inputPlayerRed.getText(),
                                                              inputPasswordRed.getText());

      switch(playerBlue.getStatePlayer()) {
        case StatePlayer.INCORRECT_PASSWORD :
          reportBlue.setText("Incorrect password"); break;
        case StatePlayer.PLAYER_NO_FOUND :
          reportBlue.setText("Name no found"); break;
        case StatePlayer.SERVER_ERROR :
          reportBlue.setText("Error server, try again"); break;
      }

      switch(playerRed.getStatePlayer()) {
        case StatePlayer.INCORRECT_PASSWORD :
          reportBlue.setText("Incorrect password"); break;
        case StatePlayer.PLAYER_NO_FOUND :
          reportBlue.setText("Name no found"); break;
        case StatePlayer.SERVER_ERROR :
          reportBlue.setText("Error server, try again"); break;
      }

      if(playerRed.getStatePlayer() == StatePlayer.SUCCESS && playerBlue.getStatePlayer() == StatePlayer.SUCCESS)
        new Game(playerBlue, playerRed, stage);

    });
 
    // Back button
    Button back = new Button("Back");
    GridPane.setConstraints(back, 0, 4);
    back.getStyleClass().add("button");
    back.setOnAction(e -> stage.setScene(leagueLobby));

   // Agregar elementos al grid
    grid.getChildren().addAll(troopBlue, inputPlayerBlue, inputPasswordBlue, troopRed,
                              inputPlayerRed, inputPasswordRed, reportBlue, reportRed,
                              start, back);
    Scene leagueLogIn = new Scene(grid, 1500, 1000);
    leagueLogIn.getStylesheets().add("/styles/login.css");
    stage.setScene(leagueLogIn);
  }

  private void leagueSignUp(Stage stage) {
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setPadding(new Insets(10, 10, 10, 10));
    grid.setVgap(20);
    grid.setHgap(20);

    // Sign up player
    Label date = new Label("Player:");
    GridPane.setConstraints(date, 0, 0);
    date.getStyleClass().add("label");

    TextField inputName = new TextField();
    inputName.setPromptText("name");
    GridPane.setConstraints(inputName, 0, 1);
    inputName.getStyleClass().add("text-field");

    PasswordField inputPassword = new PasswordField();
    inputPassword.setPromptText("password");
    GridPane.setConstraints(inputPassword, 0, 2);
    inputPassword.getStyleClass().add("text-field");

    Text report = new Text("");
    GridPane.setConstraints(report, 1, 3);
    report.getStyleClass().add("text");

    Button register = new Button("Join league");
    GridPane.setConstraints(register, 0, 4);
    register.getStyleClass().add("button");
  
    register.setOnAction(e -> {
      StatePlayer state = ConnectionDB.getInstance().createPlayer(inputName.getText(),
                                                            inputPassword.getText());
      switch(state) {
        case StatePlayer.SUCCESS :
          report.setText("Welcome the league"); break;
        case StatePlayer.PLAYER_EXISTS :
          report.setText("The name already exists"); break;
        default :
          report.setText("Error server, try again"); break;
      }
    });

    Button back = new Button("Back");
    GridPane.setConstraints(back, 0, 5);
    back.getStyleClass().add("button");
    back.setOnAction(e -> stage.setScene(leagueLobby));

    // Agregar elementos al grid
    grid.getChildren().addAll(date, inputName, inputPassword, report, register, back);
    Scene leagueSignUp = new Scene(grid, 1500, 1000);
    leagueSignUp.getStylesheets().add("/styles/login.css");
    stage.setScene(leagueSignUp);
  }
}
