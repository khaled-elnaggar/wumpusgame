package model.gamemap;

import model.gameobject.GameObject;
import model.gameobject.Killable;
import model.gameobject.Player;
import model.gameobject.hazard.Bat;
import model.gameobject.hazard.Hazard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Cave {
    private final int number;
    private final List<Cave> linkedCaves = new ArrayList<>();
    private final List<Player> players = new ArrayList<>();
    private final List<GameObject> hazards = new ArrayList<>();

    public Cave(int number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cave cave = (Cave) o;
        return number == cave.number;
    }

    public void addLink(Cave linkedCave) {
        this.linkedCaves.add(linkedCave);
    }
    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public void addHazard(GameObject hazard) {
        this.hazards.add(hazard);
        Collections.sort(this.hazards);
    }

    public void removeGameObject(GameObject gameObject) {
        this.players.remove(gameObject);
        this.hazards.remove(gameObject);
    }

    public List<Cave> getLinkedCaves() {
        return linkedCaves;
    }

    public int getNumber() {
        return number;
    }

    public List<Hazard> getHazards() {
        return this.hazards.stream().map(gameObject -> (Hazard) gameObject).collect(toList());
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public List<Killable> getKillables() {
        return Stream.concat(players.stream(), hazards.stream())
                .filter(gameObject -> gameObject instanceof Killable)
                .map(gameObject -> (Killable) gameObject)
                .collect(toList());
    }

    public boolean isLinkedTo(Cave cave) {
        return this.linkedCaves.contains(cave);
    }

    public boolean containsNoPlayer(){
        return this.players.isEmpty();
    }
    public boolean containsNoPlayerNorLinkedCavePlayer() {
        return containsNoPlayer() && this.getLinkedCaves().stream().allMatch(Cave::containsNoPlayer);
    }

    public boolean containsNoPlayerNorBat(){
        return containsNoPlayer() && hazards.stream().noneMatch(gameObject -> gameObject instanceof Bat);
    }
    public boolean containsAny(List<? extends GameObject> gameObjectsToLookFor) {
        for (GameObject gameObjectToLookFor : gameObjectsToLookFor) {
            if (players.contains(gameObjectToLookFor) || hazards.contains(gameObjectToLookFor)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsAny(GameObject gameObjectToLookFor) {
        return containsAny(Collections.singletonList(gameObjectToLookFor));
    }
}
