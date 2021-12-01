package view;

import java.awt.*;

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
    public Color getAllCavesColor() {
        return Color.orange;
    }

    @Override
    public Color getLinkedCavesColor() {
        return Color.magenta;
    }

    @Override
    public String getIconName() {
        return "move.png";
    }

    @Override
    public String toString(){
        return "Move!";
    }
}
