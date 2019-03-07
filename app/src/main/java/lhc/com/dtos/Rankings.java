package lhc.com.dtos;

import java.util.List;

import lhc.com.adapter.ObjectForAdapter.RankingCell;
import lombok.Data;

@Data
public class Rankings {

    List<RankingCell> top;

    List<RankingCell> flop;

    public Rankings(List<RankingCell> top, List<RankingCell> flop) {
        this.top = top;
        this.flop = flop;
    }
}
