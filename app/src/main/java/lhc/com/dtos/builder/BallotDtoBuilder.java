package lhc.com.dtos.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import lhc.com.dtos.BallotDto;
import lhc.com.dtos.RuleDto;
import lhc.com.dtos.VoteDto;

import static lhc.com.dtos.BallotDto.ballotDto;

public class BallotDtoBuilder {

    private String reference;
    private String match_ref;
    private String competition_ref;
    private List<VoteDto> voteDtos = new ArrayList<>();
    private List<RuleDto> ruleDtos = new ArrayList<>();

    public static BallotDtoBuilder aBallotDto(){
        return new BallotDtoBuilder();
    }

    public static BallotDtoBuilder aBallotDtoWithRules(List<RuleDto> ruleDtos){
        return aBallotDto().withRuleDtos(ruleDtos);
    }

    private BallotDtoBuilder withRuleDtos(List<RuleDto> ruleDtos) {
        this.ruleDtos = ruleDtos;
        return this;
    }

    public BallotDtoBuilder withReference(String reference){
        this.reference = reference;
        return this;
    }

    public BallotDtoBuilder withMatch_ref(String match_ref){
        this.match_ref = match_ref;
        return this;
    }

    public BallotDtoBuilder withCompetition_ref(String competition_ref){
        this.competition_ref = competition_ref;
        return this;
    }

    public VoteDtoBuilder withVotesDtos(){

        return new VoteDtoBuilder();
    }

    public class VoteDtoBuilder {

        private final BallotDtoCompletion ballotCompletion = new BallotDtoCompletion();
        Map<Integer, Integer> rules = getMapRules(ruleDtos);
        public VoteDtoBuilder() {

        }

        public VoteDtoBuilder addTopVote(String[] names){
            int i = 0;
            for (String name : names) {
                i++;
                int point = rules.get(i);
                VoteDto voteDto  = VoteDto.voteDto(reference, i, name, point);
                BallotDtoBuilder.this.voteDtos.add(voteDto);

            }
            return this;
        }

        public VoteDtoBuilder addFlopVote(String[] names){
            int i = 0;
            for (String name : names) {
                i++;
                int point = rules.get(-i);
                VoteDto voteDto  = VoteDto.voteDto(reference,-i, name, point);
                BallotDtoBuilder.this.voteDtos.add(voteDto);
            }
            return this;
        }

        public BallotDto build(){
            return ballotCompletion.build();
        }

    }


    public class BallotDtoCompletion {

        private BallotDtoCompletion(){
        }

        public BallotDto build(){
            return ballotDto(match_ref, competition_ref, voteDtos);
        }
    }

    private Map<Integer, Integer> getMapRules(List<RuleDto> ruleDtos){

        final Map<Integer, Integer> rules = new HashMap<>();
        ruleDtos.forEach(new Consumer<RuleDto>() {
            @Override
            public void accept(RuleDto ruleDto) {
                if (ruleDto.getIndication() != 0) {
                    rules.put(ruleDto.getIndication(), ruleDto.getPoints());
                }
            }
        });

        return rules;
    }

}
