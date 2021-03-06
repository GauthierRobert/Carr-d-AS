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

import lhc.com.carrdas.R;
import lhc.com.dtos.MatchDto;

import static lhc.com.otherRessources.ApplicationConstants.CLOSED;
import static lhc.com.otherRessources.ApplicationConstants.ON_HOLD;
import static lhc.com.otherRessources.ApplicationConstants.ON_HOLD_TEXT;
import static lhc.com.otherRessources.ApplicationConstants.OPEN;

public class MatchAdapter_ListMatches extends ArrayAdapter<MatchDto> {

    private List<MatchDto> matchDtos;
    private Context mContext;
    private String[] players;


    public MatchAdapter_ListMatches(Context context, List<MatchDto> matchDtos) {
        super(context, R.layout.cell_match, matchDtos);
        this.matchDtos = matchDtos;
        this.mContext = context;

    }

    //this will return the ListView Item as a View
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //we need to get the view of the xml for our list item
        //And for this we need a layoutinflater
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        //getting the view
        View view = layoutInflater.inflate(R.layout.cell_match, null, false);

        //getting the view elements of the list from the view
        TextView name = view.findViewById(R.id.info_match_cell);
        TextView date = view.findViewById(R.id.date_match_cell);
        TextView status = view.findViewById(R.id.status_match_cell);
        //getting the hero of the specified position

        MatchDto matchDto = matchDtos.get(position);

        //adding values to the list item
        if (matchDto.getStatus() != null) {
            if (CLOSED.equals(matchDto.getStatus())) {
                int red = ContextCompat.getColor(mContext, R.color.lhc_red);
                status.setText(CLOSED);
                status.setTextColor(red);
            } else if (OPEN.equals(matchDto.getStatus())) {
                int green = ContextCompat.getColor(mContext, R.color.green_primary_dark);
                status.setText(OPEN);
                status.setTextColor(green);
            } else if (ON_HOLD.equals(matchDto.getStatus())) {
                int orange = ContextCompat.getColor(mContext, R.color.orange_primary);
                status.setText(ON_HOLD_TEXT);
                status.setTextColor(orange);
            }
        }

        name.setText(matchDto.getDetails().createInfo());
        date.setText(matchDto.getDate());

        //finally returning the view
        return view;
    }







}

