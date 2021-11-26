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

    RandomNumberGenerator randomNumberGenerator;

    List<Integer> randomReturnsWhenCalledWith20 = new ArrayList<>();
    List<Integer> teleportCaves = new ArrayList<>();

    int playerStartingCave = 0;
    int wumpusStartingCave = 18;
    int firstBatStartingCave = 19;
    int secondBatStartingCave = 13;
    int firstPitCave = 3;
    int secondPitCave = 13;

    public RandomNumberGenerator build() {
        if(randomNumberGenerator == null){
            mockTheRandomNumberGenerator();
        }

        return randomNumberGenerator;
    }

    private void mockTheRandomNumberGenerator() {
        randomNumberGenerator = mock(RandomNumberGenerator.class);
        randomReturnsWhenCalledWith20.addAll(Arrays.asList(playerStartingCave,
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
    }

    public void setPlayerStartingCave(int playerStartingCave) {
        this.playerStartingCave = playerStartingCave;
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

    public void addCaveToTeleportTo(int cave){
        teleportCaves.add(cave);
    }
}
