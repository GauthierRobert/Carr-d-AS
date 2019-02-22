package lhc.com.carrdas;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lhc.com.adapter.HasVotedAdapter_InfoMatchActivity;
import lhc.com.adapter.ObjectForAdapter.HasVotedPlayerCell;
import lhc.com.dtos.BallotDto;
import lhc.com.dtos.MatchDto;
import lhc.com.otherRessources.ApplicationConstants;
import lhc.com.volley.JsonArrayRequestGet;
import lhc.com.volley.MySingletonRequestQueue;

import static lhc.com.dtos.MatchDto.createInfo;
import static lhc.com.otherRessources.ApplicationConstants.COMPETITION_REF;
import static lhc.com.otherRessources.ApplicationConstants.JSON_MATCH_INTENT;
import static lhc.com.otherRessources.ApplicationConstants.MyPREFERENCES_COMPETITION;
import static lhc.com.otherRessources.ApplicationConstants.URL_USER_GET_LIST;
import static lhc.com.otherRessources.ApplicationConstants.createURL;
import static lhc.com.volley.JsonArrayRequestGet.jsonArrayRequestGet;

public class InfoMatchActivity extends AppCompatActivity {

    private List<HasVotedPlayerCell> players;
    private List<String> hasVotedPlayers;
    final private Context mContext = InfoMatchActivity.this;
    private SharedPreferences sharedPreferencesCompetition;
    private ListView hasVotedListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_match);
        sharedPreferencesCompetition = getSharedPreferences(MyPREFERENCES_COMPETITION, MODE_PRIVATE);
        hasVotedListView = findViewById(R.id.listViewHasVoted);
        TextView matchInfo = findViewById(R.id.match_description_info);
        TextView matchDate = findViewById(R.id.match_date_info);

        String jsonMatch = getIntent().getStringExtra(JSON_MATCH_INTENT);

        ObjectMapper mapper = new ObjectMapper();
        MatchDto matchDto = null;
        try {
            matchDto = mapper.readValue(jsonMatch, MatchDto.class);
            hasVotedPlayers = createList(matchDto.getBallotDtos());
        } catch (IOException e) {
            e.printStackTrace();
        }
        matchInfo.setText(createInfo(matchDto));
        matchDate.setText(matchDto.getDate());
        
        GetUsernamesLinkedToCompetition();

    }

    private List<String> createList(List<BallotDto> ballotDtos) {

        List<String> hasVotedUsers = new ArrayList<>();

        for (BallotDto ballotDto : ballotDtos) {
            hasVotedUsers.add(ballotDto.getUsername().toLowerCase());
        }

        return hasVotedUsers;
    }

    private void GetUsernamesLinkedToCompetition() {
        ApplicationConstants.Parameter parameter = new ApplicationConstants.Parameter(COMPETITION_REF, getCompetition_ref());
        final String url = createURL(URL_USER_GET_LIST, parameter);

        RequestQueue requestQueue = MySingletonRequestQueue.getInstance(mContext.getApplicationContext()).getRequestQueue();

        JsonArrayRequestGet jsonObjectRequest = jsonArrayRequestGet(
                url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<String> playersList = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                playersList.add(response.get(i).toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        players = compareList(playersList, hasVotedPlayers);

                        HasVotedAdapter_InfoMatchActivity hasVotedAdapter = new HasVotedAdapter_InfoMatchActivity(mContext, players);
                        hasVotedListView.setAdapter(hasVotedAdapter);

                    }
                },
                mContext);
        requestQueue.add(jsonObjectRequest);
    }

    private List<HasVotedPlayerCell> compareList(List<String> playersList, List<String> hasVotedPlayers) {
        List<HasVotedPlayerCell> hasVotedPlayerCells = new ArrayList<>();
        for (String player : playersList) {
            hasVotedPlayerCells.add(new HasVotedPlayerCell(player, hasVotedPlayers.contains(player.toLowerCase())));
        }
        return hasVotedPlayerCells;
    }


    private String getCompetition_ref() {
        return sharedPreferencesCompetition.getString(COMPETITION_REF, null);
    }
}
