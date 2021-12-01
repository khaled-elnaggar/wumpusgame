package view;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class ShootMode extends Mode {

    List<Integer> intendedCavesToShoot = new ArrayList<>();

    @Override
    public void handleLeftClick(int cave) {
        if (intendedCavesToShoot.contains(cave)) {
            intendedCavesToShoot.remove(Integer.valueOf(cave));
        } else {
            if (intendedCavesToShoot.size() < 5) {
                intendedCavesToShoot.add(cave);
            }
        }
        view.setCavesToShoot(intendedCavesToShoot.stream().mapToInt(i -> i).toArray());
    }

    @Override
    public void handleRightClick() {
        if (intendedCavesToShoot.size() > 0) {
            view.shoot(intendedCavesToShoot.stream().mapToInt(i -> i).toArray());
            intendedCavesToShoot.clear();
            view.setCavesToShoot(new int[]{});
        } else {
            Mode.view.setMode(new MoveMode());
        }
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
    public String toString() {
        return "Shoot";
    }

}
