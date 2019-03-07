package lhc.com.dtos;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BallotDto {



    private String reference;

    private String match_ref;

    private String competition_ref;

    private String username;

    private String commentTop;

    private String commentFlop;

    private boolean counted;

    private List<VoteDto> voteDtos;

    public BallotDto() {
    }

    public BallotDto(String reference, String match_ref, String competition_ref, String username, String commentTop, String commentFlop, boolean counted, List<VoteDto> voteDtos) {
        this.reference = reference;
        this.match_ref = match_ref;
        this.competition_ref = competition_ref;
        this.username = username;
        this.commentTop = commentTop;
        this.commentFlop = commentFlop;
        this.counted = counted;
        this.voteDtos = voteDtos;
    }

    public static BallotDto ballotDto(String reference, String username){
        return new BallotDto(reference, null, null, username,null,null, false, new ArrayList<VoteDto>());
    }

    public static BallotDto ballotDto(String reference, String match_ref, String competition_ref, String username, String commentTop, String commentFlop, List<VoteDto> voteDtos){
        return new BallotDto(reference, match_ref, competition_ref, username, commentTop, commentFlop, false,voteDtos);
    }

    public static BallotDto ballotDto(String match_ref, String competition_ref, String username, String commentTop, String commentFlop, List<VoteDto> voteDtos){
        return new BallotDto(null, match_ref, competition_ref, username, commentTop, commentFlop, false,voteDtos);
    }

    public static BallotDto ballotDto(String reference, String match_ref, String competition_ref, String username, String commentTop, String commentFlop, boolean counted, List<VoteDto> voteDtos){
        return new BallotDto(reference, match_ref, competition_ref, username, commentTop, commentFlop, counted,voteDtos);
    }
}
