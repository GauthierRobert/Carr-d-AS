package lhc.com.carrdas;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import lhc.com.otherRessources.BaseActivity;

import static lhc.com.otherRessources.ApplicationConstants.MATCH_AWAY;
import static lhc.com.otherRessources.ApplicationConstants.MATCH_HOME;
import static lhc.com.otherRessources.ApplicationConstants.MATCH_REF;
import static lhc.com.otherRessources.ApplicationConstants.MATCH_SCORE_AWAY;
import static lhc.com.otherRessources.ApplicationConstants.MATCH_SCORE_HOME;
import static lhc.com.otherRessources.ApplicationConstants.MyPREFERENCES_COMPETITION;
import static lhc.com.otherRessources.ApplicationConstants.MyPREFERENCES_CREDENTIALS;

public class MatchActivity extends BaseActivity {
    SharedPreferences sharedPreferencesCompetition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        sharedPreferencesCompetition = getSharedPreferences(MyPREFERENCES_COMPETITION, MODE_PRIVATE);

        EditText home = findViewById(R.id.home);
        EditText away = findViewById(R.id.away);
        EditText homeScore = findViewById(R.id.home_score);
        EditText awayScore = findViewById(R.id.away_score);
        TextView reference = findViewById(R.id.reference);

        home.setText(sharedPreferencesCompetition.getString(MATCH_HOME, null));
        away.setText(sharedPreferencesCompetition.getString(MATCH_AWAY, null));
        homeScore.setText(sharedPreferencesCompetition.getInt(MATCH_SCORE_HOME, 0));
        awayScore.setText(sharedPreferencesCompetition.getInt(MATCH_SCORE_AWAY, 0));
        reference.setText(sharedPreferencesCompetition.getString(MATCH_REF, null));
    }
}
