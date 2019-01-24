package lhc.com.carrdas;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

import lhc.com.carrdas.voteFragment.GenerateRanking;
import lhc.com.carrdas.voteFragment.Ranking;
import lhc.com.carrdas.voteFragment.UserIsVoting;
import lhc.com.carrdas.voteFragment.VoteDetails;
import lhc.com.otherRessources.ApplicationConstants;
import lhc.com.otherRessources.BaseActivity;
import lhc.com.otherRessources.MySingletonRequestQueue;

import static lhc.com.otherRessources.ApplicationConstants.COMPETITION_REF;
import static lhc.com.otherRessources.ApplicationConstants.JSON_LIST_VOTES;
import static lhc.com.otherRessources.ApplicationConstants.MATCH_CREATOR;
import static lhc.com.otherRessources.ApplicationConstants.MATCH_REF;
import static lhc.com.otherRessources.ApplicationConstants.MATCH_STATUS;
import static lhc.com.otherRessources.ApplicationConstants.MyPREFERENCES_COMPETITION;
import static lhc.com.otherRessources.ApplicationConstants.URL_BALLOT_GET;
import static lhc.com.otherRessources.ApplicationConstants.USERNAME;
import static lhc.com.otherRessources.ApplicationConstants.createURL;

public class VoteActivity extends BaseActivity implements
        UserIsVoting.OnFragmentInteractionListener,
        Ranking.OnFragmentInteractionListener,
        VoteDetails.OnFragmentInteractionListener,
        GenerateRanking.OnFragmentInteractionListener{

    private SharedPreferences sharedPreferencesCompetition;
    private String status;
    private String usernameCreator;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (status){
                case "OPEN" :
                    switch (item.getItemId()) {
                        case R.id.navigation_user_is_voting:
                            loadFragment(getFragmentUserIsVoting());
                            return true;
                        case R.id.navigation_ranking:
                            loadFragment(new GenerateRanking());
                            return true;
                        case R.id.navigation_vote_details:
                            loadFragment(new GenerateRanking());
                            return true;
                    }
                case "CLOSED" :
                    switch (item.getItemId()) {
                        case R.id.navigation_user_is_voting:
                            loadFragment(getFragmentUserIsVoting());
                            return true;
                        case R.id.navigation_ranking:
                            loadFragment(new Ranking());
                            return true;
                        case R.id.navigation_vote_details:
                            loadFragment(new VoteDetails());
                            return true;
                    }
            }
            return false;
        }
    };

    final Bundle[] bundleArray = new Bundle[1];

    private UserIsVoting getFragmentUserIsVoting() {
        return new UserIsVoting();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        sharedPreferencesCompetition = getSharedPreferences(MyPREFERENCES_COMPETITION, MODE_PRIVATE);
        sharedPreferencesCompetition = getSharedPreferences(MyPREFERENCES_COMPETITION, MODE_PRIVATE);
        status = sharedPreferencesCompetition.getString(MATCH_STATUS, "OPEN");
        usernameCreator = sharedPreferencesCompetition.getString(MATCH_CREATOR, "GOD");
        GetBallotListLinkedToCompetition();

        loadFragment(getFragmentUserIsVoting());


        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    private boolean loadFragment(Fragment fragment) {
        // set Fragmentclass Arguments
        Bundle bundle = bundleArray[0];
        //switching fragment
        if (fragment != null) {
            if (bundle !=null) {
                fragment.setArguments(bundle);
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    private void GetBallotListLinkedToCompetition() {
        ApplicationConstants.Parameter parameter = new ApplicationConstants.Parameter(MATCH_REF, getMatch_ref());
        final String url = createURL(URL_BALLOT_GET, parameter);
        RequestQueue requestQueue = MySingletonRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                Request.Method.GET, url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        bundleArray[0] = new Bundle();
                        bundleArray[0].putString(JSON_LIST_VOTES, response.toString());
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

    private String getMatch_ref() {
        return  sharedPreferencesCompetition.getString(MATCH_REF, null);
    }
}