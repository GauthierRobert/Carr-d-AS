package lhc.com.carrdas;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import lhc.com.otherRessources.BaseActivity;
import lhc.com.otherRessources.MySingletonRequestQueue;

import static lhc.com.dtos.builder.CompetitionDtoBuilder.aCompetitionDto;
import static lhc.com.otherRessources.ApplicationConstants.MyPREFERENCES_CREDENTIALS;
import static lhc.com.otherRessources.ApplicationConstants.URL_BASE;
import static lhc.com.otherRessources.ApplicationConstants.URL_COMPETITION_POST;
import static lhc.com.otherRessources.ApplicationConstants.USERNAME;

public class AddCompetition extends BaseActivity {

    Button submitButton;
    Button topRulesButton;
    Button flopRulesButton;

    List<String> pointTopList;
    List<String> pointFlopList;
    SharedPreferences sharedpreferences;

    Integer[] flopVoteInt;
    Integer[] topVoteInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_competition);


        submitButton = findViewById(R.id.submit_button_competition);
        submitButton.setOnClickListener(submit());

        topRulesButton = findViewById(R.id.button_edit_top_rules);
        topRulesButton.setOnClickListener(editTopRules());

        flopRulesButton = findViewById(R.id.button_edit_flop_rules);
        flopRulesButton.setOnClickListener(editFlopRules());

        pointTopList = new ArrayList<>();
        pointFlopList = new ArrayList<>();

    }


    private View.OnClickListener editTopRules() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = AddCompetition.this;
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Create your own rules");

                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                final ListView topVote = new ListView(context);
                final TextInputLayout numberTopTIL = findViewById(R.id.numberOfTopVoteAllowed_layout_competition);
                int numberTop = Integer.parseInt(numberTopTIL.getEditText().getText().toString());

                List<String> pointTopList = createList(numberTop, topVoteInt);

                final VoteAdapter_newCompetition adapter_top = new VoteAdapter_newCompetition(AddCompetition.this,
                        pointTopList);
                topVote.setAdapter(adapter_top);
                layout.addView(topVote);

                builder.setView(layout);
                // Set up the buttons
                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        topVoteInt = getIntArray(topVote);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                dialog.getWindow().clearFlags( WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            }
        };
    }

    private View.OnClickListener editFlopRules() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = AddCompetition.this;
                AlertDialog.Builder builder = new AlertDialog.Builder(AddCompetition.this);
                builder.setTitle("Create your own rules");

                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                final ListView flopVote = new ListView(context);
                final TextInputLayout numberFlopTIL = findViewById(R.id.numberOfFlopVoteAllowed_layout_competition);
                int numberFlop = Integer.parseInt(numberFlopTIL.getEditText().getText().toString());

                List<String> pointFlopList = createList(numberFlop, flopVoteInt);

                final VoteAdapter_newCompetition adapter_flop = new VoteAdapter_newCompetition(AddCompetition.this,
                        pointFlopList);
                flopVote.setAdapter(adapter_flop);
                layout.addView(flopVote);

                builder.setView(layout);
                // Set up the buttons
                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        flopVoteInt = getIntArray(flopVote);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                dialog.getWindow().clearFlags( WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            }
        };
    }

    private List<String> createList(int max, Integer[] voteInt) {

        List<String> stringList = new ArrayList<>();
        for(int i =0; i<max; i++){
            if(voteInt.length < i){
                stringList.add(String.valueOf(voteInt[i]));
            } else {
                stringList.add("#" + i);
            }
        }
        return stringList;
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
                .withTopRuleDtos(topVoteInt)
                .withFlopRuleDtos(flopVoteInt)
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

    public Integer[] getIntArray(ListView listView) {
        Integer[] listInt = new Integer[listView.getAdapter().getCount()];

        for (int i = 0; i < listInt.length; i++) {
            View view =listView.getChildAt(i);
            EditText point_vote_cell = view.findViewById(R.id.point_vote_cell);
            String value = point_vote_cell.getText().toString();
            listInt[i] = Integer.valueOf(value);
        }
        return listInt;
    }


}
