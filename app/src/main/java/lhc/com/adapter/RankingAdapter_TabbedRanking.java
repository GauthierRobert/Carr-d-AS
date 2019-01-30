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

import static lhc.com.otherRessources.ApplicationConstants.FLOP;
import static lhc.com.otherRessources.ApplicationConstants.TOP;

public class RankingAdapter_TabbedRanking extends ArrayAdapter<RankingCell> {

    private List<RankingCell> rankingCells;
    private Context mContext;
    private String rankingType;

    public RankingAdapter_TabbedRanking(Context context, List<RankingCell> rankingCells, String rankingType) {
        super(context, R.layout.cell_match, rankingCells);
        this.rankingCells = rankingCells;
        this.mContext = context;
        this.rankingType = rankingType;
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

        String rankingCellPosition = rankingCell.getPosition();
        String rankingCellName = rankingCell.getName();
        Integer rankingCellPoints = rankingCell.getPoints();

        TextView positionInRankingTextView = result.findViewById(R.id.positionInRanking);
        positionInRankingTextView.setText(rankingCellPosition);
        TextView nameInRankingTextView = result.findViewById(R.id.nameInRanking);
        nameInRankingTextView.setText(rankingCellName);
        TextView pointsInRankingTextView = result.findViewById(R.id.pointsInRanking);
        pointsInRankingTextView.setText(rankingCellPoints.toString());

        int firstColor;
        int secondColor;
        int white = ContextCompat.getColor(mContext, R.color.white);
        if (FLOP.equals(rankingType)) {
            firstColor = ContextCompat.getColor(mContext, R.color.lhc_red_dark);
            secondColor = ContextCompat.getColor(mContext, R.color.lhc_red);
        } else if (TOP.equals(rankingType)) {
            firstColor = ContextCompat.getColor(mContext, R.color.app_green_dark);
            secondColor = ContextCompat.getColor(mContext, R.color.light_green_primary);
        } else {
            firstColor = 0;
            secondColor = 0;
        }

        if (position == 0) {
            result.setBackgroundColor(firstColor);
            positionInRankingTextView.setTextColor(white);
            nameInRankingTextView.setTextColor(white);
            pointsInRankingTextView.setTextColor(white);
        } else if (position < 4) {
            result.setBackgroundColor(secondColor);
            positionInRankingTextView.setTextColor(white);
            nameInRankingTextView.setTextColor(white);
            pointsInRankingTextView.setTextColor(white);
        }
        return result;

    }

}

