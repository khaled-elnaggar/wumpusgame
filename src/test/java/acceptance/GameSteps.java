package acceptance;

import io.cucumber.java.en.And;
import io.cucumber.java.en.But;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import model.game.GameInitialConfigurations;
import org.junit.jupiter.api.Assertions;
import presenter.WumpusPresenter;
import support.GameWorld;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GameSteps {
    private final GameWorld gameWorld;

    public GameSteps(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    @And("pit {int} is in cave {int}")
    public void pitIsInCave(int pitNumber, int cave) {
        gameWorld.getRNGBuilder().setPitStartingCave(pitNumber, cave);
    }

    @Then("game is over")
    public void gameIsOver() {
        WumpusPresenter wumpusPresenter = gameWorld.executeActionsAndGetWumpusPresenter();
        final boolean expectedStatusOfGameIsOver = true;
        final boolean isGameOver = wumpusPresenter.isGameOver();
        assertEquals(isGameOver, expectedStatusOfGameIsOver);
    }

    @And("game is still on")
    public void gameIsStillOn() {
        WumpusPresenter wumpusPresenter = gameWorld.executeActionsAndGetWumpusPresenter();
        final boolean expectedStatusOfGameIsOver = false;
        final boolean isGameOver = wumpusPresenter.isGameOver();
        assertEquals(isGameOver, expectedStatusOfGameIsOver);
    }

    @And("game is won")
    public void gameIsWon() {
        assertTrue(gameWorld.executeActionsAndGetWumpusPresenter().hasPlayerWon());
    }

    @And("game is lost")
    public void gameIsLost() {
        assertTrue(gameWorld.executeActionsAndGetWumpusPresenter().isGameLost());
    }

    @But("cave {int} is not linked to {int}, so arrow will go to {int} instead")
    public void caveIsNotLinkedToSoArrowWillGotToInstead(int wrongCave, int currentCave, int nextRandomCave) throws Exception {
        int caveIndex = GameInitialConfigurations.getCaveIndexOutOfCave(currentCave, nextRandomCave);
        gameWorld.getRNGBuilder().setNextRandomCaveForArrow(caveIndex);
    }

    @Given("game starts")
    public void gameStarts() {
        gameWorld.executeActions();
    }

    @And("pit {int} will be at cave {int}")
    public void pitWillBeAtCave(int pitNumber, int expectedCave) {
        assertTrue("Wrong pit index " + pitNumber + " .indexes start from 1", pitNumber > 0);
        final int pitsCave = gameWorld.executeActionsAndGetWumpusPresenter().getPitsCaves()[pitNumber - 1];
        Assertions.assertEquals(expectedCave, pitsCave);
    }

    @Then("there is/are {int} {string}")
    public void thereIsPlayer(int expectedNumber, String objectName) {
        if (objectName.endsWith("s")) {
            objectName = objectName.substring(0, objectName.length() - 1);
        }

        switch (objectName) {
            case "player":
                assertTrue(gameWorld.executeActionsAndGetWumpusPresenter().getPlayerCaveIndex() >= 0);
                break;
            case "enemy player":
                assertTrue(gameWorld.executeActionsAndGetWumpusPresenter().getEnemyPlayerCave() >= 0);
                break;
            case "bat":
                assertEquals("Please update the game configuration NUMBER_OF_BATS first!", GameInitialConfigurations.NUMBER_OF_BATS, expectedNumber);
                final int actualBatsCount = gameWorld.executeActionsAndGetWumpusPresenter().getBatsCaves().length;
                assertEquals(expectedNumber, actualBatsCount);
                break;
            case "pit":
                assertEquals("Please update the game configuration NUMBER_OF_PITS first!", GameInitialConfigurations.NUMBER_OF_PITS, expectedNumber);
                final int actualPitsCount = gameWorld.executeActionsAndGetWumpusPresenter().getPitsCaves().length;
                assertEquals(expectedNumber, actualPitsCount);
                break;
            default:
                throw new Error("Unknown new game object type! " + objectName);
        }
    }
}
