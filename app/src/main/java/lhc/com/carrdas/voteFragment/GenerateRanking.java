package lhc.com.carrdas.voteFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

import lhc.com.carrdas.R;
import lhc.com.otherRessources.ApplicationConstants;
import lhc.com.otherRessources.MySingletonRequestQueue;

import static lhc.com.otherRessources.ApplicationConstants.COMPETITION_REF;
import static lhc.com.otherRessources.ApplicationConstants.JSON_LIST_VOTES;
import static lhc.com.otherRessources.ApplicationConstants.MATCH_REF;
import static lhc.com.otherRessources.ApplicationConstants.URL_BALLOT_GET;
import static lhc.com.otherRessources.ApplicationConstants.URL_MATCH_CLOSE;
import static lhc.com.otherRessources.ApplicationConstants.createURL;


public class GenerateRanking extends Fragment {

    private Button buttonGenerateRanking;
    private OnFragmentInteractionListener mListener;
    private SharedPreferences sharedPreferencesCompetition;

    public GenerateRanking() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View inflate = inflater.inflate(R.layout.fragment_generate_ranking, container, false);
        buttonGenerateRanking = inflate.findViewById(R.id.button_generate_ranking);
        buttonGenerateRanking.setOnClickListener(closeMatch());
        return inflate;
    }

    private View.OnClickListener closeMatch() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostCloseMatch();
            }
        };
    }

    private void PostCloseMatch() {
        ApplicationConstants.Parameter parameter = new ApplicationConstants.Parameter(COMPETITION_REF, getMatch_ref());
        final String url = createURL(URL_MATCH_CLOSE, parameter);
        RequestQueue requestQueue = MySingletonRequestQueue
                .getInstance(Objects.requireNonNull(getActivity()).getApplicationContext()).getRequestQueue();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                Request.Method.GET, url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                    }
                }
                ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error", error.toString());
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }


    private String getMatch_ref() {
        return  sharedPreferencesCompetition.getString(MATCH_REF, null);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}