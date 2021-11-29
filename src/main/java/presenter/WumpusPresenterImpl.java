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
