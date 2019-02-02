package lhc.com.dtos;


import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class CompetitionDto {

    private String reference;

    private String name;

    private String password;

    private String confirmedPassword;

    private String creatorUsername;

    private String division;

    private String season;

    private boolean withCommentTop;

    private boolean withCommentFlop;

    private List<MatchDto> matchDtos;

    private List<RuleDto> ruleDtos;

    public CompetitionDto() {
        this.ruleDtos = new ArrayList<>();
        this.matchDtos = new ArrayList<>();
    }

    public static CompetitionDto competitionDto(String name, String season, String division,  String password, String confirmedPassword, String creatorUsername, boolean withCommentTop, boolean withCommentFlop,List<RuleDto> ruleDtos) {
        return new CompetitionDto(
                name,
                name,
                password,
                confirmedPassword,
                creatorUsername,
                division,
                season,
                withCommentTop,
                withCommentFlop,
                new ArrayList<MatchDto>() ,
                ruleDtos);

    }

    private CompetitionDto(String reference, String name, String password, String confirmedPassword, String creatorUsername, String division, String season, boolean withCommentTop, boolean withCommentFlop,List<MatchDto> matchDtos, List<RuleDto> ruleDtos) {
        this.reference = reference;
        this.name = name;
        this.password = password;
        this.confirmedPassword = confirmedPassword;
        this.creatorUsername = creatorUsername;
        this.division = division;
        this.season = season;
        this.withCommentTop = withCommentTop;
        this.withCommentFlop = withCommentFlop;
        this.matchDtos = matchDtos;
        this.ruleDtos = ruleDtos;
    }

}
