package acceptance;

import io.cucumber.java.Transpose;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import support.GameWorld;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlayerSteps {
    private final GameWorld gameWorld;

    public PlayerSteps(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    @Given("player is in cave {int}")
    public void player_is_in_cave(int playerStartingCave) {
        gameWorld.getRNGBuilder().setPlayerStartingCave(playerStartingCave);
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
}
