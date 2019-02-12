package lhc.com.dtos.enumeration;

public enum Label {

    NUMBER_VOTE_TOP(1),
    NUMBER_VOTE_FLOP(2),
    POINT_VOTE(3);

    private int value;

    Label(int value) {
        this.value = value;

    }

    public int getValue() {
        return value;
    }


}
