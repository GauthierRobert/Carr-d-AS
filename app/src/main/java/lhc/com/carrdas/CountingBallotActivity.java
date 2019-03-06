package lhc.com.carrdas;

import android.content.Context;
import android.content.SharedPreferences;
import android.renderscript.RenderScript;
import android.service.notification.NotificationListenerService;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import lhc.com.adapter.ObjectForAdapter.RankingCell;
import lhc.com.adapter.RankingAdapter_TabbedRanking;
import lhc.com.dtos.Rankings;
import lhc.com.otherRessources.ApplicationConstants;
import lhc.com.volley.JsonArrayRequestGet;
import lhc.com.volley.JsonObjectRequestGet;
import lhc.com.volley.JsonObjectRequestPost;
import lhc.com.volley.MySingletonRequestQueue;

import static lhc.com.otherRessources.ApplicationConstants.MATCH_REF;
import static lhc.com.otherRessources.ApplicationConstants.MyPREFERENCES_COMPETITION;
import static lhc.com.otherRessources.ApplicationConstants.MyPREFERENCES_CREDENTIALS;
import static lhc.com.otherRessources.ApplicationConstants.NUMBER_VOTE_FLOP;
import static lhc.com.otherRessources.ApplicationConstants.NUMBER_VOTE_TOP;
import static lhc.com.otherRessources.ApplicationConstants.TOP;
import static lhc.com.otherRessources.ApplicationConstants.URL_RANKINGS_INT;
import static lhc.com.otherRessources.ApplicationConstants.URL_RANKING_TOP;
import static lhc.com.otherRessources.ApplicationConstants.WITH_COMMENTS_FLOP;
import static lhc.com.otherRessources.ApplicationConstants.WITH_COMMENTS_TOP;
import static lhc.com.otherRessources.ApplicationConstants.createURL;

public class CountingBallotActivity extends AppCompatActivity {

    SharedPreferences sharedPreferencesCompetition;
    SharedPreferences sharedPreferencesCredentials;

    ListView topRankingListView;
    ListView flopRankingListView;

    Context mContext;

    Button next;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counting_ballot);

        sharedPreferencesCompetition = getSharedPreferences(MyPREFERENCES_COMPETITION, MODE_PRIVATE);
        sharedPreferencesCredentials = getSharedPreferences(MyPREFERENCES_CREDENTIALS, MODE_PRIVATE);

        topRankingListView = findViewById(R.id.ranking_top_counting);
        flopRankingListView = findViewById(R.id.ranking_flop_counting);

        mContext = CountingBallotActivity.this;

        int numberTop = sharedPreferencesCompetition.getInt(NUMBER_VOTE_TOP, 0);
        int numberFlop = sharedPreferencesCompetition.getInt(NUMBER_VOTE_FLOP, 0);
        boolean withCommentTop = sharedPreferencesCompetition.getBoolean(WITH_COMMENTS_TOP, false);
        boolean withCommentFlop = sharedPreferencesCompetition.getBoolean(WITH_COMMENTS_FLOP, false);

        TextView overviewOfTopVotes = findViewById(R.id.overviewOfTopVotes_vote_details);
        TextView overviewOfFlopVotes = findViewById(R.id.overviewOfFlopVotes_vote_details);
        TextView comments_vote_details_top = findViewById(R.id.comments_vote_details_top);
        TextView comments_vote_details_flop = findViewById(R.id.comments_vote_details_flop);
        LinearLayout layout_top = findViewById(R.id.layout_top_vote_vote_details);
        LinearLayout layout_flop = findViewById(R.id.layout_flop_vote_vote_details);
        TextView nameTop = findViewById(R.id.name_top_details);
        TextView nameFlop = findViewById(R.id.name_flop_details);
        if ((numberTop == 0) && (!withCommentTop)){
            layout_top.setVisibility(View.GONE);
        } else {
            if (numberTop == 0) {
                overviewOfTopVotes.setVisibility(View.GONE);
            }
            if (!withCommentTop) {
                comments_vote_details_top.setVisibility(View.GONE);
            }
        }
        if ((numberFlop == 0) && (!withCommentFlop)){
            layout_flop.setVisibility(View.GONE);
        } else {
            if (numberFlop == 0) {
                overviewOfFlopVotes.setVisibility(View.GONE);
            }
            if (!withCommentFlop) {
                comments_vote_details_flop.setVisibility(View.GONE);
            }
        }


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
}
