package lhc.com.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import lhc.com.adapter.ObjectForAdapter.RankingCell;
import lhc.com.carrdas.R;

import static lhc.com.otherRessources.ApplicationConstants.CLOSED;
import static lhc.com.otherRessources.ApplicationConstants.OPEN;

public class RankingAdapter_TabbedRanking extends ArrayAdapter<RankingCell> {

    private List<RankingCell> rankingCells;
    private Context mContext;

    public RankingAdapter_TabbedRanking(Context context, List<RankingCell> rankingCells) {
        super(context, R.layout.cell_match, rankingCells);
        this.rankingCells = rankingCells;
        this.mContext=context;

    }

    //this will return the ListView Item as a View
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //we need to get the view of the xml for our list item
        //And for this we need a layoutinflater
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        final View result;
        if (convertView == null) {
            result = layoutInflater.inflate(R.layout.cell_ranking, parent, false);
        } else {
            result = convertView;
        }

        RankingCell rankingCell = rankingCells.get(position);

        String positioncell = rankingCell.getPosition();
        String name = rankingCell.getName();
        Integer points = rankingCell.getPoints();

        ((TextView) result.findViewById(R.id.positionInRanking)).setText(positioncell);
        ((TextView) result.findViewById(R.id.nameInRanking)).setText(name);
        ((TextView) result.findViewById(R.id.pointsInRanking)).setText(points);

        //finally returning the view
        return result;
    }

}

