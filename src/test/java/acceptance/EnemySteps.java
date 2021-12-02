package acceptance;

import io.cucumber.java.Transpose;
import io.cucumber.java.en.And;
import io.cucumber.java.en.But;
import io.cucumber.java.en.Then;
import support.GameWorld;

import java.util.List;

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
        final int actualWumpusCave = gameWorld.executeActionsAndGetWumpusPresenter().getEnemyPlayerCave();
        assertEquals(expectedCave, actualWumpusCave);
    }

    @And("enemy player remains asleep")
    public void enemyPlayerRemainsAsleep() {
        gameWorld.getRNGBuilder().makeEnemyPlayerSleep();
    }

    @And("enemy player is dead")
    public void enemyPlayerIsDead() {
        assertTrue(gameWorld.executeActionsAndGetWumpusPresenter().isEnemyPlayerDead());
    }

    @And("enemy player shoots caves")
    public void enemyPlayerShootsCaves(@Transpose List<Integer> caves) throws Exception {
        final int startingCave = gameWorld.getWumpusPresenter().getEnemyPlayerCave();
        List<Integer> caveIndexes = gameWorld.getCaveIndexesOutOfCaveNumbers(startingCave, caves);
        gameWorld.getRNGBuilder().makeEnemyPlayerShootAtCaves(caveIndexes);
    }

}
