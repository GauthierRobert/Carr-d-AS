package lhc.com.adapter.ObjectForAdapter;

import lombok.Data;

@Data
public class RankingCell {

    private String position;
    private String name;
    private Integer points;

    public RankingCell(String position, String name, Integer points) {
        this.position = position;
        this.name = name;
        this.points = points;
    }
}
