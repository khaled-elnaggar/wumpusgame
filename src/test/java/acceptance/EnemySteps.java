package acceptance;

import io.cucumber.java.Transpose;
import io.cucumber.java.en.And;
import io.cucumber.java.en.But;
import io.cucumber.java.en.Then;
import model.game.GameInitialConfigurations;
import support.GameWorld;
import support.MoveAction;
import support.ShootAction;

import java.util.Arrays;
import java.util.Collections;
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

    @But("enemy player will wake up and move to caves")
    public void enemyPlayerWillWakeUpAndMoveToCave(@Transpose List<Integer> caves) throws Exception {
        int currentCave = gameWorld.getWumpusPresenter().getEnemyPlayerCave();
        for (int cave : caves) {
            int caveIndex = gameWorld.getCaveIndexOutOfCave(currentCave, cave);
            gameWorld.getRNGBuilder().makeEnemyPlayerMoveToCave(caveIndex);
            currentCave = cave;
        }
    }

    @Then("enemy player will wake up and move to cave {int}")
    public void enemyPlayerWillWakeUpAndMoveToCave(int cave) throws Exception {
        this.enemyPlayerWillWakeUpAndMoveToCave(Collections.singletonList(cave));
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

    @And("enemy player shoots cave {int}, {int} times")
    public void enemyPlayerShootsCaveTimes(int arg0, int arg1) throws Exception {
        for (int i = 0; i < arg1; i++) {
            enemyPlayerShootsCaves(Collections.singletonList(arg0));
        }
    }

    @And("enemy player used all arrows")
    public void enemyPlayerHasArrows() {
        int enemyRemainingArrows = gameWorld.executeActionsAndGetWumpusPresenter().getEnemyRemainingArrows();

        for (int i = 0; i < enemyRemainingArrows; i++) {
            gameWorld.getRNGBuilder().makeEnemyPlayerShootAtCaves(Collections.singletonList(1));

            final int playerCave = gameWorld.executeActionsAndGetWumpusPresenter().getPlayerCaveIndex();
            final int secondLinkedCave = GameInitialConfigurations.CAVE_LINKS[playerCave][1];
            gameWorld.queueAction(new MoveAction(secondLinkedCave));
        }
    }

    @And("enemy player will have {int} arrows")
    public void enemyPlayerHasArrows(int remainingArrows) {
        int enemyRemainingArrows = gameWorld.executeActionsAndGetWumpusPresenter().getEnemyRemainingArrows();
        assertEquals(remainingArrows, enemyRemainingArrows);
    }
}
