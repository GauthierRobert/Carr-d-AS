package lhc.com.dtos;

import lombok.Data;

@Data
public class NumberVote {


    String id;
    Integer points;

    public NumberVote(String id, Integer points) {
        this.id = id;
        this.points = points;
    }
}
