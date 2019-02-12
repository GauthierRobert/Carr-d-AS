package lhc.com.dtos.embedded;

import java.io.Serializable;

import lombok.Data;


@Data
public class TopFlopDetails implements Serializable {

    private boolean withCommentTop;

    private boolean withCommentFlop;

    private String topName;

    private String flopName;

    public TopFlopDetails(boolean withCommentTop, boolean withCommentFlop, String topName, String flopName) {
        this.withCommentTop = withCommentTop;
        this.withCommentFlop = withCommentFlop;
        this.topName = topName;
        this.flopName = flopName;
    }

    public static TopFlopDetails topFlopDetails(boolean withCommentTop, boolean withCommentFlop, String topName, String flopName){
        return new TopFlopDetails(withCommentTop, withCommentFlop, topName, flopName);
    }
}
