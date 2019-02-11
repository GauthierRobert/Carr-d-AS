package lhc.com.adapter;

import android.content.Context;
import android.content.Intent;
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

import com.android.volley.RequestQueue;
import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import lhc.com.carrdas.InfoMatchActivity;
import lhc.com.carrdas.ListMatches;
import lhc.com.carrdas.R;
import lhc.com.dtos.MatchDto;
import lhc.com.otherRessources.ApplicationConstants;
import lhc.com.volley.JsonArrayRequestGet;
import lhc.com.volley.MySingletonRequestQueue;

import static lhc.com.otherRessources.ApplicationConstants.CLOSED;
import static lhc.com.otherRessources.ApplicationConstants.COMPETITION_REF;
import static lhc.com.otherRessources.ApplicationConstants.JSON_MATCH_INTENT;
import static lhc.com.otherRessources.ApplicationConstants.MATCH_REF;
import static lhc.com.otherRessources.ApplicationConstants.ON_HOLD;
import static lhc.com.otherRessources.ApplicationConstants.ON_HOLD_TEXT;
import static lhc.com.otherRessources.ApplicationConstants.OPEN;
import static lhc.com.otherRessources.ApplicationConstants.URL_MATCH_GET;
import static lhc.com.otherRessources.ApplicationConstants.URL_USER_GET;
import static lhc.com.otherRessources.ApplicationConstants.createURL;
import static lhc.com.volley.JsonArrayRequestGet.jsonArrayRequestGet;

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
        toggleMatch.setOnClickListener(getMenuForToggle(matchDto.getReference()));

        //finally returning the view
        return view;
    }

    private String createInfo(MatchDto matchDto) {


        return matchDto.getHomeTeam() + " " + matchDto.getHomeScore() + " - " +
                matchDto.getAwayScore() + " " + matchDto.getAwayTeam();

    }


    private View.OnClickListener getMenuForToggle(final String match_ref) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.toggle_match:
                        PopupMenu popup = new PopupMenu(mContext.getApplicationContext(), view);
                        popup.setOnMenuItemClickListener(getOnMenuItemClickListener(match_ref));
                        popup.getMenuInflater().inflate(R.menu.clipboard_popup_match, popup.getMenu());
                        popup.show();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private PopupMenu.OnMenuItemClickListener getOnMenuItemClickListener(final String match_ref) {
        return new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.info:
                        getMatch_and_GoToInfoActivity(match_ref);
                        break;
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

    private void getMatch_and_GoToInfoActivity(String match_ref) {
        ApplicationConstants.Parameter parameter = new ApplicationConstants.Parameter(MATCH_REF, match_ref);
        final String url = createURL(URL_MATCH_GET, parameter);
        RequestQueue requestQueue = MySingletonRequestQueue.getInstance(mContext.getApplicationContext()).getRequestQueue();
        JsonArrayRequestGet jsonObjectRequest = JsonArrayRequestGet.jsonArrayRequestGet(
                url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        goToInfoActivity(response);
                    }
                },
                mContext);
        requestQueue.add(jsonObjectRequest);
    }


    private void goToInfoActivity(JSONArray response) {
        Intent intent = getIntentWithJsonBallotList(response, JSON_MATCH_INTENT);
        intent.setClass(mContext, InfoMatchActivity.class);
        mContext.startActivity(intent);
    }

    private Intent getIntentWithJsonBallotList(JSONArray response, String constant) {
        Intent intent = new Intent();
        if (response != null)
            if (response.length() > 0) {
                intent.putExtra(constant, response.toString());
            }
        return intent;
    }




}

