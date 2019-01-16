package lhc.com.dtos;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BallotDto {

    private String reference;
    
    private String match_ref;
    
    private String competition_ref;

    private List<VoteDto> voteDtos;

    private List<RuleDto> ruleDtos;

    public BallotDto() {
    }


    public BallotDto(String reference, String match_ref, String competition_ref, List<VoteDto> voteDtos, List<RuleDto> ruleDtos) {
        this.reference = reference;
        this.match_ref = match_ref;
        this.competition_ref = competition_ref;
        this.voteDtos = voteDtos;
        this.ruleDtos = ruleDtos;
    }

    public static BallotDto ballotDto(String reference){
        return new BallotDto(reference, null, null, new ArrayList<VoteDto>(), new ArrayList<RuleDto>());
    }

    public static BallotDto ballotDto(String reference, String match_ref, String competition_ref, List<VoteDto> voteDtos, List<RuleDto> ruleDtos){
        return new BallotDto(reference, match_ref, competition_ref, voteDtos, ruleDtos);
    }

    public static BallotDto ballotDto(String match_ref, String competition_ref, List<VoteDto> voteDtos, List<RuleDto> ruleDtos){
        return new BallotDto(null, match_ref, competition_ref, voteDtos, ruleDtos);
    }


}
