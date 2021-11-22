package model.gameobject.hazard;

import model.gamemap.Cave;
import model.gamemap.GameMap;
import model.gameobject.Player;
import model.gameobject.GameObject;

public class Bat extends GameObject implements Hazard {

    final String warningInTheSameCave = "a bat dropped you in a random cave";
    final String warningInTheLinkedCave = "you hear a rustling";

    private GameMap gameMap;

    public Bat(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    public void move() {
        Cave caveToMoveTo = getValidRelocationCave();
        this.getCave().removeGameObject(this);
        this.setCave(caveToMoveTo);
        caveToMoveTo.addGameObject(this);
    }

    @Override
    public void executeActionOnPlayer(Player player) {
        player.teleport(getValidRelocationCave());
        move();
    }

    private Cave getValidRelocationCave() {
        return gameMap.getACaveThatMeetsCondition(cave -> cave.getGameObjects().stream().noneMatch(gameObject -> gameObject instanceof Player || gameObject instanceof Bat));
    }

    @Override
    public String getWarningInTheLinkedCave() {
        return this.warningInTheLinkedCave;
    }

    @Override
    public String getWarningInTheSameCave() {
        return this.warningInTheSameCave;
    }

    @Override
    public int getPrecedence() {
        return 3;
    }
}
