package support;

import presenter.WumpusPresenter;
import presenter.WumpusPresenterImpl;
import utilities.RandomNumberGenerator;

public class GameWorld {
    private RandomNumberGeneratorBuilder RNGBuilder = new RandomNumberGeneratorBuilder();

    private WumpusPresenter wumpusPresenter;

    public RandomNumberGeneratorBuilder getRNGBuilder() {
        return RNGBuilder;
    }

    public WumpusPresenter getWumpusPresenter() {
        if (this.wumpusPresenter == null) {
            RandomNumberGenerator randomNumberGenerator = RNGBuilder.build();

            this.wumpusPresenter = new WumpusPresenterImpl(randomNumberGenerator);
            this.wumpusPresenter.startNewGame();
        }
        return this.wumpusPresenter;
    }
}

