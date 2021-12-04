package acceptance;

import io.cucumber.java.en.And;
import io.cucumber.java.en.But;
import io.cucumber.java.en.Then;
import model.game.GameInitialConfigurations;
import support.GameWorld;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        final int actualWumpusCave = gameWorld.executeActionsAndGetWumpusPresenter().getWumpusCaveIndex();
        assertEquals(expectedCaveIndex, actualWumpusCave);
    }

    @And("wumpus remains asleep")
    public void wumpusRemainsAsleep() {
        gameWorld.getRNGBuilder().makeWumpusSleep();
    }

    @And("wumpus will wake up and move from cave {int} to cave {int}")
    public void wumpusWillWakeUpAndMoveFromCaveToCave(int currentWumpusCave, int cave) throws Exception {
        int caveIndex = GameInitialConfigurations.getCaveIndexOutOfCave(currentWumpusCave, cave);
        gameWorld.getRNGBuilder().makeWumpusMoveTo(caveIndex);
    }

    @Then("wumpus is dead")
    public void wumpusIsDead() {
        assertTrue(gameWorld.executeActionsAndGetWumpusPresenter().isWumpusDead());
    }
}
