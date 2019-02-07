package lhc.com.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
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
        ImageView toggleMatch = view.findViewById(R.id.toggle_match);
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

        name.setText(createInfo(matchDto));
        date.setText(matchDto.getDate());
        toggleMatch.setOnClickListener(getMenuForToggle());

        //finally returning the view
        return view;
    }

    private String createInfo(MatchDto matchDto) {


        return matchDto.getHomeTeam() + " " + matchDto.getHomeScore() + " - " +
                matchDto.getAwayScore() + " " + matchDto.getAwayTeam();

    }


    private View.OnClickListener getMenuForToggle() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.toggle_match:
                        PopupMenu popup = new PopupMenu(mContext.getApplicationContext(), view);
                        popup.setOnMenuItemClickListener(getOnMenuItemClickListener());
                        popup.getMenuInflater().inflate(R.menu.clipboard_popup_match, popup.getMenu());
                        popup.show();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private PopupMenu.OnMenuItemClickListener getOnMenuItemClickListener() {
        return new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit:
                        break;
                    case R.id.delete:
                        break;
                    default:
                        break;
                }
                return true;
            }
        };
    }

}

