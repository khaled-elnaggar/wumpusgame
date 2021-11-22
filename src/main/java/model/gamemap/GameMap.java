package model.gamemap;

import model.game.GameInitialConfigurations;

import java.util.ArrayList;
import java.util.List;

public class GameMap {
    private List<Cave> caves;

    public void buildGameMap() {
        caves = new ArrayList<>();
        buildCaves();
        buildCaveLinks();
    }

    private void buildCaves() {
        for (int i = 0; i < GameInitialConfigurations.NUMBER_OF_CAVES; i++) {
            caves.add(new Cave(i));
        }
    }

    private void buildCaveLinks() {
        for (int i = 0; i < caves.size(); i++) {
            int[] link = GameInitialConfigurations.CAVE_LINKS[i];
            Cave cave = caves.get(i);
            for (int caveNumber : link) {
                Cave linkedCave = caves.get(caveNumber);
                cave.addLink(linkedCave);
            }
        }
    }

    public List<Cave> getCaves() {
        return this.caves;
    }
}
