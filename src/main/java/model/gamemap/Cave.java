package model.gamemap;

import model.gameobject.GameObject;
import model.gameobject.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    public boolean isFreeFromPlayerAndLinkedPlayer(){
        List<GameObject> allGameObjects = new ArrayList<>(gameObjects);
        List<GameObject> linkedCavesGameObjects = linkedCaves.stream().flatMap(cave -> cave.getGameObjects().stream()).collect(Collectors.toList());
        allGameObjects.addAll(linkedCavesGameObjects);
        return allGameObjects.stream().noneMatch(gameObject -> gameObject instanceof Player);
    }

    public boolean containsAny(List<? extends GameObject> gameObjectsToLookFor){
        for(GameObject gameObjectToLookFor: gameObjectsToLookFor){
            if(gameObjects.contains(gameObjectToLookFor)){
                return true;
            }
        }
        return false;
    }

}
