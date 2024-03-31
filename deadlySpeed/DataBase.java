package deadlySpeed;

import connection.ConnectionDB;

public class DataBase {
  private ConnectionDB connection;

  public DataBase() {
    connection = ConnectionDB.getInstance();
  } 

  public boolean getPlayer(String name, String id) {
    return connection.getPlayer(name, id);
  }

  public boolean createPlayer(String name, String id) {
    return connection.createPlayer(name, id);
  }

  public void saveGames(String winner, String key1, String key2) {
    connection.saveGame(winner, key1, key2);
  }
}
