package acceptance;

import io.cucumber.java.Transpose;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import model.game.GameInitialConfigurations;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import presenter.WumpusPresenter;
import presenter.WumpusPresenterImpl;
import utilities.RandomNumberGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class StepDefinition {

    RandomNumberGenerator randomNumberGenerator;
    GameWorld gameWorld = new GameWorld();
    List<Integer> randomReturnsWhenCalledWith20 = new ArrayList<>();
    int playerStartingCave = 0;
    int wumpusStartingCave = 18;
    int firstBatStartingCave = 19;
    int secondBatStartingCave = 13;
    int firstPitCave = 3;
    int secondPitCave = 13;

    class GameWorld {
        private WumpusPresenter wumpusPresenter;

        public WumpusPresenter getWumpusPresenter(){
            if (this.wumpusPresenter == null) {
                mockTheRandomNumberGenerator();
                this.wumpusPresenter = new WumpusPresenterImpl(randomNumberGenerator);
                this.wumpusPresenter.startNewGame();
            }
            return this.wumpusPresenter;
        }
    }

    private void mockTheRandomNumberGenerator() {
        randomNumberGenerator = mock(RandomNumberGenerator.class);
        randomReturnsWhenCalledWith20.addAll(Arrays.asList(playerStartingCave,
                wumpusStartingCave,
                firstBatStartingCave,
                secondBatStartingCave,
                firstPitCave,
                secondPitCave));

        Mockito.when(randomNumberGenerator.generateNumber(GameInitialConfigurations.NUMBER_OF_CAVES)).thenAnswer(new Answer<Integer>() {
            int current = 0;

            @Override
            public Integer answer(InvocationOnMock invocationOnMock) throws Throwable {
                return randomReturnsWhenCalledWith20.get(current++);
            }
        });

    }

    @Given("player is in cave {int}")
    public void player_is_in_cave(int playerStartingCave) {
        this.playerStartingCave = playerStartingCave;
    }

    @And("wumpus is in cave {int}")
    public void wumpusIsInCave(int wumpusStartingCave) {
        this.wumpusStartingCave = wumpusStartingCave;
    }

    @And("a bat is in cave {int}")
    public void aBatIsInCave(int cave) {
        this.firstBatStartingCave = cave;
    }

    @When("player moves to cave {int}")
    public void player_moves_to_cave(Integer caveToMoveTo) {
        WumpusPresenter wumpusPresenter = gameWorld.getWumpusPresenter();
        wumpusPresenter.move(caveToMoveTo);
    }

    @When("player moves to caves")
    public void playerMovesToCaves(@Transpose List<Integer> cavesToMoveTo) {
        WumpusPresenter wumpusPresenter = gameWorld.getWumpusPresenter();
        for (int caveToMoveTo : cavesToMoveTo) {
            wumpusPresenter.move(caveToMoveTo);
        }
    }


    @Then("player senses that {string}")
    public void playerSensesThat(String warning) {
        WumpusPresenter wumpusPresenter = gameWorld.getWumpusPresenter();
        List<String> warnings = wumpusPresenter.getWarnings();
        assertTrue(warnings.contains(warning));
    }

    @Then("player will be at cave {int}")
    public void player_will_be_at_cave(Integer expectedPlayerCave) {
        WumpusPresenter wumpusPresenter = gameWorld.getWumpusPresenter();
        final int playerCurrentRoom = wumpusPresenter.getPlayerCaveIndex();
        assertEquals(expectedPlayerCave, playerCurrentRoom);

        final boolean expectedStatusOfGameIsOver = false;
        final boolean isGameOver = wumpusPresenter.isGameOver();
        assertEquals(isGameOver, expectedStatusOfGameIsOver);
    }

    @And("a bat will be at cave {int}")
    public void aBatWillBeAtCave(int cave) {
        //TODO: add bat caves to presenter API?
    }

    @And("bat will teleport player to cave {int} and itself to cave {int}")
    public void batWillTeleportPlayerToCaveAndItselfToCave(int playerCave, int batCave) {
        randomReturnsWhenCalledWith20.add(playerCave);
        randomReturnsWhenCalledWith20.add(batCave);
    }

    @Then("game is over")
    public void gameIsOver() {
        WumpusPresenter wumpusPresenter = gameWorld.getWumpusPresenter();
        final boolean expectedStatusOfGameIsOver = true;
        final boolean isGameOver = wumpusPresenter.isGameOver();
        assertEquals(isGameOver, expectedStatusOfGameIsOver);
    }

    @And("pit is in cave {int}")
    public void pitIsInCave(int cave) {
        this.firstBatStartingCave = cave;
    }

}
