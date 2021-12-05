package model;

import model.game.GameInitialConfigurations;
import model.game.NewGame;
import model.gamemap.Cave;
import model.gamemap.GameMap;
import model.gameobject.hazard.Bat;
import model.gameobject.hazard.Pit;
import model.gameobject.Player;
import model.gameobject.hazard.Wumpus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


@ExtendWith(MockitoExtension.class)
public class NewGameModelTests {
    MockedRandomNumberGeneratorWorld mockedRNGWorld;

    @BeforeEach
    public void setUp(){
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
    public void testThatThreeBatsAreAddedToCaveGameMap() {
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

    @Test
    public void testThatThePlayerCanMoveToALinkedCave() {
        mockedRNGWorld.configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();
        mockedRNGWorld.makeEnemyPlayerGoBackAndForth();

        NewGame game = mockedRNGWorld.getGame();

        final int caveIndexToMoveTo = 1;
        game.playerMovesToCave(caveIndexToMoveTo);

        final int actualPlayerCaveIndex = game.getPlayerCaveIndex();
        assertEquals(caveIndexToMoveTo, actualPlayerCaveIndex);

        Cave currentPlayerCave = game.getGameMap().getCaves().get(caveIndexToMoveTo);
        Player player = game.getPlayer();
        assertTrue(currentPlayerCave.containsAny(player));

        Cave pastPlayerCave = game.getGameMap().getCaves().get(MockedRandomNumberGeneratorWorld.PLAYER_STARTING_CAVE_INDEX);
        assertFalse(pastPlayerCave.containsAny(player));
    }

    @Test
    public void testMoveToNonConnectedCave() {
        mockedRNGWorld.configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();
        mockedRNGWorld.makeEnemyPlayerGoBackAndForth();

        NewGame game = mockedRNGWorld.getGame();

        final int caveIndexToMoveTo = 17;
        game.playerMovesToCave(caveIndexToMoveTo);

        final int actualPlayerCaveIndex = game.getPlayerCaveIndex();
        assertNotEquals(caveIndexToMoveTo, actualPlayerCaveIndex);
        assertEquals(MockedRandomNumberGeneratorWorld.PLAYER_STARTING_CAVE_INDEX, actualPlayerCaveIndex);

        Cave currentPlayerCave = game.getGameMap().getCaves().get(caveIndexToMoveTo);
        Player player = game.getPlayer();
        assertFalse(currentPlayerCave.containsAny(player));

        Cave pastPlayerCave = game.getGameMap().getCaves().get(MockedRandomNumberGeneratorWorld.PLAYER_STARTING_CAVE_INDEX);
        assertTrue(pastPlayerCave.containsAny(player));
    }

    @Test
    public void testMovingPlayerToCaveThatHasAWumups() {
        mockedRNGWorld.configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();
        mockedRNGWorld.makeEnemyPlayerGoBackAndForth();

        NewGame game = mockedRNGWorld.getGame();

        int[] journeyPath = new int[]{10, 18};

        for (int cave : journeyPath) {
            game.playerMovesToCave(cave);
        }


        final boolean actualGameState = game.isGameOver();
        final boolean gameIsOver = true;
        assertEquals(gameIsOver, actualGameState);

        List<String> messages = game.getWarnings();
        assertTrue(messages.contains(game.getWumpus().getWarningInTheSameCave()));
    }

    @Test
    public void testMovingPlayerToACaveNearAWumpusAndSensingTheWumpus() {
        mockedRNGWorld.configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();
        mockedRNGWorld.makeEnemyPlayerGoBackAndForth();

        NewGame game = mockedRNGWorld.getGame();

        int[] journeyPath = new int[]{10};

        for (int cave : journeyPath) {
            game.playerMovesToCave(cave);
        }


        final boolean actualGameState = game.isGameOver();
        final boolean gameIsNotOver = false;
        assertEquals(gameIsNotOver, actualGameState);

        List<String> messages = game.getWarnings();
        assertTrue(messages.contains(game.getWumpus().getWarningInTheLinkedCave()));
    }

    @Test
    public void testThatPlayerEnterRoomWithPit() {
        mockedRNGWorld.configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();
        mockedRNGWorld.makeEnemyPlayerGoBackAndForth();

        NewGame game = mockedRNGWorld.getGame();

        final int[] journeyPath = {10, 11, 2, 3};
        for (int caveNumber : journeyPath) {
            game.playerMovesToCave(caveNumber);
        }

        final boolean actualGameState = game.isGameOver();
        final boolean gameIsOver = true;
        assertEquals(actualGameState, gameIsOver);

        List<String> messages = game.getWarnings();
        assertTrue(messages.contains(game.getPits().get(0).getWarningInTheSameCave()));
    }

    @Test
    public void testKillingTheWumpus() {
        mockedRNGWorld.configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();
        mockedRNGWorld.makeEnemyPlayerGoBackAndForth();

        NewGame game = mockedRNGWorld.getGame();

        game.playerMovesToCave(10);

        final int caveToShootTo = MockedRandomNumberGeneratorWorld.WUMPUS_STARTING_CAVE_INDEX;
        game.playerShootsToCave(caveToShootTo);

        final boolean actualGameState = game.isGameOver();
        final boolean gameIsOver = true;
        assertEquals(actualGameState, gameIsOver);
    }

    @Test
    public void testThatPlayerShootsAnArrowThatMissesTheWumpusAndWumpusRemainsSleeping() {
        mockedRNGWorld.configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();
        mockedRNGWorld.makeEnemyPlayerGoBackAndForth();

        mockedRNGWorld.makeWumpusSleep();

        NewGame game = mockedRNGWorld.getGame();
        game.playerMovesToCave(10);

        final int caveToShootTo = 11;
        game.playerShootsToCave(caveToShootTo);

        final int wumpusCaveLocation = game.getWumpusCaveIndex();
        assertEquals(wumpusCaveLocation, MockedRandomNumberGeneratorWorld.WUMPUS_STARTING_CAVE_INDEX);

        final boolean actualGameState = game.isGameOver();
        final boolean gameIsNotOver = false;
        assertEquals(actualGameState, gameIsNotOver);
    }

    @Test
    public void testThatPlayerShootsAnArrowThatMissesTheWumpusAndWumpusMoves() {
        mockedRNGWorld.configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();
        mockedRNGWorld.makeEnemyPlayerGoBackAndForth();
        mockedRNGWorld.makeWumpusWakeUp();

        final int numberOfLinkedCaves = 3;
        final int wumpusLinkedCaveIndex = 2;
        Mockito.when(mockedRNGWorld.getRandomNumberGenerator().generateNumber(numberOfLinkedCaves)).thenReturn(
                wumpusLinkedCaveIndex, MockedRandomNumberGeneratorWorld.ENEMY_PLAYER_MIDDLE_CAVE_INDEX);

        NewGame game = mockedRNGWorld.getGame();

        final int caveToShootTo = 1;
        game.playerShootsToCave(caveToShootTo);

        final int actualWumpusCaveIndex = game.getWumpusCaveIndex();
        final int expectedWumpusCaveIndex = 17;
        assertEquals(expectedWumpusCaveIndex, actualWumpusCaveIndex);

        final Cave initialWumpusCave = game.getGameMap().getCaves().get(MockedRandomNumberGeneratorWorld.WUMPUS_STARTING_CAVE_INDEX);
        assertFalse(initialWumpusCave.containsAny(game.getWumpus()));

        final boolean actualGameState = game.isGameOver();
        final boolean gameIsNotOver = false;
        assertEquals(actualGameState, gameIsNotOver);
    }

    @Test
    public void testThatPlayerRunsOutOfArrowsWithoutKillingWumpus() {
        mockedRNGWorld.configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();

        mockedRNGWorld.makeWumpusSleep();

        mockedRNGWorld.makeEnemyPlayerGoBackAndForth();

        NewGame game = mockedRNGWorld.getGame();

        final int caveToShoot = 1;
        for (int i = 0; i < GameInitialConfigurations.NUMBER_OF_ARROWS; i++) {
            game.playerShootsToCave(caveToShoot);
        }

        final boolean actualGameState = game.isGameOver();
        final boolean gameIsOver = true;
        assertEquals(actualGameState, gameIsOver);
    }

    @Test
    public void testThatPlayerShootsAnArrowMissesWumpusAndWumpusWakesUpAndMoveToEatThePlayer() {
        mockedRNGWorld.configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();
        mockedRNGWorld.makeEnemyPlayerMove();

        mockedRNGWorld.makeWumpusWakeUp();

        final int numberOfLinkedCaves = 3;
        final int wumpusLinkedCaveIndex = 1;
        Mockito.when(mockedRNGWorld.getRandomNumberGenerator().generateNumber(numberOfLinkedCaves)).thenReturn(
                wumpusLinkedCaveIndex,
                MockedRandomNumberGeneratorWorld.ENEMY_PLAYER_MIDDLE_CAVE_INDEX);

        NewGame game = mockedRNGWorld.getGame();

        game.playerMovesToCave(10);


        final int caveToShootTo = 11;
        game.playerShootsToCave(caveToShootTo);

        final int actualWumpusCaveIndex = game.getWumpusCaveIndex();
        final int expectedWumpusCaveIndex = 10;
        assertEquals(expectedWumpusCaveIndex, actualWumpusCaveIndex);

        final Cave initialWumpusCave = game.getGameMap().getCaves().get(MockedRandomNumberGeneratorWorld.WUMPUS_STARTING_CAVE_INDEX);
        assertFalse(initialWumpusCave.containsAny(game.getWumpus()));

        final boolean actualGameState = game.isGameOver();
        final boolean gameIsOver = true;
        assertEquals(actualGameState, gameIsOver);
    }

    @Test
    public void testThatPlayerEnterRoomWithBatTwice() {
        mockedRNGWorld.makeEnemyPlayerGoBackAndForth();
        final int enemyPlayerStartingCave = 9;

        final int playerFirstDropDownCaveIndex = 15;
        final int firstBatFinalCaveIndex = 2;

        final int playerSecondDropDownCaveIndex = 6;
        final int secondBatFinalCaveIndex = 0;

        final int secondBatStartingCaveIndex = 14;
        Mockito.when(mockedRNGWorld.getRandomNumberGenerator().generateNumber(GameInitialConfigurations.NUMBER_OF_CAVES)).thenReturn(
                MockedRandomNumberGeneratorWorld.PLAYER_STARTING_CAVE_INDEX,
                enemyPlayerStartingCave,
                MockedRandomNumberGeneratorWorld.WUMPUS_STARTING_CAVE_INDEX,
                MockedRandomNumberGeneratorWorld.FIRST_BAT_STARTING_CAVE_INDEX,
                secondBatStartingCaveIndex,
                MockedRandomNumberGeneratorWorld.FIRST_PIT_CAVE,
                MockedRandomNumberGeneratorWorld.SECOND_PIT_CAVE,
                playerFirstDropDownCaveIndex,
                firstBatFinalCaveIndex,
                playerSecondDropDownCaveIndex,
                secondBatFinalCaveIndex
        );


        NewGame game = mockedRNGWorld.getGame();

        final int[] journeyPath = {10, 11, 12, MockedRandomNumberGeneratorWorld.FIRST_BAT_STARTING_CAVE_INDEX};
        for (int cave : journeyPath) {
            game.playerMovesToCave(cave);
        }

        final int actualPlayerCaveIndex = game.getPlayerCaveIndex();
        assertEquals(playerFirstDropDownCaveIndex, actualPlayerCaveIndex);


        final Bat firstBat = game.getBats().get(0);
        final Cave firstBatActualCave = firstBat.getCave();
        assertEquals(firstBatFinalCaveIndex, firstBatActualCave.getNumber());

        final Cave previousCave = game.getGameMap().getCaves().get(MockedRandomNumberGeneratorWorld.FIRST_BAT_STARTING_CAVE_INDEX);
        assertFalse(previousCave.containsAny(game.getPlayer()));
        assertFalse(previousCave.containsAny(firstBat));

        game.playerMovesToCave(secondBatStartingCaveIndex);

        final int actualPlayerSecondCaveIndex = game.getPlayerCaveIndex();
        assertEquals(playerSecondDropDownCaveIndex, actualPlayerSecondCaveIndex);


        final Bat secondBat = game.getBats().get(1);
        final Cave secondBatActualCave = secondBat.getCave();
        assertEquals(secondBatFinalCaveIndex, secondBatActualCave.getNumber());

        final Cave secondBatPreviousCave = game.getGameMap().getCaves().get(secondBatStartingCaveIndex);
        assertFalse(secondBatPreviousCave.containsAny(game.getPlayer()));
        assertFalse(secondBatPreviousCave.containsAny(secondBat));

    }

    @Test
    public void testThatPlayerEnterRoomWithBat() {
        mockedRNGWorld.makeEnemyPlayerGoBackAndForth();
        final int playerDropDownCaveIndex = 8;
        final int firstBatFinalCaveIndex = 5;

        Mockito.when(mockedRNGWorld.getRandomNumberGenerator().generateNumber(GameInitialConfigurations.NUMBER_OF_CAVES)).thenReturn(
                MockedRandomNumberGeneratorWorld.PLAYER_STARTING_CAVE_INDEX,
                MockedRandomNumberGeneratorWorld.ENEMY_PLAYER_STARTING_CAVE_INDEX,
                MockedRandomNumberGeneratorWorld.WUMPUS_STARTING_CAVE_INDEX,
                MockedRandomNumberGeneratorWorld.FIRST_BAT_STARTING_CAVE_INDEX,
                MockedRandomNumberGeneratorWorld.SECOND_BAT_STARTING_CAVE_INDEX,
                MockedRandomNumberGeneratorWorld.FIRST_PIT_CAVE,
                MockedRandomNumberGeneratorWorld.SECOND_PIT_CAVE,
                playerDropDownCaveIndex,
                firstBatFinalCaveIndex
        );


        NewGame game = mockedRNGWorld.getGame();

        final int[] journeyPath = {10, 11, 12, MockedRandomNumberGeneratorWorld.FIRST_BAT_STARTING_CAVE_INDEX};
        for (int cave : journeyPath) {
            game.playerMovesToCave(cave);
        }

        final int actualPlayerCaveIndex = game.getPlayerCaveIndex();
        assertEquals(playerDropDownCaveIndex, actualPlayerCaveIndex);


        final Bat firstBat = game.getBats().get(0);
        final Cave firstBatActualCave = firstBat.getCave();
        assertEquals(firstBatFinalCaveIndex, firstBatActualCave.getNumber());

        final Cave previousCave = game.getGameMap().getCaves().get(MockedRandomNumberGeneratorWorld.FIRST_BAT_STARTING_CAVE_INDEX);
        assertFalse(previousCave.containsAny(game.getPlayer()));
        assertFalse(previousCave.containsAny(firstBat));
    }

    @Test
    public void testThatPlayerEnterRoomWithPitAndBat() {
        mockedRNGWorld.makeEnemyPlayerGoBackAndForth();
        mockedRNGWorld.configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();

        NewGame game = mockedRNGWorld.getGame();

        final int[] journeyPath = {10, 11, 12, 13};
        for (int caveNumber : journeyPath) {
            game.playerMovesToCave(caveNumber);
        }

        final boolean actualGameState = game.isGameOver();
        final boolean gameIsOver = true;
        assertEquals(actualGameState, gameIsOver);
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
    public void testThatPlayerShootsMultipleLinkedCavesWithOneArrow() {
        final int playerStartingCave = 0;
        Mockito.when(mockedRNGWorld.getRandomNumberGenerator().generateNumber(GameInitialConfigurations.NUMBER_OF_CAVES)).thenReturn(
                playerStartingCave,
                MockedRandomNumberGeneratorWorld.ENEMY_PLAYER_STARTING_CAVE_INDEX,
                MockedRandomNumberGeneratorWorld.WUMPUS_STARTING_CAVE_INDEX,
                MockedRandomNumberGeneratorWorld.FIRST_BAT_STARTING_CAVE_INDEX,
                MockedRandomNumberGeneratorWorld.SECOND_BAT_STARTING_CAVE_INDEX,
                MockedRandomNumberGeneratorWorld.FIRST_PIT_CAVE,
                MockedRandomNumberGeneratorWorld.SECOND_PIT_CAVE
        );
        NewGame game = mockedRNGWorld.getGame();

        int[] cavesToShootAt = new int[]{7, 8, 9, 10, 18};
        game.playerShootsToCave(cavesToShootAt);

        assertTrue(game.getWumpus().isDead());

        final int expectedRemainingArrows = GameInitialConfigurations.NUMBER_OF_ARROWS - 1;
        assertEquals(expectedRemainingArrows, game.getNumberOfArrows());
    }

    @Test
    public void testThatPlayerShootsMultipleNonLinkedCavesWithOneArrow() {
        mockedRNGWorld.configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();
        NewGame game = mockedRNGWorld.getGame();

        int[] cavesToShootAt = new int[]{15, 2, 11, 10, 3};
        final int cave1IndexFrom0 = 1;
        final int cave18IndexFrom10 = 1;
        Mockito.when(mockedRNGWorld.getRandomNumberGenerator().generateNumber(GameInitialConfigurations.NUMBER_OF_LINKED_CAVES)).thenReturn(
                cave1IndexFrom0,
                cave18IndexFrom10);

        game.playerShootsToCave(cavesToShootAt);

        assertTrue(game.getWumpus().isDead());
    }

    @Test
    public void testThatPlayerShootsMultipleNonLinkedCavesWithOneArrowAndDies() {
        mockedRNGWorld.configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();
        NewGame game = mockedRNGWorld.getGame();

        int[] cavesToShootAt = new int[]{1, 15};
        final int cave9IndexFrom1 = 1;
        Mockito.when(mockedRNGWorld.getRandomNumberGenerator().generateNumber(GameInitialConfigurations.NUMBER_OF_LINKED_CAVES)).thenReturn(
                cave9IndexFrom1);

        game.playerShootsToCave(cavesToShootAt);

        assertTrue(game.getPlayer().isDead());
    }

    @Test
    public void testThatEnemyPlayerIsInitialized() {
        mockedRNGWorld.configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();

        NewGame game = mockedRNGWorld.getGame();

        final int actualEnemyPlayerLocation = game.getEnemyPlayerCaveIndex();
        assertEquals(MockedRandomNumberGeneratorWorld.ENEMY_PLAYER_STARTING_CAVE_INDEX, actualEnemyPlayerLocation);
    }

    @Test
    public void testThatEnemyPlayerMovesAfterPlayerMoves() {
        mockedRNGWorld.configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();

        mockedRNGWorld.makeEnemyPlayerMove();

        final int caveIndexToMoveTo = 1;
        Mockito.when(mockedRNGWorld.getRandomNumberGenerator().generateNumber(GameInitialConfigurations.NUMBER_OF_LINKED_CAVES)).thenReturn(
                caveIndexToMoveTo);

        NewGame game = mockedRNGWorld.getGame();


        final int caveToMoveTo = 1;
        game.playerMovesToCave(caveToMoveTo);

        final int expectedEnemyPlayerCave = GameInitialConfigurations.CAVE_LINKS[MockedRandomNumberGeneratorWorld.ENEMY_PLAYER_STARTING_CAVE_INDEX][caveIndexToMoveTo];

        assertEquals(expectedEnemyPlayerCave, game.getEnemyPlayerCaveIndex());
    }

    @Test
    public void testThatEnemyPlayerMovesAfterPlayerShoots() {
        mockedRNGWorld.configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();

        mockedRNGWorld.makeEnemyPlayerMove();

        final int caveIndexToMoveTo = 1;
        Mockito.when(mockedRNGWorld.getRandomNumberGenerator().generateNumber(GameInitialConfigurations.NUMBER_OF_LINKED_CAVES)).thenReturn(
                caveIndexToMoveTo);

        mockedRNGWorld.makeWumpusSleep();

        NewGame game = mockedRNGWorld.getGame();

        final int caveToShootAt = 1;
        game.playerShootsToCave(caveToShootAt);

        final int expectedEnemyPlayerCave = GameInitialConfigurations.CAVE_LINKS[MockedRandomNumberGeneratorWorld.ENEMY_PLAYER_STARTING_CAVE_INDEX][caveIndexToMoveTo];

        final int actualEnemyPlayerCave = game.getEnemyPlayerCaveIndex();
        assertEquals(expectedEnemyPlayerCave, actualEnemyPlayerCave);
    }

    @Test
    public void testThatEnemyPlayerMoveToCaveWithWumpusAndDies() {
        mockedRNGWorld.configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();

        mockedRNGWorld.makeEnemyPlayerMove();

        final int firstCaveIndexToMoveTo = 0;
        final int secondCaveIndexToMoveTo = 1;
        Mockito.when(mockedRNGWorld.getRandomNumberGenerator().generateNumber(GameInitialConfigurations.NUMBER_OF_LINKED_CAVES)).thenReturn(
                firstCaveIndexToMoveTo,
                secondCaveIndexToMoveTo);

        NewGame game = mockedRNGWorld.getGame();


        final int[] journeyPath = new int[]{1, 0};
        for (int cave : journeyPath) {
            game.playerMovesToCave(cave);
        }

        final int expectedEnemyPlayerCave = MockedRandomNumberGeneratorWorld.WUMPUS_STARTING_CAVE_INDEX;

        assertEquals(expectedEnemyPlayerCave, game.getEnemyPlayerCaveIndex());

        assertTrue(game.isEnemyPlayerDead());
    }

    @Test
    public void testThatEnemyPlayerMoveToCaveWithBatAndTeleports() {
        final int enemyPlayerDropDownCave = 8;
        final int batDropDownCave = 4;
        Mockito.when(mockedRNGWorld.getRandomNumberGenerator().generateNumber(GameInitialConfigurations.NUMBER_OF_CAVES)).thenReturn(
                MockedRandomNumberGeneratorWorld.PLAYER_STARTING_CAVE_INDEX,
                MockedRandomNumberGeneratorWorld.ENEMY_PLAYER_STARTING_CAVE_INDEX,
                MockedRandomNumberGeneratorWorld.WUMPUS_STARTING_CAVE_INDEX,
                MockedRandomNumberGeneratorWorld.FIRST_BAT_STARTING_CAVE_INDEX,
                MockedRandomNumberGeneratorWorld.SECOND_BAT_STARTING_CAVE_INDEX,
                MockedRandomNumberGeneratorWorld.FIRST_PIT_CAVE,
                MockedRandomNumberGeneratorWorld.SECOND_PIT_CAVE,
                enemyPlayerDropDownCave,
                batDropDownCave);

        mockedRNGWorld.makeEnemyPlayerMove();

        final int firstCaveIndexToMoveTo = 2;
        final int secondCaveIndexToMoveTo = 1;
        Mockito.when(mockedRNGWorld.getRandomNumberGenerator().generateNumber(GameInitialConfigurations.NUMBER_OF_LINKED_CAVES)).thenReturn(
                firstCaveIndexToMoveTo,
                secondCaveIndexToMoveTo);

        NewGame game = mockedRNGWorld.getGame();

        final int[] journeyPath = new int[]{1, 0};
        for (int cave : journeyPath) {
            game.playerMovesToCave(cave);
        }

        assertEquals(enemyPlayerDropDownCave, game.getEnemyPlayerCaveIndex());
    }


    @Test
    public void testThatEnemyPlayerMoveToCaveWithPitAndDies() {
        mockedRNGWorld.configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();

        mockedRNGWorld.makeEnemyPlayerMove();

        final int firstCaveIndexToMoveTo = 1;
        final int secondCaveIndexToMoveTo = 2;
        Mockito.when(mockedRNGWorld.getRandomNumberGenerator().generateNumber(GameInitialConfigurations.NUMBER_OF_LINKED_CAVES)).thenReturn(
                firstCaveIndexToMoveTo,
                secondCaveIndexToMoveTo);

        NewGame game = mockedRNGWorld.getGame();


        final int[] journeyPath = new int[]{1, 0};
        for (int cave : journeyPath) {
            game.playerMovesToCave(cave);
        }

        final int expectedEnemyPlayerCave = MockedRandomNumberGeneratorWorld.FIRST_PIT_CAVE;

        assertEquals(expectedEnemyPlayerCave, game.getEnemyPlayerCaveIndex());

        assertTrue(game.isEnemyPlayerDead());
    }

    @Test
    public void testThatEnemyPlayerShootsPlayer() {
        mockedRNGWorld.configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();

        mockedRNGWorld.makeEnemyPlayerShoot();

        final int numberOfCavesEnemyPlayerShoots = 2; // will actually shoot at 2 + 1 = 3 caves
        Mockito.when(mockedRNGWorld.getRandomNumberGenerator().generateNumber(GameInitialConfigurations.MAX_CAVES_ENEMY_PLAYER_CAN_SHOOT))
                .thenReturn(numberOfCavesEnemyPlayerShoots);

        final int firstCaveIndexToShootByEnemyPlayer = 0; // cave 10
        final int secondCaveIndexToShootByEnemyPlayer = 0; // cave 9
        final int thirdCaveIndexToShootByEnemyPlayer = 1; // cave 1
        Mockito.when(mockedRNGWorld.getRandomNumberGenerator().generateNumber(GameInitialConfigurations.NUMBER_OF_LINKED_CAVES))
                .thenReturn(firstCaveIndexToShootByEnemyPlayer,
                        secondCaveIndexToShootByEnemyPlayer,
                        thirdCaveIndexToShootByEnemyPlayer);

        mockedRNGWorld.makeWumpusSleep();

        NewGame game = mockedRNGWorld.getGame();

        final int playerNextCave = 1;
        game.playerMovesToCave(playerNextCave);

        assertTrue(game.isPlayerDead());

        final int expectedRemainingArrows = GameInitialConfigurations.NUMBER_OF_ARROWS - 1;
        assertEquals(expectedRemainingArrows, game.getEnemyRemainingArrows());
    }

    @Test
    public void testThatEnemyPlayerShootsWumpusAndPlayerLoses() {
        mockedRNGWorld.configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();

        mockedRNGWorld.makeEnemyPlayerShoot();

        final int numberOfCavesEnemyPlayerShoots = 2; // will actually shoot at 2 + 1 = 3 caves
        Mockito.when(mockedRNGWorld.getRandomNumberGenerator().generateNumber(GameInitialConfigurations.MAX_CAVES_ENEMY_PLAYER_CAN_SHOOT))
                .thenReturn(numberOfCavesEnemyPlayerShoots);

        final int firstCaveIndexToShootByEnemyPlayer = 0; // cave 10
        final int secondCaveIndexToShootByEnemyPlayer = 1; // cave 18
        Mockito.when(mockedRNGWorld.getRandomNumberGenerator().generateNumber(GameInitialConfigurations.NUMBER_OF_LINKED_CAVES))
                .thenReturn(firstCaveIndexToShootByEnemyPlayer,
                        secondCaveIndexToShootByEnemyPlayer);

        NewGame game = mockedRNGWorld.getGame();

        final int playerNextCave = 1;
        game.playerMovesToCave(playerNextCave);

        assertTrue(game.isWumpusDead());
        assertTrue(game.isGameLost());
    }

    @Test
    public void testThatEnemyPlayerRunsOutofArrowsAndCantMoveOrShoot() {
        mockedRNGWorld.configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();

        mockedRNGWorld.makeEnemyPlayerShoot();

        final int numberOfCavesEnemyPlayerShoots = 0; // will actually shoot at 0 + 1 = 1 cave
        Mockito.when(mockedRNGWorld.getRandomNumberGenerator().generateNumber(GameInitialConfigurations.MAX_CAVES_ENEMY_PLAYER_CAN_SHOOT))
                .thenReturn(numberOfCavesEnemyPlayerShoots);

        final int caveIndexToShootByEnemyPlayer = 0; // cave 10
        Mockito.when(mockedRNGWorld.getRandomNumberGenerator().generateNumber(GameInitialConfigurations.NUMBER_OF_LINKED_CAVES))
                .thenReturn(caveIndexToShootByEnemyPlayer);

        mockedRNGWorld.makeWumpusSleep();

        NewGame game = mockedRNGWorld.getGame();

        final int enemyRemainingArrows = game.getEnemyRemainingArrows();
        for (int i = 0; i < enemyRemainingArrows; i++) {
            final int secondLinkedCave = GameInitialConfigurations.CAVE_LINKS[game.getPlayerCaveIndex()][1];
            game.playerMovesToCave(secondLinkedCave);
        }
        assertEquals(0, game.getEnemyRemainingArrows());
        assertEquals(MockedRandomNumberGeneratorWorld.ENEMY_PLAYER_STARTING_CAVE_INDEX, game.getEnemyPlayerCaveIndex());

        game.playerMovesToCave(1);
        assertEquals(0, game.getEnemyRemainingArrows());
        assertEquals(MockedRandomNumberGeneratorWorld.ENEMY_PLAYER_STARTING_CAVE_INDEX, game.getEnemyPlayerCaveIndex());

    }
}
