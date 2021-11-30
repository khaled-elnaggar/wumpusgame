package acceptance;

import io.cucumber.java.en.And;
import io.cucumber.java.en.But;
import io.cucumber.java.en.Then;
import model.game.GameInitialConfigurations;
import presenter.WumpusPresenter;
import support.GameWorld;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OtherSteps {
    private final GameWorld gameWorld;

    public OtherSteps(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
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

    @And("game is still on")
    public void gameIsStillOn() {
        WumpusPresenter wumpusPresenter = gameWorld.getWumpusPresenter();
        final boolean expectedStatusOfGameIsOver = false;
        final boolean isGameOver = wumpusPresenter.isGameOver();
        assertEquals(isGameOver, expectedStatusOfGameIsOver);
    }

    @And("game is won")
    public void gameIsWon() {
        assertTrue(gameWorld.getWumpusPresenter().hasPlayerWon());
    }

    @And("game is lost")
    public void gameIsLost() {
        assertTrue(gameWorld.getWumpusPresenter().isGameLost());
    }

    @But("cave {int} is not linked to {int}, so arrow will go to {int} instead")
    public void caveIsNotLinkedToSoArrowWillGotToInstead(int wrongCave, int currentCave, int nextRandomCave) {
        int[] caveLinks = GameInitialConfigurations.CAVE_LINKS[currentCave];
        int caveIndex = 0;

        for(int i = 0; i < caveLinks.length; i++){
            if(caveLinks[i] == nextRandomCave){
                caveIndex = i;
            }
        }

        gameWorld.getRNGBuilder().setNextRandomCaveForArrow(caveIndex);
    }
}
