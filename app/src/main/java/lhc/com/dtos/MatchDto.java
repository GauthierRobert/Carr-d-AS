package lhc.com.dtos;


import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class MatchDto {

    private String reference;

    private String homeTeam;

    private Integer homeScore;

    private Integer awayScore;

    private String awayTeam;
    
    private String competition_ref;

    private String creatorUsername;

    private String date;

    public MatchDto(String competition_ref, String homeTeam, Integer homeScore, Integer awayScore, String awayTeam) {
        this.homeTeam = homeTeam;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.awayTeam = awayTeam;
        this.competition_ref = competition_ref;
    }

    public MatchDto() {
    }

    public static MatchDto matchDto(String competition_ref, String homeTeam, Integer homeScore, Integer awayScore, String awayTeam){
        return new MatchDto(competition_ref, homeTeam,homeScore, awayScore,awayTeam);
    }


}
