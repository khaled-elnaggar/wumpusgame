package support;

public class ShootAction extends Action {
    private int[] cavesToShoot;

    public ShootAction(int... cavesToShoot) {
        this.cavesToShoot = cavesToShoot;
    }

    @Override
    public void execute() {
        Action.wumpusPresenter.shoot(cavesToShoot);
    }
}
