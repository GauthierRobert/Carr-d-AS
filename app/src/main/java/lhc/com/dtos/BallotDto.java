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

    private List<VoteDto> voteDtos;

    private String comment;

    public BallotDto() {
    }


    private BallotDto(String reference, String match_ref, String competition_ref, String username, String comment, List<VoteDto> voteDtos) {
        this.reference = reference;
        this.match_ref = match_ref;
        this.competition_ref = competition_ref;
        this.username = username;
        this.comment = comment;
        this.voteDtos = voteDtos;
    }

    public static BallotDto ballotDto(String reference, String username){
        return new BallotDto(reference, null, null, username,null ,new ArrayList<VoteDto>());
    }

    public static BallotDto ballotDto(String reference, String match_ref, String competition_ref, String username, String comment, List<VoteDto> voteDtos){
        return new BallotDto(reference, match_ref, competition_ref, username, comment, voteDtos);
    }

    public static BallotDto ballotDto(String match_ref, String competition_ref, String username, String comment, List<VoteDto> voteDtos){
        return new BallotDto(null, match_ref, competition_ref, username, comment, voteDtos);
    }


}
