package lhc.com.carrdas;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
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

import lhc.com.adapter.CompetitionAdapter_ListCompetitions;
import lhc.com.dtos.CompetitionDto;
import lhc.com.dtos.RuleDto;
import lhc.com.otherRessources.ApplicationConstants.Parameter;
import lhc.com.otherRessources.BaseActivity;
import lhc.com.otherRessources.MySingletonRequestQueue;

import static lhc.com.otherRessources.ApplicationConstants.COMPETITION_REF;
import static lhc.com.otherRessources.ApplicationConstants.MyPREFERENCES_COMPETITION;
import static lhc.com.otherRessources.ApplicationConstants.MyPREFERENCES_CREDENTIALS;
import static lhc.com.otherRessources.ApplicationConstants.PASSWORD;
import static lhc.com.otherRessources.ApplicationConstants.RULES;
import static lhc.com.otherRessources.ApplicationConstants.URL_COMPETITION_ADD_USER;
import static lhc.com.otherRessources.ApplicationConstants.URL_COMPETITION_GET;
import static lhc.com.otherRessources.ApplicationConstants.USERNAME;
import static lhc.com.otherRessources.ApplicationConstants.WITH_COMMENTS;
import static lhc.com.otherRessources.ApplicationConstants.createURL;

public class ListCompetitions extends BaseActivity {

    SharedPreferences sharedpreferencesCompetition;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        setContentView(R.layout.activity_list_competitions);
        Button newCompetitionButton = findViewById(R.id.newCompetitionButton);
        newCompetitionButton.setOnClickListener(newCompetitionOnClickListener());

        Button joinCompetitionButton = findViewById(R.id.joinCompetitionButton);
        joinCompetitionButton.setOnClickListener(joinCompetitionOnClickListener());

        listView = findViewById(R.id.listViewCompetition);
        GetCompetitionsListLinkedToUsername();
        listView.setOnItemClickListener(competitionClickListener());

    }
    @NonNull
    private View.OnClickListener newCompetitionOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListCompetitions.this.goToAddCompetition();
            }
        };
    }

    private void goToAddCompetition() {
        Intent addCompetitionIntent = new Intent(this, AddCompetition.class);
        startActivity(addCompetitionIntent);
    }

    @NonNull
    private View.OnClickListener joinCompetitionOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ListCompetitions.this);
                builder.setTitle("Join competition");

                // Set up the layout

                Context context = ListCompetitions.this;
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                // Add a TextView here for the "Title" label, as noted in the comments

                // Add another TextView here for the "Description" label
                final EditText competitionName = new EditText(context);
                competitionName.setInputType(InputType.TYPE_CLASS_TEXT);
                competitionName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_group_work_red_24dp, 0, 0, 0);
                competitionName.setCompoundDrawablePadding(20);
                competitionName.setHint("NAME");
                layout.addView(competitionName);

                final EditText competitionPassword = new EditText(ListCompetitions.this);
                competitionPassword.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                competitionPassword.setHint("......");
                competitionPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_red_400_24dp, 0, 0, 0);
                competitionPassword.setCompoundDrawablePadding(20);
                layout.addView(competitionPassword);
                layout.setPadding(100, 10, 100 ,10);

                builder.setView(layout);
                // Set up the buttons
                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PostAddUSerToCompetition(competitionName.getText().toString(),competitionPassword.getText().toString());
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

    private void PostAddUSerToCompetition(String comp, String pass) {

        RequestQueue requestQueue = MySingletonRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();
        Parameter username = new Parameter(USERNAME, getUsername());
        Parameter competition_ref = new Parameter(COMPETITION_REF, comp);
        Parameter competition_password = new Parameter(PASSWORD, pass);

        final String url = createURL(URL_COMPETITION_ADD_USER, competition_ref, username, competition_password);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("onResponse", "Create Competition : " + response.toString());
                        finish();
                        overridePendingTransition( 0, 0);
                        startActivity(getIntent());
                        overridePendingTransition( 0, 0);
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
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);

    }


    @NonNull
    private AdapterView.OnItemClickListener competitionClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("HelloListView", "You clicked Item: " + id + " at position:" + position);
                // Then you start a new Activity via Intent

                CompetitionDto competitionDto = (CompetitionDto) listView.getAdapter().getItem(position);
                List<RuleDto> ruleDtos = competitionDto.getRuleDtos();

                sharedpreferencesCompetition = getSharedPreferences(MyPREFERENCES_COMPETITION, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferencesCompetition.edit();
                editor.clear();

                Gson gson = new Gson();
                editor.putString(COMPETITION_REF, competitionDto.getReference());
                editor.putString(RULES,gson.toJson(ruleDtos));
                editor.putBoolean(WITH_COMMENTS, competitionDto.isWithComments());
                editor.apply();

                Intent intent = new Intent();
                intent.setClass(ListCompetitions.this, ListMatchesOfCompetition.class);
                startActivity(intent);
            }
        };
    }

    private void GetCompetitionsListLinkedToUsername() {

        Parameter parameter = new Parameter(USERNAME, getUsername());
        final String url = createURL(URL_COMPETITION_GET, parameter);
        RequestQueue requestQueue = MySingletonRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                Request.Method.GET, url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<CompetitionDto> listCompetitions = new ArrayList<>();
                        JSONObject json_competition = null;
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                json_competition = response.getJSONObject(i);

                                ObjectMapper mapper = new ObjectMapper();
                                CompetitionDto competitionDto = mapper.readValue(json_competition.toString(), CompetitionDto.class);

                                listCompetitions.add(competitionDto);
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }

                            final CompetitionAdapter_ListCompetitions adapter = new CompetitionAdapter_ListCompetitions(ListCompetitions.this,listCompetitions);
                            listView.setAdapter(adapter);


                            if (json_competition != null) {
                                Log.d(json_competition.toString(), "Output");
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

    private String getUsername() {
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES_CREDENTIALS, Context.MODE_PRIVATE);
        return  sharedpreferences.getString(USERNAME, null);
    }





}

