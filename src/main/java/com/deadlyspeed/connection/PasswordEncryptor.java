package com.deadlyspeed.connection;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordEncryptor {
  public static String generateSalt() {
    byte[] salt = new byte[16];
    SecureRandom random = new SecureRandom();
    random.nextBytes(salt);
    return Base64.getEncoder().encodeToString(salt);
  }

  public static String getHashPassword(String password, String salt) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      digest.update(Base64.getDecoder().decode(salt));

      byte[] hashedBytes = digest.digest(password.getBytes());
      return Base64.getEncoder().encodeToString(hashedBytes);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
