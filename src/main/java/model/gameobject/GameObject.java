package model.gameobject;

import model.gamemap.Cave;


public abstract class GameObject implements Comparable<GameObject> {
    String id;
    private Cave cave;

    public void setId(String id) {
        this.id = id;
    }

    public void setCave(Cave cave) {
        this.cave = cave;
    }

    public Cave getCave() {
        return cave;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameObject gameObject = (GameObject) o;
        return id.equals(gameObject.id);
    }

    @Override
    public int compareTo(GameObject gameObject) {
        return this.getPrecedence() - gameObject.getPrecedence();
    }

    public abstract int getPrecedence();
}
