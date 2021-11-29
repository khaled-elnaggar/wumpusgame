package acceptance;

import io.cucumber.java.en.And;
import io.cucumber.java.en.But;
import io.cucumber.java.en.Then;
import support.GameWorld;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnemySteps {
    private final GameWorld gameWorld;

    public EnemySteps(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    @And("enemy player is in cave {int}")
    public void enemyPlayerIsInCave(int cave) {
        gameWorld.getRNGBuilder().setEnemyPlayerStartingCave(cave);
    }

    @But("enemy player will wake up and move to cave {int}")
    public void enemyPlayerWillWakeUpAndMoveToCave(int cave) throws Exception {
        final int enemyPlayerCurrentCave = gameWorld.getWumpusPresenter().getEnemyPlayerCave();
        int caveIndex = gameWorld.getCaveIndexOutOfCave(enemyPlayerCurrentCave, cave);
        gameWorld.getRNGBuilder().makeEnemyPlayerMoveToCave(caveIndex);
    }

    @Then("enemy player will be at cave {int}")
    public void enemyWillBeAtCave(int expectedCave) {
        final int actualWumpusCave = gameWorld.getWumpusPresenter().getEnemyPlayerCave();
        assertEquals(expectedCave, actualWumpusCave);
    }
}
