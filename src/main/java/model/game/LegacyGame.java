package model.game;

import utilities.RandomNumberGenerator;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class LegacyGame implements Game {

    boolean gameOver= true;
    int numberOfArrows;
    List<String> messages;
    int numberOfCaves;
    Set<LegacyHazard>[] hazards;
    int playerCave;
    int wumpusCave;

    RandomNumberGenerator randomNumberGenerator;

    public LegacyGame(){
        randomNumberGenerator=new RandomNumberGenerator();
    }

    public LegacyGame(RandomNumberGenerator randomNumberGenerator){
        this.randomNumberGenerator=randomNumberGenerator;
    }

    @Override
    public void startGame() {
        initializeGameParameters();
        setPlayerCave(getRandomCave());
        initializeHazards();
    }

    @Override
    public void playerMovesToCave(int cave) {
        boolean isCurrentCaveIsConnectedToTargetCave= checkThatCurrentCaveIsConnectedToTargetCave(cave);
        if(isCurrentCaveIsConnectedToTargetCave){
            setPlayerCave(cave);
            move();
        }
    }

    private void move() {

        Set<LegacyHazard> set = hazards[playerCave];

        if (set.contains(LegacyHazard.Wumpus)) {
            messages.add("you've been eaten by the Wumpus");
            gameOver = true;

        } else if (set.contains(LegacyHazard.Pit)) {
            messages.add("you fell into a pit");
            gameOver = true;

        } else if (set.contains(LegacyHazard.Bat)) {
            messages.add("a bat dropped you in a random cave");

            // teleport, but avoid 2 teleports in a row
            do {
                setPlayerCave(getRandomCave());
            } while (hazards[playerCave].contains(LegacyHazard.Bat));

            // relocate the bat, but not to the player cave or a cave with a bat
            set.remove(LegacyHazard.Bat);
            int newCave;
            do {
                newCave = getRandomCave();
            } while (newCave == playerCave || hazards[newCave].contains(LegacyHazard.Bat));
            hazards[newCave].add(LegacyHazard.Bat);

            // re-evaluate
            move();

        } else {

            // look around
            for (int link : GameInitialConfigurations.CAVE_LINKS[playerCave]) {
                for (LegacyHazard hazard : hazards[link])
                    messages.add(hazard.warning);
            }
        }

    }

    @Override
    public void playerShootsToCave(int... cave) {
        final int maximumNumberForCalculatingWumpusWakeupProbability=4;
        if (hazards[cave[0]].contains(LegacyHazard.Wumpus)) {
            messages.add("You win! You've killed the Wumpus!");
            gameOver = true;

        } else {
            numberOfArrows--;
            if (numberOfArrows == 0) {
                messages.add("You ran out of arrows");
                gameOver = true;

            } else if (randomNumberGenerator.generateNumber(maximumNumberForCalculatingWumpusWakeupProbability) != 0) { // 75 %
                hazards[wumpusCave].remove(LegacyHazard.Wumpus);
                final int numberOfLinkedCaves=3;
                wumpusCave = GameInitialConfigurations.CAVE_LINKS[wumpusCave][randomNumberGenerator.generateNumber(numberOfLinkedCaves)];

                if (wumpusCave == playerCave) {
                    messages.add("You woke the Wumpus and it ate you");
                    gameOver = true;

                } else {
                    messages.add("You woke the Wumpus and it moved");
                    hazards[wumpusCave].add(LegacyHazard.Wumpus);
                }
            }
        }
    }

    private void initializeGameParameters() {
        messages = new ArrayList<>();
        numberOfArrows = GameInitialConfigurations.NUMBER_OF_ARROWS;
        gameOver = GameInitialConfigurations.GAME_OVER;
        numberOfCaves=GameInitialConfigurations.NUMBER_OF_CAVES;
    }

    private void initializeHazards() {
        hazards = new Set[numberOfCaves];
        for (int i = 0; i < numberOfCaves; i++){
            hazards[i] = EnumSet.noneOf(LegacyHazard.class);
        }

        // hazards can share caves (unless they are identical)
        int[] ordinals = {0, 1, 1, 1, 2, 2};
        LegacyHazard[] values = LegacyHazard.values();
        for (int ord : ordinals) {
            int cave;
            do {
                cave = getRandomCave();
            } while (tooClose(cave) || hazards[cave].contains(values[ord]));

            if (ord == 0) {
                wumpusCave = cave;
            }

            hazards[cave].add(values[ord]);
        }
    }

    private boolean tooClose(int cave) {
        if (playerCave == cave)
            return true;
        for (int link : GameInitialConfigurations.CAVE_LINKS[playerCave])
            if (cave == link)
                return true;
        return false;
    }

    private int getRandomCave() {
        return randomNumberGenerator.generateNumber(numberOfCaves);
    }

    public void setPlayerCave(int playerCave) {
        this.playerCave = playerCave;
    }

    private boolean checkThatCurrentCaveIsConnectedToTargetCave(int targetCave) {
        boolean isCurrentCaveIsConnectedToTargetCave=false;
        for (int caveLink : GameInitialConfigurations.CAVE_LINKS[playerCave]) {
            if (caveLink==targetCave) {
                isCurrentCaveIsConnectedToTargetCave=true;
                break;
            }
        }
        return isCurrentCaveIsConnectedToTargetCave;
    }

    @Override
    public boolean isGameOver() {
        return gameOver;
    }

    @Override
    public int getNumberOfArrows() {
        return numberOfArrows;
    }

    @Override
    public List<String> getWarnings() {
        return messages;
    }

    @Override
    public int getWumpusCaveIndex() {
        return wumpusCave;
    }

    @Override
    public int getPlayerCaveIndex() {
        return playerCave;
    }

}
