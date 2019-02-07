package lhc.com.volley;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class StringRequestPost extends StringRequest {

    private String mRequestBody;

    public static StringRequestPost stringRequestPost(String url, Response.Listener<String> listener, String mRequestBody, final Context mContext) {


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

        return new StringRequestPost(Method.POST, url, listener, errorListener, mRequestBody);
    }

    private StringRequestPost(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener, String mRequestBody) {
        super(method, url, listener, errorListener);
        this.mRequestBody = mRequestBody;
    }


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
}
