package model.game;

import model.gamemap.Cave;
import model.gamemap.GameMap;
import model.gameobject.hazard.Bat;
import model.gameobject.hazard.Pit;
import model.gameobject.Player;
import model.gameobject.hazard.Wumpus;
import utilities.RandomNumberGenerator;

import java.util.*;

public class NewGame implements Game {

    private GameMap gameMap;
    private final RandomNumberGenerator randomNumberGenerator;
    private Player player;
    private Player enemyPlayer;
    private Wumpus wumpus;
    private List<Bat> bats;
    private List<Pit> pits;

    public NewGame() {
        this.randomNumberGenerator = new RandomNumberGenerator();
    }

    public NewGame(RandomNumberGenerator randomNumberGenerator) {
        this.randomNumberGenerator = randomNumberGenerator;
    }

    @Override
    public void startGame() {
        gameMap = new GameMap(randomNumberGenerator);
        NewGameInitializer newGameInitializer = new NewGameInitializer(randomNumberGenerator, gameMap);
        player = newGameInitializer.getPlayer();
        enemyPlayer = newGameInitializer.getEnemyPlayer();
        wumpus = newGameInitializer.getWumpus();
        bats = newGameInitializer.getBats();
        pits = newGameInitializer.getPits();
    }

    @Override
    public void playerMovesToCave(int cave) {
        wumpus.clearMessages();
        Cave caveToMoveTo = gameMap.getCaves().get(cave);
        boolean successfulMove = player.move(caveToMoveTo);
        if (successfulMove) {
            doEnemyPlayerActions();
        }
    }

    @Override
    public int[] playerShootsToCave(int... caves) {
        List<Cave> cavesToShoot = this.gameMap.constructArrowCavesPath(player.getCave(), caves);
        player.shoot(cavesToShoot);

        doWumpusPostShootActions();
        doEnemyPlayerActions();
        return cavesToShoot.stream().mapToInt(Cave::getNumber).toArray();
    }

    private void doWumpusPostShootActions() {
        if (!isGameOver()) {
            wumpus.attemptToWakeup();
        }
    }

    public void doEnemyPlayerActions() {
        if (enemyPlayerShouldTakeNoAction()) {
            return;
        }

        final int fiftyPercentChance = randomNumberGenerator.generateNumber(GameInitialConfigurations.MAX_POSSIBILITY_ENEMY_PLAYER_TAKE_ACTION);
        if (fiftyPercentChance == GameInitialConfigurations.ENEMY_PLAYER_MOVE_NUMBER) {
            Cave caveToMoveTo = gameMap.getRandomCaveLinkedTo(enemyPlayer.getCave());
            enemyPlayer.move(caveToMoveTo);

        } else if (fiftyPercentChance == GameInitialConfigurations.ENEMY_PLAYER_SHOOT_NUMBER) {
            final int numberOfCavesToShoot = 1 + randomNumberGenerator.generateNumber(GameInitialConfigurations.MAX_CAVES_ENEMY_PLAYER_CAN_SHOOT);
            enemyPlayerShoot(numberOfCavesToShoot);
            doWumpusPostShootActions();
        }
    }

    private boolean enemyPlayerShouldTakeNoAction() {
        return isGameOver() || enemyPlayer.isDead() || enemyPlayer.hasNoArrows();
    }

    private void enemyPlayerShoot(int numberOfCavesToShoot) {
        int[] invalidCaves = new int[numberOfCavesToShoot];
        Arrays.fill(invalidCaves, -1);

        List<Cave> cavesToShoot = gameMap.constructArrowCavesPath(enemyPlayer.getCave(), invalidCaves);
        enemyPlayer.shoot(cavesToShoot);
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    @Override
    public boolean isGameOver() {
        return player.isDead() || player.hasNoArrows() || wumpus.isDead();
    }

    @Override
    public int getNumberOfArrows() {
        return player.getArrows().getNumber();
    }

    @Override
    public List<String> getWarnings() {
        List<String> messages =wumpus.getMessages();
        messages.addAll(player.getWarnings());
        return messages;
    }

    @Override
    public int getWumpusCaveIndex() {
        return wumpus.getCave().getNumber();
    }

    @Override
    public int getPlayerCaveIndex() {
        return player.getCave().getNumber();
    }

    @Override
    public int getEnemyPlayerCaveIndex() {
        return enemyPlayer.getCave().getNumber();
    }

    @Override
    public boolean isWumpusDead() {
        return wumpus.isDead();
    }

    @Override
    public boolean hasPlayerWon() {
        return wumpus.isDead();
    }

    @Override
    public boolean isPlayerDead() {
        return player.isDead();
    }

    @Override
    public boolean isGameLost() {
        return player.hasNoArrows() || player.isDead() || wumpus.wasKilledBy(enemyPlayer);
    }

    @Override
    public int[] getBatsCaves() {
        return bats.stream().mapToInt(bat -> bat.getCave().getNumber()).toArray();
    }

    @Override
    public int[] getPitsCaves() {
        return pits.stream().mapToInt(pit -> pit.getCave().getNumber()).toArray();
    }

    @Override
    public boolean isEnemyPlayerDead() {
        return enemyPlayer.isDead();
    }

    @Override
    public int getEnemyRemainingArrows() {
        return enemyPlayer.getArrows().getNumber();
    }

    public Player getPlayer() {
        return this.player;
    }

    public Wumpus getWumpus() {
        return this.wumpus;
    }

    public List<Bat> getBats() {
        return this.bats;
    }

    public List<Pit> getPits() {
        return this.pits;
    }
}
