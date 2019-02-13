package lhc.com.dtos.embedded;

import lhc.com.dtos.enumeration.Sport;
import lombok.Data;


@Data
public class CompetitionDetails {

    private String name;

    private String season;

    private String division;

    private Sport sport;

    public CompetitionDetails() {
    }

    public CompetitionDetails(String name, String season, String division, Sport sport) {
        this.name = name;
        this.season = season;
        this.division = division;
        this.sport = sport;
    }

    public static CompetitionDetails competitionDetails(String name, String season, String division, Sport sport){
        return new CompetitionDetails(name, season, division, sport);
    }
}
