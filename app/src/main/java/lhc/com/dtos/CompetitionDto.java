package lhc.com.dtos;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CompetitionDto {

    private String reference;

    private String name;

    private String password;

    private String confirmedPassword;

    private String usernameCreator;

    private String division;

    private String season;

    private List<MatchDto> matchDtos;

    private List<RuleDto> ruleDtos;

    public CompetitionDto() {
        this.ruleDtos = new ArrayList<>();
        this.matchDtos = new ArrayList<>();
    }


}
