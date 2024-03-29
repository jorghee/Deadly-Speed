CREATE TABLE Players (
  player_id VARCHAR(255) PRIMARY KEY,
  player_name VARCHAR(255) NOT NULL,
  total_wins INT DEFAULT 0
);

CREATE TABLE Games (
  game_id INT AUTO_INCREMENT PRIMARY KEY,
  player1_id VARCHAR(255),
  player2_id VARCHAR(255),
  winner_id VARCHAR(255), 
  game_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (player1_id) REFERENCES Players(player_id),
  FOREIGN KEY (player2_id) REFERENCES Players(player_id),
  FOREIGN KEY (winner_id) REFERENCES Players(player_id)
);
