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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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

import lhc.com.volley.JsonArrayRequestGet;
import lhc.com.adapter.CompetitionAdapter_ListCompetitions;
import lhc.com.dtos.CompetitionDto;
import lhc.com.dtos.RuleDto;
import lhc.com.otherRessources.ApplicationConstants.Parameter;
import lhc.com.otherRessources.BaseActivity;
import lhc.com.volley.MySingletonRequestQueue;

import static lhc.com.otherRessources.ApplicationConstants.COMPETITION_REF;
import static lhc.com.otherRessources.ApplicationConstants.IS_PLAYER;
import static lhc.com.otherRessources.ApplicationConstants.MyPREFERENCES_COMPETITION;
import static lhc.com.otherRessources.ApplicationConstants.MyPREFERENCES_CREDENTIALS;
import static lhc.com.otherRessources.ApplicationConstants.NAME_FLOP;
import static lhc.com.otherRessources.ApplicationConstants.NAME_TOP;
import static lhc.com.otherRessources.ApplicationConstants.NUMBER_VOTE_FLOP;
import static lhc.com.otherRessources.ApplicationConstants.NUMBER_VOTE_TOP;
import static lhc.com.otherRessources.ApplicationConstants.PASSWORD;
import static lhc.com.otherRessources.ApplicationConstants.RULES;
import static lhc.com.otherRessources.ApplicationConstants.URL_COMPETITION_ADD_USER;
import static lhc.com.otherRessources.ApplicationConstants.URL_COMPETITION_GET_LIST;
import static lhc.com.otherRessources.ApplicationConstants.USERNAME;
import static lhc.com.otherRessources.ApplicationConstants.WITH_COMMENTS_FLOP;
import static lhc.com.otherRessources.ApplicationConstants.WITH_COMMENTS_TOP;
import static lhc.com.otherRessources.ApplicationConstants.createURL;

public class ListCompetitions extends BaseActivity {

    SharedPreferences sharedpreferencesCompetition;
    private ListView listView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_competitions);
        Button newCompetitionButton = findViewById(R.id.newCompetitionButton);
        newCompetitionButton.setOnClickListener(newCompetitionOnClickListener());

        Button joinCompetitionButton = findViewById(R.id.joinCompetitionButton);
        joinCompetitionButton.setOnClickListener(joinCompetitionOnClickListener());

        listView = findViewById(R.id.listViewCompetition);
        GetCompetitionsListLinkedToUsername();
        listView.setOnItemClickListener(competitionClickListener());

        progressBar = findViewById(R.id.loadingAnimation);
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
        finish();
        startActivity(addCompetitionIntent);

    }

    @NonNull
    private View.OnClickListener joinCompetitionOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context = ListCompetitions.this;

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = ListCompetitions.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_custom, null);
                LinearLayout dialog_layout = dialogView.findViewById(R.id.dialog_layout);
                ((TextView) dialogView.findViewById(R.id.title_dialog)).setText("Join competition");


                // Add another TextView here for the "Description" label
                final EditText competitionName = new EditText(context);
                settingsForName(competitionName);
                dialog_layout.addView(competitionName);
                final EditText competitionPassword = new EditText(context);
                settingsForPassword(competitionPassword);
                dialog_layout.addView(competitionPassword);
                competitionPassword.setVisibility(View.GONE);
                final CheckBox isPlayerBox = new CheckBox(context);
                isPlayerBox.setText(R.string.isPlayer);
                dialog_layout.addView(isPlayerBox);
                dialog_layout.setPadding(100, 10, 100, 10);


                isPlayerBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            competitionPassword.setVisibility(View.VISIBLE);
                        } else {
                            competitionPassword.setVisibility(View.GONE);
                        }
                    }
                });


                builder.setView(dialogView);
                // Set up the buttons
                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PostAddUSerToCompetition(competitionName.getText().toString(), competitionPassword.getText().toString(), isPlayerBox.isChecked());
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

    private void settingsForName(EditText competitionName) {
        competitionName.setInputType(InputType.TYPE_CLASS_TEXT);
        competitionName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_group_work_red_24dp, 0, 0, 0);
        competitionName.setCompoundDrawablePadding(20);
        competitionName.setHint("NAME");
    }

    private void settingsForPassword(EditText competitionPassword) {
        competitionPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        competitionPassword.setHint("......");
        competitionPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_red_400_24dp, 0, 0, 0);
        competitionPassword.setCompoundDrawablePadding(20);
    }

    private void PostAddUSerToCompetition(String comp, String pass, boolean isPlayer) {

        final Context mContext = ListCompetitions.this;
        RequestQueue requestQueue = MySingletonRequestQueue.getInstance(mContext.getApplicationContext()).getRequestQueue();

        final String url = createURL_AddUserToCompetition(comp, pass, isPlayer);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("onResponse", "Create Competition : " + response.toString());
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error", error.toString());
                        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                        alertDialog.setTitle("Alert");
                        alertDialog.setMessage(error.toString());
                        alertDialog.show();
                        error.printStackTrace();
                    }
                }
        ) {

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

    private String createURL_AddUserToCompetition(String comp, String pass, boolean isPlayer) {
        Parameter username = new Parameter(USERNAME, getUsername());
        Parameter competition_ref = new Parameter(COMPETITION_REF, comp);
        Parameter competition_password = new Parameter(PASSWORD, pass);
        Parameter isPlayerParam = new Parameter(IS_PLAYER, Boolean.toString(isPlayer));
        return createURL(URL_COMPETITION_ADD_USER, competition_ref, username, competition_password, isPlayerParam);
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
                editor.putString(COMPETITION_REF, competitionDto.getSystemDataDto().getReference());
                editor.putString(RULES, gson.toJson(ruleDtos));
                editor.putInt(NUMBER_VOTE_TOP, getRules(NUMBER_VOTE_TOP, competitionDto.getRuleDtos()));
                editor.putInt(NUMBER_VOTE_FLOP, getRules(NUMBER_VOTE_FLOP, competitionDto.getRuleDtos()));
                editor.putBoolean(WITH_COMMENTS_TOP, competitionDto.getTopFlopDetails().isWithCommentTop());
                editor.putBoolean(WITH_COMMENTS_FLOP, competitionDto.getTopFlopDetails().isWithCommentFlop());
                editor.putString(NAME_TOP, competitionDto.getTopFlopDetails().getTopName());
                editor.putString(NAME_FLOP, competitionDto.getTopFlopDetails().getFlopName());
                editor.apply();

                Intent intent = new Intent();
                intent.setClass(ListCompetitions.this, ListMatches.class);
                startActivity(intent);
            }
        };
    }

    private int getRules(String constant, List<RuleDto> rules) {
        for (RuleDto ruleDto : rules) {
            if (constant.equals(ruleDto.getLabel())) {
                return ruleDto.getPoints();
            }
        }
        return 0;
    }

    private void GetCompetitionsListLinkedToUsername() {

        Parameter parameter = new Parameter(USERNAME, getUsername());
        final String url = createURL(URL_COMPETITION_GET_LIST, parameter);
        Context mContext = this.getApplicationContext();
        RequestQueue requestQueue = MySingletonRequestQueue.getInstance(mContext).getRequestQueue();

        JsonArrayRequestGet jsonObjectRequest = JsonArrayRequestGet.jsonArrayRequestGet(
                url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        progressBar.setVisibility(View.GONE);
                        List<CompetitionDto> listCompetitions = new ArrayList<>();
                        JSONObject json_competition = null;
                        long startTime = System.currentTimeMillis();
                        System.out.println("Execution of method mapper.readValue();");
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                json_competition = response.getJSONObject(i);

                                ObjectMapper mapper = new ObjectMapper();
                                CompetitionDto competitionDto = mapper.readValue(json_competition.toString(), CompetitionDto.class);

                                listCompetitions.add(competitionDto);
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }


                            if (json_competition != null) {
                                Log.d(json_competition.toString(), "Output");
                            }
                        }

                        long endTime = System.currentTimeMillis();
                        System.out.println("Total elapsed time in execution of method mapper.readValue() is :" + (endTime - startTime));
                        final CompetitionAdapter_ListCompetitions adapter = new CompetitionAdapter_ListCompetitions(ListCompetitions.this, listCompetitions);
                        listView.setAdapter(adapter);
                    }
                },
                mContext);

        requestQueue.add(jsonObjectRequest);
    }

    private String getUsername() {
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES_CREDENTIALS, Context.MODE_PRIVATE);
        return sharedpreferences.getString(USERNAME, null);
    }


}

