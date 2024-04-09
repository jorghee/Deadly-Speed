package com.deadlyspeed.gameLogic;

import com.deadlyspeed.field.*;

public interface Play<T extends Battle, U> {
  U game(T battle); 
  boolean gameOver();
}
