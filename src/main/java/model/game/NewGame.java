package model.game;

import model.gamemap.Cave;
import model.gamemap.GameMap;
import model.gameobject.hazard.Bat;
import model.gameobject.hazard.Pit;
import model.gameobject.Player;
import model.gameobject.hazard.Wumpus;
import utilities.RandomNumberGenerator;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

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
        Cave caveToMoveTo = gameMap.getCaves().get(cave);
        player.move(caveToMoveTo);
        enemyPlayerTakeAction();
    }

    @Override
    public void playerShootsToCave(int... caves) {
        List<Cave> cavesList = Arrays.stream(caves).mapToObj(cave -> gameMap.getCaves().get(cave)).collect(toList());
        List<Cave> cavesToShoot = validateCavesToShootAt(player.getCave(), cavesList);
        player.shoot(cavesToShoot);

        doWumpusPostShootActions();
    }

    private void doWumpusPostShootActions() {
        if (!wumpus.isDead()) {
            wumpus.attemptToWakeup();
        }
    }

    private List<Cave> validateCavesToShootAt(Cave arrowStartingCave, List<Cave> cavesToShootAt) {
        List<Cave> validCavesToShootAt = new ArrayList<>();

        Cave arrowCurrentCave = arrowStartingCave;
        for (Cave arrowNextCave : cavesToShootAt) {
            if (arrowCurrentCave.isLinkedTo(arrowNextCave)) {
                validCavesToShootAt.add(arrowNextCave);
            } else {
                validCavesToShootAt.add(arrowCurrentCave.getLinkedCaves().get(randomNumberGenerator.generateNumber(3)));
            }
            arrowCurrentCave = validCavesToShootAt.get(validCavesToShootAt.size() - 1);
        }
        return validCavesToShootAt;
    }

    public void enemyPlayerTakeAction() {
        final int fiftyPercentChance = randomNumberGenerator.generateNumber(GameInitialConfigurations.MAX_POSSIBILITY_ENEMY_PLAYER_TAKE_ACTION);
        if (fiftyPercentChance == 0) {
            Cave caveToMoveTo = enemyPlayer.getCave().getLinkedCaves().get(randomNumberGenerator.generateNumber(GameInitialConfigurations.NUMBER_OF_LINKED_CAVES));
            enemyPlayer.move(caveToMoveTo);
        } else if (fiftyPercentChance == 1) {
            final int numberOfCavesToShoot = 1 + randomNumberGenerator.generateNumber(GameInitialConfigurations.MAX_CAVES_ENEMY_PLAYER_CAN_SHOOT);

            List<Cave> litOfInvalidCaves = Stream.iterate(1, i -> i + 1)
                    .map(i -> new Cave(-1))
                    .limit(numberOfCavesToShoot)
                    .collect(toList());

            List<Cave> cavesToShoot = validateCavesToShootAt(enemyPlayer.getCave(), litOfInvalidCaves);
            enemyPlayer.shoot(cavesToShoot);

            doWumpusPostShootActions();
        }
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
        return player.getWarnings();
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
        return player.hasNoArrows() || player.isDead();
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
