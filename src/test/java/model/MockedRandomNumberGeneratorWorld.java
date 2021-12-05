package model;

import model.game.GameInitialConfigurations;
import model.game.NewGame;
import org.mockito.Mockito;
import utilities.RandomNumberGenerator;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;


public class MockedRandomNumberGeneratorWorld {
    RandomNumberGenerator randomNumberGenerator = mock(RandomNumberGenerator.class, withSettings().lenient());

    public static final int PLAYER_STARTING_CAVE_INDEX = 9;
    public static final int ENEMY_PLAYER_STARTING_CAVE_INDEX = 11;
    public static final int WUMPUS_STARTING_CAVE_INDEX = 18;
    public static final int FIRST_BAT_STARTING_CAVE_INDEX = 19;
    public static final int SECOND_BAT_STARTING_CAVE_INDEX = 13;
    public static final int FIRST_PIT_CAVE = 3;
    public static final int SECOND_PIT_CAVE = 13;
    static final int ENEMY_PLAYER_MIDDLE_CAVE_INDEX = 1;

    void configureMockingBasedOnDefaultLocationOfGameObjectsOnMap() {
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

    void makeWumpusSleep() {
        Mockito.when(randomNumberGenerator.generateNumber(GameInitialConfigurations.MAXIMUM_NUMBER_FOR_CALCULATING_WUMPUS_WAKEUP_PROBABILITY))
                .thenReturn(GameInitialConfigurations.WUMPUS_SLEEP_NUMBER);
    }

    void makeWumpusWakeUp() {
        Mockito.when(randomNumberGenerator.generateNumber(GameInitialConfigurations.MAXIMUM_NUMBER_FOR_CALCULATING_WUMPUS_WAKEUP_PROBABILITY)).thenReturn(
                GameInitialConfigurations.WUMPUS_WAKEUP_NUMBER);
    }

    void makeEnemyPlayerGoBackAndForth() {
        Mockito.when(randomNumberGenerator.generateNumber(GameInitialConfigurations.MAX_POSSIBILITY_ENEMY_PLAYER_TAKE_ACTION))
                .thenReturn(GameInitialConfigurations.ENEMY_PLAYER_MOVE_NUMBER);

        Mockito.when(randomNumberGenerator.generateNumber(GameInitialConfigurations.NUMBER_OF_LINKED_CAVES))
                .thenReturn(1);
    }

    void makeEnemyPlayerShoot() {
        Mockito.when(randomNumberGenerator.generateNumber(GameInitialConfigurations.MAX_POSSIBILITY_ENEMY_PLAYER_TAKE_ACTION))
                .thenReturn(GameInitialConfigurations.ENEMY_PLAYER_SHOOT_NUMBER);
    }

    void makeEnemyPlayerMove() {
        Mockito.when(randomNumberGenerator.generateNumber(GameInitialConfigurations.MAX_POSSIBILITY_ENEMY_PLAYER_TAKE_ACTION))
                .thenReturn(GameInitialConfigurations.ENEMY_PLAYER_MOVE_NUMBER);
    }

    public RandomNumberGenerator getRandomNumberGenerator() {
        return randomNumberGenerator;
    }

    public NewGame getGame() {
        NewGame game = new NewGame(randomNumberGenerator);
        game.startGame();
        return game;
    }
}
