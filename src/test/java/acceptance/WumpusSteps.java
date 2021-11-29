package acceptance;

import io.cucumber.java.en.And;
import io.cucumber.java.en.But;
import io.cucumber.java.en.Then;
import model.game.GameInitialConfigurations;
import support.GameWorld;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WumpusSteps {

    private final GameWorld gameWorld;

    public WumpusSteps(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    @And("wumpus is in cave {int}")
    public void wumpusIsInCave(int wumpusStartingCave) {
        gameWorld.getRNGBuilder().setWumpusStartingCave(wumpusStartingCave);
    }

    @Then("wumpus will be at cave {int}")
    public void wumpusWillBeAtCave(int expectedCaveIndex) {
        final int actualWumpusCave = gameWorld.getWumpusPresenter().getWumpusCaveIndex();
        assertEquals(expectedCaveIndex, actualWumpusCave);
    }

    @And("wumpus will remain asleep")
    public void wumpusWillRemainAsleep() {
        gameWorld.getRNGBuilder().makeWumpusSleep();
    }

    @But("wumpus will wake up and move to cave {int}")
    public void wumpusWillWakeUpAndMoveToCave(int cave) throws Exception {
        final int currentWumpusCave = gameWorld.getWumpusPresenter().getWumpusCaveIndex();
        int caveIndex = gameWorld.getCaveIndexOutOfCave(currentWumpusCave, cave);
        gameWorld.getRNGBuilder().makeWumpusMoveTo(caveIndex);
    }
}
