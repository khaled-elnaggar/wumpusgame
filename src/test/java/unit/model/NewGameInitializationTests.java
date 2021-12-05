package unit.model;

import model.game.GameInitialConfigurations;
import model.game.NewGame;
import model.gamemap.Cave;
import model.gamemap.GameMap;
import model.gameobject.Player;
import model.gameobject.hazard.Bat;
import model.gameobject.hazard.Pit;
import model.gameobject.hazard.Wumpus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import unit.support.MockedRandomNumberGeneratorWorld;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NewGameInitializationTests {

    MockedRandomNumberGeneratorWorld mockedRNGWorld;

    @BeforeEach
    public void setUp() {
        mockedRNGWorld = new MockedRandomNumberGeneratorWorld();
    }

    @Test
    public void testGameMapInitializationProducedTheCorrectNumberOfCaves() {
        NewGame game = new NewGame();
        game.startGame();
        GameMap gameMap = game.getGameMap();

        assertEquals(GameInitialConfigurations.NUMBER_OF_CAVES, gameMap.getCaves().size());
    }

    @Test
    public void testThatGameMapInitializationProducedTheCorrectCaveLinks() {
        NewGame game = new NewGame();
        game.startGame();
        GameMap gameMap = game.getGameMap();
        final List<Cave> mapCaves = gameMap.getCaves();

        final int[] caveLinkIndexesToTest = new int[]{0, 9, 3, 4};

        for (int caveLinkIndexToTest : caveLinkIndexesToTest) {
            final Cave firstCave = mapCaves.get(
                    caveLinkIndexToTest);

            final int connectedCavesCount = 3;
            final List<Cave> actualLinkedCavesToFirstCave = firstCave.getLinkedCaves();
            assertEquals(connectedCavesCount, actualLinkedCavesToFirstCave.size());

            final int[] expectedLinkedCavesToFirstCave = GameInitialConfigurations.CAVE_LINKS[caveLinkIndexToTest];

            for (int caveLink : expectedLinkedCavesToFirstCave) {
                assertTrue(actualLinkedCavesToFirstCave.contains(new Cave(caveLink)));
            }
        }
    }

    @Test
    public void testThatPlayerIsAddedToInitialCave() {
        mockedRNGWorld.configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();

        NewGame game = mockedRNGWorld.getGame();

        final int actualPlayerCaveIndex = game.getPlayerCaveIndex();
        assertEquals(MockedRandomNumberGeneratorWorld.PLAYER_STARTING_CAVE_INDEX, actualPlayerCaveIndex);
    }

    @Test
    public void testThatPlayerCaveIsAddedToGameMap() {
        mockedRNGWorld.configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();

        NewGame game = mockedRNGWorld.getGame();

        final int actualPlayerCaveIndex = game.getPlayerCaveIndex();
        assertEquals(MockedRandomNumberGeneratorWorld.PLAYER_STARTING_CAVE_INDEX, actualPlayerCaveIndex);

        Cave playerCave = game.getGameMap().getCaves().get(MockedRandomNumberGeneratorWorld.PLAYER_STARTING_CAVE_INDEX);
        Player player = game.getPlayer();
        assertTrue(playerCave.containsAny(player));
    }

    @Test
    public void testThatWumpusIsAddedToCaveGameMap() {
        mockedRNGWorld.configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();

        NewGame game = mockedRNGWorld.getGame();

        final int actualWumpusCaveIndex = game.getWumpusCaveIndex();
        assertEquals(MockedRandomNumberGeneratorWorld.WUMPUS_STARTING_CAVE_INDEX, actualWumpusCaveIndex);

        Cave wumpusCave = game.getGameMap().getCaves().get(MockedRandomNumberGeneratorWorld.WUMPUS_STARTING_CAVE_INDEX);
        Wumpus wumpus = game.getWumpus();
        assertTrue(wumpusCave.containsAny(wumpus));

    }

    @Test
    public void testThatTwoBatsAreAddedToCaveGameMap() {
        mockedRNGWorld.configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();

        int[] batsStartingCavesIndexes = {MockedRandomNumberGeneratorWorld.FIRST_BAT_STARTING_CAVE_INDEX, MockedRandomNumberGeneratorWorld.SECOND_BAT_STARTING_CAVE_INDEX};

        NewGame game = mockedRNGWorld.getGame();

        List<Bat> listOfBats = game.getBats();

        assertEquals(GameInitialConfigurations.NUMBER_OF_BATS, listOfBats.size());

        for (int i = 0; i < listOfBats.size(); i++) {
            assertEquals(batsStartingCavesIndexes[i], listOfBats.get(i).getCave().getNumber());
            Cave batCave = game.getGameMap().getCaves().get(batsStartingCavesIndexes[i]);
            Bat bat = listOfBats.get(i);
            assertTrue(batCave.containsAny(bat));
        }
    }

    @Test
    public void testThatTwoPitsAreAddedToCaveGameMap() {
        mockedRNGWorld.configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();

        int[] pitsInCavesIndexes = {MockedRandomNumberGeneratorWorld.FIRST_PIT_CAVE, MockedRandomNumberGeneratorWorld.SECOND_PIT_CAVE};

        NewGame game = mockedRNGWorld.getGame();

        List<Pit> listOfPits = game.getPits();

        assertEquals(GameInitialConfigurations.NUMBER_OF_PITS, listOfPits.size());

        for (int i = 0; i < listOfPits.size(); i++) {
            assertEquals(pitsInCavesIndexes[i], listOfPits.get(i).getCave().getNumber());
            Cave PitInCave = game.getGameMap().getCaves().get(pitsInCavesIndexes[i]);
            Pit pit = listOfPits.get(i);
            assertTrue(PitInCave.containsAny(pit));
        }

    }

    @Test
    public void testTheInitialNumberOfAllCreatures() {
        mockedRNGWorld.configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();
        NewGame game = mockedRNGWorld.getGame();

        Map<String, Integer> creaturesAndTheirCount = new HashMap<>();
        String[] creatureNames = {Player.class.getSimpleName(), Wumpus.class.getSimpleName(), Pit.class.getSimpleName(), Bat.class.getSimpleName()};
        for (String creatureName : creatureNames) {
            creaturesAndTheirCount.put(creatureName, 0);
        }

        List<Cave> allCaves = game.getGameMap().getCaves();

        allCaves.stream()
                .flatMap(cave -> Stream.concat(cave.getPlayers().stream(), cave.getHazards().stream()))
                .map(gameObject -> gameObject.getClass().getSimpleName())
                .forEach(gameObjectClassName -> creaturesAndTheirCount.put(gameObjectClassName, creaturesAndTheirCount.get(gameObjectClassName) + 1));

        final int playersCount = 2;
        assertEquals(playersCount, creaturesAndTheirCount.get("Player"));

        final int wumpusCount = 1;
        assertEquals(wumpusCount, creaturesAndTheirCount.get("Wumpus"));

        final int batsCount = 2;
        assertEquals(batsCount, creaturesAndTheirCount.get("Bat"));

        final int pitsCount = 2;
        assertEquals(pitsCount, creaturesAndTheirCount.get("Pit"));
    }

    @Test
    public void testThatHazardsInASingleCaveAreSortedByPrecedence() {
        final int WUMPUS_STARTING_CAVE_INDEX = 18;
        final int FIRST_BAT_STARTING_CAVE_INDEX = 18;
        final int FIRST_PIT_CAVE = 18;

        Mockito.when(mockedRNGWorld.getRandomNumberGenerator().generateNumber(GameInitialConfigurations.NUMBER_OF_CAVES)).thenReturn(
                MockedRandomNumberGeneratorWorld.PLAYER_STARTING_CAVE_INDEX,
                MockedRandomNumberGeneratorWorld.ENEMY_PLAYER_STARTING_CAVE_INDEX,
                WUMPUS_STARTING_CAVE_INDEX,
                FIRST_BAT_STARTING_CAVE_INDEX,
                MockedRandomNumberGeneratorWorld.SECOND_BAT_STARTING_CAVE_INDEX,
                FIRST_PIT_CAVE,
                MockedRandomNumberGeneratorWorld.SECOND_PIT_CAVE
        );

        NewGame game = mockedRNGWorld.getGame();

        Cave cave = game.getGameMap().getCaves().get(WUMPUS_STARTING_CAVE_INDEX);

        assertTrue(cave.getHazards().get(0) instanceof Pit);
        assertTrue(cave.getHazards().get(1) instanceof Wumpus);
        assertTrue(cave.getHazards().get(2) instanceof Bat);

    }

    @Test
    public void testThatWumpusIsNotInitializedInSameCaveAsPlayer() {
        final int wumpusStartingWrongCaveIndex = 9;
        final int wumpusStartingCorrectCaveIndex = 17;

        Mockito.when(mockedRNGWorld.getRandomNumberGenerator().generateNumber(GameInitialConfigurations.NUMBER_OF_CAVES)).thenReturn(
                MockedRandomNumberGeneratorWorld.PLAYER_STARTING_CAVE_INDEX,
                MockedRandomNumberGeneratorWorld.ENEMY_PLAYER_STARTING_CAVE_INDEX,
                wumpusStartingWrongCaveIndex,
                wumpusStartingCorrectCaveIndex,
                MockedRandomNumberGeneratorWorld.FIRST_BAT_STARTING_CAVE_INDEX,
                MockedRandomNumberGeneratorWorld.SECOND_BAT_STARTING_CAVE_INDEX,
                MockedRandomNumberGeneratorWorld.FIRST_PIT_CAVE,
                MockedRandomNumberGeneratorWorld.SECOND_PIT_CAVE
        );

        NewGame game = mockedRNGWorld.getGame();

        final int actualWumpusCaveIndex = game.getWumpusCaveIndex();
        assertEquals(wumpusStartingCorrectCaveIndex, actualWumpusCaveIndex);

        Cave wumpusCave = game.getGameMap().getCaves().get(wumpusStartingCorrectCaveIndex);
        Wumpus wumpus = game.getWumpus();
        assertTrue(wumpusCave.containsAny(wumpus));
    }

    @Test
    public void testThatBatsAreNotInitializedAtSameLocation() {
        final int secondBatWrongStartingCaveIndex = 19;

        final int secondBatCorrectStartingCaveIndex = 13;
        int[] batsStartingCavesIndexes = {MockedRandomNumberGeneratorWorld.FIRST_BAT_STARTING_CAVE_INDEX, secondBatCorrectStartingCaveIndex};

        Mockito.when(mockedRNGWorld.getRandomNumberGenerator().generateNumber(GameInitialConfigurations.NUMBER_OF_CAVES)).thenReturn(
                MockedRandomNumberGeneratorWorld.PLAYER_STARTING_CAVE_INDEX,
                MockedRandomNumberGeneratorWorld.ENEMY_PLAYER_STARTING_CAVE_INDEX,
                MockedRandomNumberGeneratorWorld.WUMPUS_STARTING_CAVE_INDEX,
                MockedRandomNumberGeneratorWorld.FIRST_BAT_STARTING_CAVE_INDEX,
                secondBatWrongStartingCaveIndex,
                secondBatCorrectStartingCaveIndex,
                MockedRandomNumberGeneratorWorld.FIRST_PIT_CAVE,
                MockedRandomNumberGeneratorWorld.SECOND_PIT_CAVE
        );

        NewGame game = mockedRNGWorld.getGame();

        List<Bat> listOfBats = game.getBats();

        assertEquals(GameInitialConfigurations.NUMBER_OF_BATS, listOfBats.size());

        for (int i = 0; i < listOfBats.size(); i++) {
            assertEquals(batsStartingCavesIndexes[i], listOfBats.get(i).getCave().getNumber());
            Cave batCave = game.getGameMap().getCaves().get(batsStartingCavesIndexes[i]);
            Bat bat = listOfBats.get(i);
            assertTrue(batCave.containsAny(bat));
        }
    }

    @Test
    public void testThatPitsAreNotInitializedAtSameLocation() {
        final int secondWrongPitCave = 3;
        final int secondCorrectPitCave = 13;
        int[] correctPitsInCavesIndexes = {MockedRandomNumberGeneratorWorld.FIRST_PIT_CAVE, secondCorrectPitCave};

        Mockito.when(mockedRNGWorld.getRandomNumberGenerator().generateNumber(GameInitialConfigurations.NUMBER_OF_CAVES)).thenReturn(
                MockedRandomNumberGeneratorWorld.PLAYER_STARTING_CAVE_INDEX,
                MockedRandomNumberGeneratorWorld.ENEMY_PLAYER_STARTING_CAVE_INDEX,
                MockedRandomNumberGeneratorWorld.WUMPUS_STARTING_CAVE_INDEX,
                MockedRandomNumberGeneratorWorld.FIRST_BAT_STARTING_CAVE_INDEX,
                MockedRandomNumberGeneratorWorld.SECOND_BAT_STARTING_CAVE_INDEX,
                MockedRandomNumberGeneratorWorld.FIRST_PIT_CAVE,
                secondWrongPitCave,
                secondCorrectPitCave
        );

        NewGame game = mockedRNGWorld.getGame();

        List<Pit> listOfPits = game.getPits();

        assertEquals(GameInitialConfigurations.NUMBER_OF_PITS, listOfPits.size());

        for (int i = 0; i < listOfPits.size(); i++) {
            assertEquals(correctPitsInCavesIndexes[i], listOfPits.get(i).getCave().getNumber());
            Cave PitInCave = game.getGameMap().getCaves().get(correctPitsInCavesIndexes[i]);
            Pit pit = listOfPits.get(i);
            assertTrue(PitInCave.containsAny(pit));
        }

    }

    @Test
    public void testThatHazardsAreNotInitializedInTheCaveLinkedToThePlayer() {
        final int wumpusStartingWrongCaveIndex = 1;
        final int wumpusStartingCorrectCaveIndex = 17;

        final int firstBatStartingWrongCaveIndex = 10;
        final int firstBatStartingCorrectCaveIndex = MockedRandomNumberGeneratorWorld.FIRST_BAT_STARTING_CAVE_INDEX;

        final int firstPitStartingWrongCaveIndex = 8;
        final int firstPitStartingCorrectCaveIndex = MockedRandomNumberGeneratorWorld.FIRST_PIT_CAVE;

        Mockito.when(mockedRNGWorld.getRandomNumberGenerator().generateNumber(GameInitialConfigurations.NUMBER_OF_CAVES)).thenReturn(
                MockedRandomNumberGeneratorWorld.PLAYER_STARTING_CAVE_INDEX,
                MockedRandomNumberGeneratorWorld.ENEMY_PLAYER_STARTING_CAVE_INDEX,
                wumpusStartingWrongCaveIndex,
                wumpusStartingCorrectCaveIndex,
                firstBatStartingWrongCaveIndex,
                firstBatStartingCorrectCaveIndex,
                MockedRandomNumberGeneratorWorld.SECOND_BAT_STARTING_CAVE_INDEX,
                firstPitStartingWrongCaveIndex,
                firstPitStartingCorrectCaveIndex,
                MockedRandomNumberGeneratorWorld.SECOND_PIT_CAVE
        );

        NewGame game = mockedRNGWorld.getGame();

        final int actualWumpusCaveIndex = game.getWumpusCaveIndex();
        assertEquals(wumpusStartingCorrectCaveIndex, actualWumpusCaveIndex);

        Cave wumpusCave = game.getGameMap().getCaves().get(wumpusStartingCorrectCaveIndex);
        Wumpus wumpus = game.getWumpus();
        assertTrue(wumpusCave.containsAny(wumpus));

        final int actualFirstBatCaveIndex = game.getBats().get(0).getCave().getNumber();
        assertEquals(firstBatStartingCorrectCaveIndex, actualFirstBatCaveIndex);

        Cave firstBatCave = game.getGameMap().getCaves().get(firstBatStartingCorrectCaveIndex);
        Bat bat = game.getBats().get(0);
        assertTrue(firstBatCave.containsAny(bat));

        final int actualFirstPitCaveIndex = game.getPits().get(0).getCave().getNumber();
        assertEquals(firstPitStartingCorrectCaveIndex, actualFirstPitCaveIndex);

        Cave firstPitCave = game.getGameMap().getCaves().get(firstPitStartingCorrectCaveIndex);
        Pit pit = game.getPits().get(0);
        assertTrue(firstPitCave.containsAny(pit));
    }

}
