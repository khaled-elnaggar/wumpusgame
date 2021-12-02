package model;

import model.game.GameInitialConfigurations;
import model.game.NewGame;
import model.gamemap.Cave;
import model.gamemap.GameMap;
import model.gameobject.hazard.Bat;
import model.gameobject.hazard.Pit;
import model.gameobject.Player;
import model.gameobject.hazard.Wumpus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import utilities.RandomNumberGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class NewGameModelTests {

    public static final int PLAYER_STARTING_CAVE_INDEX = 9;
    public static final int ENEMY_PLAYER_STARTING_CAVE_INDEX = 11;
    public static final int WUMPUS_STARTING_CAVE_INDEX = 18;
    public static final int FIRST_BAT_STARTING_CAVE_INDEX = 19;
    public static final int SECOND_BAT_STARTING_CAVE_INDEX = 13;
    public static final int FIRST_PIT_CAVE = 3;
    public static final int SECOND_PIT_CAVE = 13;
    @Mock
    RandomNumberGenerator randomNumberGenerator;

    private void configureMockingBasedOnDefaultLocationOfGameObjectsOnMap() {
        Mockito.when(randomNumberGenerator.generateNumber(GameInitialConfigurations.NUMBER_OF_CAVES)).thenReturn(
                PLAYER_STARTING_CAVE_INDEX,
                ENEMY_PLAYER_STARTING_CAVE_INDEX,
                WUMPUS_STARTING_CAVE_INDEX,
                FIRST_BAT_STARTING_CAVE_INDEX,
                SECOND_BAT_STARTING_CAVE_INDEX,
                FIRST_PIT_CAVE,
                SECOND_PIT_CAVE
        );
    }

    private void makeEnemyPlayerSleep() {
        final int numberAtWhichEnemyPlayerWillRemainAsleep = 3;
        Mockito.when(randomNumberGenerator.generateNumber(GameInitialConfigurations.MAX_POSSIBILITY_ENEMY_PLAYER_TAKE_ACTION))
                .thenReturn(numberAtWhichEnemyPlayerWillRemainAsleep);
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
        configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();

        NewGame game = new NewGame(randomNumberGenerator);
        game.startGame();

        final int actualPlayerCaveIndex = game.getPlayerCaveIndex();
        assertEquals(PLAYER_STARTING_CAVE_INDEX, actualPlayerCaveIndex);
    }

    @Test
    public void testThatPlayerCaveIsAddedToGameMap() {
        configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();

        NewGame game = new NewGame(randomNumberGenerator);
        game.startGame();

        final int actualPlayerCaveIndex = game.getPlayerCaveIndex();
        assertEquals(PLAYER_STARTING_CAVE_INDEX, actualPlayerCaveIndex);

        Cave playerCave = game.getGameMap().getCaves().get(PLAYER_STARTING_CAVE_INDEX);
        Player player = game.getPlayer();
        assertTrue(playerCave.containsAny(player));
    }

    @Test
    public void testThatWumpusIsAddedToCaveGameMap() {
        configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();

        NewGame game = new NewGame(randomNumberGenerator);
        game.startGame();

        final int actualWumpusCaveIndex = game.getWumpusCaveIndex();
        assertEquals(WUMPUS_STARTING_CAVE_INDEX, actualWumpusCaveIndex);

        Cave wumpusCave = game.getGameMap().getCaves().get(WUMPUS_STARTING_CAVE_INDEX);
        Wumpus wumpus = game.getWumpus();
        assertTrue(wumpusCave.containsAny(wumpus));

    }

    @Test
    public void testThatThreeBatsAreAddedToCaveGameMap() {
        configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();

        int[] batsStartingCavesIndexes = {FIRST_BAT_STARTING_CAVE_INDEX, SECOND_BAT_STARTING_CAVE_INDEX};

        NewGame game = new NewGame(randomNumberGenerator);
        game.startGame();

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
        configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();

        int[] pitsInCavesIndexes = {FIRST_PIT_CAVE, SECOND_PIT_CAVE};

        NewGame game = new NewGame(randomNumberGenerator);
        game.startGame();

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

        Mockito.when(randomNumberGenerator.generateNumber(GameInitialConfigurations.NUMBER_OF_CAVES)).thenReturn(
                PLAYER_STARTING_CAVE_INDEX,
                ENEMY_PLAYER_STARTING_CAVE_INDEX,
                wumpusStartingWrongCaveIndex,
                wumpusStartingCorrectCaveIndex,
                FIRST_BAT_STARTING_CAVE_INDEX,
                SECOND_BAT_STARTING_CAVE_INDEX,
                FIRST_PIT_CAVE,
                SECOND_PIT_CAVE
        );

        NewGame game = new NewGame(randomNumberGenerator);
        game.startGame();

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
        int[] batsStartingCavesIndexes = {FIRST_BAT_STARTING_CAVE_INDEX, secondBatCorrectStartingCaveIndex};

        Mockito.when(randomNumberGenerator.generateNumber(GameInitialConfigurations.NUMBER_OF_CAVES)).thenReturn(
                PLAYER_STARTING_CAVE_INDEX,
                ENEMY_PLAYER_STARTING_CAVE_INDEX,
                WUMPUS_STARTING_CAVE_INDEX,
                FIRST_BAT_STARTING_CAVE_INDEX,
                secondBatWrongStartingCaveIndex,
                secondBatCorrectStartingCaveIndex,
                FIRST_PIT_CAVE,
                SECOND_PIT_CAVE
        );

        NewGame game = new NewGame(randomNumberGenerator);
        game.startGame();

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
        int[] correctPitsInCavesIndexes = {FIRST_PIT_CAVE, secondCorrectPitCave};

        Mockito.when(randomNumberGenerator.generateNumber(GameInitialConfigurations.NUMBER_OF_CAVES)).thenReturn(
                PLAYER_STARTING_CAVE_INDEX,
                ENEMY_PLAYER_STARTING_CAVE_INDEX,
                WUMPUS_STARTING_CAVE_INDEX,
                FIRST_BAT_STARTING_CAVE_INDEX,
                SECOND_BAT_STARTING_CAVE_INDEX,
                FIRST_PIT_CAVE,
                secondWrongPitCave,
                secondCorrectPitCave
        );

        NewGame game = new NewGame(randomNumberGenerator);
        game.startGame();

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
        final int firstBatStartingCorrectCaveIndex = FIRST_BAT_STARTING_CAVE_INDEX;

        final int firstPitStartingWrongCaveIndex = 8;
        final int firstPitStartingCorrectCaveIndex = FIRST_PIT_CAVE;

        Mockito.when(randomNumberGenerator.generateNumber(GameInitialConfigurations.NUMBER_OF_CAVES)).thenReturn(
                PLAYER_STARTING_CAVE_INDEX,
                ENEMY_PLAYER_STARTING_CAVE_INDEX,
                wumpusStartingWrongCaveIndex,
                wumpusStartingCorrectCaveIndex,
                firstBatStartingWrongCaveIndex,
                firstBatStartingCorrectCaveIndex,
                SECOND_BAT_STARTING_CAVE_INDEX,
                firstPitStartingWrongCaveIndex,
                firstPitStartingCorrectCaveIndex,
                SECOND_PIT_CAVE
        );

        NewGame game = new NewGame(randomNumberGenerator);
        game.startGame();

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
        configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();
        makeEnemyPlayerSleep();

        NewGame game = new NewGame(randomNumberGenerator);
        game.startGame();

        final int caveIndexToMoveTo = 1;
        game.playerMovesToCave(caveIndexToMoveTo);

        final int actualPlayerCaveIndex = game.getPlayerCaveIndex();
        assertEquals(caveIndexToMoveTo, actualPlayerCaveIndex);

        Cave currentPlayerCave = game.getGameMap().getCaves().get(caveIndexToMoveTo);
        Player player = game.getPlayer();
        assertTrue(currentPlayerCave.containsAny(player));

        Cave pastPlayerCave = game.getGameMap().getCaves().get(PLAYER_STARTING_CAVE_INDEX);
        assertFalse(pastPlayerCave.containsAny(player));
    }

    @Test
    public void testMoveToNonConnectedCave() {
        configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();
        makeEnemyPlayerSleep();

        NewGame game = new NewGame(randomNumberGenerator);
        game.startGame();

        final int caveIndexToMoveTo = 17;
        game.playerMovesToCave(caveIndexToMoveTo);

        final int actualPlayerCaveIndex = game.getPlayerCaveIndex();
        assertNotEquals(caveIndexToMoveTo, actualPlayerCaveIndex);
        assertEquals(PLAYER_STARTING_CAVE_INDEX, actualPlayerCaveIndex);

        Cave currentPlayerCave = game.getGameMap().getCaves().get(caveIndexToMoveTo);
        Player player = game.getPlayer();
        assertFalse(currentPlayerCave.containsAny(player));

        Cave pastPlayerCave = game.getGameMap().getCaves().get(PLAYER_STARTING_CAVE_INDEX);
        assertTrue(pastPlayerCave.containsAny(player));
    }

    @Test
    public void testMovingPlayerToCaveThatHasAWumups() {
        configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();
        makeEnemyPlayerSleep();

        NewGame game = new NewGame(randomNumberGenerator);
        game.startGame();

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
        configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();
        makeEnemyPlayerSleep();

        NewGame game = new NewGame(randomNumberGenerator);
        game.startGame();

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
        configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();
        makeEnemyPlayerSleep();

        NewGame game = new NewGame(randomNumberGenerator);
        game.startGame();

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
        configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();
        makeEnemyPlayerSleep();

        NewGame game = new NewGame(randomNumberGenerator);
        game.startGame();

        game.playerMovesToCave(10);

        final int caveToShootTo = WUMPUS_STARTING_CAVE_INDEX;
        game.playerShootsToCave(caveToShootTo);

        final boolean actualGameState = game.isGameOver();
        final boolean gameIsOver = true;
        assertEquals(actualGameState, gameIsOver);
    }

    @Test
    public void testThatPlayerShootsAnArrowThatMissesTheWumpusAndWumpusRemainsSleeping() {
        configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();
        makeEnemyPlayerSleep();

        final int maximumNumberForCalculatingWumpusWakeupProbability = 4;
        final int numberAtWhichWumpusWillRemainSleeping = 0;
        Mockito.when(randomNumberGenerator.generateNumber(maximumNumberForCalculatingWumpusWakeupProbability)).thenReturn(
                numberAtWhichWumpusWillRemainSleeping);

        NewGame game = new NewGame(randomNumberGenerator);
        game.startGame();
        game.playerMovesToCave(10);

        final int caveToShootTo = 11;
        game.playerShootsToCave(caveToShootTo);

        final int wumpusCaveLocation = game.getWumpusCaveIndex();
        assertEquals(wumpusCaveLocation, WUMPUS_STARTING_CAVE_INDEX);

        final boolean actualGameState = game.isGameOver();
        final boolean gameIsNotOver = false;
        assertEquals(actualGameState, gameIsNotOver);
    }

    @Test
    public void testThatPlayerShootsAnArrowThatMissesTheWumpusAndWumpusMoves() {
        configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();

        final int maximumNumberForCalculatingWumpusWakeupProbability = 4;
        final int numberAtWhichWumpusWillWakeUp = 1;
        Mockito.when(randomNumberGenerator.generateNumber(maximumNumberForCalculatingWumpusWakeupProbability)).thenReturn(
                numberAtWhichWumpusWillWakeUp);

        final int numberOfLinkedCaves = 3;
        final int wumpusLinkedCaveIndex = 2;
        Mockito.when(randomNumberGenerator.generateNumber(numberOfLinkedCaves)).thenReturn(
                wumpusLinkedCaveIndex);

        NewGame game = new NewGame(randomNumberGenerator);
        game.startGame();

        final int caveToShootTo = 1;
        game.playerShootsToCave(caveToShootTo);

        final int actualWumpusCaveIndex = game.getWumpusCaveIndex();
        final int expectedWumpusCaveIndex = 17;
        assertEquals(expectedWumpusCaveIndex, actualWumpusCaveIndex);

        final Cave initialWumpusCave = game.getGameMap().getCaves().get(WUMPUS_STARTING_CAVE_INDEX);
        assertFalse(initialWumpusCave.containsAny(game.getWumpus()));

        final boolean actualGameState = game.isGameOver();
        final boolean gameIsNotOver = false;
        assertEquals(actualGameState, gameIsNotOver);
    }

    @Test
    public void testThatPlayerRunsOutOfArrowsWithoutKillingWumpus() {
        configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();

        final int maximumNumberForCalculatingWumpusWakeupProbability = 4;
        final int numberAtWhichWumpusWillWakeUp = 0;
        Mockito.when(randomNumberGenerator.generateNumber(maximumNumberForCalculatingWumpusWakeupProbability)).thenReturn(
                numberAtWhichWumpusWillWakeUp);

        NewGame game = new NewGame(randomNumberGenerator);
        game.startGame();

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
        configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();
        makeEnemyPlayerSleep();

        final int maximumNumberForCalculatingWumpusWakeupProbability = 4;
        final int numberAtWhichWumpusWillWakeUp = 1;
        Mockito.when(randomNumberGenerator.generateNumber(maximumNumberForCalculatingWumpusWakeupProbability)).thenReturn(
                numberAtWhichWumpusWillWakeUp);

        final int numberOfLinkedCaves = 3;
        final int wumpusLinkedCaveIndex = 1;
        Mockito.when(randomNumberGenerator.generateNumber(numberOfLinkedCaves)).thenReturn(
                wumpusLinkedCaveIndex);

        NewGame game = new NewGame(randomNumberGenerator);
        game.startGame();

        game.playerMovesToCave(10);


        final int caveToShootTo = 11;
        game.playerShootsToCave(caveToShootTo);

        final int actualWumpusCaveIndex = game.getWumpusCaveIndex();
        final int expectedWumpusCaveIndex = 10;
        assertEquals(expectedWumpusCaveIndex, actualWumpusCaveIndex);

        final Cave initialWumpusCave = game.getGameMap().getCaves().get(WUMPUS_STARTING_CAVE_INDEX);
        assertFalse(initialWumpusCave.containsAny(game.getWumpus()));

        final boolean actualGameState = game.isGameOver();
        final boolean gameIsOver = true;
        assertEquals(actualGameState, gameIsOver);
    }

    @Test
    public void testThatPlayerEnterRoomWithBatTwice() {
        makeEnemyPlayerSleep();
        final int playerFirstDropDownCaveIndex = 15;
        final int firstBatFinalCaveIndex = 2;

        final int playerSecondDropDownCaveIndex = 6;
        final int secondBatFinalCaveIndex = 0;

        final int secondBatStartingCaveIndex = 14;
        Mockito.when(randomNumberGenerator.generateNumber(GameInitialConfigurations.NUMBER_OF_CAVES)).thenReturn(
                PLAYER_STARTING_CAVE_INDEX,
                ENEMY_PLAYER_STARTING_CAVE_INDEX,
                WUMPUS_STARTING_CAVE_INDEX,
                FIRST_BAT_STARTING_CAVE_INDEX,
                secondBatStartingCaveIndex,
                FIRST_PIT_CAVE,
                SECOND_PIT_CAVE,
                playerFirstDropDownCaveIndex,
                firstBatFinalCaveIndex,
                playerSecondDropDownCaveIndex,
                secondBatFinalCaveIndex
        );


        NewGame game = new NewGame(randomNumberGenerator);
        game.startGame();

        final int[] journeyPath = {10, 11, 12, FIRST_BAT_STARTING_CAVE_INDEX};
        for (int cave : journeyPath) {
            game.playerMovesToCave(cave);
        }

        final int actualPlayerCaveIndex = game.getPlayerCaveIndex();
        assertEquals(playerFirstDropDownCaveIndex, actualPlayerCaveIndex);


        final Bat firstBat = game.getBats().get(0);
        final Cave firstBatActualCave = firstBat.getCave();
        assertEquals(firstBatFinalCaveIndex, firstBatActualCave.getNumber());

        final Cave previousCave = game.getGameMap().getCaves().get(FIRST_BAT_STARTING_CAVE_INDEX);
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
        makeEnemyPlayerSleep();
        final int playerDropDownCaveIndex = 8;
        final int firstBatFinalCaveIndex = 2;

        Mockito.when(randomNumberGenerator.generateNumber(GameInitialConfigurations.NUMBER_OF_CAVES)).thenReturn(
                PLAYER_STARTING_CAVE_INDEX,
                ENEMY_PLAYER_STARTING_CAVE_INDEX,
                WUMPUS_STARTING_CAVE_INDEX,
                FIRST_BAT_STARTING_CAVE_INDEX,
                SECOND_BAT_STARTING_CAVE_INDEX,
                FIRST_PIT_CAVE,
                SECOND_PIT_CAVE,
                playerDropDownCaveIndex,
                firstBatFinalCaveIndex
        );


        NewGame game = new NewGame(randomNumberGenerator);
        game.startGame();

        final int[] journeyPath = {10, 11, 12, FIRST_BAT_STARTING_CAVE_INDEX};
        for (int cave : journeyPath) {
            game.playerMovesToCave(cave);
        }

        final int actualPlayerCaveIndex = game.getPlayerCaveIndex();
        assertEquals(playerDropDownCaveIndex, actualPlayerCaveIndex);


        final Bat firstBat = game.getBats().get(0);
        final Cave firstBatActualCave = firstBat.getCave();
        assertEquals(firstBatFinalCaveIndex, firstBatActualCave.getNumber());

        final Cave previousCave = game.getGameMap().getCaves().get(FIRST_BAT_STARTING_CAVE_INDEX);
        assertFalse(previousCave.containsAny(game.getPlayer()));
        assertFalse(previousCave.containsAny(firstBat));
    }

    @Test
    public void testThatPlayerEnterRoomWithPitAndBat() {
        makeEnemyPlayerSleep();
        configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();

        NewGame game = new NewGame(randomNumberGenerator);
        game.startGame();

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

        Mockito.when(randomNumberGenerator.generateNumber(GameInitialConfigurations.NUMBER_OF_CAVES)).thenReturn(
                PLAYER_STARTING_CAVE_INDEX,
                ENEMY_PLAYER_STARTING_CAVE_INDEX,
                WUMPUS_STARTING_CAVE_INDEX,
                FIRST_BAT_STARTING_CAVE_INDEX,
                SECOND_BAT_STARTING_CAVE_INDEX,
                FIRST_PIT_CAVE,
                SECOND_PIT_CAVE
        );

        NewGame game = new NewGame(randomNumberGenerator);
        game.startGame();

        Cave cave = game.getGameMap().getCaves().get(WUMPUS_STARTING_CAVE_INDEX);

        assertTrue(cave.getHazards().get(0) instanceof Pit);
        assertTrue(cave.getHazards().get(1) instanceof Wumpus);
        assertTrue(cave.getHazards().get(2) instanceof Bat);

    }

    @Test
    public void testTheInitialNumberOfAllCreatures() {
        configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();
        NewGame game = new NewGame(randomNumberGenerator);
        game.startGame();

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
        Mockito.when(randomNumberGenerator.generateNumber(GameInitialConfigurations.NUMBER_OF_CAVES)).thenReturn(
                playerStartingCave,
                ENEMY_PLAYER_STARTING_CAVE_INDEX,
                WUMPUS_STARTING_CAVE_INDEX,
                FIRST_BAT_STARTING_CAVE_INDEX,
                SECOND_BAT_STARTING_CAVE_INDEX,
                FIRST_PIT_CAVE,
                SECOND_PIT_CAVE
        );
        NewGame game = new NewGame(randomNumberGenerator);
        game.startGame();

        int[] cavesToShootAt = new int[]{7, 8, 9, 10, 18};
        game.playerShootsToCave(cavesToShootAt);

        assertTrue(game.getWumpus().isDead());

        final int expectedRemainingArrows = GameInitialConfigurations.NUMBER_OF_ARROWS - 1;
        assertEquals(expectedRemainingArrows, game.getNumberOfArrows());
    }

    @Test
    public void testThatPlayerShootsMultipleNonLinkedCavesWithOneArrow() {
        configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();
        NewGame game = new NewGame(randomNumberGenerator);
        game.startGame();

        int[] cavesToShootAt = new int[]{15, 2, 11, 10, 3};
        final int cave1IndexFrom0 = 1;
        final int cave18IndexFrom10 = 1;
        Mockito.when(randomNumberGenerator.generateNumber(GameInitialConfigurations.NUMBER_OF_LINKED_CAVES)).thenReturn(
                cave1IndexFrom0,
                cave18IndexFrom10);

        game.playerShootsToCave(cavesToShootAt);

        assertTrue(game.getWumpus().isDead());
    }

    @Test
    public void testThatPlayerShootsMultipleNonLinkedCavesWithOneArrowAndDies() {
        configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();
        NewGame game = new NewGame(randomNumberGenerator);
        game.startGame();

        int[] cavesToShootAt = new int[]{1, 15};
        final int cave9IndexFrom1 = 1;
        Mockito.when(randomNumberGenerator.generateNumber(GameInitialConfigurations.NUMBER_OF_LINKED_CAVES)).thenReturn(
                cave9IndexFrom1);

        game.playerShootsToCave(cavesToShootAt);

        assertTrue(game.getPlayer().isDead());
    }

    @Test
    public void testThatEnemyPlayerIsInitialized() {
        configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();

        NewGame game = new NewGame(randomNumberGenerator);
        game.startGame();

        final int actualEnemyPlayerLocation = game.getEnemyPlayerCaveIndex();
        assertEquals(ENEMY_PLAYER_STARTING_CAVE_INDEX, actualEnemyPlayerLocation);
    }

    @Test
    public void testThatEnemyPlayerMovesAfterPlayerMoves() {
        configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();

        final int numberAtWhichEnemyPlayerWillMove = 0;
        Mockito.when(randomNumberGenerator.generateNumber(GameInitialConfigurations.MAX_POSSIBILITY_ENEMY_PLAYER_TAKE_ACTION))
                .thenReturn(numberAtWhichEnemyPlayerWillMove);

        final int caveIndexToMoveTo = 1;
        Mockito.when(randomNumberGenerator.generateNumber(GameInitialConfigurations.NUMBER_OF_LINKED_CAVES)).thenReturn(
                caveIndexToMoveTo);

        NewGame game = new NewGame(randomNumberGenerator);
        game.startGame();


        final int caveToMoveTo = 1;
        game.playerMovesToCave(caveToMoveTo);

        final int expectedEnemyPlayerCave = GameInitialConfigurations.CAVE_LINKS[ENEMY_PLAYER_STARTING_CAVE_INDEX][caveIndexToMoveTo];

        assertEquals(expectedEnemyPlayerCave, game.getEnemyPlayerCaveIndex());
    }

    @Test
    public void testThatEnemyPlayerMovesAfterPlayerShoots() {
        configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();
        final int numberAtWhichEnemyPlayerWillMove = 0;
        Mockito.when(randomNumberGenerator.generateNumber(GameInitialConfigurations.MAX_POSSIBILITY_ENEMY_PLAYER_TAKE_ACTION))
                .thenReturn(numberAtWhichEnemyPlayerWillMove);

        final int caveIndexToMoveTo = 1;
        Mockito.when(randomNumberGenerator.generateNumber(GameInitialConfigurations.NUMBER_OF_LINKED_CAVES)).thenReturn(
                caveIndexToMoveTo);

        NewGame game = new NewGame(randomNumberGenerator);
        game.startGame();


        final int caveToShootAt = 1;
        game.playerMovesToCave(caveToShootAt);

        final int expectedEnemyPlayerCave = GameInitialConfigurations.CAVE_LINKS[ENEMY_PLAYER_STARTING_CAVE_INDEX][caveIndexToMoveTo];

        assertEquals(expectedEnemyPlayerCave, game.getEnemyPlayerCaveIndex());
    }

    @Test
    public void testThatEnemyPlayerMoveToCaveWithWumpusAndDies() {
        configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();

        final int numberAtWhichEnemyPlayerWillMove = 0;
        Mockito.when(randomNumberGenerator.generateNumber(GameInitialConfigurations.MAX_POSSIBILITY_ENEMY_PLAYER_TAKE_ACTION))
                .thenReturn(numberAtWhichEnemyPlayerWillMove);

        final int firstCaveIndexToMoveTo = 0;
        final int secondCaveIndexToMoveTo = 1;
        Mockito.when(randomNumberGenerator.generateNumber(GameInitialConfigurations.NUMBER_OF_LINKED_CAVES)).thenReturn(
                firstCaveIndexToMoveTo,
                secondCaveIndexToMoveTo);

        NewGame game = new NewGame(randomNumberGenerator);
        game.startGame();


        final int[] journeyPath = new int[]{1, 0};
        for (int cave : journeyPath) {
            game.playerMovesToCave(cave);
        }

        final int expectedEnemyPlayerCave = WUMPUS_STARTING_CAVE_INDEX;

        assertEquals(expectedEnemyPlayerCave, game.getEnemyPlayerCaveIndex());

        assertTrue(game.isEnemyPlayerDead());
    }

    @Test
    public void testThatEnemyPlayerMoveToCaveWithBatAndTeleports() {
        final int enemyPlayerDropDownCave = 8;
        final int batDropDownCave = 4;
        Mockito.when(randomNumberGenerator.generateNumber(GameInitialConfigurations.NUMBER_OF_CAVES)).thenReturn(
                PLAYER_STARTING_CAVE_INDEX,
                ENEMY_PLAYER_STARTING_CAVE_INDEX,
                WUMPUS_STARTING_CAVE_INDEX,
                FIRST_BAT_STARTING_CAVE_INDEX,
                SECOND_BAT_STARTING_CAVE_INDEX,
                FIRST_PIT_CAVE,
                SECOND_PIT_CAVE,
                enemyPlayerDropDownCave,
                batDropDownCave);


        final int numberAtWhichEnemyPlayerWillMove = 0;
        Mockito.when(randomNumberGenerator.generateNumber(GameInitialConfigurations.MAX_POSSIBILITY_ENEMY_PLAYER_TAKE_ACTION))
                .thenReturn(numberAtWhichEnemyPlayerWillMove);

        final int firstCaveIndexToMoveTo = 2;
        final int secondCaveIndexToMoveTo = 1;
        Mockito.when(randomNumberGenerator.generateNumber(GameInitialConfigurations.NUMBER_OF_LINKED_CAVES)).thenReturn(
                firstCaveIndexToMoveTo,
                secondCaveIndexToMoveTo);

        NewGame game = new NewGame(randomNumberGenerator);
        game.startGame();


        final int[] journeyPath = new int[]{1, 0};
        for (int cave : journeyPath) {
            game.playerMovesToCave(cave);
        }


        assertEquals(enemyPlayerDropDownCave, game.getEnemyPlayerCaveIndex());
    }

}
