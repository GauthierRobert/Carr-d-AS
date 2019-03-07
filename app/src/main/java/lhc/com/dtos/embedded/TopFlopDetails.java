package lhc.com.dtos.embedded;

import java.io.Serializable;

import lombok.Data;


@Data
public class TopFlopDetails implements Serializable {

    private boolean withCommentTop;

    private boolean withCommentFlop;

    private boolean withValidationTop;

    private boolean withValidationFlop;

    private String topName;

    private String flopName;

    public TopFlopDetails() {
    }

    public TopFlopDetails(boolean withCommentTop, boolean withCommentFlop, boolean withValidationTop, boolean withValidationFlop, String topName, String flopName) {
        this.withCommentTop = withCommentTop;
        this.withCommentFlop = withCommentFlop;
        this.withValidationTop = withValidationTop;
        this.withValidationFlop = withValidationFlop;
        this.topName = topName;
        this.flopName = flopName;
    }

    public static TopFlopDetails topFlopDetails(boolean withCommentTop, boolean withCommentFlop, boolean withValidationTop, boolean withValidationFlop, String topName, String flopName){
        if(!withCommentTop){
            withValidationTop = false;
        }

        if(!withCommentFlop){
            withValidationFlop = false;
        }
        return new TopFlopDetails(withCommentTop,
                withCommentFlop,
                withValidationTop,
                withValidationFlop,
                topName,
                flopName);
    }
}
