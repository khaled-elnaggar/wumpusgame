package model.game;

import java.util.List;

public interface Game {
     void startGame();
     void playerMovesToCave(int cave);
     int[] playerShootsToCave(int... cave);
     boolean isGameOver();
     int getNumberOfArrows();
     List<String> getWarnings();
     int getWumpusCaveIndex();
     int getPlayerCaveIndex();
     int getEnemyPlayerCaveIndex();
     boolean isWumpusDead();
     boolean hasPlayerWon();

     boolean isPlayerDead();

     boolean isGameLost();

    int[] getBatsCaves();

     int[] getPitsCaves();

    boolean isEnemyPlayerDead();

     int getEnemyRemainingArrows();
}
