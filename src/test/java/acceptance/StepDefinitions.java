package acceptance;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import model.game.GameInitialConfigurations;
import org.mockito.Mockito;
import presenter.WumpusPresenter;
import presenter.WumpusPresenterImpl;
import utilities.RandomNumberGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class StepDefinitions {

    RandomNumberGenerator randomNumberGenerator;
    WumpusPresenter wumpusPresenter;

    final int wumpusStartingCave = 18;
    final int firstBatStartingCave = 19;
    final int secondBatStartingCave = 13;
    final int firstPitCave = 3;
    final int secondPitCave = 13;

    @Given("player is in cave {int}")
    public void player_is_in_cave(Integer playerStartingCave) {
        randomNumberGenerator=mock(RandomNumberGenerator.class);
        Mockito.when(randomNumberGenerator.generateNumber(GameInitialConfigurations.NUMBER_OF_CAVES)).thenReturn(
                playerStartingCave,
                wumpusStartingCave,
                firstBatStartingCave,
                secondBatStartingCave,
                firstPitCave,
                secondPitCave);

        wumpusPresenter = new WumpusPresenterImpl(randomNumberGenerator);
        wumpusPresenter.startNewGame();
    }
    @When("player moves to cave {int}")
    public void player_moves_to_cave(Integer caveToMoveTo) {
        wumpusPresenter.move(caveToMoveTo);
    }
    @Then("player will be at cave {int}")
    public void player_will_be_at_cave(Integer expectedPlayerCave) {
        final int playerCurrentRoom = wumpusPresenter.getPlayerCaveIndex();
        assertEquals(expectedPlayerCave, playerCurrentRoom);

        final boolean expectedStatusOfGameIsOver = false;
        final boolean isGameOver = wumpusPresenter.isGameOver();
        assertEquals(isGameOver, expectedStatusOfGameIsOver);
    }
}
