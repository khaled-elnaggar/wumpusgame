package model.gamemap;

import model.gameobject.GameObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Cave {
    private int number;
    private List<Cave> linkedCaves;
    private List<GameObject> gameObjects;

    public Cave(int number){
        this.number = number;
        this.linkedCaves = new ArrayList<>();
        this.gameObjects=new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cave cave = (Cave) o;
        return number == cave.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    public void addLink(Cave linkedCave) {
        this.linkedCaves.add(linkedCave);
    }

    public List<Cave> getLinkedCaves() {
        return linkedCaves;
    }

    public int getNumber() {
        return number;
    }

    public List<GameObject> getGameObjects() {
        return this.gameObjects;
    }

    public void addGameObject(GameObject gameObject) {
        this.gameObjects.add(gameObject);
        Collections.sort(this.gameObjects);
    }

    public void removeGameObject(GameObject gameObject) {
        this.gameObjects.remove(gameObject);
    }

    public boolean isLinkedTo(Cave cave) {
        return this.linkedCaves.contains(cave);
    }
}
