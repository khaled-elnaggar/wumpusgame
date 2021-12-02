package support;

import model.game.GameInitialConfigurations;
import presenter.WumpusPresenter;
import presenter.WumpusPresenterImpl;
import utilities.RandomNumberGenerator;

import java.util.ArrayList;
import java.util.List;

public class GameWorld {
    private RandomNumberGeneratorBuilder RNGBuilder = new RandomNumberGeneratorBuilder();
    private WumpusPresenter wumpusPresenter;
    private List<Action> actionsToExecute = new ArrayList<>();

    public RandomNumberGeneratorBuilder getRNGBuilder() {
        return RNGBuilder;
    }

    public WumpusPresenter executeActionsAndGetWumpusPresenter() {
        final WumpusPresenter wumpusPresenter = getWumpusPresenter();
        this.executeActions();
        return wumpusPresenter;
    }

    public WumpusPresenter getWumpusPresenter() {
        if (this.wumpusPresenter == null) {
            RandomNumberGenerator randomNumberGenerator = RNGBuilder.build();
            this.wumpusPresenter = new WumpusPresenterImpl(randomNumberGenerator);
            this.wumpusPresenter.startNewGame();
            Action.setWumpusPresenter(wumpusPresenter);
        }
        return this.wumpusPresenter;
    }

    public void queueAction(Action action) {
        this.actionsToExecute.add(action);
    }

    public void executeActions() {
        this.RNGBuilder.updateMockArrays();
        actionsToExecute.forEach(Action::execute);
        actionsToExecute.clear();
    }

    public int getCaveIndexOutOfCave(int cave, int linkedCave) throws Exception {
        int[] caveLinks = GameInitialConfigurations.CAVE_LINKS[cave];

        for (int i = 0; i < caveLinks.length; i++) {
            if (caveLinks[i] == linkedCave) {
                return i;
            }
        }

        throw new Exception("Cave " + linkedCave + " is not actually linked to " + cave);
    }

    public List<Integer> getCaveIndexesOutOfCaveNumbers(int startingCave, List<Integer> cavesToShoot) throws Exception {
        List<Integer> cavesToShootIndexes = new ArrayList<>();
        for (int cave : cavesToShoot) {
            int caveIndex = getCaveIndexOutOfCave(startingCave, cave);
            cavesToShootIndexes.add(caveIndex);
            startingCave = cave;
        }
        return cavesToShootIndexes;
    }
}

