package support;

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
    List<Integer> randomReturnsWhenCalledWith2 = new ArrayList<>();
    List<Integer> randomReturnsWhenCalledWith3 = new ArrayList<>();
    List<Integer> teleportCaves = new ArrayList<>();

    int playerStartingCave = 0;
    int enemyPlayerStartingCave = 6;
    int wumpusStartingCave = 18;
    List<Integer> batsStartingCaves = new ArrayList<>(Arrays.asList(19, 13));
    List<Integer> pitsStartingCaves = new ArrayList<>(Arrays.asList(3, 13));
    public static final int MAXIMUM_NUMBER_FOR_CALCULATING_WUMPUS_WAKEUP_PROBABILITY = 4;

    public RandomNumberGenerator build() {
        mockTheRandomNumberGenerator();
        return randomNumberGenerator;
    }

    private void mockTheRandomNumberGenerator() {
        randomReturnsWhenCalledWith20.addAll(Arrays.asList(
                playerStartingCave,
                enemyPlayerStartingCave,
                wumpusStartingCave));
        randomReturnsWhenCalledWith20.addAll(batsStartingCaves);
        randomReturnsWhenCalledWith20.addAll(pitsStartingCaves);

        Mockito.when(randomNumberGenerator.generateNumber(GameInitialConfigurations.NUMBER_OF_CAVES)).thenAnswer(new Answer<Integer>() {
            int current = 0;

            @Override
            public Integer answer(InvocationOnMock invocationOnMock) throws Throwable {
                return randomReturnsWhenCalledWith20.get(current++);
            }
        });

        Mockito.when(randomNumberGenerator.generateNumber(GameInitialConfigurations.MAX_POSSIBILITY_ENEMY_PLAYER_TAKE_ACTION)).thenAnswer(new Answer<Integer>() {
            int current = 0;

            @Override
            public Integer answer(InvocationOnMock invocationOnMock) throws Throwable {
                return randomReturnsWhenCalledWith2.get(current++);
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

    public void addCaveToTeleportTo(int cave) {
        teleportCaves.add(cave);
    }

    public void updateMockArrays() {
        randomReturnsWhenCalledWith20.addAll(teleportCaves);
        teleportCaves.clear();
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

    public void makeEnemyPlayerMoveToCave(int caveIndex) {
        final int numberAtWhichEnemyPlayerWillMove = 0;
        randomReturnsWhenCalledWith2.add(numberAtWhichEnemyPlayerWillMove);
        randomReturnsWhenCalledWith3.add(caveIndex);
    }

    public void makeEnemyPlayerShootAtCaves(List<Integer> caveIndexes) {

        final int numberAtWhichEnemyPlayerWillShoot = 1;
        randomReturnsWhenCalledWith2.add(numberAtWhichEnemyPlayerWillShoot);

        final int numberOfCavesEnemyPlayerWillShoot = caveIndexes.size() - 1; // Since generate number is 0 based
        Mockito.when(randomNumberGenerator.generateNumber(GameInitialConfigurations.MAX_CAVES_ENEMY_PLAYER_CAN_SHOOT)).thenReturn(
                numberOfCavesEnemyPlayerWillShoot);

        randomReturnsWhenCalledWith3.addAll(caveIndexes);
    }

    public void setPlayerStartingCave(int playerStartingCave) {
        this.playerStartingCave = playerStartingCave;
    }

    public void setWumpusStartingCave(int wumpusStartingCave) {
        this.wumpusStartingCave = wumpusStartingCave;
    }

    public void setBatStartingCave(int batNumber, int cave) {
        batsStartingCaves.set(batNumber - 1, cave);
    }

    public void setPitStartingCave(int pitNumber, int cave) {
        pitsStartingCaves.set(pitNumber - 1, cave);
    }

    public void setNextRandomCaveForArrow(int caveIndex) {
        randomReturnsWhenCalledWith3.add(caveIndex);
    }

    public void setEnemyPlayerStartingCave(int enemyPlayerStartingCave) {
        this.enemyPlayerStartingCave = enemyPlayerStartingCave;
    }

    public void makeEnemyMoveIfEnemyHasNoAction(int size) {
        for (int i = 0; i < size - randomReturnsWhenCalledWith2.size(); i++) {
            randomReturnsWhenCalledWith2.add(0);
            randomReturnsWhenCalledWith3.add(1);
        }
    }
}
