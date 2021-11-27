package model.game;

import java.util.List;

public interface Game {
     void startGame();
     void playerMovesToCave(int cave);
     void playerShootsToCave(int... cave);
     boolean isGameOver();
     int getNumberOfArrows();
     List<String> getWarnings();
     int getWumpusCaveIndex();
     int getPlayerCaveIndex();
}
