package lhc.com.carrdas.voteFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import lhc.com.adapter.MatchAdapter_ListMatches;
import lhc.com.adapter.VoteAdapter_UserIsVoting;
import lhc.com.carrdas.ListCompetitions;
import lhc.com.carrdas.ListMatchesOfCompetition;
import lhc.com.carrdas.R;
import lhc.com.dtos.BallotDto;
import lhc.com.dtos.MatchDto;
import lhc.com.dtos.RuleDto;
import lhc.com.otherRessources.ApplicationConstants;
import lhc.com.otherRessources.ApplicationConstants.Parameter;
import lhc.com.otherRessources.MySingletonRequestQueue;

import static android.content.Context.MODE_PRIVATE;
import static lhc.com.dtos.builder.BallotDtoBuilder.aBallotDtoWithRules;
import static lhc.com.otherRessources.ApplicationConstants.COMPETITION_REF;
import static lhc.com.otherRessources.ApplicationConstants.MATCH_REF;
import static lhc.com.otherRessources.ApplicationConstants.MyPREFERENCES_COMPETITION;
import static lhc.com.otherRessources.ApplicationConstants.MyPREFERENCES_CREDENTIALS;
import static lhc.com.otherRessources.ApplicationConstants.NUMBER_VOTE_FLOP;
import static lhc.com.otherRessources.ApplicationConstants.NUMBER_VOTE_TOP;
import static lhc.com.otherRessources.ApplicationConstants.RULES;
import static lhc.com.otherRessources.ApplicationConstants.URL_BALLOT_POST;
import static lhc.com.otherRessources.ApplicationConstants.URL_MATCH_GET;
import static lhc.com.otherRessources.ApplicationConstants.URL_USER_GET;
import static lhc.com.otherRessources.ApplicationConstants.USERNAME;
import static lhc.com.otherRessources.ApplicationConstants.createURL;


public class UserIsVoting extends Fragment {

    SharedPreferences sharedPreferencesCompetition;
    SharedPreferences sharedPreferencesCredentials;
    List<String> topVotes;
    List<String> flopVotes;
    List<RuleDto> rules;

    ListView listViewTop;
    ListView listViewFlop;

    int numberTop;
    int numberFlop;

    Button submit;

    String[] users;


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
        users[0] = "GOD";
        View view = inflater.inflate(R.layout.fragment_user_is_voting, container, false);
        sharedPreferencesCredentials = this.getActivity().getSharedPreferences(MyPREFERENCES_CREDENTIALS, MODE_PRIVATE);
        sharedPreferencesCompetition = this.getActivity().getSharedPreferences(MyPREFERENCES_COMPETITION, MODE_PRIVATE);

        GetUsernamesLinkedToCompetition();

        rules = getRulesOfCompetition();
        numberTop = getRules(NUMBER_VOTE_TOP);
        numberFlop = getRules(NUMBER_VOTE_FLOP);


        System.out.println(numberTop);

        topVotes = createEmptyList(numberTop);
        flopVotes = createEmptyList(numberFlop);

        listViewTop = view.findViewById(R.id.userIsVoting_topList);
        listViewFlop = view.findViewById(R.id.userIsVoting_flopList);
        submit = view.findViewById(R.id.submit_ballot);

        final VoteAdapter_UserIsVoting adapter_top = new VoteAdapter_UserIsVoting(getActivity().getApplicationContext(),
                topVotes, users);
        listViewTop.setAdapter(adapter_top);

        final VoteAdapter_UserIsVoting adapter_flop = new VoteAdapter_UserIsVoting(getActivity().getApplicationContext(),
                flopVotes, users);
        listViewFlop.setAdapter(adapter_flop);


        submit.setOnClickListener(submit());

        return view;

    }

    private View.OnClickListener submit() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveBallot_JsonPostRequest();
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

    private void goToListCompetition() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), ListCompetitions.class);
        startActivity(intent);
        getActivity().finish();

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
                .withVotesDtos()
                .addTopVote(getVotesTop())
                .addFlopVote(getVotesFlop())
                .build();

        Gson gson = new Gson();
        return gson.toJson(ballotDto);
    }



    private String[] getVotesTop() {
       return getStringArray(listViewTop);
    }

    private String[] getVotesFlop() {
        return getStringArray(listViewFlop);
    }

    public String[] getStringArray(ListView listView) {
        String[] listString = new String[listView.getAdapter().getCount()];

        for (int i = 0; i < listString.length; i++) {
            View view =listView.getChildAt(i);
            EditText point_vote_cell = view.findViewById(R.id.user_vote_cell_auto_complete);
            listString[i] = point_vote_cell.getText().toString();
        }
        return listString;
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
                            JSONObject json = null;
                            try {
                                json = response.getJSONObject(i);
                                users[i] = json.toString();
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
                        users[0] = "GOD";
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

}
