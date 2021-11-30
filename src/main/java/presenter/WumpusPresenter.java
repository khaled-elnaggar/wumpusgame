package presenter;

import java.util.List;

public interface WumpusPresenter {
    void startNewGame();
    void move(int cave);
    int getPlayerCaveIndex();
    boolean isGameOver();
    void shoot(int... cave);
    int getNumberOfArrows();
    List<String> getWarnings();
    int getWumpusCaveIndex();
    int getEnemyPlayerCave();

    boolean isWumpusDead();

    boolean hasPlayerWon();

    boolean isPlayerDead();

    boolean isGameLost();

    int[] getBatsCaves();

    int[] getPitsCaves();
}
