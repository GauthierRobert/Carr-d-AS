package lhc.com.carrdas;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.widget.Toast;

import lhc.com.carrdas.voteFragment.RankingFragment;
import lhc.com.carrdas.voteFragment.VoteFragment;
import lhc.com.carrdas.voteFragment.DetailsFragment;
import lhc.com.otherRessources.BaseActivity;

import static lhc.com.otherRessources.ApplicationConstants.CLOSED;
import static lhc.com.otherRessources.ApplicationConstants.GOD;
import static lhc.com.otherRessources.ApplicationConstants.JSON_LIST_VOTES_BUNDLE;
import static lhc.com.otherRessources.ApplicationConstants.JSON_LIST_VOTES_INTENT;
import static lhc.com.otherRessources.ApplicationConstants.MATCH_CREATOR;
import static lhc.com.otherRessources.ApplicationConstants.MATCH_REF;
import static lhc.com.otherRessources.ApplicationConstants.MATCH_STATUS;
import static lhc.com.otherRessources.ApplicationConstants.MyPREFERENCES_COMPETITION;
import static lhc.com.otherRessources.ApplicationConstants.MyPREFERENCES_CREDENTIALS;
import static lhc.com.otherRessources.ApplicationConstants.ON_HOLD;
import static lhc.com.otherRessources.ApplicationConstants.OPEN;
import static lhc.com.otherRessources.ApplicationConstants.USERNAME;

public class VoteActivity extends BaseActivity implements
        VoteFragment.OnFragmentInteractionListener,
        RankingFragment.OnFragmentInteractionListener,
        DetailsFragment.OnFragmentInteractionListener{

    private SharedPreferences sharedPreferencesCompetition;
    private SharedPreferences sharedPreferencesCredentials;
    private String status;
    private String usernameCreator;
    private String usernameConnected;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (status){
                case OPEN :
                    switch (item.getItemId()) {
                        case R.id.navigation_user_is_voting:
                            loadFragment(getFragmentUserIsVoting());
                            return true;
                        case R.id.navigation_ranking:
                            toastRankingIfNotCreator();
                            return true;
                        case R.id.navigation_vote_details:
                            toastDetailsIfNotCreator();
                            return true;
                    }
                case CLOSED :
                    switch (item.getItemId()) {
                        case R.id.navigation_user_is_voting:
                            loadFragment(getFragmentUserIsVoting());
                            return true;
                        case R.id.navigation_ranking:
                            loadFragment(new RankingFragment());
                            return true;
                        case R.id.navigation_vote_details:
                            loadFragment(new DetailsFragment());
                            return true;
                    }
            }
            return false;
        }
    };

    private void toastRankingIfNotCreator(){
        if (usernameCreator.equals(usernameConnected)){
            loadFragment(new RankingFragment());
        } else {
            createToast();
        }
    }

    private void toastDetailsIfNotCreator(){
        if (usernameCreator.equals(usernameConnected)){
            loadFragment(new DetailsFragment());
        } else {
            createToast();
        }
    }

    private void createToast() {
        Context context = this.getApplicationContext();
        CharSequence text = "You are not allowed to do that ! Please ask to the game creator (" + usernameCreator + ") to close the game and generate the ranking"  ;
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private VoteFragment getFragmentUserIsVoting() {
        return new VoteFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        sharedPreferencesCompetition = getSharedPreferences(MyPREFERENCES_COMPETITION, MODE_PRIVATE);
        sharedPreferencesCredentials = getSharedPreferences(MyPREFERENCES_CREDENTIALS, MODE_PRIVATE);
        status = sharedPreferencesCompetition.getString(MATCH_STATUS, OPEN);
        usernameCreator = sharedPreferencesCompetition.getString(MATCH_CREATOR, GOD);
        usernameConnected = sharedPreferencesCredentials.getString(USERNAME, null);

        loadFragment(getFragmentUserIsVoting());


        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    private boolean loadFragment(Fragment fragment) {

        //switching fragment
        if (fragment != null) {
            Bundle bundle =  new Bundle();
            if(getIntent().getStringExtra(JSON_LIST_VOTES_INTENT)!=null) {
                bundle.putString(JSON_LIST_VOTES_BUNDLE, getIntent().getStringExtra(JSON_LIST_VOTES_INTENT));
            }
            fragment.setArguments(bundle);

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

    private String getMatch_ref() {
        return  sharedPreferencesCompetition.getString(MATCH_REF, null);
    }
}