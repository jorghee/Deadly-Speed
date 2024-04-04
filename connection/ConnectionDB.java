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
   * @param password is the password of the player
   * @return true if the insertion succeeds
   */
  public boolean createPlayer( String name, String password ) {
    String salt = PasswordEncryptor.generateSalt(password);
    String hashPassword = PasswordEncryptor.getHashPassword(salt);
    if(hashPassword == null ) return false;

    String newPlayer = "INSERT INTO Players ( name, password, salt) VALUES ( ?, ?, ? )";
      
    try( PreparedStatement insertPlayer = connection.prepareStatement( newPlayer ) ) {
      insertPlayer.setString( 1, name );
      insertPlayer.setString( 2, hashPassword );
      insertPlayer.setString( 3, salt );
      insertPlayer.executeUpdate();
          
      return true;
    } catch( SQLException e ) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * This method checks the existence of a player.
   * @param name is the name of the player
   * @param password is the password of the player
   * @return true if such player exists
   */
  public boolean getPlayer( String name, String password ) {
    String query = "SELECT password FROM Players WHERE password = ? AND name = ?";

    try( PreparedStatement statement = connection.prepareStatement( query ) ) {
      statement.setString( 1, password );
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
   * @param winnerId is the id of the winning player
   * @param loserId is the id of the losing player
   */
  public boolean saveGame( int winnerId, int loserId ) {
    String saveGame = "INSERT INTO Games ( winnerId, loserId ) VALUES ( ?, ? )";
    String updVic = "UPDATE Players SET victories = victories + 1 WHERE id = ?";
    String updDef = "UPDATE Players SET defeats = defeats + 1 WHERE id = ?";

    try( PreparedStatement save = connection.prepareStatement( saveGame );
        PreparedStatement updateVic = connection.prepareStatement( updVic );
        PreparedStatement updateDef = connection.prepareStatement( updDef )) {
      save.setInt( 1, winnerId );
      save.setInt( 2, loserId );
      save.executeUpdate();

      updateVic.setInt( 1, winnerId );
      updateVic.executeUpdate();

      updateDef.setInt( 1, loserId );
      updateDef.executeUpdate();

      return true;
    } catch( SQLException e ) {
      e.printStackTrace();
      return false;
    }
  }
}
