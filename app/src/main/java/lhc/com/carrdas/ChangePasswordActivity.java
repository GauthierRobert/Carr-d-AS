package lhc.com.carrdas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import lhc.com.otherRessources.ApplicationConstants;
import lhc.com.volley.MySingletonRequestQueue;

import static lhc.com.otherRessources.ApplicationConstants.COMPETITION_REF;
import static lhc.com.otherRessources.ApplicationConstants.MyPREFERENCES_CREDENTIALS;
import static lhc.com.otherRessources.ApplicationConstants.NEW_CONFIRMED_PASSWORD;
import static lhc.com.otherRessources.ApplicationConstants.NEW_PASSWORD;
import static lhc.com.otherRessources.ApplicationConstants.PASSWORD;
import static lhc.com.otherRessources.ApplicationConstants.URL_BASE;
import static lhc.com.otherRessources.ApplicationConstants.URL_CHANGE_PASSWORD;
import static lhc.com.otherRessources.ApplicationConstants.URL_LOGIN;
import static lhc.com.otherRessources.ApplicationConstants.URL_MATCH_GET;
import static lhc.com.otherRessources.ApplicationConstants.USERNAME;
import static lhc.com.otherRessources.ApplicationConstants.createURL;
import static lhc.com.volley.StringRequestPost.stringRequestPost;

public class ChangePasswordActivity extends AppCompatActivity {


    SharedPreferences sharedPreferencesCredentials;
    EditText oldPassword;
    EditText newPassword;
    EditText confirmedNewPassword;
    Button changepasswordSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        sharedPreferencesCredentials = getSharedPreferences(MyPREFERENCES_CREDENTIALS, MODE_PRIVATE);
        oldPassword = findViewById(R.id.text_input_old_password_changePassword);
        newPassword = findViewById(R.id.text_input_new_password_changePassword);
        confirmedNewPassword = findViewById(R.id.text_input_confirmed_new_password_changePassword);
        changepasswordSubmit = findViewById(R.id.submit_button_changePassword);

        changepasswordSubmit.setOnClickListener(submitOnClickListener());

    }

    private View.OnClickListener submitOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePasswordPostRequest();
            }
        };
    }


    private void changePasswordPostRequest() {
        final Context mContext = ChangePasswordActivity.this;

        ApplicationConstants.Parameter newPassword_param = new ApplicationConstants.Parameter(NEW_PASSWORD, newPassword.getText().toString());
        ApplicationConstants.Parameter confirmedNewPassword_param = new ApplicationConstants.Parameter(NEW_CONFIRMED_PASSWORD, confirmedNewPassword.getText().toString());
        final String url = createURL(URL_CHANGE_PASSWORD, newPassword_param, confirmedNewPassword_param);

        RequestQueue requestQueue = MySingletonRequestQueue.getInstance(mContext.getApplicationContext()).getRequestQueue();
        final String mRequestBody = new JSONObject(getParamsMap()).toString();
        StringRequest stringRequest = stringRequestPost(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (Boolean.parseBoolean(response)) {
                            goToMainActivity();
                        } else {
                            EditText password = findViewById(R.id.text_input_password_login);
                            password.setError("Old password is incorrect. Try again.");
                        }
                    }
                },
                mRequestBody,
                mContext);
        requestQueue.add(stringRequest);
    }

    @NonNull
    private Map<String, String> getParamsMap() {

        Map<String, String> params = new HashMap<>();

        params.put(USERNAME, sharedPreferencesCredentials.getString(USERNAME, null));
        params.put(PASSWORD, oldPassword.getText().toString());
        return params;
    }


    private void goToMainActivity() {

        Intent intent = new Intent();
        intent.setClass(ChangePasswordActivity.this, ListCompetitions.class);
        startActivity(intent);
        finish();

    }

}
