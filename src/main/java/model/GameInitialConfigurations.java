package model;

public interface GameInitialConfigurations {

    int[][] CAVE_LINKS = {{4, 7, 1}, {0, 9, 2}, {1, 11, 3}, {4, 13, 2}, {0, 5, 3},
            {4, 6, 14}, {7, 16, 5}, {6, 0, 8}, {7, 17, 9}, {8, 1, 10}, {9, 18, 11},
            {10, 2, 12}, {13, 19, 11}, {14, 3, 12}, {5, 15, 13}, {14, 16, 19},
            {6, 17, 15}, {16, 8, 18}, {19, 10, 17}, {15, 12, 18}};

    int NUMBER_OF_ARROWS=5;
    boolean GAME_OVER=false;
    int NUMBER_OF_CAVES=20;
    String PLAYER_ID = "The player";
    String WUMPUS_ID = "The Wumpus";
    String BAT_ID_PREFIX = "Bat ";
    String PITS_ID_PREFIX = "The pit ";
    int NUMBER_OF_BATS = 2;
    int NUMBER_OF_PITS = 2;
    int MAXIMUM_NUMBER_FOR_CALCULATING_WUMPUS_WAKEUP_PROBABILITY = 4;
    int NUMBER_OF_LINKED_CAVES = 3;
}
