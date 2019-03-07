package lhc.com.dtos.builder;

import java.util.ArrayList;
import java.util.List;

import lhc.com.dtos.CompetitionDto;
import lhc.com.dtos.RuleDto;
import lhc.com.dtos.enumeration.Label;
import lhc.com.dtos.enumeration.Sport;

import static lhc.com.dtos.CompetitionDto.competitionDto;
import static lhc.com.dtos.embedded.CompetitionDetails.competitionDetails;
import static lhc.com.dtos.embedded.DataName.dataName;
import static lhc.com.dtos.embedded.TopFlopDetails.topFlopDetails;

public class CompetitionDtoBuilder {



    private String name;
    private String season;
    private String division;
    private Sport sport;

    private String password;
    private String confirmedPassword;
    private String creatorUsername;

    private String imageAsBase64;

    private String topName;
    private String flopName;
    private boolean withCommentTop;
    private boolean withCommentFlop;

    private String dataName_1;
    private String dataName_2;
    private String dataName_3;
    private String dataName_4;
    private String dataName_5;

    private List<RuleDto> ruleDtos = new ArrayList<>();


    static CompetitionDtoBuilder aCompetitionDto() {
        return new CompetitionDtoBuilder();
    }

    public static CompetitionDtoBuilder aCompetitionDtoCreatedBy(String creatorUsername) {
        return aCompetitionDto().withCreatorUsername(creatorUsername);
    }

    private CompetitionDtoBuilder withCreatorUsername(String creatorUsername) {
        this.creatorUsername = creatorUsername;
        return this;
    }

    public CompetitionDtoBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public CompetitionDtoBuilder withConfirmedPassword(String confirmedPassword) {
        this.confirmedPassword = confirmedPassword;
        return this;
    }

    public CompetitionDtoBuilder withImage(String imageAsBase64) {
        this.imageAsBase64 = imageAsBase64;
        return this;
    }

    public CompetitionDtoDetailsBuilder finallySport(Sport sport) {
        return new CompetitionDtoDetailsBuilder(sport);
    }


    public class CompetitionDtoDetailsBuilder{

        CompetitionDtoDetailsBuilder(Sport sport) {
            CompetitionDtoBuilder.this.sport = sport;
        }

        public CompetitionDtoDetailsBuilder withName(String name){
            CompetitionDtoBuilder.this.name = name;
            return this;
        }

        public CompetitionDtoDetailsBuilder withSeason(String season){
            CompetitionDtoBuilder.this.season = season;
            return this;
        }

        public CompetitionDtoDetailsBuilder withDivision(String division){
            CompetitionDtoBuilder.this.division = division;
            return this;}

        public TopFlopDetailsDtoBuilder finallyStatisticsName(String _1, String _2, String _3, String _4, String _5){
            return new TopFlopDetailsDtoBuilder(_1,_2,_3,_4,_5);
        }

    }

    public class TopFlopDetailsDtoBuilder{

        TopFlopDetailsDtoBuilder(String _1, String _2, String _3, String _4, String _5) {
            CompetitionDtoBuilder.this.dataName_1 = _1;
            CompetitionDtoBuilder.this.dataName_2 = _2;
            CompetitionDtoBuilder.this.dataName_3 = _3;
            CompetitionDtoBuilder.this.dataName_4 = _4;
            CompetitionDtoBuilder.this.dataName_5 = _5;
        }

        public TopFlopDetailsDtoBuilder withComments(boolean top, boolean flop){
            CompetitionDtoBuilder.this.withCommentTop = top;
            CompetitionDtoBuilder.this.withCommentFlop = flop;
            return this;
        }

        public TopFlopDetailsDtoBuilder withTopFlopName(String topName, String flopName){
            CompetitionDtoBuilder.this.topName = topName;
            CompetitionDtoBuilder.this.flopName = flopName;
            return this;
        }

        public RuleDtoDtoBuilder finallyRules(int numberOfTopVote, int numberOfFlopVote){
            return new RuleDtoDtoBuilder(numberOfTopVote, numberOfFlopVote);
        }
    }


    public class RuleDtoDtoBuilder {

        private final CompetitionDtoCompletion competitionCompletion = new CompetitionDtoCompletion();


        RuleDtoDtoBuilder(int numberOfTopVote, int numberOfFlopVote) {
            RuleDto RuleDtoTop = new RuleDto(Label.NUMBER_VOTE_TOP.name(),numberOfTopVote, 0);
            RuleDto RuleDtoFlop = new RuleDto(Label.NUMBER_VOTE_FLOP.name(), numberOfFlopVote, 0);
            CompetitionDtoBuilder.this.ruleDtos.add(RuleDtoTop);
            CompetitionDtoBuilder.this.ruleDtos.add(RuleDtoFlop);
        }

        public RuleDtoDtoBuilder withTopRuleDtos(Integer[] points){
            if(points != null) {
                int i = 0;
                for (Integer point : points) {
                    i++;
                    RuleDto RuleDto = new RuleDto(Label.POINT_VOTE.name(), point, i);
                    CompetitionDtoBuilder.this.ruleDtos.add(RuleDto);
                }
            }
            return this;
        }

        public RuleDtoDtoBuilder withFlopRuleDtos(Integer[] points){
            if(points != null) {
                int i = 0;
                for (Integer point : points) {
                    i++;
                    RuleDto RuleDto = new RuleDto(Label.POINT_VOTE.name(), point, -i);
                    CompetitionDtoBuilder.this.ruleDtos.add(RuleDto);
                }
            }
            return this;
        }

        public CompetitionDto build(){
            return competitionCompletion.build();
        }

    }


    public class CompetitionDtoCompletion {

        private CompetitionDtoCompletion() {
        }

        public CompetitionDto build() {
            return competitionDto(
                    competitionDetails(name, season, division,sport),
                    topFlopDetails(withCommentTop, withCommentFlop,false, false, topName, flopName ),
                    dataName(dataName_1, dataName_2, dataName_3, dataName_4, dataName_5),
                    password, confirmedPassword, creatorUsername, imageAsBase64, ruleDtos);
        }
    }



}
