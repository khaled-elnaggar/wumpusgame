package model.gameobject.hazard;

import model.gamemap.Cave;
import model.game.GameInitialConfigurations;
import model.gameobject.GameObject;
import model.gameobject.Killable;
import model.gameobject.Player;
import utilities.RandomNumberGenerator;

import java.util.Optional;

public class Wumpus extends GameObject implements Hazard, Killable {
    final String warningInTheSameCave = "You woke the Wumpus and it ate you";
    final String warningInTheLinkedCave = "there's an awful smell";
    private final RandomNumberGenerator randomNumberGenerator;
    boolean dead;

    public Wumpus(RandomNumberGenerator randomNumberGenerator) {
        this.randomNumberGenerator = randomNumberGenerator;
    }

    @Override
    public String getWarningInTheLinkedCave() {
        return this.warningInTheLinkedCave;
    }

    @Override
    public String getWarningInTheSameCave() {
        return this.warningInTheSameCave;
    }

    public void wakeup(int randomLinkedCaveIndex) {
        this.move(randomLinkedCaveIndex);

        Optional<GameObject> player = this.getCave().getGameObjects().stream()
                .filter(gameObject -> gameObject instanceof Player)
                .findFirst();

        if(player.isPresent()){
            executeActionOnPlayer((Player) player.get());
        }
    }

    private void move(int randomLinkedCaveIndex) {
        this.getCave().removeGameObject(this);

        Cave caveToMoveTo = (Cave) this.getCave().getLinkedCaves().toArray()[randomLinkedCaveIndex];
        this.setCave(caveToMoveTo);
        caveToMoveTo.addGameObject(this);
    }

    @Override
    public void executeActionOnPlayer(Player player) {
        player.kill();
        player.addAWarning(this.warningInTheSameCave);
    }

    public boolean isDead() {
        return dead;
    }

    @Override
    public void kill() {
        this.dead = true;
    }

    public void attemptToWakeup() {
            int maximumNumberForCalculatingWumpusWakeupProbability = GameInitialConfigurations.MAXIMUM_NUMBER_FOR_CALCULATING_WUMPUS_WAKEUP_PROBABILITY;
            if (randomNumberGenerator.generateNumber(maximumNumberForCalculatingWumpusWakeupProbability) != 0) { // 75 %
                int randomLinkedCaveIndex = randomNumberGenerator.generateNumber(GameInitialConfigurations.NUMBER_OF_LINKED_CAVES);
                wakeup(randomLinkedCaveIndex);
        }
    }

    @Override
    public int getPrecedence() {
        return 2;
    }
}
