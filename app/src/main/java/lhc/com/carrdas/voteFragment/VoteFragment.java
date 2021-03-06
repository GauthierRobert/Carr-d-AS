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
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lhc.com.dtos.MatchDto;
import lhc.com.volley.JsonArrayRequestGet;
import lhc.com.volley.JsonObjectRequestPost;
import lhc.com.adapter.VoteAdapter_VoteFragment;
import lhc.com.carrdas.ListMatches;
import lhc.com.carrdas.R;
import lhc.com.dtos.BallotDto;
import lhc.com.dtos.RuleDto;
import lhc.com.dtos.VoteDto;
import lhc.com.otherRessources.ApplicationConstants;
import lhc.com.otherRessources.ApplicationConstants.Parameter;
import lhc.com.volley.MySingletonRequestQueue;
import lombok.Data;

import static android.content.Context.MODE_PRIVATE;
import static lhc.com.otherRessources.ApplicationConstants.JSON_LIST_SPECTATORS_BUNDLE;
import static lhc.com.otherRessources.ApplicationConstants.NAME_FLOP;
import static lhc.com.otherRessources.ApplicationConstants.NAME_TOP;
import static lhc.com.otherRessources.ApplicationConstants.URL_BASE;
import static lhc.com.otherRessources.ApplicationConstants.URL_MATCH_ADD_SPECTATOR;
import static lhc.com.volley.JsonArrayRequestGet.jsonArrayRequestGet;
import static lhc.com.dtos.builder.BallotDtoBuilder.aBallotDtoWithRules;
import static lhc.com.otherRessources.ApplicationConstants.COMPETITION_REF;
import static lhc.com.otherRessources.ApplicationConstants.FLOP;
import static lhc.com.otherRessources.ApplicationConstants.GOD;
import static lhc.com.otherRessources.ApplicationConstants.HAS_VOTED;
import static lhc.com.otherRessources.ApplicationConstants.JSON_LIST_VOTES_BUNDLE;
import static lhc.com.otherRessources.ApplicationConstants.MATCH_REF;
import static lhc.com.otherRessources.ApplicationConstants.MyPREFERENCES_COMPETITION;
import static lhc.com.otherRessources.ApplicationConstants.MyPREFERENCES_CREDENTIALS;
import static lhc.com.otherRessources.ApplicationConstants.NUMBER_VOTE_FLOP;
import static lhc.com.otherRessources.ApplicationConstants.NUMBER_VOTE_TOP;
import static lhc.com.otherRessources.ApplicationConstants.RULES;
import static lhc.com.otherRessources.ApplicationConstants.TOP;
import static lhc.com.otherRessources.ApplicationConstants.URL_BALLOT_POST;
import static lhc.com.otherRessources.ApplicationConstants.URL_USER_GET_LIST;
import static lhc.com.otherRessources.ApplicationConstants.USERNAME;
import static lhc.com.otherRessources.ApplicationConstants.WITH_COMMENTS_FLOP;
import static lhc.com.otherRessources.ApplicationConstants.WITH_COMMENTS_TOP;
import static lhc.com.otherRessources.ApplicationConstants.createURL;


public class VoteFragment extends Fragment {

    SharedPreferences sharedPreferencesCompetition;
    SharedPreferences sharedPreferencesCredentials;

    List<String> topVotes;
    List<String> flopVotes;
    List<RuleDto> rules;

    int numberTop;
    int numberFlop;

    Button submit;
    Button addSpectator;
    Button voteTopButton;
    Button voteFlopButton;

    TextView overviewTop;
    TextView overviewFlop;

    TextView nameTop;
    TextView nameFlop;

    String commentTop;
    String commentFlop;
    EditText commentsEditTextTop;
    EditText commentsEditTextFlop;
    boolean withCommentTop;
    boolean withCommentFlop;

    String[] players;
    String[] spectators;
    String[] topVoteString;
    String[] flopVoteString;

    String jsonArrayOfListVotes;

    public final static String CHOOSE_A_PLAYER = "-- Choose a player --";


    private OnFragmentInteractionListener mListener;


    public VoteFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        players = new String[1];
        players[0] = GOD;
        View view = inflater.inflate(R.layout.fragment_vote, container, false);
        sharedPreferencesCredentials = this.getActivity().getSharedPreferences(MyPREFERENCES_CREDENTIALS, MODE_PRIVATE);
        sharedPreferencesCompetition = this.getActivity().getSharedPreferences(MyPREFERENCES_COMPETITION, MODE_PRIVATE);

        if (getArguments() != null) {
            jsonArrayOfListVotes = getArguments().getString(JSON_LIST_VOTES_BUNDLE);
        }

        GetUsernamesLinkedToCompetition();
        ArrayList<String> stringArrayList = getArguments().getStringArrayList(JSON_LIST_SPECTATORS_BUNDLE);
        spectators = stringArrayList.toArray(new String[stringArrayList.size()]);

        overviewTop = view.findViewById(R.id.overviewOfTopVotes);
        overviewFlop = view.findViewById(R.id.overviewOfFlopVotes);
        voteFlopButton = view.findViewById(R.id.vote_flop_button_user_is_voting);
        voteTopButton = view.findViewById(R.id.vote_top_button_user_is_voting);
        commentsEditTextTop = view.findViewById(R.id.comments_top_vote);
        commentsEditTextFlop = view.findViewById(R.id.comments_flop_vote);
        addSpectator = view.findViewById(R.id.add_spectator);
        submit = view.findViewById(R.id.submit_ballot);

        nameTop = view.findViewById(R.id.name_top_vote);
        nameFlop = view.findViewById(R.id.name_flop_vote);
        nameTop.setText(sharedPreferencesCompetition.getString(NAME_TOP, TOP));
        nameFlop.setText(sharedPreferencesCompetition.getString(NAME_FLOP, FLOP));

        rules = getRulesOfCompetition();
        numberTop = sharedPreferencesCompetition.getInt(NUMBER_VOTE_TOP, 0);
        numberFlop = sharedPreferencesCompetition.getInt(NUMBER_VOTE_FLOP, 0);
        withCommentTop = sharedPreferencesCompetition.getBoolean(WITH_COMMENTS_TOP, false);
        withCommentFlop = sharedPreferencesCompetition.getBoolean(WITH_COMMENTS_FLOP, false);


        if ((numberTop == 0) && (!withCommentTop)) {
            LinearLayout layout = view.findViewById(R.id.layout_top_vote_user_is_voting);
            layout.setVisibility(View.GONE);
        } else {
            if (numberTop == 0) {
                voteTopButton.setVisibility(View.GONE);
                overviewTop.setVisibility(View.GONE);
            }
            if (!withCommentTop) {
                commentsEditTextTop.setVisibility(View.GONE);
            }
        }
        if ((numberFlop == 0) && (!withCommentFlop)) {
            LinearLayout layout = view.findViewById(R.id.layout_flop_vote_user_is_voting);
            layout.setVisibility(View.GONE);
        } else {
            if (numberFlop == 0) {
                voteFlopButton.setVisibility(View.GONE);
                overviewFlop.setVisibility(View.GONE);
            }
            if (!withCommentFlop) {
                commentsEditTextFlop.setVisibility(View.GONE);
            }
        }


        topVotes = createEmptyList(numberTop);
        flopVotes = createEmptyList(numberFlop);

        voteTopButton.setOnClickListener(editTopVotes());
        voteFlopButton.setOnClickListener(editFlopVotes());
        addSpectator.setOnClickListener(addSpectator());
        submit.setOnClickListener(submit());


        if (jsonArrayOfListVotes != null) {
            try {
                JSONVote jsonVote = jsonVote();
                if (jsonVote.isHasVoted()) {
                    voteTopButton.setVisibility(View.GONE);
                    voteFlopButton.setVisibility(View.GONE);
                    commentsEditTextTop.setEnabled(false);
                    commentsEditTextFlop.setEnabled(false);
                    submit.setVisibility(View.GONE);
                    addSpectator.setVisibility(View.GONE);

                    ObjectMapper mapper = new ObjectMapper();
                    BallotDto ballotDto = mapper.readValue(jsonVote.getBallot().toString(), BallotDto.class);
                    overviewTop.setText(generateOverView(ballotDto, TOP));
                    overviewFlop.setText(generateOverView(ballotDto, FLOP));
                    commentsEditTextTop.setText(ballotDto.getCommentTop());
                    commentsEditTextFlop.setText(ballotDto.getCommentFlop());
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }

        return view;
    }

    private JSONVote jsonVote() throws JSONException {
        JSONArray arrayBallot = new JSONArray(jsonArrayOfListVotes);
        for (int i = 0; i < arrayBallot.length(); i++) {
            JSONObject ballot = arrayBallot.getJSONObject(i);
            String username = ballot.getString("username");
            if (getUsername().equalsIgnoreCase(username)) {
                return new JSONVote(username, ballot, true);
            }
        }
        return new JSONVote(GOD, null, false);
    }

    private String generateOverView(BallotDto ballotDto, String constantVote) {
        String[] strings = null;
        if (ballotDto != null) {
            if (ballotDto.getVoteDtos() != null) {
                strings = new String[ballotDto.getVoteDtos().size()];
                for (VoteDto voteDto : ballotDto.getVoteDtos()) {
                    int indication = voteDto.getIndication();
                    if (TOP.equals(constantVote)) {
                        if (indication > 0) {
                            strings[indication - 1] = voteDto.getName();
                        }
                    } else if (FLOP.equals(constantVote)) {
                        if (indication < 0) {
                            strings[(-(1 + indication))] = voteDto.getName();
                        }
                    }
                }
            }
        }

        return createOverviewWithArray(strings);
    }

    private String createOverviewWithArray(String[] flopVoteString) {
        if (flopVoteString != null) {
            if (flopVoteString.length != 0) {
                String overview = "#1 : " + flopVoteString[0];
                String nl = System.getProperty("line.separator");

                for (int i = 2; i <= flopVoteString.length; i++) {
                    if (flopVoteString[i - 1] != null)
                        overview = overview + nl + "#" + i + " : " + flopVoteString[i - 1];
                }
                return overview;
            }
        }
        return null;
    }


    private View.OnClickListener editTopVotes() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getActivity();

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_custom, null);
                LinearLayout dialog_layout = dialogView.findViewById(R.id.dialog_layout);
                ((TextView) dialogView.findViewById(R.id.title_dialog)).setText("Vote for TOP players");

                final ListView topVote = new ListView(context);

                final VoteAdapter_VoteFragment adapter_top = new VoteAdapter_VoteFragment(context,
                        topVotes, players, spectators);
                topVote.setAdapter(adapter_top);
                dialog_layout.addView(topVote);
                dialog_layout.setPadding(50, 30, 50, 0);

                builder.setView(dialogView);
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
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            }
        };
    }

    private View.OnClickListener editFlopVotes() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getActivity();

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_custom, null);
                LinearLayout dialog_layout = dialogView.findViewById(R.id.dialog_layout);
                ((TextView) dialogView.findViewById(R.id.title_dialog)).setText("Vote for FLOP players");

                final ListView flopVote = new ListView(context);

                final VoteAdapter_VoteFragment adapter_flop = new VoteAdapter_VoteFragment(context,
                        flopVotes, players, spectators);
                flopVote.setAdapter(adapter_flop);
                dialog_layout.addView(flopVote);
                dialog_layout.setPadding(50, 30, 50, 0);

                builder.setView(dialogView);
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
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            }
        };
    }


    private View.OnClickListener submit() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = getActivity();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirmation");
                builder.setMessage("Do you confirm your vote?");

                // Set up the buttons
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!overviewTop.getText().toString().contains(CHOOSE_A_PLAYER) &&
                                !overviewFlop.getText().toString().contains(CHOOSE_A_PLAYER)) {
                            saveComments();
                            saveBallot_JsonPostRequest();
                        }
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


    private View.OnClickListener addSpectator() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context = getActivity();

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_custom, null);
                LinearLayout dialog_layout = dialogView.findViewById(R.id.dialog_layout);
                ((TextView) dialogView.findViewById(R.id.title_dialog)).setText("Add Spectator");

                final EditText editText = new EditText(context);
                editText.setHint("Spectator");
                dialog_layout.addView(editText);
                dialog_layout.setPadding(50, 30, 50, 0);

                builder.setView(dialogView);
                // Set up the buttons


                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String url = URL_BASE + URL_MATCH_ADD_SPECTATOR;

                        List<String> specList = new ArrayList<>();
                        if (editText.getText() == null) {
                            return;
                        }
                        specList.add(editText.getText().toString());
                        MatchDto matchDto = MatchDto.matchDto(getMatch_ref(), specList);
                        Gson gson = new Gson();
                        final String mRequestBody = gson.toJson(matchDto);

                        Context mContext = context;
                        RequestQueue requestQueue = MySingletonRequestQueue.getInstance(mContext.getApplicationContext()).getRequestQueue();
                        JsonObjectRequestPost jsonObjectRequest = JsonObjectRequestPost.jsonObjectRequestPost(
                                url,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.d("onResponse", "Create Ballot : " + response.toString());
                                        ObjectMapper mapper = new ObjectMapper();
                                        MatchDto matchDto = null;
                                        try {
                                            matchDto = mapper.readValue(response.toString(), MatchDto.class);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        spectators = matchDto.getVisitors().toArray(new String[matchDto.getVisitors().size()]);
                                    }
                                },
                                mRequestBody,
                                mContext
                        );

                        requestQueue.add(jsonObjectRequest);
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


    private void saveComments() {
        commentTop = commentsEditTextTop.getText().toString();
        commentFlop = commentsEditTextFlop.getText().toString();
    }

    private void saveBallot_JsonPostRequest() {
        Parameter parameter = new Parameter(USERNAME, getUsername());
        final String url = createURL(URL_BALLOT_POST, parameter);

        Context mContext = this.getActivity();
        RequestQueue requestQueue = MySingletonRequestQueue.getInstance(mContext.getApplicationContext()).getRequestQueue();
        final String mRequestBody = getJsonRequest();
        JsonObjectRequestPost jsonObjectRequest = JsonObjectRequestPost.jsonObjectRequestPost(
                url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("onResponse", "Create Balot : " + response.toString());
                        goToMatchList();
                    }
                },
                mRequestBody,
                mContext
        );

        requestQueue.add(jsonObjectRequest);
    }

    private void goToMatchList() {
        Intent intent = new Intent(getActivity(), ListMatches.class);
        intent.putExtra(HAS_VOTED, true);
        getActivity().finish();
        startActivity(intent);
    }

    private List<String> createEmptyList(int numberTop) {
        List<String> emptyList = new ArrayList<>();

        for (int i = 0; i < numberTop; i++) {
            emptyList.add(null);
        }
        return emptyList;

    }

    private String getJsonRequest() {
        BallotDto ballotDto = aBallotDtoWithRules(rules)
                .withCompetition_ref(getCompetition_ref())
                .withMatch_ref(getMatch_ref())
                .withComment(commentTop, commentFlop)
                .withVotesDtos()
                .addTopVote(topVoteString)
                .addFlopVote(flopVoteString)
                .build();

        Gson gson = new Gson();
        return gson.toJson(ballotDto);
    }

    public String[] getStringArray(ListView listView) {

        if (listView != null) {
            if (listView.getAdapter() != null) {
                String[] listString = new String[listView.getAdapter().getCount()];

                for (int i = 0; i < listString.length; i++) {
                    View view = listView.getChildAt(i);
                    Spinner point_vote_cell = view.findViewById(R.id.user_vote_cell_auto_complete);
                    listString[i] = point_vote_cell.getSelectedItem().toString();
                }
                return listString;
            }
        }
        return null;
    }


    private String getUsername() {
        return sharedPreferencesCredentials.getString(USERNAME, null);
    }

    private String getCompetition_ref() {
        return sharedPreferencesCompetition.getString(COMPETITION_REF, null);
    }

    private String getMatch_ref() {
        return sharedPreferencesCompetition.getString(MATCH_REF, null);
    }

    private List<RuleDto> getRulesOfCompetition() {
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
        final String url = createURL(URL_USER_GET_LIST, parameter);
        Context mContext = getActivity().getApplicationContext();
        RequestQueue requestQueue = MySingletonRequestQueue.getInstance(mContext).getRequestQueue();

        JsonArrayRequestGet jsonObjectRequest = jsonArrayRequestGet(
                url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        players = new String[response.length() + 1];
                        players[0] = CHOOSE_A_PLAYER;
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                players[i+1] = response.get(i).toString();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                mContext);
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

        public boolean isHasVoted() {
            return hasVoted;
        }
    }
}
