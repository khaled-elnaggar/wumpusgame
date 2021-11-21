package model.gamemap;

import java.util.List;

public class GameMap {
    private List<Cave> caves;
    public void setCaves(List<Cave> caves) {
        this.caves = caves;
    }

    public List<Cave> getCaves() {
        return this.caves;
    }
}
