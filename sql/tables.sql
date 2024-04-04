CREATE TABLE Players (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  salt VARCHAR(255) NOT NULL,
  victories INT DEFAULT 0,
  defeats INT DEFAULT 0
);

CREATE TABLE Games (
  id INT AUTO_INCREMENT PRIMARY KEY,
  player1_id INT,
  player2_id INT,
  winner_id INT, 
  game_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (player1_id) REFERENCES Players(id),
  FOREIGN KEY (player2_id) REFERENCES Players(id),
  FOREIGN KEY (winner_id) REFERENCES Players(id)
);

CREATE INDEX idx_name ON Players (name);
