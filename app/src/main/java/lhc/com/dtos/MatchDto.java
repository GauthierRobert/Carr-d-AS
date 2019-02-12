package lhc.com.dtos;


import lhc.com.dtos.embedded.MatchDetails;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class MatchDto {

    private String reference;

    private MatchDetails details;

    private String competition_ref;

    private String creatorUsername;

    private String date;

    private String status;

    private List<String> visitors;

    private List<BallotDto> ballotDtos;

    private MatchDto(String competition_ref, MatchDetails details, String creatorUsername, List<String> visitors) {
        this.competition_ref = competition_ref;
        this.details = details;
        this.creatorUsername = creatorUsername;
        this.visitors = visitors;
    }

    private MatchDto() {
    }

    public static MatchDto matchDto(String competition_ref, MatchDetails details, String creatorUsername, List<String> visitors){
        return new MatchDto(competition_ref, details, creatorUsername, visitors);
    }

    public static MatchDto matchDto() {
        return new MatchDto();
    }

    public void addVisitor(String visitor) {
        this.visitors.add(visitor);
    }


}
