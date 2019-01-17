package lhc.com.carrdas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lhc.com.adapter.VoteAdapter_newCompetition;
import lhc.com.dtos.CompetitionDto;
import lhc.com.otherRessources.MySingletonRequestQueue;

import static lhc.com.dtos.builder.CompetitionDtoBuilder.aCompetitionDto;
import static lhc.com.otherRessources.ApplicationConstants.MyPREFERENCES_CREDENTIALS;
import static lhc.com.otherRessources.ApplicationConstants.URL_BASE;
import static lhc.com.otherRessources.ApplicationConstants.URL_COMPETITION_POST;
import static lhc.com.otherRessources.ApplicationConstants.USERNAME;

public class AddCompetition extends AppCompatActivity {

    Button submitButton;

    List<String> pointTopList;
    List<String> pointFlopList;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_competition);


        submitButton = findViewById(R.id.submit_button_competition);
        submitButton.setOnClickListener(submit());


        pointTopList = new ArrayList<>();
        final ListView topVote = findViewById(R.id.list_item_top_vote_competition);
        final TextInputLayout numberTop = findViewById(R.id.numberOfTopVoteAllowed_layout_competition);
        final VoteAdapter_newCompetition adapter_top = new VoteAdapter_newCompetition(AddCompetition.this,
                pointTopList);
        topVote.setAdapter(adapter_top);

        pointFlopList = new ArrayList<>();
        final ListView flopVote = findViewById(R.id.list_item_flop_vote_competition);
        final TextInputLayout numberFlop = findViewById(R.id.numberOfFlopVoteAllowed_layout_competition);
        final VoteAdapter_newCompetition adapter_flop = new VoteAdapter_newCompetition(AddCompetition.this,
                pointFlopList);
        flopVote.setAdapter(adapter_flop);

        Objects.requireNonNull(numberTop.getEditText()).addTextChangedListener(getWatcherTop(adapter_top));
        Objects.requireNonNull(numberFlop.getEditText()).addTextChangedListener(getWatcherFlop(adapter_flop));

    }





    @NonNull
    private TextWatcher getWatcherTop(final ArrayAdapter adapter_top) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<String> list = new ArrayList<>();
                if(s.length() != 0) {
                    int numberTop_value = Integer.parseInt(String.valueOf(s));
                    list = getListVote(numberTop_value);
                }
                pointTopList.clear();
                pointTopList.addAll(list);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() != 0) {
                    adapter_top.notifyDataSetChanged();
                }
            }
        };
    }

    @NonNull
    private TextWatcher getWatcherFlop(final ArrayAdapter adapter) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<String> list = new ArrayList<>();
                if(s.length() != 0) {
                    int numberFlop_value = Integer.parseInt(String.valueOf(s));
                    list = getListVote(numberFlop_value);
                }
                pointFlopList.clear();
                pointFlopList.addAll(list);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() != 0) {
                    adapter.notifyDataSetChanged();
                }
            }
        };
    }

    private List<String> getListVote(int max){
        List<String> list = new ArrayList<>();
        int i = 1;
        while (i <= max) {
            list.add("#" + i);
            i++;
        }
        return list;
    }

    private View.OnClickListener submit() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCompetition_JsonPostRequest();
            }
        };
    }



    private void AddCompetition_JsonPostRequest() {
        RequestQueue requestQueue = MySingletonRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();
        final String mRequestBody = getJsonRequest();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL_BASE + URL_COMPETITION_POST,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("onResponse", "Create Competition : " + response.toString());
                        goToListCompetition();
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

    private String getJsonRequest(){
        sharedpreferences = getSharedPreferences(MyPREFERENCES_CREDENTIALS, Context.MODE_PRIVATE);

        String name = ((EditText) findViewById(R.id.name_competition)).getText().toString();
        String division =((EditText)  findViewById(R.id.division_competition)).getText().toString();
        String season = ((EditText)  findViewById(R.id.season_competition)).getText().toString();
        String username = sharedpreferences.getString(USERNAME, null);
        String password =((EditText)  findViewById(R.id.password_competition)).getText().toString();
        String confirmedPassword =((EditText)  findViewById(R.id.confirmedPassword_competition)).getText().toString();
        int numberTopVotes = Integer.parseInt(((EditText)  findViewById(R.id.numberOfTopVoteAllowed_competition)).getText().toString());
        int numberFlopVotes = Integer.parseInt(((EditText)  findViewById(R.id.numberOfFlopVoteAllowed_competition)).getText().toString());

        CompetitionDto competitionDto = aCompetitionDto()
                .withName(name)
                .withDivision(division)
                .withSeason(season)
                .withCreatorUsername(username)
                .withPassword(password)
                .withConfirmedPassword(confirmedPassword)
                .withRuleDtos(numberTopVotes,numberFlopVotes)
                .build();

        Gson gson = new Gson();
        return gson.toJson(competitionDto);

    }

    private void goToListCompetition() {
        Intent intent = new Intent();
        intent.setClass(AddCompetition.this, ListCompetitions.class);
        startActivity(intent);
        finish();

    }



}
