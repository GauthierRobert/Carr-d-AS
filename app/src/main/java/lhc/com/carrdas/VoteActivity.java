package lhc.com.carrdas;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import lhc.com.carrdas.voteFragment.Ranking;
import lhc.com.carrdas.voteFragment.UserIsVoting;
import lhc.com.carrdas.voteFragment.VoteDetails;
import lhc.com.otherRessources.BaseActivity;

public class VoteActivity extends BaseActivity implements
        UserIsVoting.OnFragmentInteractionListener,
        Ranking.OnFragmentInteractionListener,
        VoteDetails.OnFragmentInteractionListener {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_user_is_voting:
                    loadFragment(new UserIsVoting());
                    return true;
                case R.id.navigation_ranking:
                    loadFragment(new Ranking());
                    return true;
                case R.id.navigation_vote_details:
                    loadFragment(new VoteDetails());
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        loadFragment(new UserIsVoting());
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
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
}