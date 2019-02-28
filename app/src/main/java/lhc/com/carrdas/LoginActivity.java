package lhc.com.carrdas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import lhc.com.volley.MySingletonRequestQueue;

import static lhc.com.otherRessources.ApplicationConstants.IS_REMEMBER_ME;
import static lhc.com.otherRessources.ApplicationConstants.IS_USER_LOGGED_IN;
import static lhc.com.otherRessources.ApplicationConstants.MyPREFERENCES_CREDENTIALS;
import static lhc.com.otherRessources.ApplicationConstants.PASSWORD;
import static lhc.com.otherRessources.ApplicationConstants.URL_BASE;
import static lhc.com.otherRessources.ApplicationConstants.URL_LOGIN;
import static lhc.com.otherRessources.ApplicationConstants.USERNAME;
import static lhc.com.volley.StringRequestPost.stringRequestPost;

/**
 * A login screen that offers login via username/password.
 */
public class LoginActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    EditText username;
    EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedpreferences = getSharedPreferences(MyPREFERENCES_CREDENTIALS, Context.MODE_PRIVATE);

        username = findViewById(R.id.text_input_username_login);
        password = findViewById(R.id.text_input_password_login);

        boolean isUserLoggedIn = sharedpreferences.getBoolean(IS_USER_LOGGED_IN, false);
        boolean isRememberMe = sharedpreferences.getBoolean(IS_REMEMBER_ME, false);

        if (isUserLoggedIn){
            goToMainActivity();
        }

        if(isRememberMe){
            rememberMe();
        }

        settingsOfButtonListener();
    }

    private void settingsOfButtonListener() {
        Button signUpButton = findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(SignUpOnClickListener());

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(loginOnClickListener());
    }

    private void rememberMe() {
        String username_pref = sharedpreferences.getString(USERNAME, null);
        username.setText(username_pref);
        String password_pref = sharedpreferences.getString(USERNAME, null);
        password.setText(password_pref);
    }


    @NonNull
    private View.OnClickListener loginOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonPostLoginRequest();
            }
        };
    }

    private void rememberMeCheckBox() {
        CheckBox rememberMeCheckBox = findViewById(R.id.remember_me_login);
        boolean rememberMe = rememberMeCheckBox.isChecked();

        if (rememberMe){
            saveIsRememberMe(true);
            saveIsLoggedStatusUser();
        } else {
            saveIsRememberMe(false);
        }
    }

    private void saveCredentials() {
        Editor editor = sharedpreferences.edit();
        editor.putString(USERNAME, username.getText().toString());
        editor.putString(PASSWORD, password.getText().toString());
        editor.apply();
    }

    private void saveIsLoggedStatusUser() {
        sharedpreferences.edit().putBoolean(IS_USER_LOGGED_IN, true).apply();
    }

    private void saveIsRememberMe(boolean value) {
        sharedpreferences.edit().putBoolean(IS_REMEMBER_ME, value).apply();
    }

    private void goToMainActivity() {
        Intent signupSuccessHome = new Intent(this, ListCompetitions.class);
        startActivity(signupSuccessHome);
        finish();
    }

    private void JsonPostLoginRequest() {
        final Context mContext = LoginActivity.this;
        RequestQueue requestQueue = MySingletonRequestQueue.getInstance(mContext.getApplicationContext()).getRequestQueue();
        final String mRequestBody = new JSONObject(getParamsMap()).toString();
        StringRequest stringRequest = stringRequestPost(
                URL_BASE + URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (Boolean.parseBoolean(response)){
                            rememberMeCheckBox();
                            saveCredentials();
                            goToMainActivity();
                        } else {
                            EditText password = findViewById(R.id.text_input_password_login);
                            password.setError("The username or password is incorrect. Try again.");
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
        EditText username = findViewById(R.id.text_input_username_login);
        EditText password = findViewById(R.id.text_input_password_login);

        params.put("username", username.getText().toString());
        params.put("password", password.getText().toString());
        return params;
    }

    @NonNull
    private View.OnClickListener SignUpOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity();
            }
        };
    }

    private void launchActivity() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        sharedpreferences = getSharedPreferences(MyPREFERENCES_CREDENTIALS, Context.MODE_PRIVATE);
        boolean isUserLoggedIn = sharedpreferences.getBoolean(IS_USER_LOGGED_IN, false);

        if (isUserLoggedIn){
            goToMainActivity();
        }
        super.onRestart();
        super.onResume();
    }

    @Override
    protected void onRestart() {
        sharedpreferences = getSharedPreferences(MyPREFERENCES_CREDENTIALS, Context.MODE_PRIVATE);
        boolean isUserLoggedIn = sharedpreferences.getBoolean(IS_USER_LOGGED_IN, false);

        if (isUserLoggedIn){
            goToMainActivity();
        }
        super.onRestart();
    }

}

