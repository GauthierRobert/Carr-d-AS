package lhc.com.carrdas;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lhc.com.adapter.CompetitionAdapter_ListCompetitions;
import lhc.com.adapter.MatchAdapter_ListMatches;
import lhc.com.dtos.CompetitionDto;
import lhc.com.dtos.MatchDto;
import lhc.com.otherRessources.ApplicationConstants;
import lhc.com.otherRessources.MySingletonRequestQueue;

import static lhc.com.dtos.builder.CompetitionDtoBuilder.aCompetitionDto;
import static lhc.com.dtos.builder.MatchDtoBuilder.aMatchDto;
import static lhc.com.otherRessources.ApplicationConstants.COMPETITION_REF;
import static lhc.com.otherRessources.ApplicationConstants.MyPREFERENCES_COMPETITION;
import static lhc.com.otherRessources.ApplicationConstants.MyPREFERENCES_CREDENTIALS;
import static lhc.com.otherRessources.ApplicationConstants.URL_BASE;
import static lhc.com.otherRessources.ApplicationConstants.URL_COMPETITION_GET;
import static lhc.com.otherRessources.ApplicationConstants.URL_COMPETITION_POST;
import static lhc.com.otherRessources.ApplicationConstants.URL_MATCH_GET;
import static lhc.com.otherRessources.ApplicationConstants.URL_MATCH_POST;
import static lhc.com.otherRessources.ApplicationConstants.USERNAME;
import static lhc.com.otherRessources.ApplicationConstants.createURL;

public class ListMatchesOfCompetition extends AppCompatActivity {

    SharedPreferences sharedpreferencesCompetition;
    Button newMatchButton;
    ListView listView;
    List<MatchDto> listMatches;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_match_of_competition);
        sharedpreferencesCompetition = getSharedPreferences(MyPREFERENCES_COMPETITION, MODE_PRIVATE);
        listView = findViewById(R.id.listViewMatch);
        GetMatchesListLinkedToCompetition();
        listView.setOnItemClickListener(matchClickListener());

        newMatchButton = findViewById(R.id.newMatchButton);
        newMatchButton.setOnClickListener(newMatchAlerlDialog());

    }

    private AdapterView.OnItemClickListener matchClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToVoteActivity();
            }
        };
    }

    private void GetMatchesListLinkedToCompetition() {
        ApplicationConstants.Parameter parameter = new ApplicationConstants.Parameter(COMPETITION_REF, getCompetition_ref());
        final String url = createURL(URL_MATCH_GET, parameter);
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

                            final MatchAdapter_ListMatches adapter = new MatchAdapter_ListMatches(ListMatchesOfCompetition.this,listMatches);
                            listView.setAdapter(adapter);


                            if (json_match != null) {
                                Log.d(json_match.toString(), "Output");
                            }
                        }
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

        return sharedpreferencesCompetition.getString(COMPETITION_REF, null);

    }

    private View.OnClickListener newMatchAlerlDialog() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ListMatchesOfCompetition.this);
                builder.setTitle("Join competition");

                // Set up the layout

                Context context = ListMatchesOfCompetition.this;
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                // Add a TextView here for the "Title" label, as noted in the comments

                LinearLayout layout_horizontal_1 = new LinearLayout(context);
                layout_horizontal_1.setOrientation(LinearLayout.HORIZONTAL);

                final EditText homeTeamInput = new EditText(context);
                homeTeamInput.setInputType(InputType.TYPE_CLASS_TEXT);
                homeTeamInput.setHint("Home");
                layout_horizontal_1.addView(homeTeamInput);
                final EditText awayTeamInput = new EditText(context);
                awayTeamInput.setInputType(InputType.TYPE_CLASS_TEXT);
                awayTeamInput.setHint("Away");
                layout_horizontal_1.addView(awayTeamInput);


                LinearLayout layout_horizontal_2 = new LinearLayout(context);
                layout_horizontal_1.setOrientation(LinearLayout.HORIZONTAL);
                final EditText homeScoreInput = new EditText(context);
                homeScoreInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                homeScoreInput.setHint("Home Score");
                layout_horizontal_2.addView(homeScoreInput);
                final EditText awayScoreInput = new EditText(context);
                awayScoreInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                awayScoreInput.setHint("Away Score");
                layout_horizontal_2.addView(awayScoreInput);

                layout.addView(layout_horizontal_1);
                layout.addView(layout_horizontal_2);
                layout.setPadding(100, 10, 100 ,10);

                builder.setView(layout);



                // Set up the buttons
                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String homeTeam = homeTeamInput.getText().toString();
                        String awayTeam=  awayTeamInput.getText().toString();
                        Integer scoreHome = Integer.valueOf(homeScoreInput.getText().toString());
                        Integer scoreAway = Integer.valueOf(awayScoreInput.getText().toString());

                        MatchDto matchDto = aMatchDto()
                                .inCompetiton(getCompetition_ref())
                                .withHomeTeam(homeTeam)
                                .withScore(scoreHome)
                                .againstTeam(awayTeam)
                                .withScore(scoreAway)
                                .build();

                        Gson gson = new Gson();
                        final String jsonMatch = gson.toJson(matchDto);

                        saveMatch(jsonMatch);
                        finish();
                        startActivity(getIntent());
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


    private void saveMatch(String jsonMatch) {
        RequestQueue requestQueue = MySingletonRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();
        final String mRequestBody = jsonMatch;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL_BASE + URL_MATCH_POST,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("onResponse", "Create match : " + response.toString());
                        
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error", error.toString());
                        error.printStackTrace();
                    }
                }
        ){

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public byte[] getBody() {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    private void goToVoteActivity(){
        Intent intent = new Intent();
        intent.setClass(ListMatchesOfCompetition.this, VoteActivity.class);
        startActivity(intent);
        finish();

    }


}
