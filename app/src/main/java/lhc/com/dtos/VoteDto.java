package lhc.com.dtos;


import lombok.Data;

@Data
public class VoteDto {

    private String reference;
    
    private String ballot_ref;
    
    private String match_ref;
    
    private String competition_ref;

    // 1 for first top vote
    // 2 for second top vote
    // ...
    // -1 for first flop vote
    // -2 for second flop vote
    private Integer indication;

    private String name;

    private Integer points;

    public VoteDto() {
    }

    public VoteDto(String reference, String ballot_ref, String match_ref, String competition_ref, Integer indication, String name, Integer points) {
        this.reference = reference;
        this.ballot_ref = ballot_ref;
        this.match_ref = match_ref;
        this.competition_ref = competition_ref;
        this.indication = indication;
        this.name = name;
        this.points = points;
    }

    public static VoteDto voteDto(String ballot_ref, String match_ref, String competition_ref, Integer indication, String name, Integer points){
        return new VoteDto(null, ballot_ref, match_ref, competition_ref, indication, name, points);
    }
}
