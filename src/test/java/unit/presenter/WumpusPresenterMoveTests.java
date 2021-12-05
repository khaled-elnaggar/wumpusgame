package unit.presenter;

import model.game.GameInitialConfigurations;
import model.game.LegacyHazard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import presenter.WumpusPresenter;
import unit.support.MockedRandomNumberGeneratorWorld;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WumpusPresenterMoveTests {

    MockedRandomNumberGeneratorWorld mockedRNGWorld;

    @BeforeEach
    public void setUp() {
        mockedRNGWorld = new MockedRandomNumberGeneratorWorld();
    }


    @Test
    public void testMovingPlayerToCave() {
        mockedRNGWorld.configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();
        mockedRNGWorld.makeEnemyPlayerGoBackAndForth();

        WumpusPresenter wumpusPresenter = mockedRNGWorld.getWumpusPresenter();

        final int playerNextCave = 1;
        wumpusPresenter.move(playerNextCave);

        final int playerCurrentRoom = wumpusPresenter.getPlayerCaveIndex();
        assertEquals(playerNextCave, playerCurrentRoom);

        final boolean expectedStatusOfGameIsOver = false;
        final boolean isGameOver = wumpusPresenter.isGameOver();
        assertEquals(isGameOver, expectedStatusOfGameIsOver);

    }

    @Test
    public void testMoveToNonConnectedCave() {
        mockedRNGWorld.configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();
        mockedRNGWorld.makeEnemyPlayerGoBackAndForth();

        WumpusPresenter wumpusPresenter = mockedRNGWorld.getWumpusPresenter();

        final int playerNextCave = 16;
        wumpusPresenter.move(playerNextCave);

        final int playerCurrentRoom = wumpusPresenter.getPlayerCaveIndex();
        assertEquals(MockedRandomNumberGeneratorWorld.PLAYER_STARTING_CAVE_INDEX, playerCurrentRoom);

        final boolean gameIsNotOver = false;
        final boolean isGameOver = wumpusPresenter.isGameOver();
        assertEquals(isGameOver, gameIsNotOver);
    }

    @Test
    public void testMovingPlayerToCaveThatHasAWumups() {
        mockedRNGWorld.configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();
        mockedRNGWorld.makeEnemyPlayerGoBackAndForth();

        WumpusPresenter wumpusPresenter = mockedRNGWorld.getWumpusPresenter();

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
        mockedRNGWorld.configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();
        mockedRNGWorld.makeEnemyPlayerGoBackAndForth();

        WumpusPresenter wumpusPresenter = mockedRNGWorld.getWumpusPresenter();

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
        mockedRNGWorld.makeEnemyPlayerGoBackAndForth();
        final int playerStartingCave = 11;
        final int enemyPlayerStartingCave = 0;
        final int playerDropDownCave = 8;
        final int firstBatFinalCave = 2;

        Mockito.when(mockedRNGWorld.getRandomNumberGenerator().generateNumber(GameInitialConfigurations.NUMBER_OF_CAVES)).thenReturn(
                playerStartingCave,
                enemyPlayerStartingCave,
                MockedRandomNumberGeneratorWorld.WUMPUS_STARTING_CAVE_INDEX,
                MockedRandomNumberGeneratorWorld.FIRST_BAT_STARTING_CAVE_INDEX,
                MockedRandomNumberGeneratorWorld.SECOND_BAT_STARTING_CAVE_INDEX,
                MockedRandomNumberGeneratorWorld.FIRST_PIT_CAVE,
                MockedRandomNumberGeneratorWorld.SECOND_PIT_CAVE,
                playerDropDownCave,
                firstBatFinalCave);

        WumpusPresenter wumpusPresenter = mockedRNGWorld.getWumpusPresenter();

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
        final int playerStartingCave = 0;
        Mockito.when(mockedRNGWorld.getRandomNumberGenerator().generateNumber(GameInitialConfigurations.NUMBER_OF_CAVES)).thenReturn(
                playerStartingCave,
                MockedRandomNumberGeneratorWorld.ENEMY_PLAYER_STARTING_CAVE_INDEX,
                MockedRandomNumberGeneratorWorld.WUMPUS_STARTING_CAVE_INDEX,
                MockedRandomNumberGeneratorWorld.FIRST_BAT_STARTING_CAVE_INDEX,
                MockedRandomNumberGeneratorWorld.SECOND_BAT_STARTING_CAVE_INDEX,
                MockedRandomNumberGeneratorWorld.FIRST_PIT_CAVE,
                MockedRandomNumberGeneratorWorld.SECOND_PIT_CAVE);
        mockedRNGWorld.makeEnemyPlayerGoBackAndForth();

        WumpusPresenter wumpusPresenter = mockedRNGWorld.getWumpusPresenter();

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
        mockedRNGWorld.makeEnemyPlayerGoBackAndForth();
        final int playerStartingCave = 11;
        final int enemyPlayerStartingCave = 0;
        Mockito.when(mockedRNGWorld.getRandomNumberGenerator().generateNumber(GameInitialConfigurations.NUMBER_OF_CAVES)).thenReturn(
                playerStartingCave,
                enemyPlayerStartingCave,
                MockedRandomNumberGeneratorWorld.WUMPUS_STARTING_CAVE_INDEX,
                MockedRandomNumberGeneratorWorld.FIRST_BAT_STARTING_CAVE_INDEX,
                MockedRandomNumberGeneratorWorld.SECOND_BAT_STARTING_CAVE_INDEX,
                MockedRandomNumberGeneratorWorld.FIRST_PIT_CAVE,
                MockedRandomNumberGeneratorWorld.SECOND_PIT_CAVE);

        WumpusPresenter wumpusPresenter = mockedRNGWorld.getWumpusPresenter();

        final int[] journeyPath = {12, 13};
        for (int caveNumber : journeyPath) {
            wumpusPresenter.move(caveNumber);
        }


        final boolean actualGameState = wumpusPresenter.isGameOver();
        final boolean gameIsOver = true;
        assertEquals(actualGameState, gameIsOver);
    }

}
