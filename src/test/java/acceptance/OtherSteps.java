package acceptance;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import presenter.WumpusPresenter;
import support.GameWorld;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OtherSteps {
    private final GameWorld gameWorld;

    public OtherSteps(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }


    @And("wumpus is in cave {int}")
    public void wumpusIsInCave(int wumpusStartingCave) {
        gameWorld.getRNGBuilder().setWumpusStartingCave(wumpusStartingCave);
    }

    @And("pit is in cave {int}")
    public void pitIsInCave(int cave) {
        gameWorld.getRNGBuilder().setFirstPitCave(cave);
    }


    @Then("game is over")
    public void gameIsOver() {
        WumpusPresenter wumpusPresenter = gameWorld.getWumpusPresenter();
        final boolean expectedStatusOfGameIsOver = true;
        final boolean isGameOver = wumpusPresenter.isGameOver();
        assertEquals(isGameOver, expectedStatusOfGameIsOver);
    }

}
