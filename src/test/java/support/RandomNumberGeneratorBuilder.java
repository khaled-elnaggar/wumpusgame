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
        setUpAllTheMocks();
        return randomNumberGenerator;
    }

    private void setUpAllTheMocks() {
        addStartingCavesToMockedList();

        mockRandomNumberGeneratorCalledWith(GameInitialConfigurations.NUMBER_OF_CAVES, randomReturnsWhenCalledWith20);
        mockRandomNumberGeneratorCalledWith(GameInitialConfigurations.MAX_POSSIBILITY_ENEMY_PLAYER_TAKE_ACTION, randomReturnsWhenCalledWith2);
        mockRandomNumberGeneratorCalledWith(GameInitialConfigurations.NUMBER_OF_LINKED_CAVES, randomReturnsWhenCalledWith3);
    }

    private void addStartingCavesToMockedList() {
        randomReturnsWhenCalledWith20.addAll(Arrays.asList(
                playerStartingCave,
                enemyPlayerStartingCave,
                wumpusStartingCave));
        randomReturnsWhenCalledWith20.addAll(batsStartingCaves);
        randomReturnsWhenCalledWith20.addAll(pitsStartingCaves);
    }

    private void mockRandomNumberGeneratorCalledWith(int randomNumberMax, final List<Integer> listWithMockedValues) {
        Mockito.when(randomNumberGenerator.generateNumber(randomNumberMax)).thenAnswer(new Answer<Integer>() {
            int current = 0;

            @Override
            public Integer answer(InvocationOnMock invocationOnMock) throws Throwable {
                return listWithMockedValues.get(current++);
            }
        });
    }

    public void setNextRandomCaveForArrow(int caveIndex) {
        randomReturnsWhenCalledWith3.add(caveIndex);
    }

    public void addCaveToTeleportTo(int cave) {
        teleportCaves.add(cave);
    }

    public void updateTeleportCavesList() {
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

    public void makeEnemyMoveIfEnemyHasNoAction(int playerActionsCount) {
        final int enemyPlayerActionsCount = randomReturnsWhenCalledWith2.size();
        for (int i = 0; i < playerActionsCount - enemyPlayerActionsCount; i++) {
            randomReturnsWhenCalledWith2.add(0);
            randomReturnsWhenCalledWith3.add(1);
        }
    }

    public void setPlayerStartingCave(int playerStartingCave) {
        this.playerStartingCave = playerStartingCave;
    }

    public void setEnemyPlayerStartingCave(int enemyPlayerStartingCave) {
        this.enemyPlayerStartingCave = enemyPlayerStartingCave;
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
}
