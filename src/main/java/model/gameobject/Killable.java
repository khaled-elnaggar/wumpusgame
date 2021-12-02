package model.gameobject;

public interface Killable {
    void kill(GameObject killer);
    boolean wasKilledBy(GameObject killer);
    boolean isDead();
}
