package lhc.com.carrdas.voteFragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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

import lhc.com.adapter.VoteAdapter_UserIsVoting;
import lhc.com.carrdas.ListCompetitions;
import lhc.com.carrdas.R;
import lhc.com.carrdas.VoteActivity;
import lhc.com.dtos.BallotDto;
import lhc.com.dtos.RuleDto;
import lhc.com.dtos.VoteDto;
import lhc.com.otherRessources.ApplicationConstants;
import lhc.com.otherRessources.ApplicationConstants.Parameter;
import lhc.com.otherRessources.MySingletonRequestQueue;
import lombok.Data;

import static android.content.Context.MODE_PRIVATE;
import static lhc.com.dtos.builder.BallotDtoBuilder.aBallotDtoWithRules;
import static lhc.com.otherRessources.ApplicationConstants.COMPETITION_REF;
import static lhc.com.otherRessources.ApplicationConstants.FLOP;
import static lhc.com.otherRessources.ApplicationConstants.GOD;
import static lhc.com.otherRessources.ApplicationConstants.JSON_LIST_VOTES;
import static lhc.com.otherRessources.ApplicationConstants.MATCH_REF;
import static lhc.com.otherRessources.ApplicationConstants.MyPREFERENCES_COMPETITION;
import static lhc.com.otherRessources.ApplicationConstants.MyPREFERENCES_CREDENTIALS;
import static lhc.com.otherRessources.ApplicationConstants.NUMBER_VOTE_FLOP;
import static lhc.com.otherRessources.ApplicationConstants.NUMBER_VOTE_TOP;
import static lhc.com.otherRessources.ApplicationConstants.RULES;
import static lhc.com.otherRessources.ApplicationConstants.TOP;
import static lhc.com.otherRessources.ApplicationConstants.URL_BALLOT_POST;
import static lhc.com.otherRessources.ApplicationConstants.URL_USER_GET;
import static lhc.com.otherRessources.ApplicationConstants.USERNAME;
import static lhc.com.otherRessources.ApplicationConstants.WITH_COMMENTS;
import static lhc.com.otherRessources.ApplicationConstants.createURL;


public class UserIsVoting extends Fragment {

    SharedPreferences sharedPreferencesCompetition;
    SharedPreferences sharedPreferencesCredentials;
    List<String> topVotes;
    List<String> flopVotes;
    List<RuleDto> rules;

    int numberTop;
    int numberFlop;

    Button submit;
    Button voteTopButton;
    Button voteFlopButton;

    TextView overviewTop;
    TextView overviewFlop;

    String comment;

    String[] users;
    String[] topVoteString;
    String[] flopVoteString;

    String jsonArrayOfListVotes;


    private OnFragmentInteractionListener mListener;


    public UserIsVoting() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        users = new String[1];
        users[0] = GOD;
        View view = inflater.inflate(R.layout.fragment_user_is_voting, container, false);
        sharedPreferencesCredentials = this.getActivity().getSharedPreferences(MyPREFERENCES_CREDENTIALS, MODE_PRIVATE);
        sharedPreferencesCompetition = this.getActivity().getSharedPreferences(MyPREFERENCES_COMPETITION, MODE_PRIVATE);

        if (getArguments() != null) {
            jsonArrayOfListVotes = getArguments().getString(JSON_LIST_VOTES);
        }

        GetUsernamesLinkedToCompetition();

        overviewTop = view.findViewById(R.id.overviewOfTopVotes);
        overviewFlop = view.findViewById(R.id.overviewOfFlopVotes);

        rules = getRulesOfCompetition();
        numberTop = getRules(NUMBER_VOTE_TOP);
        numberFlop = getRules(NUMBER_VOTE_FLOP);
        boolean withComment = sharedPreferencesCompetition.getBoolean(WITH_COMMENTS, false);

        if(numberTop == 0) {
            LinearLayout layout = view.findViewById(R.id.layout_top_vote_user_is_voting);
            layout.setVisibility(View.GONE);
        }
        if(numberFlop == 0) {
            LinearLayout layout = view.findViewById(R.id.layout_flop_vote_user_is_voting);
            layout.setVisibility(View.GONE);
        }
        if(withComment) {
            LinearLayout layout = view.findViewById(R.id.layout_comments_user_is_voting);
            layout.setVisibility(View.GONE);
        }

        topVotes = createEmptyList(numberTop);
        flopVotes = createEmptyList(numberFlop);

        voteTopButton= view.findViewById(R.id.vote_top_button_user_is_voting);
        voteTopButton.setOnClickListener(editTopVotes());
        voteFlopButton = view.findViewById(R.id.vote_flop_button_user_is_voting);
        voteFlopButton.setOnClickListener(editFlopVotes());

        submit = view.findViewById(R.id.submit_ballot);
        submit.setOnClickListener(submit());

        String comment = ((EditText) view.findViewById(R.id.comments_user_is_voting)).getText().toString();

        if(jsonArrayOfListVotes !=null) {
            try {
                JSONVote jsonVote = jsonVote();
                if (jsonVote.isHasVoted()) {
                    voteTopButton.setVisibility(View.GONE);
                    voteFlopButton.setVisibility(View.GONE);
                    submit.setVisibility(View.GONE);
                }
                ObjectMapper mapper = new ObjectMapper();
                BallotDto ballotDto = mapper.readValue(jsonVote.getBallot().toString(), BallotDto.class);
                overviewTop.setText(generateOverView(ballotDto, TOP));
                overviewFlop.setText(generateOverView(ballotDto, FLOP));

            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }

        return view;
    }

    private JSONVote jsonVote() throws JSONException {
        JSONArray arrayBallot = new JSONArray(jsonArrayOfListVotes);
        for (int i =0; i<arrayBallot.length(); i++){
            JSONObject ballot = arrayBallot.getJSONObject(i);
            JSONObject user = ballot.getJSONObject("user");
            if (getUsername().equals(user.get(USERNAME).toString())){
                return new JSONVote(user.get(USERNAME).toString(),ballot,true);
            }
        }
        return new JSONVote(GOD,null,false);
    }

    private String generateOverView(BallotDto ballotDto, String constantVote) {
        String[] strings = null;
        if (ballotDto !=null) {
            if (ballotDto.getVoteDtos() != null) {
                strings = new String[ballotDto.getVoteDtos().size()];
                for (VoteDto voteDto : ballotDto.getVoteDtos()) {
                    int indication = voteDto.getIndication();
                    if(TOP.equals(constantVote)) {
                        if (indication > 0) {
                            strings[indication] = voteDto.getName();
                        }
                    } else if (FLOP.equals(constantVote)){
                        if (indication < 0) {
                            strings[-indication] = voteDto.getName();
                        }
                    }
                }
            }
        }
        return createOverviewWithArray(strings);
    }




    private View.OnClickListener editTopVotes() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getActivity();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Vote for TOP players");

                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                final ListView topVote = new ListView(context);

                final VoteAdapter_UserIsVoting adapter_top = new VoteAdapter_UserIsVoting(context,
                        topVotes, users);
                topVote.setAdapter(adapter_top);
                layout.addView(topVote);
                layout.setPadding(50, 30, 50, 0);
                builder.setView(layout);
                // Set up the buttons
                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        topVoteString = getStringArray(topVote);
                        overviewTop.setText(createOverviewWithArray(topVoteString));
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

    private View.OnClickListener editFlopVotes() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getActivity();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Vote for FLOP players");


                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                final ListView flopVote = new ListView(context);

                final VoteAdapter_UserIsVoting adapter_flop = new VoteAdapter_UserIsVoting(context,
                        flopVotes, users);
                flopVote.setAdapter(adapter_flop);
                layout.addView(flopVote);
                layout.setPadding(50, 30, 50, 0);
                builder.setView(layout);
                // Set up the buttons
                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        flopVoteString = getStringArray(flopVote);
                        overviewFlop.setText(createOverviewWithArray(flopVoteString));
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

    private String createOverviewWithArray(String[] flopVoteString) {
        if (flopVoteString !=null) {
            if (flopVoteString.length != 0) {
                String overview = "#1 : " + flopVoteString[0];
                String nl = System.getProperty("line.separator");

                for (int i = 2; i <= flopVoteString.length; i++) {
                    overview = overview + nl + "#" + i + " : " + flopVoteString[i - 1];
                }
                return overview;
            }
        }
        return null;
    }

    private View.OnClickListener submit() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = getActivity();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirmation");
                builder.setMessage("Are you happy with this vote ?");

                EditText editText = new EditText(context);
                builder.setView(editText);
                // Set up the buttons
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SaveBallot_JsonPostRequest();

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();




            }
        };
    }

    private void SaveBallot_JsonPostRequest() {
        Parameter parameter = new Parameter(USERNAME, getUsername());
        final String url = createURL(URL_BALLOT_POST, parameter);

        RequestQueue requestQueue = MySingletonRequestQueue.getInstance(this.getActivity().getApplicationContext()).getRequestQueue();
        final String mRequestBody = getJsonRequest();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url ,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("onResponse", "Create Competition : " + response.toString());
                        refreshPage();
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

    private void refreshPage() {
        getActivity().finish();
        getActivity().overridePendingTransition( 0, 0);
        startActivity(getActivity().getIntent());
        getActivity().overridePendingTransition( 0, 0);
    }

    private List<String> createEmptyList(int numberTop) {
        List<String> emptyList = new ArrayList<>();

        for (int i = 0; i<numberTop;i++){
            emptyList.add(null);
        }
        return emptyList;

    }

    private int getRules (String constant){
        for (RuleDto ruleDto: rules) {
            if (constant.equals(ruleDto.getLabel())){
                return ruleDto.getPoints();
            }
        }
       return 0;
    }

    private String getJsonRequest(){
        BallotDto ballotDto = aBallotDtoWithRules(rules)
                .withCompetition_ref(getCompetition_ref())
                .withMatch_ref(getMatch_ref())
                .withComment(comment)
                .withVotesDtos()
                .addTopVote(topVoteString)
                .addFlopVote(flopVoteString)
                .build();

        Gson gson = new Gson();
        return gson.toJson(ballotDto);
    }

    public String[] getStringArray(ListView listView) {

        if(listView !=null){
            if (listView.getAdapter() !=null){
                String[] listString = new String[listView.getAdapter().getCount()];

                for (int i = 0; i < listString.length; i++) {
                    View view =listView.getChildAt(i);
                    EditText point_vote_cell = view.findViewById(R.id.user_vote_cell_auto_complete);
                    listString[i] = point_vote_cell.getText().toString();
                }
                return listString;
            }
        }
        return null;
    }


    private String getUsername() {
        return  sharedPreferencesCredentials.getString(USERNAME, null);
    }

    private String getCompetition_ref() {
        return  sharedPreferencesCompetition.getString(COMPETITION_REF, null);
    }

    private String getMatch_ref() {
        return  sharedPreferencesCompetition.getString(MATCH_REF, null);
    }

    private List<RuleDto> getRulesOfCompetition(){
        List<RuleDto> ruleDtoList = new ArrayList<>();

        String jsonStringOfRuleDto = sharedPreferencesCompetition.getString(RULES, null);
        JSONArray jsonArrayOfRuleDto;
        try {
            jsonArrayOfRuleDto = new JSONArray(jsonStringOfRuleDto);
            JSONObject json_rule;
            for (int i = 0; i < jsonArrayOfRuleDto.length(); i++) {

                json_rule = jsonArrayOfRuleDto.getJSONObject(i);

                ObjectMapper mapper = new ObjectMapper();
                RuleDto ruleDto = mapper.readValue(json_rule.toString(), RuleDto.class);
                ruleDtoList.add(ruleDto);
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        return ruleDtoList;
    }


    private void GetUsernamesLinkedToCompetition() {
        ApplicationConstants.Parameter parameter = new ApplicationConstants.Parameter(COMPETITION_REF, getCompetition_ref());
        final String url = createURL(URL_USER_GET, parameter);
        RequestQueue requestQueue = MySingletonRequestQueue.getInstance(getActivity().getApplicationContext()).getRequestQueue();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                Request.Method.GET, url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        users = new String[response.length()];
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject json;
                            try {
                                users[i] = response.get(i).toString();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        users[0] = GOD;
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


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Data
    private class JSONVote {

        private String username;
        private JSONObject ballot;
        private boolean hasVoted;

        private JSONVote(String username, JSONObject ballot, boolean hasVoted) {
            this.username = username;
            this.ballot = ballot;
            this.hasVoted = hasVoted;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public JSONObject getBallot() {
            return ballot;
        }

        public void setBallot(JSONObject ballot) {
            this.ballot = ballot;
        }

        public boolean isHasVoted() {
            return hasVoted;
        }

        public void setHasVoted(boolean hasVoted) {
            this.hasVoted = hasVoted;
        }
    }
}
