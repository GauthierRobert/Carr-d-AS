package lhc.com.carrdas;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lhc.com.dtos.CompetitionDto;
import lhc.com.otherRessources.ApplicationConstants;
import lhc.com.otherRessources.ApplicationConstants.Parameter;
import lhc.com.otherRessources.MySingletonRequestQueue;

import static lhc.com.otherRessources.ApplicationConstants.URL_BASE;
import static lhc.com.otherRessources.ApplicationConstants.URL_COMPETITION;
import static lhc.com.otherRessources.ApplicationConstants.USERNAME;
import static lhc.com.otherRessources.ApplicationConstants.createURL;

public class ListCompetitions extends AppCompatActivity {

    private ListView listView;
    private Button newCompetitionButton;
    private Button joinCompetitionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_competitions);

        newCompetitionButton = findViewById(R.id.newCompetitionButton);
        newCompetitionButton.setOnClickListener(newCompetitionOnClickListener());

        joinCompetitionButton = findViewById(R.id.joinCompetitionButton);
        joinCompetitionButton.setOnClickListener(joinCompetitionOnClickListener());

        listView = findViewById(R.id.listViewCompetition);
        PostGetCompetitionsListLinkedToUsername();
        listView.setOnItemClickListener(competitionClickListener());

    }
    @NonNull
    private View.OnClickListener newCompetitionOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAddCompetition();
            }
        };
    }

    private void goToAddCompetition() {
        Intent addCompetitionIntent = new Intent(this, AddCompetition_1.class);
        startActivity(addCompetitionIntent);
        finish();
    }

    @NonNull
    private View.OnClickListener joinCompetitionOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToJoinCompetition();
            }
        };
    }

    private void goToJoinCompetition() {
        Intent addCompetitionIntent = new Intent(this, JoinCompetition.class);
        startActivity(addCompetitionIntent);
        finish();
    }

    @NonNull
    private AdapterView.OnItemClickListener competitionClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("HelloListView", "You clicked Item: " + id + " at position:" + position);
                // Then you start a new Activity via Intent
                Intent intent = new Intent();
                intent.setClass(ListCompetitions.this, ListMatchesOfCompetition.class);
                intent.putExtra("position", position);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        };
    }

    private void PostGetCompetitionsListLinkedToUsername() {

        Parameter parameter = new Parameter(USERNAME, getUsername());
        final String url = createURL(URL_COMPETITION, parameter);
        RequestQueue requestQueue = MySingletonRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                Request.Method.GET, url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<Map<String,String>> ListCompetitions = new ArrayList<>();
                        for(int i=0; i < response.length() ; i++) {
                            JSONObject json_competition   = null;
                            try {
                                json_competition = response.getJSONObject(i);
                                Map<String, String> competition = getMapCompetition(json_competition);
                                ListCompetitions.add(competition);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            ListAdapter adapter = new SimpleAdapter(
                                    ListCompetitions.this, ListCompetitions,
                                    R.layout.competition_cell, new String[]{"name", "season", "division"}, new int[]{R.id.name_competition_cell,
                                    R.id.season_competition_cell, R.id.division_competition_cell});
                            listView.setAdapter(adapter);
                            if (json_competition != null) {
                                Log.d(json_competition.toString(),"Output");
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error",error.toString());
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



    @NonNull
    private Map<String, String> getMapCompetition(JSONObject json_competition) throws JSONException {
        Map<String,String> competition = new HashMap<>();
        competition.put("name",json_competition.getString("name"));
        //String season = json_competition.getInt("season") + " - " + json_competition.getInt("season")+1;
        competition.put("season","2008");
        //String division = json_competition.getString("division");
        competition.put("division", "D1" );
        return competition;
    }

    private String getUsername() {
        Intent intent = this.getIntent();
        return intent.getStringExtra(USERNAME);
    }



}

