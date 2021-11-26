package acceptance;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import support.GameWorld;

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
}
