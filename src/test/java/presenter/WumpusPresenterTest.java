package presenter;

import model.game.GameInitialConfigurations;
import model.game.LegacyHazard;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import utilities.RandomNumberGenerator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class WumpusPresenterTest {

    @Mock
    RandomNumberGenerator randomNumberGenerator;

    final int playerStartingCave = 0;
    public static final int ENEMY_PLAYER_STARTING_CAVE_INDEX = 11;
    final int wumpusStartingCave = 18;
    final int firstBatStartingCave = 19;
    final int secondBatStartingCave = 13;
    final int firstPitCave = 3;
    final int secondPitCave = 13;

    private void configureMockingBasedOnDefaultLocationOfGameObjectsOnMap() {
        Mockito.when(randomNumberGenerator.generateNumber(GameInitialConfigurations.NUMBER_OF_CAVES)).thenReturn(
                playerStartingCave,
                ENEMY_PLAYER_STARTING_CAVE_INDEX,
                wumpusStartingCave,
                firstBatStartingCave,
                secondBatStartingCave,
                firstPitCave,
                secondPitCave);
    }

    private void makeEnemyPlayerSleep() {
        final int numberAtWhichEnemyPlayerWillRemainAsleep = 3;
        Mockito.when(randomNumberGenerator.generateNumber(GameInitialConfigurations.MAX_POSSIBILITY_ENEMY_PLAYER_TAKE_ACTION))
                .thenReturn(numberAtWhichEnemyPlayerWillRemainAsleep);
    }

    @Test
    public void testMovingPlayerToCave() {
        configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();
        makeEnemyPlayerSleep();

        WumpusPresenter wumpusPresenter = new WumpusPresenterImpl(randomNumberGenerator);
        wumpusPresenter.startNewGame();

        final int playerNextCave = 7;
        wumpusPresenter.move(playerNextCave);

        final int playerCurrentRoom = wumpusPresenter.getPlayerCaveIndex();
        assertEquals(playerNextCave, playerCurrentRoom);

        final boolean expectedStatusOfGameIsOver = false;
        final boolean isGameOver = wumpusPresenter.isGameOver();
        assertEquals(isGameOver, expectedStatusOfGameIsOver);

    }

    @Test
    public void testMoveToNonConnectedCave() {
        configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();
        makeEnemyPlayerSleep();

        WumpusPresenter wumpusPresenter = new WumpusPresenterImpl(randomNumberGenerator);
        wumpusPresenter.startNewGame();

        final int playerNextCave = 16;
        wumpusPresenter.move(playerNextCave);

        final int playerCurrentRoom = wumpusPresenter.getPlayerCaveIndex();
        assertEquals(playerStartingCave, playerCurrentRoom);

        final boolean gameIsNotOver = false;
        final boolean isGameOver = wumpusPresenter.isGameOver();
        assertEquals(isGameOver, gameIsNotOver);
    }

    @Test
    public void testMovingPlayerToCaveThatHasAWumups() {
        configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();
        makeEnemyPlayerSleep();

        WumpusPresenter wumpusPresenter = new WumpusPresenterImpl(randomNumberGenerator);
        wumpusPresenter.startNewGame();

        final int[] journeyPath = {1, 9, 10, 18};
        for (int caveNumber : journeyPath) {
            wumpusPresenter.move(caveNumber);
        }

        final boolean actualGameState = wumpusPresenter.isGameOver();
        final boolean gameIsOver = true;
        assertEquals(actualGameState, gameIsOver);
    }

    @Test
    public void testMovingPlayerToACaveNearAWumpusAndSensingTheWumpus() {
        configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();
        makeEnemyPlayerSleep();

        WumpusPresenter wumpusPresenter = new WumpusPresenterImpl(randomNumberGenerator);
        wumpusPresenter.startNewGame();

        final int[] journeyPath = {1, 9, 10};
        for (int caveNumber : journeyPath) {
            wumpusPresenter.move(caveNumber);
        }

        List<String> messages = wumpusPresenter.getWarnings();
        assertTrue(messages.contains(LegacyHazard.Wumpus.getWarning()));

        final boolean actualGameState = wumpusPresenter.isGameOver();
        final boolean gameIsOver = false;
        assertEquals(actualGameState, gameIsOver);
    }

    @Test
    public void testThatPlayerEnterRoomWithBat() {
        makeEnemyPlayerSleep();
        final int playerStartingCave = 11;
        final int enemyPlayerStartingCave = 0;
        final int playerDropDownCave = 8;
        final int firstBatFinalCave = 2;

        Mockito.when(randomNumberGenerator.generateNumber(GameInitialConfigurations.NUMBER_OF_CAVES)).thenReturn(
                playerStartingCave,
                enemyPlayerStartingCave,
                wumpusStartingCave,
                firstBatStartingCave,
                secondBatStartingCave,
                firstPitCave,
                secondPitCave,
                playerDropDownCave,
                firstBatFinalCave);

        WumpusPresenter wumpusPresenter = new WumpusPresenterImpl(randomNumberGenerator);
        wumpusPresenter.startNewGame();

        final int[] journeyPath = {12, 19};
        for (int caveNumber : journeyPath) {
            wumpusPresenter.move(caveNumber);
        }

        final int playerCurrentCave = wumpusPresenter.getPlayerCaveIndex();
        assertEquals(playerDropDownCave, playerCurrentCave);

        final boolean actualGameState = wumpusPresenter.isGameOver();
        final boolean gameIsOver = false;
        assertEquals(actualGameState, gameIsOver);
    }

    @Test
    public void testThatPlayerEnterRoomWithPit() {
        configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();
        makeEnemyPlayerSleep();

        WumpusPresenter wumpusPresenter = new WumpusPresenterImpl(randomNumberGenerator);
        wumpusPresenter.startNewGame();

        final int[] journeyPath = {4, 3};
        for (int caveNumber : journeyPath) {
            wumpusPresenter.move(caveNumber);
        }

        final boolean actualGameState = wumpusPresenter.isGameOver();
        final boolean gameIsOver = true;
        assertEquals(actualGameState, gameIsOver);
    }

    @Test
    public void testThatPlayerEnterRoomWithPitAndBat() {
        makeEnemyPlayerSleep();
        final int playerStartingCave = 11;
        final int enemyPlayerStartingCave = 0;
        Mockito.when(randomNumberGenerator.generateNumber(GameInitialConfigurations.NUMBER_OF_CAVES)).thenReturn(
                playerStartingCave,
                enemyPlayerStartingCave,
                wumpusStartingCave,
                firstBatStartingCave,
                secondBatStartingCave,
                firstPitCave,
                secondPitCave);

        WumpusPresenter wumpusPresenter = new WumpusPresenterImpl(randomNumberGenerator);
        wumpusPresenter.startNewGame();

        final int[] journeyPath = {12, 13};
        for (int caveNumber : journeyPath) {
            wumpusPresenter.move(caveNumber);
        }


        final boolean actualGameState = wumpusPresenter.isGameOver();
        final boolean gameIsOver = true;
        assertEquals(actualGameState, gameIsOver);
    }

    @Test
    public void testKillingTheWumpus() {
        configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();
        makeEnemyPlayerSleep();

        WumpusPresenter wumpusPresenter = new WumpusPresenterImpl(randomNumberGenerator);
        wumpusPresenter.startNewGame();

        final int[] journeyPath = {1, 9, 10};
        for (int caveNumber : journeyPath) {
            wumpusPresenter.move(caveNumber);
        }

        final int shootToCave = 18;
        wumpusPresenter.shoot(shootToCave);

        final boolean actualGameState = wumpusPresenter.isGameOver();
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

        WumpusPresenter wumpusPresenter = new WumpusPresenterImpl(randomNumberGenerator);
        wumpusPresenter.startNewGame();

        final int[] journeyPath = {1, 9, 10};
        for (int caveNumber : journeyPath) {
            wumpusPresenter.move(caveNumber);
        }

        final int shootToCave = 11;
        wumpusPresenter.shoot(shootToCave);

        final int wumpusCaveLocation = wumpusPresenter.getWumpusCaveIndex();
        assertEquals(wumpusCaveLocation, wumpusStartingCave);

        final boolean actualGameState = wumpusPresenter.isGameOver();
        final boolean gameIsOver = false;
        assertEquals(actualGameState, gameIsOver);
    }

    @Test
    public void testThatPlayerShootsAnArrowThatMissesTheWumpusAndWumpusMoves() {
        configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();
        makeEnemyPlayerSleep();

        final int maximumNumberForCalculatingWumpusWakeupProbability = 4;
        final int numberAtWhichWumpusWillRemainSleeping = 1;
        Mockito.when(randomNumberGenerator.generateNumber(maximumNumberForCalculatingWumpusWakeupProbability)).thenReturn(
                numberAtWhichWumpusWillRemainSleeping);

        final int numberOfLinkedCaves = 3;
        final int wumpusLinkedCaveIndex = 2;
        Mockito.when(randomNumberGenerator.generateNumber(numberOfLinkedCaves)).thenReturn(
                wumpusLinkedCaveIndex);

        WumpusPresenter wumpusPresenter = new WumpusPresenterImpl(randomNumberGenerator);
        wumpusPresenter.startNewGame();

        final int[] journeyPath = {1, 9, 10};
        for (int caveNumber : journeyPath) {
            wumpusPresenter.move(caveNumber);
        }

        final int shootToCave = 11;
        wumpusPresenter.shoot(shootToCave);

        final int wumpusCaveLocation = wumpusPresenter.getWumpusCaveIndex();
        final int wumpusCaveCurrLocation = 17;
        assertEquals(wumpusCaveCurrLocation, wumpusCaveLocation);

        final boolean actualGameState = wumpusPresenter.isGameOver();
        final boolean gameIsOver = false;
        assertEquals(actualGameState, gameIsOver);
    }

    @Test
    public void testThatPlayerRunsOutOfArrowsWithoutKillingWumpus() {
        configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();

        final int maximumNumberForCalculatingWumpusWakeupProbability = 4;
        final int numberAtWhichWumpusWillRemainSleeping = 0;
        Mockito.when(randomNumberGenerator.generateNumber(maximumNumberForCalculatingWumpusWakeupProbability)).thenReturn(
                numberAtWhichWumpusWillRemainSleeping);

        WumpusPresenter wumpusPresenter = new WumpusPresenterImpl(randomNumberGenerator);
        wumpusPresenter.startNewGame();

        final int caveToShoot = 7;
        for (int i = 0; i < GameInitialConfigurations.NUMBER_OF_ARROWS; i++) {
            wumpusPresenter.shoot(caveToShoot);
        }

        final String runningOutOfArrowsMessage = "You ran out of arrows";
        final List<String> actualGameMessages = wumpusPresenter.getWarnings();
        assertTrue(actualGameMessages.contains(runningOutOfArrowsMessage));

        final boolean expectedGameStateGameIsOver = true;
        final boolean actualGameState = wumpusPresenter.isGameOver();
        assertEquals(expectedGameStateGameIsOver, actualGameState);
    }

    @Test
    public void testThatPlayerShootsAnArrowMissesWumpusAndWumpusWakesUpAndMoveToEatThePlayer() {
        makeEnemyPlayerSleep();
        configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();

        final int maximumNumberForCalculatingWumpusWakeupProbability = 4;
        final int numberAtWhichWumpusWillRemainSleeping = 1;
        Mockito.when(randomNumberGenerator.generateNumber(maximumNumberForCalculatingWumpusWakeupProbability)).thenReturn(
                numberAtWhichWumpusWillRemainSleeping);

        final int numberOfLinkedCaves = 3;
        final int wumpusLinkedCaveIndex = 1;
        Mockito.when(randomNumberGenerator.generateNumber(numberOfLinkedCaves)).thenReturn(
                wumpusLinkedCaveIndex);

        WumpusPresenter wumpusPresenter = new WumpusPresenterImpl(randomNumberGenerator);
        wumpusPresenter.startNewGame();

        final int[] journeyPath = {1, 9, 10};
        for (int caveNumber : journeyPath) {
            wumpusPresenter.move(caveNumber);
        }

        final int shootToCave = 11;
        wumpusPresenter.shoot(shootToCave);

        final int wumpusCaveLocation = wumpusPresenter.getWumpusCaveIndex();
        final int wumpusCaveCurrLocation = wumpusPresenter.getPlayerCaveIndex();
        assertEquals(wumpusCaveCurrLocation, wumpusCaveLocation);

        final String wokeTheWumpusMessage = "You woke the Wumpus and it ate you";
        final List<String> actualGameMessages = wumpusPresenter.getWarnings();
        assertTrue(actualGameMessages.contains(wokeTheWumpusMessage));

        final boolean expectedGameStateGameIsOver = true;
        final boolean actualGameState = wumpusPresenter.isGameOver();
        assertEquals(expectedGameStateGameIsOver, actualGameState);
    }

}