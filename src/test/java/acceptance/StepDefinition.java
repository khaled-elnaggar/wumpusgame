package acceptance;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import model.game.GameInitialConfigurations;
import org.mockito.Mockito;
import presenter.WumpusPresenter;
import presenter.WumpusPresenterImpl;
import utilities.RandomNumberGenerator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class StepDefinition {

    RandomNumberGenerator randomNumberGenerator;
    WumpusPresenter wumpusPresenter;
    int playerStartingCave = 0;
    int wumpusStartingCave = 18;
    int firstBatStartingCave = 19;
    int secondBatStartingCave = 13;
    int firstPitCave = 3;
    int secondPitCave = 13;

    public void initializeWumpusPresenter(){
        if(this.wumpusPresenter == null){
            mockTheRandomNumberGenerator();
            wumpusPresenter = new WumpusPresenterImpl(randomNumberGenerator);
            wumpusPresenter.startNewGame();
        }
    }

    private void mockTheRandomNumberGenerator() {
        randomNumberGenerator=mock(RandomNumberGenerator.class);
        Mockito.when(randomNumberGenerator.generateNumber(GameInitialConfigurations.NUMBER_OF_CAVES)).thenReturn(
                playerStartingCave,
                wumpusStartingCave,
                firstBatStartingCave,
                secondBatStartingCave,
                firstPitCave,
                secondPitCave);
    }

    @Given("player is in cave {int}")
    public void player_is_in_cave(int playerStartingCave) {
        this.playerStartingCave = playerStartingCave;
    }

    @And("wumpus is in cave {int}")
    public void wumpusIsInCave(int wumpusStartingCave) {
        this.wumpusStartingCave = wumpusStartingCave;
    }

    @When("player moves to cave {int}")
    public void player_moves_to_cave(Integer caveToMoveTo) {
        initializeWumpusPresenter();
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

    @Then("game is over")
    public void gameIsOver() {
        final boolean expectedStatusOfGameIsOver = true;
        final boolean isGameOver = wumpusPresenter.isGameOver();
        assertEquals(isGameOver, expectedStatusOfGameIsOver);
    }

    @Then("player senses that {string}")
    public void playerSensesThat(String warning) {
        List<String> warnings = this.wumpusPresenter.getWarnings();
        assertTrue(warnings.contains(warning));
    }
}
