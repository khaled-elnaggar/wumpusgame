package model.gameobject.hazard;

import model.gameobject.Player;

public class Pit extends Hazard {
    final String warningInTheSameCave = "you fell into a pit";
    final String warningInTheLinkedCave = "you feel a draft";

    public String getWarningInTheLinkedCave() {
        return warningInTheLinkedCave;
    }



    public String getWarningInTheSameCave() {
        return warningInTheSameCave;
    }


    @Override
    public void executeActionOnPlayer(Player player) {
        player.kill(this);
        player.addAWarning(this.warningInTheSameCave);
    }

    @Override
    public int getPrecedence() {
        return 1;
    }
}
