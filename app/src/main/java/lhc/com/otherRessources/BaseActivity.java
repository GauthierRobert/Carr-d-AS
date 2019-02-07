package lhc.com.otherRessources;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import lhc.com.carrdas.AddCompetition;
import lhc.com.carrdas.ChangePasswordActivity;
import lhc.com.carrdas.ListCompetitions;
import lhc.com.carrdas.LoginActivity;
import lhc.com.carrdas.R;

import static lhc.com.otherRessources.ApplicationConstants.GOD;
import static lhc.com.otherRessources.ApplicationConstants.MyPREFERENCES_CREDENTIALS;
import static lhc.com.otherRessources.ApplicationConstants.USERNAME;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    SharedPreferences sharedPreferencesCredentials;
    DrawerLayout mDrawerLayout;

    protected void onCreateDrawer() {
        sharedPreferencesCredentials = getSharedPreferences(MyPREFERENCES_CREDENTIALS, MODE_PRIVATE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mDrawerLayout = findViewById(R.id.drawer_layout);



        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView usernameHeader = headerView.findViewById(R.id.username_header);
        usernameHeader.setText(sharedPreferencesCredentials.getString(USERNAME, GOD));
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {

        DrawerLayout fullView = (DrawerLayout) getLayoutInflater().inflate(R.layout.drawer, null);
        FrameLayout activityContainer = fullView.findViewById(R.id.content_frame);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(fullView);

        onCreateDrawer();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // Handle the camera action
        } else if (id == R.id.nav_change_password) {
            goToChangePasswordActivity();
        } else if (id == R.id.nav_log_out) {
            logOut();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void goToChangePasswordActivity() {

            Intent intent = new Intent();
            intent.setClass(BaseActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
            finish();

    }

    private void logOut() {
        SharedPreferences.Editor editor = sharedPreferencesCredentials.edit();
        editor.clear().apply();
        finish();
        Intent intent = new Intent(BaseActivity.this,
                LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
