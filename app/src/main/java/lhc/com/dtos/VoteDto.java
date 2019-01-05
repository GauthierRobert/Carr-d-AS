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
}
