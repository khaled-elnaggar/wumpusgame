package acceptance;

import io.cucumber.java.en.And;
import io.cucumber.java.en.But;
import io.cucumber.java.en.Then;
import support.GameWorld;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EnemySteps {
    private final GameWorld gameWorld;

    public EnemySteps(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    @And("enemy player is in cave {int}")
    public void enemyPlayerIsInCave(int cave) {
        gameWorld.getRNGBuilder().setEnemyPlayerStartingCave(cave);
    }

    @But("enemy player will wake up and move from cave {int} to cave {int}")
    public void enemyPlayerWillWakeUpAndMoveToCave(int currentCave, int cave) throws Exception {
        int caveIndex = gameWorld.getCaveIndexOutOfCave(currentCave, cave);
        gameWorld.getRNGBuilder().makeEnemyPlayerMoveToCave(caveIndex);
    }

    @Then("enemy player will be at cave {int}")
    public void enemyWillBeAtCave(int expectedCave) {
        final int actualWumpusCave = gameWorld.getWumpusPresenter().getEnemyPlayerCave();
        assertEquals(expectedCave, actualWumpusCave);
    }

    @And("enemy player remains asleep")
    public void enemyPlayerRemainsAsleep() {
        gameWorld.getRNGBuilder().makeEnemyPlayerSleep();
    }

    @And("enemy player is dead")
    public void enemyPlayerIsDead() {
        assertTrue(gameWorld.getWumpusPresenter().isEnemyPlayerDead());
    }
}
