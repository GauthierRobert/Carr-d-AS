package lhc.com.dtos;


import lombok.Data;

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
    
    private Date date;

}
