package support;

import java.util.Arrays;

public class ShootAction extends Action {
    private final int[] cavesToShoot;

    public ShootAction(int... cavesToShoot) {
        this.cavesToShoot = cavesToShoot;
    }

    @Override
    public void execute() {
        Action.wumpusPresenter.shoot(cavesToShoot);
    }
    @Override
    public String toString(){
        return "Player Shoot at caves " + Arrays.toString(cavesToShoot);
    }
}
