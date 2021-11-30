package support;

import presenter.WumpusPresenter;

public abstract class Action {
    static WumpusPresenter wumpusPresenter;

    public static void setWumpusPresenter(WumpusPresenter wumpusPresenter) {
        Action.wumpusPresenter = wumpusPresenter;
    }

    public abstract void execute();
}
