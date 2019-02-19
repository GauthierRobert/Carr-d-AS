package lhc.com.carrdas;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lhc.com.adapter.VoteAdapter_newCompetition;
import lhc.com.dtos.CompetitionDto;
import lhc.com.otherRessources.BaseActivity;
import lhc.com.volley.JsonObjectRequestPost;
import lhc.com.volley.MySingletonRequestQueue;

import static lhc.com.dtos.builder.CompetitionDtoBuilder.aCompetitionDtoCreatedBy;
import static lhc.com.otherRessources.ApplicationConstants.COMPETITION_REF;
import static lhc.com.otherRessources.ApplicationConstants.MyPREFERENCES_CREDENTIALS;
import static lhc.com.otherRessources.ApplicationConstants.URL_BASE;
import static lhc.com.otherRessources.ApplicationConstants.URL_COMPETITION_POST;
import static lhc.com.otherRessources.ApplicationConstants.USERNAME;

public class AddCompetition extends BaseActivity {

    Button nextButton;
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


        nextButton = findViewById(R.id.next_button_competition);
        nextButton.setOnClickListener(next());

        topRulesButton = findViewById(R.id.button_edit_top_rules);
        topRulesButton.setEnabled(false);
        topRulesButton.setOnClickListener(editTopRules());

        flopRulesButton = findViewById(R.id.button_edit_flop_rules);
        flopRulesButton.setEnabled(false);
        flopRulesButton.setOnClickListener(editFlopRules());

        pointTopList = new ArrayList<>();
        pointFlopList = new ArrayList<>();


        final EditText numberTopEditText = findViewById(R.id.numberOfTopVoteAllowed_competition);
        final EditText numberFlopEditText = findViewById(R.id.numberOfFlopVoteAllowed_competition);
        Objects.requireNonNull(numberTopEditText).addTextChangedListener(getWatcherTop());
        Objects.requireNonNull(numberFlopEditText).addTextChangedListener(getWatcherFlop());

    }

    private TextWatcher getWatcherTop() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    int numberTop_value = Integer.parseInt(String.valueOf(s));
                    if (numberTop_value != 0) {
                        topRulesButton.setEnabled(true);
                    } else {
                        topRulesButton.setEnabled(false);
                    }
                } else {
                    topRulesButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

    }

    private TextWatcher getWatcherFlop() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    int numberFlop_value = Integer.parseInt(String.valueOf(s));
                    if (numberFlop_value != 0) {
                        flopRulesButton.setEnabled(true);
                    } else {
                        flopRulesButton.setEnabled(false);
                    }
                } else {
                    flopRulesButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }


    private View.OnClickListener editTopRules() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = AddCompetition.this;

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater =  AddCompetition.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_custom, null);
                LinearLayout dialog_layout = dialogView.findViewById(R.id.dialog_layout);
                ((TextView) dialogView.findViewById(R.id.title_dialog)).setText("Create your own rules");


                TextView explication = new TextView(context);
                explication.setText("Enter the number of points you choose to allocate to the player at the choosen position : ");
                dialog_layout.addView(explication);

                final ListView topVote = new ListView(context);
                final EditText numberTopET = findViewById(R.id.numberOfTopVoteAllowed_competition);
                int numberTop = Integer.parseInt(numberTopET.getText().toString());

                List<String> pointTopList = createList(numberTop, topVoteInt);

                final VoteAdapter_newCompetition adapter_top = new VoteAdapter_newCompetition(AddCompetition.this,
                        pointTopList);
                topVote.setAdapter(adapter_top);
                dialog_layout.addView(topVote);
                dialog_layout.setPadding(50, 30, 50, 0);
                builder.setView(dialogView);
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
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            }
        };
    }

    private View.OnClickListener editFlopRules() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = AddCompetition.this;
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater =  AddCompetition.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_custom, null);
                LinearLayout dialog_layout = dialogView.findViewById(R.id.dialog_layout);
                ((TextView) dialogView.findViewById(R.id.title_dialog)).setText("Create your own rules");

                TextView explication = new TextView(context);
                explication.setText("Enter the number of points you choose to allocate to the player at the choosen position : ");
                dialog_layout.addView(explication);

                final ListView flopVote = new ListView(context);
                final EditText numberFlopET = findViewById(R.id.numberOfFlopVoteAllowed_competition);
                int numberFlop = Integer.parseInt(numberFlopET.getText().toString());

                List<String> pointFlopList = createList(numberFlop, flopVoteInt);

                final VoteAdapter_newCompetition adapter_flop = new VoteAdapter_newCompetition(AddCompetition.this,
                        pointFlopList);
                flopVote.setAdapter(adapter_flop);
                dialog_layout.addView(flopVote);
                dialog_layout.setPadding(50, 30, 50, 0);
                builder.setView(dialogView);
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
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            }
        };
    }

    private List<String> createList(int max, Integer[] voteInt) {

        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < max; i++) {
            int j = i + 1;
            if (voteInt == null) {
                stringList.add("#" + j);
            } else {
                if (voteInt.length < i) {
                    stringList.add(String.valueOf(voteInt[i]));
                } else {
                    stringList.add("#" + j);
                }
            }
        }
        return stringList;
    }

    private View.OnClickListener next() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCompetition_JsonPostRequest();
            }
        };
    }


    private void AddCompetition_JsonPostRequest() {
        Context mContext = AddCompetition.this;
        RequestQueue requestQueue = MySingletonRequestQueue.getInstance(mContext.getApplicationContext()).getRequestQueue();
        final String mRequestBody = getJsonRequest();

        JsonObjectRequestPost jsonObjectRequest = JsonObjectRequestPost.jsonObjectRequestPost(
                URL_BASE + URL_COMPETITION_POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("onResponse", "Create Competition : " + response.toString());
                        ObjectMapper mapper = new ObjectMapper();
                        CompetitionDto competitionDto = null;
                        try {
                            competitionDto = mapper.readValue(response.toString(), CompetitionDto.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (competitionDto != null) {
                            goToImageCompetition(competitionDto.getSystemDataDto().getReference());
                        }
                    }
                },
                mRequestBody,
                mContext
        );
        requestQueue.add(jsonObjectRequest);
    }

    private String getJsonRequest() {
        sharedpreferences = getSharedPreferences(MyPREFERENCES_CREDENTIALS, Context.MODE_PRIVATE);

        String name = ((EditText) findViewById(R.id.name_competition)).getText().toString();
        String division = ((EditText) findViewById(R.id.division_competition)).getText().toString();
        String season = ((EditText) findViewById(R.id.season_competition)).getText().toString();
        String username = sharedpreferences.getString(USERNAME, null);
        String password = ((EditText) findViewById(R.id.password_competition)).getText().toString();
        String confirmedPassword = ((EditText) findViewById(R.id.confirmedPassword_competition)).getText().toString();
        boolean isCheckedTop = ((CheckBox) findViewById(R.id.withCommentsCheckBoxTop)).isChecked();
        boolean isCheckedFlop = ((CheckBox) findViewById(R.id.withCommentsCheckBoxFlop)).isChecked();
        String nameTop = ((EditText) findViewById(R.id.name_top_competition)).getText().toString();
        String nameFlop = ((EditText) findViewById(R.id.name_flop_competition)).getText().toString();
        int numberTopVotes = Integer.parseInt(((EditText) findViewById(R.id.numberOfTopVoteAllowed_competition)).getText().toString());
        int numberFlopVotes = Integer.parseInt(((EditText) findViewById(R.id.numberOfFlopVoteAllowed_competition)).getText().toString());

        CompetitionDto competitionDto = aCompetitionDtoCreatedBy(username)
                .withPassword(password)
                .withConfirmedPassword(confirmedPassword)
                .finallySport(null)
                .withName(name)
                .withDivision(division)
                .withSeason(season)
                .finallyStatisticsName("Goals", null, null, null, null)
                .withComments(isCheckedTop, isCheckedFlop)
                .withTopFlopName(nameTop, nameFlop)
                .finallyRules(numberTopVotes, numberFlopVotes)
                .withTopRuleDtos(topVoteInt)
                .withFlopRuleDtos(flopVoteInt)
                .build();

        Gson gson = new Gson();
        return gson.toJson(competitionDto);

    }

    private void goToImageCompetition(String competition_ref) {
        Intent intent = new Intent();
        intent.setClass(AddCompetition.this, ImageCompetitionActivity.class);
        intent.putExtra(COMPETITION_REF, competition_ref);
        startActivity(intent);
        finish();
    }

    public Integer[] getIntArray(ListView listView) {
        if (listView.getAdapter() != null) {
            if (listView.getAdapter().getCount() > 0) {
                Integer[] listInt = new Integer[listView.getAdapter().getCount()];

                for (int i = 0; i < listInt.length; i++) {
                    View view = listView.getChildAt(i);
                    EditText point_vote_cell = view.findViewById(R.id.point_vote_cell);
                    String value = point_vote_cell.getText().toString();
                    listInt[i] = Integer.valueOf(value);
                }
                return listInt;
            }
        }
        return null;
    }
}
