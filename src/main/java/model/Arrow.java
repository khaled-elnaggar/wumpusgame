package model;

public class Arrow {
    private int numberOfArrows;

    public Arrow(int numberOfArrows) {
        this.numberOfArrows = numberOfArrows;

    }

    public void decrementByOne() {
        this.numberOfArrows--;
    }

    public int getNumber() {
        return numberOfArrows;
    }
}
