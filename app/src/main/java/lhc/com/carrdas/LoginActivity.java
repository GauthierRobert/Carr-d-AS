package lhc.com.carrdas;

import android.app.AlertDialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lhc.com.volley.MySingletonRequestQueue;

import static lhc.com.otherRessources.ApplicationConstants.IS_REMEMBER_ME;
import static lhc.com.otherRessources.ApplicationConstants.IS_USER_LOGGED_IN;
import static lhc.com.otherRessources.ApplicationConstants.MyPREFERENCES_CREDENTIALS;
import static lhc.com.otherRessources.ApplicationConstants.PASSWORD;
import static lhc.com.otherRessources.ApplicationConstants.URL_BASE;
import static lhc.com.otherRessources.ApplicationConstants.URL_LOGIN;
import static lhc.com.otherRessources.ApplicationConstants.USERNAME;

/**
 * A login screen that offers login via username/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

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
        final Context mContext = this.getApplicationContext();
        RequestQueue requestQueue = MySingletonRequestQueue.getInstance(mContext).getRequestQueue();
        final String mRequestBody = new JSONObject(getParamsMap()).toString();
        StringRequest jsonObjectRequest = new StringRequest(
                 Request.Method.POST,
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
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error", error.toString());
                        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                        alertDialog.setTitle("Alert");
                        alertDialog.setMessage(error.toString());
                        alertDialog.show();
                        error.printStackTrace();
                    }
                }
        ){

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public byte[] getBody() {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
            @Override
            public Map<String, String> getHeaders() {
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
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
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

