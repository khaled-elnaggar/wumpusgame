package model.game;

import model.gamemap.Cave;
import model.gamemap.GameMap;
import model.gameobject.GameObject;
import model.gameobject.hazard.Bat;
import model.gameobject.hazard.Pit;
import model.gameobject.Player;
import model.gameobject.hazard.Wumpus;
import utilities.RandomNumberGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewGame implements Game {

    private GameMap gameMap;
    private final RandomNumberGenerator randomNumberGenerator;
    private Player player;
    private Wumpus wumpus;
    private List<Bat> bats;
    private List<Pit> pits;
    private final Map<String, List<? extends GameObject>> hazardsMap = new HashMap<>();

    public NewGame() {
        this.randomNumberGenerator = new RandomNumberGenerator();
    }

    public NewGame(RandomNumberGenerator randomNumberGenerator) {
        this.randomNumberGenerator = randomNumberGenerator;
    }

    @Override
    public void startGame() {
        buildGameMap();

        initializePlayer();
        initializeWumpus();
        initializeBats();
        initializePits();
    }

    private void initializePlayer() {
        player = new Player(GameInitialConfigurations.NUMBER_OF_ARROWS);
        player.setId(GameInitialConfigurations.PLAYER_ID);
        setGameObjectInitialCave(player);
    }

    private void initializeWumpus() {
        wumpus = new Wumpus(randomNumberGenerator);
        wumpus.setId(GameInitialConfigurations.WUMPUS_ID);
        setGameObjectInitialCave(wumpus);
        List<Wumpus> wumpusList = new ArrayList<>();
        wumpusList.add(wumpus);
        hazardsMap.put(Wumpus.class.getSimpleName(), wumpusList);
    }

    private void initializeBats() {
        bats = new ArrayList<>();
        for (int index = 0; index < GameInitialConfigurations.NUMBER_OF_BATS; index++) {
            Bat bat = new Bat(gameMap);
            bats.add(bat);
            bats.get(index).setId(GameInitialConfigurations.BAT_ID_PREFIX + index);
            setGameObjectInitialCave(bats.get(index));
            hazardsMap.put(Bat.class.getSimpleName(), bats);
        }
    }

    private void initializePits() {
        pits = new ArrayList<>();
        for (int index = 0; index < GameInitialConfigurations.NUMBER_OF_PITS; index++) {
            Pit pit = new Pit();
            pits.add(pit);
            pits.get(index).setId(GameInitialConfigurations.PITS_ID_PREFIX + index);
            setGameObjectInitialCave(pits.get(index));
            hazardsMap.put(Pit.class.getSimpleName(), pits);
        }
    }

    private void setGameObjectInitialCave(GameObject gameObject) {
        Cave cave = gameMap.getRandomCave();
        if (caveIsNotValidForGameObject(gameObject, cave)) {
            setGameObjectInitialCave(gameObject);
        } else {
            gameObject.setCave(cave);
            cave.addGameObject(gameObject);
        }
    }

    private boolean caveIsNotValidForGameObject(GameObject gameObject, Cave cave) {
        if (!isGameObjectInTheSameCaveAsPlayer(cave)) {
            return isHazardousGameObjectLocatedNearPlayersCave(cave) ||
                    isHazardousGameObjectLocatedInTheSameCaveAsItsLikes(gameObject, cave);
        }

        return true;
    }

    private boolean isGameObjectInTheSameCaveAsPlayer(Cave cave) {
        Cave playerCave = player.getCave();

        if (playerCave != null) {
            return cave.equals(playerCave);
        }

        return false;
    }

    private boolean isHazardousGameObjectLocatedNearPlayersCave(Cave cave) {
        List<Cave> linkedCaves = cave.getLinkedCaves();
        Cave playerCave = player.getCave();
        return linkedCaves.contains(playerCave);
    }

    private boolean isHazardousGameObjectLocatedInTheSameCaveAsItsLikes(GameObject gameObject, Cave cave) {
        return hazardsMap.entrySet().stream()
                .filter(entry -> gameObject.getClass().getSimpleName().equals(entry.getKey()))
                .flatMap(entrySet -> entrySet.getValue().stream())
                .map(GameObject::getCave)
                .anyMatch(cave::equals);
    }

    private void buildGameMap() {
        gameMap = new GameMap(randomNumberGenerator);
    }

    public GameMap getGameMap() {
        return this.gameMap;
    }

    @Override
    public void playerMovesToCave(int cave) {
        Cave caveToMoveTo = gameMap.getCaves().get(cave);
        player.move(caveToMoveTo);
    }

    @Override
    public void playerShootsToCave(int... caves) {
        for (int cave : caves) {
            Cave caveToShoot = gameMap.getCaves().get(cave);
            player.shoot(caveToShoot);
        }

        if (!wumpus.isDead()) {
            wumpus.attemptToWakeup();
        }
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
}
