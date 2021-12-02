package presenter;

import model.game.Game;
import model.game.NewGame;
import utilities.RandomNumberGenerator;

import java.util.List;


public class WumpusPresenterImpl implements WumpusPresenter {
    Game gameModel;

    public WumpusPresenterImpl(){
        this.gameModel=new NewGame();
    }

    public WumpusPresenterImpl(RandomNumberGenerator randomNumberGenerator){
        this.gameModel=new NewGame(randomNumberGenerator);
    }

    @Override
    public void startNewGame() {
        gameModel.startGame();
    }

    @Override
    public void move(int cave) {
        gameModel.playerMovesToCave(cave);
    }

    @Override
    public void shoot(int... cave) {
        gameModel.playerShootsToCave(cave);
    }


    @Override
    public int getWumpusCaveIndex() {
        return gameModel.getWumpusCaveIndex();
    }

    @Override
    public int getEnemyPlayerCave() {
        return gameModel.getEnemyPlayerCaveIndex();
    }

    @Override
    public boolean isWumpusDead() {
        return gameModel.isWumpusDead();
    }

    @Override
    public boolean hasPlayerWon() {
        return gameModel.hasPlayerWon();
    }

    @Override
    public boolean isPlayerDead() {
        return gameModel.isPlayerDead();
    }

    @Override
    public boolean isGameLost() {
        return gameModel.isGameLost();
    }

    @Override
    public int[] getBatsCaves() {
        return gameModel.getBatsCaves();
    }

    @Override
    public int[] getPitsCaves() {
        return gameModel.getPitsCaves();
    }

    @Override
    public boolean isEnemyPlayerDead() {
        return gameModel.isEnemyPlayerDead();
    }


    @Override
    public int getPlayerCaveIndex() {
        return gameModel.getPlayerCaveIndex();
    }

    @Override
    public boolean isGameOver() {
        return gameModel.isGameOver();
    }

    @Override
    public int getNumberOfArrows() {
        return gameModel.getNumberOfArrows();
    }

    @Override
    public List<String> getWarnings() {
        return gameModel.getWarnings();
    }

}
