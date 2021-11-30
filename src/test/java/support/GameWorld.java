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

    public WumpusPresenter getWumpusPresenter() {
        if (this.wumpusPresenter == null) {
            RandomNumberGenerator randomNumberGenerator = RNGBuilder.build();

            this.wumpusPresenter = new WumpusPresenterImpl(randomNumberGenerator);
            this.wumpusPresenter.startNewGame();
            Action.setWumpusPresenter(wumpusPresenter);
        }
        this.executeActions();
        return this.wumpusPresenter;
    }

    public void queueAction(Action action) {
        this.actionsToExecute.add(action);
    }

    private void executeActions() {
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

}

