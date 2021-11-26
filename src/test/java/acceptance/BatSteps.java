package acceptance;

import io.cucumber.java.en.And;
import support.GameWorld;

public class BatSteps {
    private final GameWorld gameWorld;

    public BatSteps(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }
    @And("a bat is in cave {int}")
    public void aBatIsInCave(int cave) {
        gameWorld.getRNGBuilder().setFirstBatStartingCave(cave);
    }

    @And("a bat will be at cave {int}")
    public void aBatWillBeAtCave(int cave) {
        //TODO: add bat caves to presenter API?
    }

    @And("bat will teleport player to cave {int} and itself to cave {int}")
    public void batWillTeleportPlayerToCaveAndItselfToCave(int playerCave, int batCave) {
        gameWorld.getRNGBuilder().addCaveToTeleportTo(playerCave);
        gameWorld.getRNGBuilder().addCaveToTeleportTo(batCave);
    }

}
