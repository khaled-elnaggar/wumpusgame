package model.gameobject.hazard;

import model.gameobject.Player;

public interface Hazard {
    void executeActionOnPlayer(Player player);
    String getWarningInTheLinkedCave();
    String getWarningInTheSameCave();
}
