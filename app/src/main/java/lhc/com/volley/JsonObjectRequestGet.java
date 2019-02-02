package lhc.com.volley;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JsonObjectRequestGet extends JsonObjectRequest {

    public static JsonObjectRequestGet jsonObjectRequestGet(String url, Response.Listener<JSONObject> listener, final Context mContext){

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.toString());
                AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage(error.toString());
                alertDialog.show();
                error.printStackTrace();
            }
        };

        return new JsonObjectRequestGet(Method.GET, url, listener, errorListener);
    }

    private JsonObjectRequestGet(int method, String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, null, listener, errorListener);
    }


    @Override
    public Map<String, String> getHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json; charset=utf-8");
        return headers;
    }
}
