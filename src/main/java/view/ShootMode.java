package view;

import java.awt.*;

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
    public Color getAllCavesColor() {
        return Color.red;
    }

    @Override
    public Color getLinkedCavesColor() {
        return getAllCavesColor();
    }

    @Override
    public String getIconName() {
        return "shoot.png";
    }

    @Override
    public String toString(){
        return "Shoot";
    }

}
