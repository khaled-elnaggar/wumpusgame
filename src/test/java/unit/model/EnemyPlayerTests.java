package unit.model;

import model.game.GameInitialConfigurations;
import model.game.NewGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import unit.support.MockedRandomNumberGeneratorWorld;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EnemyPlayerTests {
    MockedRandomNumberGeneratorWorld mockedRNGWorld;

    @BeforeEach
    public void setUp(){
        mockedRNGWorld = new MockedRandomNumberGeneratorWorld();
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
