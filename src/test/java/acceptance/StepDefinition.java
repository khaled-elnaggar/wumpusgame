package acceptance;

import io.cucumber.java.Transpose;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import presenter.WumpusPresenter;
import support.GameWorld;
import utilities.RandomNumberGenerator;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StepDefinition {
    RandomNumberGenerator randomNumberGenerator;
    GameWorld gameWorld = new GameWorld();
    List<Integer> randomReturnsWhenCalledWith20 = new ArrayList<>();

    @Given("player is in cave {int}")
    public void player_is_in_cave(int playerStartingCave) {
        gameWorld.getRNGBuilder().setPlayerStartingCave(playerStartingCave);
    }

    @And("wumpus is in cave {int}")
    public void wumpusIsInCave(int wumpusStartingCave) {
        gameWorld.getRNGBuilder().setWumpusStartingCave(wumpusStartingCave);
    }

    @And("a bat is in cave {int}")
    public void aBatIsInCave(int cave) {
        gameWorld.getRNGBuilder().setFirstBatStartingCave(cave);
    }

    @And("pit is in cave {int}")
    public void pitIsInCave(int cave) {
        gameWorld.getRNGBuilder().setFirstPitCave(cave);
    }

    @When("player moves to cave {int}")
    public void player_moves_to_cave(Integer caveToMoveTo) {
        gameWorld.getWumpusPresenter().move(caveToMoveTo);
    }

    @When("player moves to caves")
    public void playerMovesToCaves(@Transpose List<Integer> cavesToMoveTo) {
        for (int caveToMoveTo : cavesToMoveTo) {
            gameWorld.getWumpusPresenter().move(caveToMoveTo);
        }
    }

    @Then("player senses that {string}")
    public void playerSensesThat(String warning) {
        List<String> warnings = gameWorld.getWumpusPresenter().getWarnings();
        assertTrue(warnings.contains(warning));
    }

    @Then("player will be at cave {int}")
    public void player_will_be_at_cave(Integer expectedPlayerCave) {
        final int playerCurrentRoom = gameWorld.getWumpusPresenter().getPlayerCaveIndex();
        assertEquals(expectedPlayerCave, playerCurrentRoom);

        final boolean expectedStatusOfGameIsOver = false;
        final boolean isGameOver = gameWorld.getWumpusPresenter().isGameOver();
        assertEquals(isGameOver, expectedStatusOfGameIsOver);
    }

    @And("a bat will be at cave {int}")
    public void aBatWillBeAtCave(int cave) {
        //TODO: add bat caves to presenter API?
    }

    @And("bat will teleport player to cave {int} and itself to cave {int}")
    public void batWillTeleportPlayerToCaveAndItselfToCave(int playerCave, int batCave) {
        gameWorld.getRNGBuilder().addCaveToTeleportTo(playerCave);
        gameWorld.getRNGBuilder().addCaveToTeleportTo(batCave);
    }

    @Then("game is over")
    public void gameIsOver() {
        WumpusPresenter wumpusPresenter = gameWorld.getWumpusPresenter();
        final boolean expectedStatusOfGameIsOver = true;
        final boolean isGameOver = wumpusPresenter.isGameOver();
        assertEquals(isGameOver, expectedStatusOfGameIsOver);
    }

}
