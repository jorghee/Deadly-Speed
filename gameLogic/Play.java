package gameLogic;

import field.*;

public interface Play<T extends Battle, U> {
  U game(T battle); 
}
