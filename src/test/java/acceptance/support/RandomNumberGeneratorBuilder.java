package acceptance.support;

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
    List<Integer> randomReturnsWhenCalledWith4 = new ArrayList<>();
    List<Integer> randomReturnsWhenCalledWith5 = new ArrayList<>();
    List<Integer> teleportCaves = new ArrayList<>();

    int playerStartingCave = 0;
    int enemyPlayerStartingCave = 6;
    int wumpusStartingCave = 18;
    List<Integer> batsStartingCaves = new ArrayList<>(Arrays.asList(19, 13));
    List<Integer> pitsStartingCaves = new ArrayList<>(Arrays.asList(3, 13));

    public RandomNumberGenerator build() {
        setUpAllTheMocks();
        return randomNumberGenerator;
    }

    private void setUpAllTheMocks() {
        addStartingCavesToMockedList();

        mockRandomNumberGeneratorCalledWith(GameInitialConfigurations.NUMBER_OF_CAVES, randomReturnsWhenCalledWith20);
        mockRandomNumberGeneratorCalledWith(GameInitialConfigurations.MAXIMUM_NUMBER_FOR_CALCULATING_WUMPUS_WAKEUP_PROBABILITY, randomReturnsWhenCalledWith4);
        mockRandomNumberGeneratorCalledWith(GameInitialConfigurations.MAX_POSSIBILITY_ENEMY_PLAYER_TAKE_ACTION, randomReturnsWhenCalledWith2);
        mockRandomNumberGeneratorCalledWith(GameInitialConfigurations.MAX_CAVES_ENEMY_PLAYER_CAN_SHOOT, randomReturnsWhenCalledWith5);
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
        randomReturnsWhenCalledWith4.add(GameInitialConfigurations.WUMPUS_SLEEP_NUMBER);
    }

    public void makeWumpusMoveTo(int caveIndex) {
        randomReturnsWhenCalledWith4.add(GameInitialConfigurations.WUMPUS_WAKEUP_NUMBER);
        randomReturnsWhenCalledWith3.add(caveIndex);
    }

    public void makeEnemyPlayerMoveToCave(int caveIndex) {
        randomReturnsWhenCalledWith2.add(GameInitialConfigurations.ENEMY_PLAYER_MOVE_NUMBER);
        randomReturnsWhenCalledWith3.add(caveIndex);
    }

    public void makeEnemyPlayerShootAtCaves(List<Integer> caveIndexes) {
        randomReturnsWhenCalledWith2.add(GameInitialConfigurations.ENEMY_PLAYER_SHOOT_NUMBER);

        final int numberOfCavesEnemyPlayerWillShoot = caveIndexes.size() - 1; // Since generate number is 0 based
        randomReturnsWhenCalledWith5.add(numberOfCavesEnemyPlayerWillShoot);

        randomReturnsWhenCalledWith3.addAll(caveIndexes);
    }

    public void makeEnemyMoveIfEnemyHasNoEnoughActions(int numberOfPlayerActions) {
        final int numberOfActualEnemyPlayerActions = randomReturnsWhenCalledWith2.size();
        for (int i = 0; i < numberOfPlayerActions - numberOfActualEnemyPlayerActions; i++) {
            makeEnemyPlayerMoveToCave(1);
        }
    }

    public void makeWumpusSleepIfWumpusHasNoEnoughAction(int numberOfShootActions) {
        final int numberOfActualWumpusAction = randomReturnsWhenCalledWith4.size();
        final long numberOfEnemyPlayerShootActions = randomReturnsWhenCalledWith2.stream().filter(n -> n == 1).count();
        for (int i = 0; i < (numberOfShootActions + numberOfEnemyPlayerShootActions) - numberOfActualWumpusAction; i++) {
            makeWumpusSleep();
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
