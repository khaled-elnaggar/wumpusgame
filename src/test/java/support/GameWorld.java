package support;

import presenter.WumpusPresenter;
import presenter.WumpusPresenterImpl;
import utilities.RandomNumberGenerator;

import java.util.ArrayList;
import java.util.List;

public class GameWorld {
    private final RandomNumberGeneratorBuilder RNGBuilder = new RandomNumberGeneratorBuilder();
    private WumpusPresenter wumpusPresenter;
    private final List<Action> actionsToExecute = new ArrayList<>();

    public void queueAction(Action action) {
        this.RNGBuilder.makeEnemyMoveIfEnemyHasNoAction(actionsToExecute.size());
        this.actionsToExecute.add(action);
    }

    public WumpusPresenter executeActionsAndGetWumpusPresenter() {
        final WumpusPresenter wumpusPresenter = getWumpusPresenter();
        this.executeActions();
        return wumpusPresenter;
    }

    public void executeActions() {
        this.RNGBuilder.updateTeleportCavesList();
        this.RNGBuilder.makeEnemyMoveIfEnemyHasNoAction(actionsToExecute.size());
        actionsToExecute.forEach(Action::execute);
        actionsToExecute.clear();
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

    public RandomNumberGeneratorBuilder getRNGBuilder() {
        return RNGBuilder;
    }
}

