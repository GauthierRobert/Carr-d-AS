package lhc.com.carrdas;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lhc.com.adapter.MatchAdapter_ListMatches;
import lhc.com.adapter.SpectatorAdapter_AddMatch;
import lhc.com.dtos.MatchDto;
import lhc.com.otherRessources.ApplicationConstants;
import lhc.com.otherRessources.BaseActivity;
import lhc.com.volley.JsonArrayRequestGet;
import lhc.com.volley.JsonObjectRequestPost;
import lhc.com.volley.MySingletonRequestQueue;

import static lhc.com.dtos.builder.MatchDtoBuilder.aMatchDto;
import static lhc.com.otherRessources.ApplicationConstants.COMPETITION_REF;
import static lhc.com.otherRessources.ApplicationConstants.HAS_VOTED;
import static lhc.com.otherRessources.ApplicationConstants.JSON_LIST_VOTES_INTENT;
import static lhc.com.otherRessources.ApplicationConstants.JSON_MATCH_INTENT;
import static lhc.com.otherRessources.ApplicationConstants.MATCH_CREATOR;
import static lhc.com.otherRessources.ApplicationConstants.MATCH_REF;
import static lhc.com.otherRessources.ApplicationConstants.MATCH_STATUS;
import static lhc.com.otherRessources.ApplicationConstants.MyPREFERENCES_COMPETITION;
import static lhc.com.otherRessources.ApplicationConstants.MyPREFERENCES_CREDENTIALS;
import static lhc.com.otherRessources.ApplicationConstants.URL_BALLOT_GET_LIST;
import static lhc.com.otherRessources.ApplicationConstants.URL_BASE;
import static lhc.com.otherRessources.ApplicationConstants.URL_MATCH_GET;
import static lhc.com.otherRessources.ApplicationConstants.URL_MATCH_GET_LIST;
import static lhc.com.otherRessources.ApplicationConstants.URL_MATCH_POST;
import static lhc.com.otherRessources.ApplicationConstants.USERNAME;
import static lhc.com.otherRessources.ApplicationConstants.createURL;

public class ListMatches extends BaseActivity {


    SharedPreferences sharedPreferencesCompetition;
    SharedPreferences sharedPreferencesCredentials;
    Button newMatchButton;
    ListView listOfMatchesListView;
    List<MatchDto> listMatches;
    SwipeRefreshLayout mySwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_matches);
        sharedPreferencesCompetition = getSharedPreferences(MyPREFERENCES_COMPETITION, MODE_PRIVATE);
        sharedPreferencesCredentials = getSharedPreferences(MyPREFERENCES_CREDENTIALS, MODE_PRIVATE);

        if (getIntent().getBooleanExtra(HAS_VOTED, false)) {
            showDialogBox();
            getIntent().removeExtra(HAS_VOTED);
        }


        GetMatchesListLinkedToCompetition();
        listOfMatchesListView = findViewById(R.id.listViewMatch);
        listOfMatchesListView.setOnItemClickListener(matchClickListener());

        newMatchButton = findViewById(R.id.newMatchButton);
        newMatchButton.setOnClickListener(newMatchAlertDialog());

        mySwipeRefreshLayout = findViewById(R.id.swipe_refresh_matches);
        mySwipeRefreshLayout.setOnRefreshListener(getOnRefreshListener());
    }

    private SwipeRefreshLayout.OnRefreshListener getOnRefreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i("LOG", "onRefresh called from SwipeRefreshLayout");

                // This method performs the actual data-refresh operation.
                // The method calls setRefreshing(false) when it's finished.
                myUpdateOperation();
            }
        };
    }


    private void myUpdateOperation() {
        GetMatchesListLinkedToCompetition();
        ((MatchAdapter_ListMatches) listOfMatchesListView.getAdapter()).notifyDataSetChanged();
    }


    private void showDialogBox() {
        Toast toast = Toast.makeText(ListMatches.this, "Thank you for your vote !", Toast.LENGTH_SHORT);
        toast.show();
    }

    private AdapterView.OnItemClickListener matchClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = sharedPreferencesCompetition.edit();
                MatchDto matchDto = (MatchDto) listOfMatchesListView.getAdapter().getItem(position);
                editor.putString(MATCH_REF, matchDto.getReference());
                editor.putString(MATCH_STATUS, matchDto.getStatus());
                editor.putString(MATCH_CREATOR, matchDto.getCreatorUsername());
                editor.apply();

                getBallotListLinkedToMatch_and_GoToVoteActivity(matchDto.getReference());
            }
        };
    }

    private void GetMatchesListLinkedToCompetition() {
        ApplicationConstants.Parameter parameter = new ApplicationConstants.Parameter(COMPETITION_REF, getCompetition_ref());
        final String url = createURL(URL_MATCH_GET_LIST, parameter);
        RequestQueue requestQueue = MySingletonRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                Request.Method.GET, url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        listMatches = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject json_match = null;

                            try {
                                json_match = response.getJSONObject(i);

                                ObjectMapper mapper = new ObjectMapper();
                                MatchDto matchDto = mapper.readValue(json_match.toString(), MatchDto.class);

                                listMatches.add(matchDto);
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }
                            if (json_match != null) {
                                Log.d(json_match.toString(), "Output");
                            }
                        }
                        final MatchAdapter_ListMatches adapter = new MatchAdapter_ListMatches(ListMatches.this, listMatches);
                        listOfMatchesListView.setAdapter(adapter);
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
                ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error", error.toString());
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    private String getCompetition_ref() {

        return sharedPreferencesCompetition.getString(COMPETITION_REF, null);

    }

    private String getCurrentUser() {

        return sharedPreferencesCredentials.getString(USERNAME, null);

    }

    private View.OnClickListener newMatchAlertDialog() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Set up the layout

                Context context = ListMatches.this;

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = ListMatches.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_custom_match, null);
                ((TextView) dialogView.findViewById(R.id.title_dialog)).setText("Create match");


                final EditText homeTeamET = dialogView.findViewById(R.id.dialog_home);
                final EditText awayTeamET = dialogView.findViewById(R.id.dialog_away);
                final EditText homeScoreET = dialogView.findViewById(R.id.dialog_home_score);
                final EditText awayScoreET = dialogView.findViewById(R.id.dialog_away_score);

                final List<String> spectatorList = new ArrayList<>();
                final ListView listView = dialogView.findViewById(R.id.list_spectator);
                SpectatorAdapter_AddMatch spectatorAdapter_addMatch = new SpectatorAdapter_AddMatch(context, spectatorList);
                listView.setAdapter(spectatorAdapter_addMatch);
                final EditText spectatorET = dialogView.findViewById(R.id.dialog_spectator_name);
                final ImageView add_spectator = dialogView.findViewById(R.id.dialog_image_add_spectator);
                add_spectator.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (spectatorET.getText() != null) {
                            spectatorList.add(spectatorET.getText().toString());
                            ((SpectatorAdapter_AddMatch) listView.getAdapter()).notifyDataSetChanged();
                        }
                    }
                });


                builder.setView(dialogView);

                // Set up the buttons
                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String homeTeam = homeTeamET.getText().toString();
                        String awayTeam = awayTeamET.getText().toString();
                        Integer homeScore = Integer.valueOf(homeScoreET.getText().toString());
                        Integer awayScore = Integer.valueOf(awayScoreET.getText().toString());
                        MatchDto matchDto = getMatchDto(homeTeam, awayTeam, homeScore, awayScore, spectatorList);
                        String jsonMatch = getJsonBody(matchDto);
                        saveMatch(jsonMatch);
                        refresh();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

            }
        };
    }


    private String getJsonBody(MatchDto matchDto) {
        Gson gson = new Gson();
        return gson.toJson(matchDto);
    }

    private void refresh() {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    private MatchDto getMatchDto(String homeTeam, String awayTeam, Integer scoreHome, Integer scoreAway, List<String> spectators) {
        return aMatchDto().createBy(getCurrentUser())
                .withSpectators(spectators)
                .inCompetiton(getCompetition_ref())
                .withHomeTeam(homeTeam)
                .finallyScore(scoreHome)
                .withAwayTeam(awayTeam)
                .finallyScore(scoreAway)
                .build();
    }


    private void saveMatch(String jsonMatch) {
        final Context context = ListMatches.this;
        RequestQueue requestQueue = MySingletonRequestQueue.getInstance(context.getApplicationContext()).getRequestQueue();
        final String mRequestBody = jsonMatch;
        JsonObjectRequestPost jsonObjectRequest = JsonObjectRequestPost.jsonObjectRequestPost(
                URL_BASE + URL_MATCH_POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("onResponse", "Create match : " + response.toString());

                    }
                },
                mRequestBody,
                context);
        requestQueue.add(jsonObjectRequest);
    }


    private void getBallotListLinkedToMatch_and_GoToVoteActivity(String match_ref) {
        final Context context = ListMatches.this;
        ApplicationConstants.Parameter parameter = new ApplicationConstants.Parameter(MATCH_REF, match_ref);
        final String url = createURL(URL_BALLOT_GET_LIST, parameter);
        RequestQueue requestQueue = MySingletonRequestQueue.getInstance(context.getApplicationContext()).getRequestQueue();
        JsonArrayRequestGet jsonObjectRequest = JsonArrayRequestGet.jsonArrayRequestGet(
                url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        goToVoteActivity(response);
                    }
                },
                context);
        requestQueue.add(jsonObjectRequest);
    }

    private void goToVoteActivity(JSONArray response) {
        Intent intent = getIntentWithJsonBallotList(response, JSON_LIST_VOTES_INTENT);
        intent.setClass(ListMatches.this, VoteActivity.class);
        startActivity(intent);
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
