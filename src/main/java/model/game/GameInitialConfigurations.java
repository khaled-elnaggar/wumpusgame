package model.game;

import java.util.ArrayList;
import java.util.List;

public interface GameInitialConfigurations {

    int[][] CAVE_LINKS = {{4, 7, 1}, {0, 9, 2}, {1, 11, 3}, {4, 13, 2}, {0, 5, 3},
            {4, 6, 14}, {7, 16, 5}, {6, 0, 8}, {7, 17, 9}, {8, 1, 10}, {9, 18, 11},
            {10, 2, 12}, {13, 19, 11}, {14, 3, 12}, {5, 15, 13}, {14, 16, 19},
            {6, 17, 15}, {16, 8, 18}, {19, 10, 17}, {15, 12, 18}};

    int NUMBER_OF_ARROWS=5;
    int NUMBER_OF_CAVES=20;
    String PLAYER_ID = "The player";
    String WUMPUS_ID = "The Wumpus";
    String BAT_ID_PREFIX = "Bat ";
    String PITS_ID_PREFIX = "The pit ";
    int NUMBER_OF_BATS = 2;
    int NUMBER_OF_PITS = 2;
    int MAXIMUM_NUMBER_FOR_CALCULATING_WUMPUS_WAKEUP_PROBABILITY = 4;
    int NUMBER_OF_LINKED_CAVES = 3;
    int MAX_POSSIBILITY_ENEMY_PLAYER_TAKE_ACTION = 2;
    int MAX_CAVES_ENEMY_PLAYER_CAN_SHOOT = 5;

    int WUMPUS_WAKEUP_NUMBER = 3;
    int WUMPUS_SLEEP_NUMBER = 0;

    int ENEMY_PLAYER_SHOOT_NUMBER = 1;
    int ENEMY_PLAYER_MOVE_NUMBER = 0;
    static int getCaveIndexOutOfCave(int cave, int linkedCave) throws Exception {
        int[] caveLinks = GameInitialConfigurations.CAVE_LINKS[cave];

        for (int i = 0; i < caveLinks.length; i++) {
            if (caveLinks[i] == linkedCave) {
                return i;
            }
        }

        throw new Exception("Cave " + linkedCave + " is not actually linked to " + cave);
    }

    static List<Integer> getCaveIndexesOutOfCaveNumbers(int startingCave, List<Integer> cavesToShoot) throws Exception {
        List<Integer> cavesToShootIndexes = new ArrayList<>();
        for (int cave : cavesToShoot) {
            int caveIndex = getCaveIndexOutOfCave(startingCave, cave);
            cavesToShootIndexes.add(caveIndex);
            startingCave = cave;
        }
        return cavesToShootIndexes;
    }


}
