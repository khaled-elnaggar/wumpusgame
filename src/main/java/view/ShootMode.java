package view;

public class ShootMode extends Mode {

    @Override
    public void handleLeftClick(int cave) {
        view.shoot(cave);
    }

    @Override
    public void handleRightClick() {
        Mode.view.setMode(new MoveMode());
    }

    @Override
    public String toString(){
        return "Shoot";
    }

}
