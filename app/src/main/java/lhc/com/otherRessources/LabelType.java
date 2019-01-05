package lhc.com.otherRessources;

public enum LabelType {

    NUMBER_VOTE_TOP(1),
    NUMBER_VOTE_FLOP(2),
    POINT_VOTE(3);

    private int value;

    LabelType(int value) {
        this.value = value;

    }

    public int getValue() {
        return value;
    }


}
