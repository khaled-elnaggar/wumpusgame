package model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import utilities.RandomNumberGenerator;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
public class NewGameModelTests {

    public static final int PLAYER_STARTING_CAVE_INDEX = 9;
    public static final int WUMPUS_STARTING_CAVE_INDEX = 15;
    public static final int FIRST_BAT_STARTING_CAVE_INDEX = 19;
    public static final int SECOND_BAT_STARTING_CAVE_INDEX = 13;
    public static final int THIRD_BAT_STARTING_CAVE_INDEX = 14;
    public static final int FIRST_PIT_CAVE = 3;
    public static final int SECOND_PIT_CAVE = 13;
    @Mock
    RandomNumberGenerator randomNumberGenerator;

    private void configureMockingBasedOnDefaultLocationOfGameObjectsOnMap() {
        Mockito.when(randomNumberGenerator.generateNumber(GameInitialConfigurations.NUMBER_OF_CAVES)).thenReturn(
                PLAYER_STARTING_CAVE_INDEX,
                WUMPUS_STARTING_CAVE_INDEX,
                FIRST_BAT_STARTING_CAVE_INDEX,
                SECOND_BAT_STARTING_CAVE_INDEX,
                THIRD_BAT_STARTING_CAVE_INDEX,
                FIRST_PIT_CAVE,
                SECOND_PIT_CAVE
        );
    }

    @Test
    public void testGameMapInitializationProducedTheCorrectNumberOfCaves() {
        NewGame game = new NewGame();
        game.startGame();
        GameMap gameMap = game.getGameMap();

        assertEquals(GameInitialConfigurations.NUMBER_OF_CAVES,gameMap.getCaves().size());
    }

    @Test
    public void testThatGameMapInitializationProducedTheCorrectCaveLinks() {
        NewGame game = new NewGame();
        game.startGame();
        GameMap gameMap = game.getGameMap();
        final List<Cave> mapCaves = gameMap.getCaves();

        final int[] caveLinkIndexesToTest= new int[]{0,9,3,4};

        for(int caveLinkIndexToTest:caveLinkIndexesToTest){
            final Cave firstCave = mapCaves.get(
                    caveLinkIndexToTest);

            final int connectedCavesCount = 3;
            final Set<Cave> actualLinkedCavesToFirstCave = firstCave.getLinkedCaves();
            assertEquals(connectedCavesCount,actualLinkedCavesToFirstCave.size());

            final int[] expectedLinkedCavesToFirstCave = GameInitialConfigurations.CAVE_LINKS[caveLinkIndexToTest];

            for(int caveLink:expectedLinkedCavesToFirstCave){
                assertTrue(actualLinkedCavesToFirstCave.contains(new Cave(caveLink)));
            }
        }
    }

    @Test
    public void testThatPlayerIsAddedToInitialCave() {
        configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();

        NewGame game = new NewGame(randomNumberGenerator);
        game.startGame();

        final int actualPlayerCaveIndex = game.getPlayerCave();
        assertEquals(PLAYER_STARTING_CAVE_INDEX, actualPlayerCaveIndex);
    }

    @Test
    public void testThatPlayerCaveIsAddedToGameMap() {
        configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();

        NewGame game = new NewGame(randomNumberGenerator);
        game.startGame();

        final int actualPlayerCaveIndex = game.getPlayerCave();
        assertEquals(PLAYER_STARTING_CAVE_INDEX, actualPlayerCaveIndex);

        Cave playerCave=game.getGameMap().getCaves().get(PLAYER_STARTING_CAVE_INDEX);
        Player player=game.getPlayer();
        assertTrue(playerCave.getGameObjects().contains(player));
    }

    @Test
    public void testThatWumpusIsAddedToCaveGameMap(){
        configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();

        NewGame game = new NewGame(randomNumberGenerator);
        game.startGame();

        final int actualWumpusCaveIndex = game.getWumpusCave();
        assertEquals(WUMPUS_STARTING_CAVE_INDEX,actualWumpusCaveIndex);

        Cave wumpusCave=game.getGameMap().getCaves().get(WUMPUS_STARTING_CAVE_INDEX);
        Wumpus wumpus=game.getWumpus();
        assertTrue(wumpusCave.getGameObjects().contains(wumpus));

    }

    @Test
    public void testThatThreeBatsAreAddedToCaveGameMap() {
        configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();

        int[] batsStartingCavesIndexes = {FIRST_BAT_STARTING_CAVE_INDEX, SECOND_BAT_STARTING_CAVE_INDEX, THIRD_BAT_STARTING_CAVE_INDEX};

        NewGame game = new NewGame(randomNumberGenerator);
        game.startGame();

        List<Bat> listOfBats = game.getBats();

        assertEquals(GameInitialConfigurations.NUMBER_OF_BATS,listOfBats.size());

        for(int i = 0; i < listOfBats.size(); i++) {
            assertEquals(batsStartingCavesIndexes[i], listOfBats.get(i).getCave().getNumber());
            Cave batCave= game.getGameMap().getCaves().get(batsStartingCavesIndexes[i]);
            Bat bat = listOfBats.get(i);
            assertTrue(batCave.getGameObjects().contains(bat));
        }
    }

    @Test
    public void testThatTwoPitsAreAddedToCaveGameMap(){
        configureMockingBasedOnDefaultLocationOfGameObjectsOnMap();

        int[] pitsInCavesIndexes = {FIRST_PIT_CAVE, SECOND_PIT_CAVE};

        NewGame game = new NewGame(randomNumberGenerator);
        game.startGame();

        List<Pit> listOfPits = game.getPits();

        assertEquals(GameInitialConfigurations.NUMBER_OF_PITS,listOfPits.size());

        for(int i = 0; i < listOfPits.size(); i++) {
            assertEquals(pitsInCavesIndexes[i], listOfPits.get(i).getCave().getNumber());
            Cave PitInCave= game.getGameMap().getCaves().get(pitsInCavesIndexes[i]);
            Pit pit = listOfPits.get(i);
            assertTrue(PitInCave.getGameObjects().contains(pit));
        }

    }

    @Test
    public void testThatWumpusIsNotInitializedInSameCaveAsPlayer(){
        final int wumpusStartingWrongCaveIndex = 9;
        final int wumpusStartingCorrectCaveIndex = 17;

        Mockito.when(randomNumberGenerator.generateNumber(GameInitialConfigurations.NUMBER_OF_CAVES)).thenReturn(
                PLAYER_STARTING_CAVE_INDEX,
                wumpusStartingWrongCaveIndex,
                wumpusStartingCorrectCaveIndex,
                FIRST_BAT_STARTING_CAVE_INDEX,
                SECOND_BAT_STARTING_CAVE_INDEX,
                THIRD_BAT_STARTING_CAVE_INDEX,
                FIRST_PIT_CAVE,
                SECOND_PIT_CAVE
                );

        NewGame game = new NewGame(randomNumberGenerator);
        game.startGame();

        final int actualWumpusCaveIndex = game.getWumpusCave();
        assertEquals(wumpusStartingCorrectCaveIndex,actualWumpusCaveIndex);

        Cave wumpusCave=game.getGameMap().getCaves().get(wumpusStartingCorrectCaveIndex);
        Wumpus wumpus=game.getWumpus();
        assertTrue(wumpusCave.getGameObjects().contains(wumpus));
    }

    @Test
    public void testThatBatsAreNotInitializedAtSameLocation(){
        final int secondBatWrongStartingCaveIndex = 19;
        final int thirdBatWrongStartingCaveIndex = 19;

        final int secondBatCorrectStartingCaveIndex = 13;
        final int thirdBatCorrectStartingCaveIndex = 14;
        int[] batsStartingCavesIndexes = {FIRST_BAT_STARTING_CAVE_INDEX, secondBatCorrectStartingCaveIndex, thirdBatCorrectStartingCaveIndex};

        Mockito.when(randomNumberGenerator.generateNumber(GameInitialConfigurations.NUMBER_OF_CAVES)).thenReturn(
                PLAYER_STARTING_CAVE_INDEX,
                WUMPUS_STARTING_CAVE_INDEX,
                FIRST_BAT_STARTING_CAVE_INDEX,
                secondBatWrongStartingCaveIndex,
                secondBatCorrectStartingCaveIndex,
                thirdBatWrongStartingCaveIndex,
                thirdBatCorrectStartingCaveIndex,
                FIRST_PIT_CAVE,
                SECOND_PIT_CAVE
        );

        NewGame game = new NewGame(randomNumberGenerator);
        game.startGame();

        List<Bat> listOfBats = game.getBats();

        assertEquals(GameInitialConfigurations.NUMBER_OF_BATS,listOfBats.size());

        for(int i = 0; i < listOfBats.size(); i++) {
            assertEquals(batsStartingCavesIndexes[i], listOfBats.get(i).getCave().getNumber());
            Cave batCave= game.getGameMap().getCaves().get(batsStartingCavesIndexes[i]);
            Bat bat = listOfBats.get(i);
            assertTrue(batCave.getGameObjects().contains(bat));
        }
    }

    @Test
    public void testThatPitsAreNotInitializedAtSameLocation(){
        final int secondWrongPitCave = 3;
        final int secondCorrectPitCave = 13;
        int[] correctPitsInCavesIndexes = {FIRST_PIT_CAVE, secondCorrectPitCave};

        Mockito.when(randomNumberGenerator.generateNumber(GameInitialConfigurations.NUMBER_OF_CAVES)).thenReturn(
                PLAYER_STARTING_CAVE_INDEX,
                WUMPUS_STARTING_CAVE_INDEX,
                FIRST_BAT_STARTING_CAVE_INDEX,
                SECOND_BAT_STARTING_CAVE_INDEX,
                THIRD_BAT_STARTING_CAVE_INDEX,
                FIRST_PIT_CAVE,
                secondWrongPitCave,
                secondCorrectPitCave
        );

        NewGame game = new NewGame(randomNumberGenerator);
        game.startGame();

        List<Pit> listOfPits = game.getPits();

        assertEquals(GameInitialConfigurations.NUMBER_OF_PITS,listOfPits.size());

        for(int i = 0; i < listOfPits.size(); i++) {
            assertEquals(correctPitsInCavesIndexes[i], listOfPits.get(i).getCave().getNumber());
            Cave PitInCave= game.getGameMap().getCaves().get(correctPitsInCavesIndexes[i]);
            Pit pit = listOfPits.get(i);
            assertTrue(PitInCave.getGameObjects().contains(pit));
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
                wumpusStartingWrongCaveIndex,
                wumpusStartingCorrectCaveIndex,
                firstBatStartingWrongCaveIndex,
                firstBatStartingCorrectCaveIndex,
                SECOND_BAT_STARTING_CAVE_INDEX,
                THIRD_BAT_STARTING_CAVE_INDEX,
                firstPitStartingWrongCaveIndex,
                firstPitStartingCorrectCaveIndex,
                SECOND_PIT_CAVE
        );

        NewGame game = new NewGame(randomNumberGenerator);
        game.startGame();

        final int actualWumpusCaveIndex = game.getWumpusCave();
        assertEquals(wumpusStartingCorrectCaveIndex,actualWumpusCaveIndex);

        Cave wumpusCave =game.getGameMap().getCaves().get(wumpusStartingCorrectCaveIndex);
        Wumpus wumpus= game.getWumpus();
        assertTrue(wumpusCave.getGameObjects().contains(wumpus));

        final int actualFirstBatCaveIndex = game.getBats().get(0).getCave().getNumber();
        assertEquals(firstBatStartingCorrectCaveIndex,actualFirstBatCaveIndex);

        Cave firstBatCave = game.getGameMap().getCaves().get(firstBatStartingCorrectCaveIndex);
        Bat bat = game.getBats().get(0);
        assertTrue(firstBatCave.getGameObjects().contains(bat));

        final int actualFirstPitCaveIndex = game.getPits().get(0).getCave().getNumber();
        assertEquals(firstPitStartingCorrectCaveIndex,actualFirstPitCaveIndex);

        Cave firstPitCave = game.getGameMap().getCaves().get(firstPitStartingCorrectCaveIndex);
        Pit pit = game.getPits().get(0);
        assertTrue(firstPitCave.getGameObjects().contains(pit));
    }


    //TODO Implement same test cases as those in presenter
}
