package model.game;

public enum LegacyHazard {
    Wumpus("there's an awful smell"),
    Bat("you hear a rustling"),
    Pit("you feel a draft");

    LegacyHazard(String warning) {
        this.warning = warning;
    }

    public String getWarning() {
        return warning;
    }

    final String warning;
}
