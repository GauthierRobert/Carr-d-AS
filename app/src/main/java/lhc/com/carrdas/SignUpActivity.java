package lhc.com.carrdas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import lhc.com.volley.JsonObjectRequestPost;
import lhc.com.volley.MySingletonRequestQueue;

import static lhc.com.volley.JsonObjectRequestPost.jsonObjectRequestPost;
import static lhc.com.otherRessources.ApplicationConstants.IS_USER_LOGGED_IN;
import static lhc.com.otherRessources.ApplicationConstants.MyPREFERENCES_CREDENTIALS;
import static lhc.com.otherRessources.ApplicationConstants.PASSWORD;
import static lhc.com.otherRessources.ApplicationConstants.URL_BASE;
import static lhc.com.otherRessources.ApplicationConstants.URL_SIGN_UP;
import static lhc.com.otherRessources.ApplicationConstants.USERNAME;

public class SignUpActivity extends AppCompatActivity {


    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        settingsOfButtonListener();
    }

    private void settingsOfButtonListener() {
        Button signUpButton = findViewById(R.id.SubmitSignUpButton);
        signUpButton.setOnClickListener(SignUpOnClickListener());
    }

    @NonNull
    private View.OnClickListener SignUpOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean error = settingsOfErrors();
                if(!error)
                    JsonPostSignUpRequest();

            }
        };
    }
    private boolean settingsOfErrors(){
        EditText username = findViewById(R.id.text_input_username_signUp);
        EditText password = findViewById(R.id.text_input_password_signUp);
        EditText confirmedPassword = findViewById(R.id.text_input_confirmedPassword_signUp);

        if (username.getText().toString().matches("")){
            username.setError("The item username cannot be empty");
            return true;
        }
        if(password.getText().toString().matches("")){
            password.setError("The item password cannot be empty");
            return true;
        }
        if(confirmedPassword.getText().toString().matches("")){
            confirmedPassword.setError("The item confirmed password cannot be empty");
            return true;
        }
        if(!confirmedPassword.getText().toString().equals(password.getText().toString())) {
            password.setError("Password and confirmed password must match");
            confirmedPassword.setError("Password and confirmed password must match");
            return true;
        }
        return false;
    }

    private void saveIsLoggedStatusUser() {
        sharedpreferences = getSharedPreferences(MyPREFERENCES_CREDENTIALS, Context.MODE_PRIVATE);
        sharedpreferences.edit().putBoolean(IS_USER_LOGGED_IN, true).apply();
    }

    private void saveCredentials() {
        EditText username = findViewById(R.id.text_input_username_signUp);
        EditText password = findViewById(R.id.text_input_password_signUp);

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(USERNAME, username.getText().toString());
        editor.putString(PASSWORD, password.getText().toString());
        editor.apply();
    }

    private void goToMainActivity() {
        Intent signupSuccessHome = new Intent(this, ListCompetitions.class);
        startActivity(signupSuccessHome);
        finish();
    }

    private void JsonPostSignUpRequest() {

        Context mContext = SignUpActivity.this;
        RequestQueue requestQueue = MySingletonRequestQueue.getInstance(mContext.getApplicationContext()).getRequestQueue();
        final String mRequestBody = getJSONObject().toString();
        JsonObjectRequestPost jsonObjectRequest = jsonObjectRequestPost(
                URL_BASE+URL_SIGN_UP,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("onResponse", "Sign Up API called : " + response.toString());
                        saveIsLoggedStatusUser();
                        saveCredentials();
                        goToMainActivity();
                    }
                },
                mRequestBody,
                mContext
        );
        requestQueue.add(jsonObjectRequest);
    }

    @NonNull
    private Map<String, String> getParamsMap() {
        Map<String, String> params = new HashMap<>();
        EditText username = findViewById(R.id.text_input_username_signUp);
        EditText password = findViewById(R.id.text_input_password_signUp);
        EditText passwordConfirm = findViewById(R.id.text_input_confirmedPassword_signUp);

        params.put("username", username.getText().toString());
        params.put("password", password.getText().toString());
        params.put("passwordConfirm", passwordConfirm.getText().toString());
        return params;
    }

    private JSONObject getJSONObject(){
        return new JSONObject(getParamsMap());
    }

}
