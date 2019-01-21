package lhc.com.dtos;


import lombok.Data;

@Data
public class VoteDto {

    private String reference;
    
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

    public VoteDto(String reference, String competition_ref, Integer indication, String name, Integer points) {
        this.reference = reference;
        this.competition_ref = competition_ref;
        this.indication = indication;
        this.name = name;
        this.points = points;
    }

    public static VoteDto voteDto(String competition_ref, Integer indication, String name, Integer points){
        return new VoteDto(null, competition_ref, indication, name, points);
    }

    public static VoteDto voteDto(Integer indication, String name, Integer points){
        return new VoteDto(null, null, indication, name, points);
    }
}
