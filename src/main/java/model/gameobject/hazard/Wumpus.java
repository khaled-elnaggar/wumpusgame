package model.gameobject.hazard;

import model.gamemap.Cave;
import model.game.GameInitialConfigurations;
import model.gameobject.GameObject;
import model.gameobject.Killable;
import model.gameobject.Player;
import utilities.RandomNumberGenerator;


public class Wumpus extends Hazard implements Killable {
    final String warningInTheSameCave = "You woke the Wumpus and it ate you";
    final String warningInTheLinkedCave = "there's an awful smell";
    private final RandomNumberGenerator randomNumberGenerator;
    boolean dead;
    private GameObject killer;

    public Wumpus(RandomNumberGenerator randomNumberGenerator) {
        this.randomNumberGenerator = randomNumberGenerator;
    }

    @Override
    public void executeActionOnPlayer(Player player) {
        player.kill(this);
        player.addAWarning(this.warningInTheSameCave);
    }

    public void attemptToWakeup() {
        int maximumNumberForCalculatingWumpusWakeupProbability = GameInitialConfigurations.MAXIMUM_NUMBER_FOR_CALCULATING_WUMPUS_WAKEUP_PROBABILITY;
        if (randomNumberGenerator.generateNumber(maximumNumberForCalculatingWumpusWakeupProbability) != GameInitialConfigurations.WUMPUS_SLEEP_NUMBER) { // 75 %
            int randomLinkedCaveIndex = randomNumberGenerator.generateNumber(GameInitialConfigurations.NUMBER_OF_LINKED_CAVES);
            wakeup(randomLinkedCaveIndex);
        }
    }

    public void wakeup(int randomLinkedCaveIndex) {
        this.move(randomLinkedCaveIndex);
        this.getCave().getPlayers().forEach(this::executeActionOnPlayer);
    }

    private void move(int randomLinkedCaveIndex) {
        this.getCave().removeGameObject(this);

        Cave caveToMoveTo = (Cave) this.getCave().getLinkedCaves().toArray()[randomLinkedCaveIndex];
        this.setCave(caveToMoveTo);
        caveToMoveTo.addHazard(this);
    }

    public boolean isDead() {
        return dead;
    }

    @Override
    public void kill(GameObject killer) {
        this.dead = true;
        this.killer = killer;
    }

    @Override
    public boolean wasKilledBy(GameObject killer) {
        return this.killer != null && this.killer.equals(killer);
    }

    @Override
    public int getPrecedence() {
        return 2;
    }

    @Override
    public String getWarningInTheLinkedCave() {
        return this.warningInTheLinkedCave;
    }

    @Override
    public String getWarningInTheSameCave() {
        return this.warningInTheSameCave;
    }
}
