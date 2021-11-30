package view;

public class MoveMode extends Mode {

    @Override
    public void handleLeftClick(int cave) {
        Mode.view.move(cave);
    }

    @Override
    public void handleRightClick() {
        Mode.view.setMode(new ShootMode());
    }

    @Override
    public String toString(){
        return "Move!";
    }
}
