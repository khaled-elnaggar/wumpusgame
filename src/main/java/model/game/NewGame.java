package model.game;

import model.gamemap.Cave;
import model.gamemap.GameMap;
import model.gameobject.hazard.Bat;
import model.gameobject.hazard.Pit;
import model.gameobject.Player;
import model.gameobject.hazard.Wumpus;
import utilities.RandomNumberGenerator;

import java.util.*;

import static java.util.stream.Collectors.toList;

public class NewGame implements Game {

    private GameMap gameMap;
    private final RandomNumberGenerator randomNumberGenerator;
    private Player player;
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
        wumpus = newGameInitializer.getWumpus();
        bats = newGameInitializer.getBats();
        pits = newGameInitializer.getPits();
    }

    @Override
    public void playerMovesToCave(int cave) {
        Cave caveToMoveTo = gameMap.getCaves().get(cave);
        player.move(caveToMoveTo);
    }

    @Override
    public void playerShootsToCave(int... caves) {
        List<Cave> cavesToShoot = validateCavesToShootAt(caves);
        player.shoot(cavesToShoot);

        if (!wumpus.isDead()) {
            wumpus.attemptToWakeup();
        }
    }

    private List<Cave> validateCavesToShootAt(int... caves) {
        List<Cave> cavesToShootAt = Arrays.stream(caves).mapToObj(cave -> gameMap.getCaves().get(cave)).collect(toList());
        List<Cave> validCavesToShootAt = new ArrayList<>();

        Cave arrowCurrentCave = player.getCave();

        for (int i = 0; i < caves.length; i++) {
            Cave arrowNextCave = cavesToShootAt.get(i);

            if (arrowCurrentCave.isLinkedTo(arrowNextCave)) {
                validCavesToShootAt.add(arrowNextCave);
            } else {
                validCavesToShootAt.add(arrowCurrentCave.getLinkedCaves().get(randomNumberGenerator.generateNumber(3)));
            }
            arrowCurrentCave = validCavesToShootAt.get(validCavesToShootAt.size() - 1);
        }
        return validCavesToShootAt;
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

    public int getEnemyPlayerCaveIndex() {
        return 0;
    }
}
