package lhc.com.dtos.embedded;

import java.io.Serializable;

import lombok.Data;


@Data
public class MatchDetails implements Serializable {


    private String homeTeam;

    private Integer homeScore;

    private String awayTeam;

    private Integer awayScore;

    public MatchDetails(String homeTeam, Integer homeScore, String awayTeam, Integer awayScore) {
        this.homeTeam = homeTeam;
        this.homeScore = homeScore;
        this.awayTeam = awayTeam;
        this.awayScore = awayScore;
    }

    public static MatchDetails matchDetails(String homeTeam, Integer homeScore, String awayTeam, Integer awayScore){
        return new MatchDetails(homeTeam, homeScore, awayTeam, awayScore);
    }
}
