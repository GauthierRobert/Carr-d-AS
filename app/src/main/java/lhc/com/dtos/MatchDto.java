package lhc.com.dtos;


import java.util.List;

import lhc.com.dtos.embedded.MatchDetails;
import lhc.com.dtos.embedded.SystemDataDto;
import lombok.Data;

import static lhc.com.dtos.embedded.SystemDataDto.systemDataDto;

@Data
public class MatchDto {

    private SystemDataDto systemDataDto;

    private MatchDetails details;

    private String competition_ref;

    private String date;

    private String status;

    private List<String> visitors;

    private List<BallotDto> ballotDtos;

    private MatchDto(String competition_ref, MatchDetails details, String createdBy, List<String> visitors) {
        this.systemDataDto = systemDataDto(null, createdBy);
        this.competition_ref = competition_ref;
        this.details = details;
        this.visitors = visitors;
    }

    private MatchDto() {
    }

    public static MatchDto matchDto(String competition_ref, MatchDetails details, String createdBy, List<String> visitors){
        return new MatchDto(competition_ref, details, createdBy, visitors);
    }

    private MatchDto(SystemDataDto systemDataDto, MatchDetails details, String competition_ref, String date, String status, List<String> visitors, List<BallotDto> ballotDtos) {
        this.systemDataDto = systemDataDto;
        this.details = details;
        this.competition_ref = competition_ref;
        this.date = date;
        this.status = status;
        this.visitors = visitors;
        this.ballotDtos = ballotDtos;
    }

    public static MatchDto matchDto(){
        return new MatchDto();
    }

    public static MatchDto matchDto(String reference, List<String> visitors){
        return new MatchDto(systemDataDto(reference, null), null, null, null, null, visitors, null);
    }

    public void addVisitor(String visitor) {
        this.visitors.add(visitor);
    }

    public static String createInfo(MatchDto matchDto) {

        return matchDto.getDetails().getHomeTeam() + " " + matchDto.getDetails().getHomeScore() + " - " +
                matchDto.getDetails().getAwayScore() + " " + matchDto.getDetails().getAwayTeam();
    }



}
