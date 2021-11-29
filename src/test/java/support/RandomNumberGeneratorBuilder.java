package support;

import io.cucumber.java.PendingException;
import model.game.GameInitialConfigurations;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import utilities.RandomNumberGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;

public class RandomNumberGeneratorBuilder {

    RandomNumberGenerator randomNumberGenerator = mock(RandomNumberGenerator.class);

    List<Integer> randomReturnsWhenCalledWith20 = new ArrayList<>();
    List<Integer> randomReturnsWhenCalledWith3 = new ArrayList<>();
    List<Integer> teleportCaves = new ArrayList<>();

    int playerStartingCave = 0;
    int enemyPlayerStartingCave = 6;
    int wumpusStartingCave = 18;
    int firstBatStartingCave = 19;
    int secondBatStartingCave = 13;
    int firstPitCave = 3;
    int secondPitCave = 13;
    public static final int MAXIMUM_NUMBER_FOR_CALCULATING_WUMPUS_WAKEUP_PROBABILITY = 4;

    public RandomNumberGenerator build() {
        mockTheRandomNumberGenerator();
        return randomNumberGenerator;
    }

    private void mockTheRandomNumberGenerator() {
        randomReturnsWhenCalledWith20.addAll(Arrays.asList(
                playerStartingCave,
                enemyPlayerStartingCave,
                wumpusStartingCave,
                firstBatStartingCave,
                secondBatStartingCave,
                firstPitCave,
                secondPitCave));

        randomReturnsWhenCalledWith20.addAll(teleportCaves);

        Mockito.when(randomNumberGenerator.generateNumber(GameInitialConfigurations.NUMBER_OF_CAVES)).thenAnswer(new Answer<Integer>() {
            int current = 0;

            @Override
            public Integer answer(InvocationOnMock invocationOnMock) throws Throwable {
                return randomReturnsWhenCalledWith20.get(current++);
            }
        });

        Mockito.when(randomNumberGenerator.generateNumber(GameInitialConfigurations.NUMBER_OF_LINKED_CAVES)).thenAnswer(new Answer<Integer>() {
            int current = 0;

            @Override
            public Integer answer(InvocationOnMock invocationOnMock) throws Throwable {
                return randomReturnsWhenCalledWith3.get(current++);
            }
        });
    }

    public void setPlayerStartingCave(int playerStartingCave) {
        this.playerStartingCave = playerStartingCave;
    }

    public void makeWumpusSleep() {
        final int numberAtWhichWumpusWillRemainSleeping = 0;
        Mockito.when(randomNumberGenerator.generateNumber(MAXIMUM_NUMBER_FOR_CALCULATING_WUMPUS_WAKEUP_PROBABILITY)).thenReturn(
                numberAtWhichWumpusWillRemainSleeping);
    }

    public void makeWumpusMoveTo(int caveIndex) {
        final int numberAtWhichWumpusWillWakeup = 1;
        Mockito.when(randomNumberGenerator.generateNumber(MAXIMUM_NUMBER_FOR_CALCULATING_WUMPUS_WAKEUP_PROBABILITY)).thenReturn(
                numberAtWhichWumpusWillWakeup);
        randomReturnsWhenCalledWith3.add(caveIndex);
    }

    public void setWumpusStartingCave(int wumpusStartingCave) {
        this.wumpusStartingCave = wumpusStartingCave;
    }

    public void setFirstBatStartingCave(int firstBatStartingCave) {
        this.firstBatStartingCave = firstBatStartingCave;
    }

    public void setFirstPitCave(int firstPitCave) {
        this.firstPitCave = firstPitCave;
    }

    public void addCaveToTeleportTo(int cave) {
        teleportCaves.add(cave);
    }

    public void setNextRandomCaveForArrow(int caveIndex) {
        randomReturnsWhenCalledWith3.add(caveIndex);
    }

    public void setEnemyPlayerStartingCave(int enemyPlayerStartingCave) {
        this.enemyPlayerStartingCave = enemyPlayerStartingCave;
    }

    public void makeEnemyPlayerMoveToCave(int caveIndex) {
        final int numberAtWhichEnemyPlayerWillMove = 0;
        Mockito.when(randomNumberGenerator.generateNumber(1)).thenReturn(
                numberAtWhichEnemyPlayerWillMove);

        randomReturnsWhenCalledWith3.add(caveIndex);
    }
}
