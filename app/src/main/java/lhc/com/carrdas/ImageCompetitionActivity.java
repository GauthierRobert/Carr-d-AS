package lhc.com.carrdas;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import lhc.com.carrdas.imageFragment.GalleryFragment;
import lhc.com.carrdas.imageFragment.TakePictureFragment;
import lhc.com.carrdas.voteFragment.RankingFragment;
import lhc.com.carrdas.voteFragment.VoteFragment;
import lhc.com.otherRessources.BaseActivity;

import static lhc.com.otherRessources.ApplicationConstants.JSON_LIST_VOTES_BUNDLE;
import static lhc.com.otherRessources.ApplicationConstants.JSON_LIST_VOTES_INTENT;

public class ImageCompetitionActivity extends BaseActivity implements
        TakePictureFragment.OnFragmentInteractionListener,
        GalleryFragment.OnFragmentInteractionListener {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_camera:
                    //loadFragment(new TakePictureFragment());
                    return true;
                case R.id.navigation_gallery:
                    loadFragment(new GalleryFragment());
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_competition);

        loadFragment(new TakePictureFragment());

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    private boolean loadFragment(Fragment fragment) {

        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container_image, fragment)
                    .commit();
            return true;
        }
        return false;
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
