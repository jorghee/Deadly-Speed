package com.deadlyspeed.connection;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
  /**
   * The static method that is responsible for load the properties from the confidential/database.properties 
   * file. This method is static because we do not need to create instances to may use it.
   * @return a list of key-value pairs that are the configurations of the connection to a data base.
   */
  public static Properties loadProperty(String propertiesUrl){
    try {
      Properties properties = new Properties();
      InputStream inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(propertiesUrl);
      properties.load(inputStream);
      return properties;
    } catch (Exception e) {
      // System.out.println("\nError loading the properties file\n");
      e.printStackTrace();
      return null;
    }
  }
}
