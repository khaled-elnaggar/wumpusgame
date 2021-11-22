package model.gameobject;

import model.*;
import model.gamemap.Cave;
import model.gameobject.hazard.Hazard;
import model.gameobject.hazard.Wumpus;

import java.util.ArrayList;
import java.util.List;

public class Player extends GameObject {
    private boolean dead;
    private Arrow arrow;
    private List<String> warnings;

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public Player(int numberOfArrows) {
        warnings = new ArrayList<>();
        arrow = new Arrow();
        this.arrow.initializeNumberOfArrows(numberOfArrows);
    }

    public void move(Cave caveToMoveTo) {
        this.warnings.clear();

        if (this.isMoveValid(caveToMoveTo)) {
            changeTheCaveLocation(caveToMoveTo);
        }

        executePostMoveActions();
        senseWarning();
    }

    public void teleport(Cave caveToMoveTo) {
        this.warnings.clear();
        changeTheCaveLocation(caveToMoveTo);
        addWarning("a bat dropped you in a random cave");
        executePostMoveActions();
        senseWarning();
    }


    private void changeTheCaveLocation(Cave caveToMoveTo) {
        this.getCave().removeGameObject(this);
        this.setCave(caveToMoveTo);
        caveToMoveTo.addGameObject(this);
    }

    public List<String> getWarnings() {
        return warnings;
    }

    private void senseWarning() {
        List<Cave> linkedCaves = this.getCave().getLinkedCaves();

        linkedCaves.stream()
                .flatMap(cave -> cave.getGameObjects().stream())
                .filter(gameObject -> gameObject instanceof Hazard)
                .map(gameObject-> ((Hazard) gameObject).getWarningInTheLinkedCave())
                .forEach(warning -> warnings.add(warning));
    }

    public void addWarning(String warning) {
        warnings.add(warning);
    }

    private void executePostMoveActions() {
        List<GameObject> gameObjects = this.getCave().getGameObjects();
        List<GameObject> copyOfGameObjects = new ArrayList<>(gameObjects);

        for (GameObject gameObject : copyOfGameObjects) {
            if (gameObject instanceof Hazard) {
                ((Hazard) gameObject).executeActionOnPlayer(this);
                if (isDead()) {
                    break;
                }
            }
        }
    }

    private boolean isMoveValid(Cave caveToMoveTo) {
        return this.getCave().getLinkedCaves().contains(caveToMoveTo);
    }

    public boolean isDead() {
        return dead;
    }

    public void shoot(Cave caveToShoot) {
        this.warnings.clear();

        arrow.decrementByOne();

        caveToShoot.getGameObjects().stream()
                .filter(gameObject -> gameObject instanceof Wumpus)
                .map(gameObject -> ((Wumpus) gameObject))
                .forEach(wumpus -> wumpus.setDead(true));

        if (hasNoArrows()) {
            warnings.add("You ran out of arrows");
        }
    }

    public boolean hasNoArrows() {
        return getArrows().getNumber() == 0;
    }

    public Arrow getArrows() {
        return this.arrow;
    }
}
