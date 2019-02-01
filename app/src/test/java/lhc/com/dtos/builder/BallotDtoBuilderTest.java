package lhc.com.dtos.builder;

import org.junit.Assert;
import org.junit.Test;

import lhc.com.dtos.BallotDto;

import static lhc.com.dtos.builder.BallotDtoBuilder.aBallotDto;

public class BallotDtoBuilderTest {

    public static final String COMMENT = "Top someone, flop someone";

    @Test
    public void withComment() {

        BallotDto actual = aBallotDto().withComment(COMMENT).buildWithoutVotes();

        Assert.assertEquals(actual.getComment(), COMMENT);

    }
}