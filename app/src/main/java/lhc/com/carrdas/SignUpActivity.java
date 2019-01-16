package lhc.com.carrdas;

import android.app.AlertDialog;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import lhc.com.otherRessources.MySingletonRequestQueue;

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

        Button signUpButton = findViewById(R.id.SubmitSignUpButton);
        signUpButton.setOnClickListener(SignUpOnClickListener());
    }

    @NonNull
    private View.OnClickListener SignUpOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonPostSignUpRequest();

            }
        };
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

        RequestQueue requestQueue = MySingletonRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();
        final String mRequestBody = getJSONObject().toString();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL_BASE+URL_SIGN_UP,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("onResponse", "Sign Up API called : " + response.toString());
                        saveIsLoggedStatusUser();
                        saveCredentials();
                        goToMainActivity();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error", error.toString());
                        AlertDialog alertDialog = new AlertDialog.Builder(SignUpActivity.this).create();
                        alertDialog.setTitle("Alert");
                        alertDialog.setMessage(error.toString());
                        alertDialog.show();
                        error.printStackTrace();
                    }
                }
        ){
            @Override
            public String getBodyContentType () {
                return "application/json; charset=utf-8";
            }
            @Override
            public byte[] getBody () {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
            @Override
            public Map<String, String> getHeaders () {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
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
