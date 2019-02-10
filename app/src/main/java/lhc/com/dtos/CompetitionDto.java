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

    private String imageAsBase64;

    private String topName;

    private String flopName;

    private boolean withCommentTop;

    private boolean withCommentFlop;

    private List<MatchDto> matchDtos;

    private List<RuleDto> ruleDtos;

    public CompetitionDto() {
        this.ruleDtos = new ArrayList<>();
        this.matchDtos = new ArrayList<>();
    }

    public static CompetitionDto competitionDto(String name, String season, String division,  String password, String confirmedPassword, String creatorUsername, String topName, String flopName, boolean withCommentTop, boolean withCommentFlop,List<RuleDto> ruleDtos) {
        return new CompetitionDto(
                null,
                name,
                password,
                confirmedPassword,
                creatorUsername,
                division,
                season,
                null,
                topName,
                flopName,
                withCommentTop,
                withCommentFlop,
                new ArrayList<MatchDto>() ,
                ruleDtos);

    }

    public CompetitionDto(String reference, String name, String password, String confirmedPassword, String creatorUsername, String division, String season, String imageAsBase64, String topName, String flopName, boolean withCommentTop, boolean withCommentFlop, List<MatchDto> matchDtos, List<RuleDto> ruleDtos) {
        this.reference = reference;
        this.name = name;
        this.password = password;
        this.confirmedPassword = confirmedPassword;
        this.creatorUsername = creatorUsername;
        this.division = division;
        this.season = season;
        this.imageAsBase64 = imageAsBase64;
        this.topName = topName;
        this.flopName = flopName;
        this.withCommentTop = withCommentTop;
        this.withCommentFlop = withCommentFlop;
        this.matchDtos = matchDtos;
        this.ruleDtos = ruleDtos;
    }
}
