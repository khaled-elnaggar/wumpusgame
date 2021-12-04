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

    private boolean pendingActions = true;
    public void queueAction(Action action) {
        pendingActions = true;
        this.RNGBuilder.makeEnemyMoveIfEnemyHasNoAction(actionsToExecute.size());
        final int numberOfShootActions = (int) actionsToExecute.stream().filter(shootAction -> shootAction instanceof ShootAction).count();
        this.RNGBuilder.makeWumpusSleepIfWumpusHasNoAction(numberOfShootActions);
        this.actionsToExecute.add(action);
    }

    public WumpusPresenter executeActionsAndGetWumpusPresenter() {
        final WumpusPresenter wumpusPresenter = getWumpusPresenter();
        this.executeActions();
        return wumpusPresenter;
    }

    public void executeActions() {
        Action.setWumpusPresenter(getWumpusPresenter());
        this.RNGBuilder.updateTeleportCavesList();
        this.RNGBuilder.makeEnemyMoveIfEnemyHasNoAction(actionsToExecute.size());
        final int numberOfShootActions = (int) actionsToExecute.stream().filter(shootAction -> shootAction instanceof ShootAction).count();
        this.RNGBuilder.makeWumpusSleepIfWumpusHasNoAction(numberOfShootActions);
        actionsToExecute.forEach(Action::execute);
        actionsToExecute.clear();
        pendingActions = false;
    }

    public WumpusPresenter getWumpusPresenter() {
        if (this.wumpusPresenter == null) {
            RandomNumberGenerator randomNumberGenerator = RNGBuilder.build();
            this.wumpusPresenter = new WumpusPresenterImpl(randomNumberGenerator);
            this.wumpusPresenter.startNewGame();
        }
        return this.wumpusPresenter;
    }

    public RandomNumberGeneratorBuilder getRNGBuilder() {
        return RNGBuilder;
    }

    public boolean hasPendingActions() {
        return this.pendingActions;
    }

    public List<Action> getActionsToExecute() {
        return actionsToExecute;
    }
}

