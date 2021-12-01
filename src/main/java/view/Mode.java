package view;

import java.awt.*;

public abstract class Mode {
    protected static WumpusViewImpl view;
    public static void setView(WumpusViewImpl view){
        Mode.view = view;
    }

    public abstract void handleLeftClick(int cave);

    public abstract void handleRightClick();

    public abstract Color getAllCavesColor();

    public abstract Color getLinkedCavesColor();

    public abstract String getIconName();
}
