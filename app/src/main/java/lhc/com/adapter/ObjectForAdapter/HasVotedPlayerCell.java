package lhc.com.adapter.ObjectForAdapter;

import lombok.Data;

@Data
public class HasVotedPlayerCell {

    private String player;
    private boolean hasVoted;

    public HasVotedPlayerCell(String player, boolean hasVoted) {
        this.player = player;
        this.hasVoted = hasVoted;
    }

    public HasVotedPlayerCell() {
    }
}
