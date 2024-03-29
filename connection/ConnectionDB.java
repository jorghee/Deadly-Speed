package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectionDB {
  /**
   * Date that will be used to perform the connection with a data base.
   * Data is into the file confindential/database.properties, when we wish to change the
   * data base, just is necessary going to this file.
   */
  private static final String DB_PROPERTIES = "confidential/database.properties";
  private static final String DB_HOST_PROP = "host";
  private static final String DB_NAME_PROP = "dbname";
  private static final String DB_PORT_PROP = "port";
  private static final String DB_USER_PROP = "user";
  private static final String DB_PASSWORD_PROP = "password";

  // We are implementing the Singleton Pattern Design
  private static ConnectionDB instance;

  // Field that will be used into the write methods of the deadlySpeed/Database.java class
  private Connection connection;

  private ConnectionDB() {
    connection = getConnection();
  }

  /**
   * @return the single instance of this class.
   */
  public static synchronized ConnectionDB getInstance() {
    if ( instance == null ) {
      instance = new ConnectionDB();
    }
    return instance;
  }

  private Connection getConnection() {
    try {
      String url = createUrl();
      Connection connection = DriverManager.getConnection( url );
      // System.out.println( "Connection class ==> " + connection.getClass().getName() );
      return connection;
    } catch( Exception e ) {
      e.printStackTrace();
      return null;
    }
  }
  
  private String createUrl() {
    Properties prop = PropertiesUtil.loadProperty( DB_PROPERTIES );
    String host = prop.getProperty( DB_HOST_PROP );
    String port = prop.getProperty( DB_PORT_PROP );
    String db = prop.getProperty( DB_NAME_PROP );
    String user = prop.getProperty( DB_USER_PROP );
    String password = prop.getProperty( DB_PASSWORD_PROP );

    String url = "jdbc:mariadb://" + host 
      + ":" + port + "/" + db + "?user=" + user + "&password=" + password;
    // System.out.println( "ConnectionString ==> " + url );
    return url;
  }

  /**
   * This method inserts a new player into the data base.
   * @param name is the name of the player
   * @param key is the password of the player
   * @return true if the insertion succeeds
   */
  public boolean createPlayer( String name, String key ) {
    if ( getPlayer( name, key ) ) {
      return false;
    }

    String query = "INSERT INTO Players ( player_id, player_name ) VALUES ( ?, ? )";
      
    try( PreparedStatement statement = connection.prepareStatement( query ) ) {
      statement.setString( 1, key );
      statement.setString( 2, name );
      statement.executeUpdate();
          
      System.out.println( "Jugador creado exitosamente." );
      return true;
    } catch( SQLException e ) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * This method checks the existence of a player.
   * @param name is the name of the player
   * @param key is the password of the player
   * @return true if such player exists
   */
  private boolean getPlayer( String name, String key ) {
    String query = "SELECT player_id FROM Players WHERE player_id = ? AND player_name = ?";

    try(  PreparedStatement statement = connection.prepareStatement( query )  ) {
      statement.setString( 1, key );
      statement.setString( 2, name );
          
      try ( ResultSet resultSet = statement.executeQuery() ) {
        // If the result contains at least one row with the same input date, returns true
        return resultSet.next();
      }
    } catch ( SQLException e ) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * This method saves the game and updates the number of games won by the winner
   * @param winner is the password of the winning player
   * @param key1 is the password of the player 1
   * @param key1 is the password of the player 2
   */
  public void saveGame( String winner, String key1, String key2 ) {
    String saveMatch = "INSERT INTO Matches ( player1_id, player2_id, winner_id ) VALUES ( ?, ?, ? )";
    String update = "UPDATE Players SET total_wins = total_wins + 1 WHERE player_id = ?";

    try( PreparedStatement save = connection.prepareStatement( saveMatch );
        PreparedStatement updateWin = connection.prepareStatement( update )) {
      save.setString( 1, key1 );
      save.setString( 2, key2 );
      save.setString( 3, winner );
      save.executeUpdate();

      updateWin.setString( 1, winner );
      updateWin.executeUpdate();

      System.out.println( "Partida guardada exitosamente." );
    } catch( SQLException e ) {
      e.printStackTrace();
      System.out.println( "Hubo un error al guardar la partida." );
    }
  }
}
