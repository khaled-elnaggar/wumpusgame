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
        this.RNGBuilder.makeEnemyMoveIfEnemyHasNoAction(actionsToExecute.size());
        this.actionsToExecute.add(action);
    }

    public void executeActions() {
        this.RNGBuilder.updateMockArrays();
        this.RNGBuilder.makeEnemyMoveIfEnemyHasNoAction(actionsToExecute.size());
        actionsToExecute.forEach(Action::execute);
        actionsToExecute.clear();
    }


}

