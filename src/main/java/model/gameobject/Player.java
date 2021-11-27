package model.gameobject;

import model.Arrow;
import model.gamemap.Cave;
import model.gameobject.hazard.Hazard;

import java.util.ArrayList;
import java.util.List;

public class Player extends GameObject implements Killable {
    private boolean dead;
    private final Arrow arrow;
    private final List<String> warnings;

    @Override
    public void kill() {
        this.dead = true;
    }

    public Player(int numberOfArrows) {
        warnings = new ArrayList<>();
        arrow = new Arrow();
        this.arrow.initializeNumberOfArrows(numberOfArrows);
    }

    public void move(Cave caveToMoveTo) {
        this.warnings.clear();

        if (this.isMyCurrentCaveLinkedTo(caveToMoveTo)) {
            changeMyCaveLocationTo(caveToMoveTo);
        }

        executePostMoveActions();
        senseWarnings();
    }

    private boolean isMyCurrentCaveLinkedTo(Cave caveToMoveTo) {
        return this.getCave().getLinkedCaves().contains(caveToMoveTo);
    }

    public void teleport(Cave caveToMoveTo) {
        this.warnings.clear();
        changeMyCaveLocationTo(caveToMoveTo);
        addAWarning("a bat dropped you in a random cave");
        executePostMoveActions();
        senseWarnings();
    }

    private void changeMyCaveLocationTo(Cave caveToMoveTo) {
        this.getCave().removeGameObject(this);
        this.setCave(caveToMoveTo);
        caveToMoveTo.addGameObject(this);
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

    private void senseWarnings() {
        List<Cave> linkedCaves = this.getCave().getLinkedCaves();

        linkedCaves.stream()
                .flatMap(cave -> cave.getGameObjects().stream())
                .filter(gameObject -> gameObject instanceof Hazard)
                .map(gameObject -> ((Hazard) gameObject))
                .map(Hazard::getWarningInTheLinkedCave)
                .forEach(warnings::add);
    }

    public void addAWarning(String warning) {
        warnings.add(warning);
    }

    public boolean isDead() {
        return dead;
    }

    public void shoot(List<Cave> caves) {
        this.warnings.clear();
        for (Cave cave : caves) {
            shootSingle(cave);
        }
        arrow.decrementByOne();
        if (hasNoArrows()) {
            warnings.add("You ran out of arrows");
        }
    }

    public void shootSingle(Cave caveToShoot) {
        caveToShoot.getGameObjects().stream()
                .filter(gameObject -> gameObject instanceof Killable)
                .map(gameObject -> ((Killable) gameObject))
                .forEach(Killable::kill);
    }

    public boolean hasNoArrows() {
        return arrow.getNumber() == 0;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public Arrow getArrows() {
        return this.arrow;
    }

    @Override
    public int getPrecedence() {
        return 4;
    }

}
