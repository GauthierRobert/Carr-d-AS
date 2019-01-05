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

    public BallotDto() {
        this.voteDtos = new ArrayList<>();
    }
}
