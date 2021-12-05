package acceptance.support;

public class MoveAction extends Action {

    private final int caveToMoveTo;

    public MoveAction(int caveToMoveTo) {
        this.caveToMoveTo = caveToMoveTo;
    }

    @Override
    public void execute() {
        wumpusPresenter.move(caveToMoveTo);
    }

    @Override
    public String toString(){
        return "Player Move to caves " + caveToMoveTo;
    }
}
