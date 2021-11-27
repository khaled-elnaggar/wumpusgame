package model.game;

import model.gamemap.Cave;
import model.gamemap.GameMap;
import model.gameobject.GameObject;
import model.gameobject.Player;
import model.gameobject.hazard.Bat;
import model.gameobject.hazard.Pit;
import model.gameobject.hazard.Wumpus;
import utilities.RandomNumberGenerator;

import java.util.ArrayList;
import java.util.function.Predicate;

public class NewGameInitializer {
    private Player player;
    private Wumpus wumpus;
    private final RandomNumberGenerator randomNumberGenerator;
    private ArrayList<Bat> bats;
    private ArrayList<Pit> pits;
    private final GameMap gameMap;

    public NewGameInitializer(RandomNumberGenerator randomNumberGenerator, GameMap gameMap){
        this.randomNumberGenerator = randomNumberGenerator;
        this.gameMap = gameMap;
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

    public Player getPlayer() {
        return player;
    }

    public Wumpus getWumpus() {
        return wumpus;
    }

    public ArrayList<Bat> getBats() {
        return bats;
    }

    public ArrayList<Pit> getPits() {
        return pits;
    }
}
