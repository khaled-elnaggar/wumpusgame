package view;

public abstract class Mode {
    protected static WumpusViewImpl view;
    public static void setView(WumpusViewImpl view){
        Mode.view = view;
    }

    public abstract void handleLeftClick(int cave);

    public abstract void handleRightClick();
}
