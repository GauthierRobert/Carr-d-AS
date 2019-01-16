package lhc.com.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import lhc.com.carrdas.R;
import lhc.com.dtos.MatchDto;

public class MatchAdapter_ListMatches extends ArrayAdapter<MatchDto> {

    private List<MatchDto> matchDtos;
    private Context mContext;

    public MatchAdapter_ListMatches(Context context, List<MatchDto> matchDtos) {
        super(context, R.layout.match_cell, matchDtos);
        this.matchDtos = matchDtos;
        this.mContext=context;

    }

    //this will return the ListView Item as a View
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //we need to get the view of the xml for our list item
        //And for this we need a layoutinflater
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        //getting the view
        View view = layoutInflater.inflate(R.layout.match_cell, null, false);

        //getting the view elements of the list from the view
        TextView name = view.findViewById(R.id.info_match_cell);
        TextView date = view.findViewById(R.id.date_match);

        //getting the hero of the specified position

        MatchDto matchDto = matchDtos.get(position);

        //adding values to the list item
        name.setText(createInfo(matchDto));
        date.setText(matchDto.getDate());

        //finally returning the view
        return view;
    }

    private String createInfo(MatchDto matchDto) {


        return matchDto.getHomeTeam() + " " + matchDto.getHomeScore() + " - " +
                matchDto.getAwayScore() + " " + matchDto.getAwayTeam();

    }

}

