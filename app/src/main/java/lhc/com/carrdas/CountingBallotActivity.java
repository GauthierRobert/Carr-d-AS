package lhc.com.carrdas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.util.List;
import java.util.function.Predicate;

import lhc.com.adapter.ObjectForAdapter.RankingCell;
import lhc.com.adapter.RankingAdapter_TabbedRanking;
import lhc.com.dtos.BallotDto;
import lhc.com.dtos.CompetitionDto;
import lhc.com.dtos.MatchDto;
import lhc.com.dtos.Rankings;
import lhc.com.dtos.VoteDto;
import lhc.com.dtos.builder.BallotDtoBuilder;
import lhc.com.dtos.builder.BallotDtoBuilder.VoteDtoBuilder;
import lhc.com.otherRessources.ApplicationConstants;
import lhc.com.volley.JsonArrayRequestGet;
import lhc.com.volley.JsonObjectRequestGet;
import lhc.com.volley.JsonObjectRequestPost;
import lhc.com.volley.MySingletonRequestQueue;

import static lhc.com.carrdas.voteFragment.VoteFragment.CHOOSE_A_PLAYER;
import static lhc.com.dtos.builder.BallotDtoBuilder.aBallotDto;
import static lhc.com.dtos.builder.BallotDtoBuilder.aCountedBallotDto;
import static lhc.com.otherRessources.ApplicationConstants.COMPETITION_REF;
import static lhc.com.otherRessources.ApplicationConstants.FLOP;
import static lhc.com.otherRessources.ApplicationConstants.JSON_MATCH_INTENT;
import static lhc.com.otherRessources.ApplicationConstants.MATCH_REF;
import static lhc.com.otherRessources.ApplicationConstants.MyPREFERENCES_COMPETITION;
import static lhc.com.otherRessources.ApplicationConstants.MyPREFERENCES_CREDENTIALS;
import static lhc.com.otherRessources.ApplicationConstants.NAME_FLOP;
import static lhc.com.otherRessources.ApplicationConstants.NAME_TOP;
import static lhc.com.otherRessources.ApplicationConstants.NUMBER_VOTE_FLOP;
import static lhc.com.otherRessources.ApplicationConstants.NUMBER_VOTE_TOP;
import static lhc.com.otherRessources.ApplicationConstants.TOP;
import static lhc.com.otherRessources.ApplicationConstants.URL_BASE;
import static lhc.com.otherRessources.ApplicationConstants.URL_COMPETITION_POST;
import static lhc.com.otherRessources.ApplicationConstants.URL_RANKINGS_INT;
import static lhc.com.otherRessources.ApplicationConstants.URL_USER_GET_LIST;
import static lhc.com.otherRessources.ApplicationConstants.WITH_COMMENTS_FLOP;
import static lhc.com.otherRessources.ApplicationConstants.WITH_COMMENTS_TOP;
import static lhc.com.otherRessources.ApplicationConstants.WITH_VALIDATION_FLOP;
import static lhc.com.otherRessources.ApplicationConstants.WITH_VALIDATION_TOP;
import static lhc.com.otherRessources.ApplicationConstants.createURL;
import static lhc.com.volley.JsonArrayRequestGet.jsonArrayRequestGet;

public class CountingBallotActivity extends AppCompatActivity {

    SharedPreferences sharedPreferencesCompetition;
    SharedPreferences sharedPreferencesCredentials;

    ListView topRankingListView;
    ListView flopRankingListView;

    Context mContext;

    Button next;

    String[] players;
    String[] spectators;

    Spinner validationTop;
    Spinner validationFlop;

    String jsonMatch;
    MatchDto matchDto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counting_ballot);

        jsonMatch = getIntent().getStringExtra(JSON_MATCH_INTENT);


        ObjectMapper mapper = new ObjectMapper();
        try {
            matchDto = mapper.readValue(jsonMatch, MatchDto.class);
        } catch (IOException e) {
            e.printStackTrace();
        }


        sharedPreferencesCompetition = getSharedPreferences(MyPREFERENCES_COMPETITION, MODE_PRIVATE);
        sharedPreferencesCredentials = getSharedPreferences(MyPREFERENCES_CREDENTIALS, MODE_PRIVATE);

        topRankingListView = findViewById(R.id.ranking_top_counting);
        flopRankingListView = findViewById(R.id.ranking_flop_counting);

        mContext = CountingBallotActivity.this;

        getRankings();
        GetUsernamesLinkedToCompetition();
        spectators = matchDto.getVisitors().toArray(new String[matchDto.getVisitors().size()]);

        int numberTop = sharedPreferencesCompetition.getInt(NUMBER_VOTE_TOP, 0);
        int numberFlop = sharedPreferencesCompetition.getInt(NUMBER_VOTE_FLOP, 0);
        boolean withCommentTop = sharedPreferencesCompetition.getBoolean(WITH_COMMENTS_TOP, false);
        boolean withCommentFlop = sharedPreferencesCompetition.getBoolean(WITH_COMMENTS_FLOP, false);
        boolean withValidationTop = sharedPreferencesCompetition.getBoolean(WITH_VALIDATION_TOP, false);
        boolean withValidationFlop = sharedPreferencesCompetition.getBoolean(WITH_VALIDATION_FLOP, false);

        TextView overviewOfTopVotes = findViewById(R.id.overviewOfTopVotes_counting);
        TextView overviewOfFlopVotes = findViewById(R.id.overviewOfFlopVotes_counting);
        TextView comments_counting_top = findViewById(R.id.comments_counting_top);
        TextView comments_counting_flop = findViewById(R.id.comments_counting_flop);
        LinearLayout layout_top = findViewById(R.id.layout_top_vote_counting);
        LinearLayout layout_flop = findViewById(R.id.layout_flop_vote_counting);
        TextView nameTop = findViewById(R.id.name_top_details);
        TextView nameFlop = findViewById(R.id.name_flop_details);
        validationTop = findViewById(R.id.validation_top_spinner);
        validationFlop = findViewById(R.id.validation_flop_spinner);
        if ((numberTop == 0) && (!withCommentTop)) {
            layout_top.setVisibility(View.GONE);
        } else {
            if (numberTop == 0) {
                overviewOfTopVotes.setVisibility(View.GONE);
            }
            if (!withCommentTop) {
                comments_counting_top.setVisibility(View.GONE);
            }
            if (!withValidationTop) {
                validationTop.setVisibility(View.GONE);
            }
        }
        if ((numberFlop == 0) && (!withCommentFlop)) {
            layout_flop.setVisibility(View.GONE);
        } else {
            if (numberFlop == 0) {
                overviewOfFlopVotes.setVisibility(View.GONE);
            }
            if (!withCommentFlop) {
                comments_counting_flop.setVisibility(View.GONE);
            }
            if (!withValidationFlop) {
                validationFlop.setVisibility(View.GONE);
            }
        }
        nameTop.setText(sharedPreferencesCompetition.getString(NAME_TOP, TOP));
        nameFlop.setText(sharedPreferencesCompetition.getString(NAME_FLOP, FLOP));


        BallotDto ballotDto = getUncountedFirstBallot(matchDto);

        if (ballotDto == null) {
            Intent intent = new Intent();
            intent.setClass(CountingBallotActivity.this, ListMatches.class);
            startActivity(intent);
            finish();
        } else {
            overviewOfTopVotes.setText(generateOverView(ballotDto, TOP));
            overviewOfFlopVotes.setText(generateOverView(ballotDto, FLOP));
            nameTop.setText(sharedPreferencesCompetition.getString(NAME_TOP, TOP));
            nameFlop.setText(sharedPreferencesCompetition.getString(NAME_FLOP, FLOP));
            comments_counting_top.setText(ballotDto.getCommentTop());
            comments_counting_flop.setText(ballotDto.getCommentFlop());

            next = findViewById(R.id.next_ballot_counting);
            next.setOnClickListener(next(ballotDto.getReference()));
        }
    }

    private BallotDto getUncountedFirstBallot(MatchDto matchDto) {
        List<BallotDto> ballotDtos = matchDto.getBallotDtos();
        for (BallotDto ballotDto : ballotDtos) {
            if (ballotDto.isCounted())
                return ballotDto;
        }
        return null;
    }

    private View.OnClickListener next(final String reference) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CountBallot(reference);
            }
        };
    }

    private void CountBallot(String reference) {

        RequestQueue requestQueue = MySingletonRequestQueue.getInstance(mContext.getApplicationContext()).getRequestQueue();
        final String mRequestBody = getMRequest(reference);

        JsonObjectRequestPost jsonObjectRequest = JsonObjectRequestPost.jsonObjectRequestPost(
                URL_BASE + URL_COMPETITION_POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        finish();
                        startActivity(getIntent());
                    }
                },
                mRequestBody,
                mContext
        );
        requestQueue.add(jsonObjectRequest);
    }

    private String getMRequest(String reference) {
        VoteDtoBuilder dtoBuilder = aCountedBallotDto().withReference(reference).withVotesDtos();
        if (validationTop.getSelectedItem() != null) {
            String playerValidation = validationTop.getSelectedItem().toString();
            dtoBuilder.addValidationTopVote(playerValidation);
        } else if (validationFlop.getSelectedItem() != null) {
            String playerValidation = validationFlop.getSelectedItem().toString();
            dtoBuilder.addValidationTopVote(playerValidation);
        }

        BallotDto ballotDto = dtoBuilder.build();
        Gson gson = new Gson();
        return gson.toJson(ballotDto);
    }

    private void getRankings() {
        ApplicationConstants.Parameter parameter = new ApplicationConstants.Parameter(MATCH_REF, getMatch_ref());
        final String url = createURL(URL_RANKINGS_INT, parameter);
        RequestQueue requestQueue = MySingletonRequestQueue
                .getInstance(mContext).getRequestQueue();
        JsonObjectRequestGet jsonObjectRequest = JsonObjectRequestGet.jsonObjectRequestGet(
                url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Rankings rankings = null;
                        try {
                            ObjectMapper mapper = new ObjectMapper();
                            rankings = mapper.readValue(response.toString(), Rankings.class);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        List<RankingCell> rankingCellsTop = getRankingCellsTop(rankings);
                        List<RankingCell> rankingCellsFlop = getRankingCellsFlop(rankings);
                        RankingAdapter_TabbedRanking rankingAdapterTop = new RankingAdapter_TabbedRanking(mContext, rankingCellsTop, TOP);
                        RankingAdapter_TabbedRanking rankingAdapterFlop = new RankingAdapter_TabbedRanking(mContext, rankingCellsFlop, TOP);
                        topRankingListView.setAdapter(rankingAdapterTop);
                        flopRankingListView.setAdapter(rankingAdapterFlop);
                    }
                }, mContext
        );
        requestQueue.add(jsonObjectRequest);
    }

    private List<RankingCell> getRankingCellsFlop(final Rankings rankings) {
        List<RankingCell> top = rankings.getTop();
        top.removeIf(new Predicate<RankingCell>() {
            @Override
            public boolean test(RankingCell rankingCell) {
                return rankings.getTop().indexOf(rankingCell) > 4;
            }
        });
        return top;
    }

    private List<RankingCell> getRankingCellsTop(final Rankings rankings) {
        List<RankingCell> flop = rankings.getFlop();
        flop.removeIf(new Predicate<RankingCell>() {
            @Override
            public boolean test(RankingCell rankingCell) {
                return rankings.getTop().indexOf(rankingCell) > 4;
            }
        });
        return flop;
    }

    private String getMatch_ref() {
        return sharedPreferencesCompetition.getString(MATCH_REF, null);
    }

    private String getCompetition_ref() {
        return sharedPreferencesCompetition.getString(COMPETITION_REF, null);
    }

    private void GetUsernamesLinkedToCompetition() {
        ApplicationConstants.Parameter parameter = new ApplicationConstants.Parameter(COMPETITION_REF, getCompetition_ref());
        final String url = createURL(URL_USER_GET_LIST, parameter);
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
                                players[i + 1] = response.get(i).toString();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adapterForSpinner();
                    }
                },
                mContext);
        requestQueue.add(jsonObjectRequest);
    }

    private void adapterForSpinner() {
        String[] users = players;
        if (spectators != null) {
            if (spectators.length != 0) {
                users = ApplicationConstants.concat(players, spectators);
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, R.layout.cell_vote_fragment_spinner, R.id.auto_complete_text, users);
        validationTop.setAdapter(adapter);
        validationFlop.setAdapter(adapter);
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


}
