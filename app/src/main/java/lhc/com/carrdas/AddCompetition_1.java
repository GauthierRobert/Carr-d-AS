package lhc.com.carrdas;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import lhc.com.otherRessources.LabelType;

public class AddCompetition_1 extends AppCompatActivity {

    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_competition_1);

        nextButton = findViewById(R.id.next_competition_1);
        nextButton.setOnClickListener(next());
    }

    private View.OnClickListener next() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAddCompetition_2();
            }
        };
    }

    private void goToAddCompetition_2() {
        Intent intent = new Intent();
        intent.setClass(AddCompetition_1.this, AddCompetition_2.class);
        intent.putExtra("competition", getBodyMap().toString());
        startActivity(intent);
        finish();
    }


    @NonNull
    private JSONObject getBodyMap() {
        JSONObject body = new JSONObject();
        EditText username = findViewById(R.id.text_input_username_login);
        EditText password = findViewById(R.id.text_input_password_login);

        try {
            body.put("name", username.getText().toString());
            body.put("password", password.getText().toString());
            body.put("passwordConfirmed", password.getText().toString());
            body.put("season", password.getText().toString());
            body.put("division", password.getText().toString());
            body.put("usernameCreator", password.getText().toString());
            body.put("rules", getBodyRulesMap());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return body;
    }

    @NonNull
    private JSONArray getBodyRulesMap() throws JSONException {
        JSONArray body = new JSONArray();
        EditText ruleTop = findViewById(R.id.numberOfTopVoteAllowed_competition);
        EditText ruleflop = findViewById(R.id.numberOfFlopVoteAllowed_competition);
        int ruleTop_int = Integer.parseInt(ruleTop.getText().toString());
        int ruleFlop_int = Integer.parseInt(ruleflop.getText().toString());

        JSONObject ruleTopJSON = new JSONObject();
        ruleTopJSON.put("label", LabelType.NUMBER_VOTE_TOP.name());
        ruleTopJSON.put("points", ruleTop_int);

        JSONObject ruleFlopJSON = new JSONObject();
        ruleFlopJSON.put("label", LabelType.NUMBER_VOTE_FLOP.name());
        ruleFlopJSON.put("points", ruleFlop_int);

        body.put(ruleTopJSON);
        body.put(ruleFlopJSON);
        return body;
    }
}
