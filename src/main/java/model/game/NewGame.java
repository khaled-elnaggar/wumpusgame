package model.game;

import model.gamemap.Cave;
import model.gamemap.GameMap;
import model.gameobject.GameObject;
import model.gameobject.hazard.Bat;
import model.gameobject.hazard.Pit;
import model.gameobject.Player;
import model.gameobject.hazard.Wumpus;
import utilities.RandomNumberGenerator;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
        buildGameMap();

        initializePlayer();
        initializeWumpus();
        initializeBats();
        initializePits();
    }

    private void initializePlayer() {
        player = new Player(GameInitialConfigurations.NUMBER_OF_ARROWS);
        player.setId(GameInitialConfigurations.PLAYER_ID);
        InitializeCaveAccordingToCondition(player, cave -> true);
    }

    private void initializeWumpus() {
        wumpus = new Wumpus(randomNumberGenerator);
        wumpus.setId(GameInitialConfigurations.WUMPUS_ID);

        final Predicate<Cave> cavePredicate = Cave::isFreeFromPlayerAndLinkedPlayer;
        InitializeCaveAccordingToCondition(wumpus, cavePredicate);
    }

    private void initializeBats() {
        bats = new ArrayList<>();
        for (int index = 0; index < GameInitialConfigurations.NUMBER_OF_BATS; index++) {
            Bat bat = new Bat(gameMap);
            bats.add(bat);
            bat.setId(GameInitialConfigurations.BAT_ID_PREFIX + index);
            final Predicate<Cave> cavePredicate = cave -> cave.isFreeFromPlayerAndLinkedPlayer() && !cave.containsAny(bats);
            InitializeCaveAccordingToCondition(bat, cavePredicate);
        }
    }

    private void initializePits() {
        pits = new ArrayList<>();
        for (int index = 0; index < GameInitialConfigurations.NUMBER_OF_PITS; index++) {
            Pit pit = new Pit();
            pits.add(pit);
            pit.setId(GameInitialConfigurations.PITS_ID_PREFIX + index);
            final Predicate<Cave> cavePredicate = cave -> cave.isFreeFromPlayerAndLinkedPlayer() && !cave.containsAny(pits);
            InitializeCaveAccordingToCondition(pit, cavePredicate);
        }
    }

    private void InitializeCaveAccordingToCondition(GameObject gameObject, Predicate<Cave> cavePredicate) {
        Cave batCave = gameMap.getACaveThatMeetsCondition(cavePredicate);
        batCave.addGameObject(gameObject);
        gameObject.setCave(batCave);
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
        List<Cave> cavesToShoot = validateCavesToShootAt(caves);
        player.shoot(cavesToShoot);

        if (!wumpus.isDead()) {
            wumpus.attemptToWakeup();
        }
    }

    private List<Cave> validateCavesToShootAt(int... caves) {
        List<Cave> cavesToShootAt = Arrays.stream(caves).mapToObj(cave -> gameMap.getCaves().get(cave)).collect(Collectors.toList());
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
