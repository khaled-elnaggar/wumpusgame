package acceptance;

import io.cucumber.java.Transpose;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import model.game.GameInitialConfigurations;
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

    @And("player shoots at cave {int}")
    public void playerShootsAtCave(int cave) {
        gameWorld.getWumpusPresenter().shoot(cave);
    }

    @When("player shoots an arrow at caves")
    public void playerShootsAnArrowAtCaves(@Transpose List<Integer> cavesToShoot) {
        int[] cavesArray = new int[cavesToShoot.size()];
        for(int i = 0; i < cavesToShoot.size(); i++){
            cavesArray[i] = cavesToShoot.get(i);
        }
        gameWorld.getWumpusPresenter().shoot(cavesArray);
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

    @And("player used all arrows but {int}")
    public void playerHasArrowLeft(int remainingAroows) throws Exception {
        if (remainingAroows > GameInitialConfigurations.NUMBER_OF_ARROWS) {
            throw new Exception("Logical error, remaining arrows can not be greater than initial arrows");
        }

        final int playerCave = gameWorld.getWumpusPresenter().getPlayerCaveIndex();
        final int firstLinkedCave = GameInitialConfigurations.CAVE_LINKS[playerCave][0];
        for (int i = 0; i < GameInitialConfigurations.NUMBER_OF_ARROWS - remainingAroows; i++) {
            gameWorld.getWumpusPresenter().shoot(firstLinkedCave);
        }
    }
}
