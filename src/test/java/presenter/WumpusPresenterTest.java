package presenter;

import utilities.RandomNumberGenerator;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

/*
TODO


(2) Test that player shoots an arrow and does not kill the Wumpus.
Wumpus should wake up and move to another room but not eat the player

 */
@ExtendWith(MockitoExtension.class)
class WumpusPresenterTest {

    @Mock
    RandomNumberGenerator randomNumberGenerator;

    final int playerStartingCave = 0;
    final int wumpusStartingCave = 18;
    final int firstBatStartingCave = 19;
    final int secondBatStartingCave = 13;
    final int thirdBatStartingCave = 14;
    final int firstPitCave = 3;
    final int secondPitCave = 13;
    final int numberOfCaves = 20;

    @Test
    public void testMovingPlayerToCave() {
        final int playerNextCave = 7;
        final boolean gameIsNotOver = false;

        Mockito.when(randomNumberGenerator.generateNumber(numberOfCaves)).thenReturn(
                playerStartingCave,
                wumpusStartingCave,
                firstBatStartingCave,
                secondBatStartingCave,
                thirdBatStartingCave,
                firstPitCave,
                secondPitCave);

        WumpusPresenter wumpusPresenter=new WumpusPresenterImpl(randomNumberGenerator);
        wumpusPresenter.startNewGame();

        wumpusPresenter.move(playerNextCave);

        final int playerCurrentRoom=wumpusPresenter.getCurrentCave();
        final boolean isGameOver=wumpusPresenter.isGameOver();

        assertEquals(playerNextCave,playerCurrentRoom);
        assertEquals(isGameOver,gameIsNotOver);

    }

    @Test
    public void testMoveToNonConnectedCave() {
        final int playerNextCave = 16;
        final boolean gameIsNotOver = false;

        Mockito.when(randomNumberGenerator.generateNumber(numberOfCaves)).thenReturn(
                playerStartingCave,
                wumpusStartingCave,
                firstBatStartingCave,
                secondBatStartingCave,
                thirdBatStartingCave,
                firstPitCave,
                secondPitCave);

        WumpusPresenter wumpusPresenter = new WumpusPresenterImpl(randomNumberGenerator);
        wumpusPresenter.startNewGame();

        wumpusPresenter.move(playerNextCave);

        final int playerCurrentRoom = wumpusPresenter.getCurrentCave();
        final boolean isGameOver = wumpusPresenter.isGameOver();

        assertEquals(playerStartingCave, playerCurrentRoom);
        assertEquals(isGameOver, gameIsNotOver);
    }

    @Test
    public void testMovingPlayerToCaveThatHasAWumups() {
        final boolean gameIsOver = true;
        final int[] journeyPath = {1, 9, 10, 18};

        Mockito.when(randomNumberGenerator.generateNumber(numberOfCaves)).thenReturn(
                playerStartingCave,
                wumpusStartingCave,
                firstBatStartingCave,
                secondBatStartingCave,
                thirdBatStartingCave,
                firstPitCave,
                secondPitCave);

        WumpusPresenter wumpusPresenter = new WumpusPresenterImpl(randomNumberGenerator);
        wumpusPresenter.startNewGame();
        
        for(int caveNumber : journeyPath) {
            wumpusPresenter.move(caveNumber);
        }

        final boolean actualGameState = wumpusPresenter.isGameOver();
        assertEquals(actualGameState, gameIsOver);
    }

    @Test
    public void testMovingPlayerToACaveNearAWumpusAndSensingTheWumpus(){
        final boolean gameIsOver = false;
        final int[] journeyPath = {1, 9, 10};

        Mockito.when(randomNumberGenerator.generateNumber(numberOfCaves)).thenReturn(
                playerStartingCave,
                wumpusStartingCave,
                firstBatStartingCave,
                secondBatStartingCave,
                thirdBatStartingCave,
                firstPitCave,
                secondPitCave);

        WumpusPresenter wumpusPresenter = new WumpusPresenterImpl(randomNumberGenerator);
        wumpusPresenter.startNewGame();

        for(int caveNumber : journeyPath) {
            wumpusPresenter.move(caveNumber);
        }

        final boolean actualGameState = wumpusPresenter.isGameOver();
        List<String> messages = wumpusPresenter.getMessages();

        // Assert
        assertTrue(messages.contains(WumpusPresenterImpl.Hazard.Wumpus.warning));
        assertEquals(actualGameState, gameIsOver);
    }

    @Test
    public void testThatPlayerEnterRoomWithBat() {
        final int playerStartingCave = 11;
        final boolean gameIsOver = false;
        final int[] journeyPath = {12, 19};
        final int playerDropDownCave = 8;
        final int firstBatFinalCave = 2;

        Mockito.when(randomNumberGenerator.generateNumber(numberOfCaves)).thenReturn(
                playerStartingCave,
                wumpusStartingCave,
                firstBatStartingCave,
                secondBatStartingCave,
                thirdBatStartingCave,
                firstPitCave,
                secondPitCave,
                playerDropDownCave,
                firstBatFinalCave);

        WumpusPresenter wumpusPresenter = new WumpusPresenterImpl(randomNumberGenerator);
        wumpusPresenter.startNewGame();

        for(int caveNumber : journeyPath) {
            wumpusPresenter.move(caveNumber);
        }

        final int playerCurrentCave = wumpusPresenter.getCurrentCave();
        final boolean actualGameState = wumpusPresenter.isGameOver();

        assertEquals(playerDropDownCave, playerCurrentCave);
        assertEquals(actualGameState, gameIsOver);
    }

    @Test
    public void testThatPlayerEnterRoomWithPit() {
        final boolean gameIsOver = true;
        final int[] journeyPath = {4, 3};

        Mockito.when(randomNumberGenerator.generateNumber(numberOfCaves)).thenReturn(
                playerStartingCave,
                wumpusStartingCave,
                firstBatStartingCave,
                secondBatStartingCave,
                thirdBatStartingCave,
                firstPitCave,
                secondPitCave);

        WumpusPresenter wumpusPresenter = new WumpusPresenterImpl(randomNumberGenerator);
        wumpusPresenter.startNewGame();

        for(int caveNumber : journeyPath) {
            wumpusPresenter.move(caveNumber);
        }

        final boolean actualGameState = wumpusPresenter.isGameOver();
        assertEquals(actualGameState, gameIsOver);
    }

    @Test
    public void testKillingTheWumpus() {
        final boolean gameIsOver = true;
        final int[] journeyPath = {1, 9, 10};
        final int shootToCave = 18;

        Mockito.when(randomNumberGenerator.generateNumber(numberOfCaves)).thenReturn(
                playerStartingCave,
                wumpusStartingCave,
                firstBatStartingCave,
                secondBatStartingCave,
                thirdBatStartingCave,
                firstPitCave,
                secondPitCave);

        WumpusPresenter wumpusPresenter = new WumpusPresenterImpl(randomNumberGenerator);
        wumpusPresenter.startNewGame();

        for(int caveNumber : journeyPath) {
            wumpusPresenter.move(caveNumber);
        }

        wumpusPresenter.shoot(shootToCave);

        final boolean actualGameState = wumpusPresenter.isGameOver();
        assertEquals(actualGameState, gameIsOver);
    }

    @Test
    public void testThatPlayerEnterRoomWithPitAndBat() {
        final int playerStartingCave = 11;
        final boolean gameIsOver = true;
        final int[] journeyPath = {12, 13};


        Mockito.when(randomNumberGenerator.generateNumber(numberOfCaves)).thenReturn(
                playerStartingCave,
                wumpusStartingCave,
                firstBatStartingCave,
                secondBatStartingCave,
                thirdBatStartingCave,
                firstPitCave,
                secondPitCave);

        WumpusPresenter wumpusPresenter = new WumpusPresenterImpl(randomNumberGenerator);
        wumpusPresenter.startNewGame();

        for(int caveNumber : journeyPath) {
            wumpusPresenter.move(caveNumber);
        }


        final boolean actualGameState = wumpusPresenter.isGameOver();

        assertEquals(actualGameState, gameIsOver);
    }

    @Test
    public void testThatPlayerShootsAnArrowThatMissesTheWumpus() {
        final boolean gameIsOver = false;
        final int[] journeyPath = {1, 9, 10};
        final int shootToCave = 11;

        final int maximumNumberForCalculatingWumpusWakeupProbability = 4;
        final int numberAtWhichWumpusWillRemainSleeping = 0;

        Mockito.when(randomNumberGenerator.generateNumber(numberOfCaves)).thenReturn(
                playerStartingCave,
                wumpusStartingCave,
                firstBatStartingCave,
                secondBatStartingCave,
                thirdBatStartingCave,
                firstPitCave,
                secondPitCave);


        Mockito.when(randomNumberGenerator.generateNumber(maximumNumberForCalculatingWumpusWakeupProbability)).thenReturn(
                numberAtWhichWumpusWillRemainSleeping);

        WumpusPresenter wumpusPresenter = new WumpusPresenterImpl(randomNumberGenerator);
        wumpusPresenter.startNewGame();

        for(int caveNumber : journeyPath) {
            wumpusPresenter.move(caveNumber);
        }

        wumpusPresenter.shoot(shootToCave);

        final boolean actualGameState = wumpusPresenter.isGameOver();
        final int wumpusCaveLocation = wumpusPresenter.getWumpusCave();

        assertEquals(actualGameState, gameIsOver);

        assertEquals(wumpusCaveLocation, wumpusStartingCave);
    }

}