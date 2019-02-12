package lhc.com.dtos.enumeration;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.ArrayList;
import java.util.List;

public enum Sport {

    HOCKEY(1L, "Hockey"),
    ICE_HOCKEY(2L, "Ice hockey"),
    FOOTBALL(3L, "Football"),
    BACKETBALL(4L, "BasketBall"),
    FUTSAL(5L, "Futsal"),
    HANDBALL(6L, "Handball"),
    RUGBY(7L, "Rugby"),
    AMERICAN_FOOTBALL(8L, "American football")
    ;

    private Long value;
    private String name;

    Sport(Long value, String name) {
        this.value = value;
        this.name = name;
    }

    public Long getValue() {
        return value;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    public List<String> getSports(){
        List<String> sports = new ArrayList<>();
        for (Sport sport:Sport.values()) {
            sports.add(sport.getName());
        }
        return sports;
    }


}
