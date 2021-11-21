package model.gameobject;

import model.*;
import model.gamemap.Cave;
import model.gameobject.hazard.Bat;
import model.gameobject.hazard.Hazard;
import model.gameobject.hazard.Wumpus;

import java.util.ArrayList;
import java.util.List;

public class Player extends GameObject {
    private boolean dead;
    private Arrow arrow;
    private List<String> warnings;

    public Teleportation getTeleportation() {
        return teleportation;
    }

    public void setTeleportation(Teleportation teleportation) {
        this.teleportation = teleportation;
    }

    private Teleportation teleportation;

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
        moveBat();
        executePostMoveActions();
        senseWarning();
    }

    private void moveBat() {
        Bat bat=teleportation.getBat();
        bat.move();
        teleportation=null;
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

        for(Cave linkedCave: linkedCaves) {
            List<GameObject> gameObjects = linkedCave.getGameObjects();
            for(GameObject gameObject : gameObjects){
                if(gameObject instanceof Hazard){
                    warnings.add(((Hazard) gameObject).getWarningInTheLinkedCave());
                }
            }
        }
    }

    public void addWarning(String warning) {
        warnings.add(warning);
    }

    private void executePostMoveActions() {
        List<GameObject> gameObjects = this.getCave().getGameObjects();
        for(GameObject gameObject : gameObjects){
            if(gameObject instanceof Hazard){
                ((Hazard)gameObject).executeActionOnPlayer(this);
                if(isDead()){
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

        List<GameObject> gameObjects = caveToShoot.getGameObjects();
        for(GameObject gameObject : gameObjects){
            if(gameObject instanceof Wumpus){
                ((Wumpus)gameObject).setDead(true);
            }
        }

        if(hasNoArrows()){
            warnings.add("You ran out of arrows");
        }
    }

    public boolean hasNoArrows(){
        return getArrows().getNumber()==0;
    }

    public Arrow getArrows() {
        return this.arrow;
    }
}
