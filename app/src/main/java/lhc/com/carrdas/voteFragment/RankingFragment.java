package lhc.com.carrdas.voteFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import lhc.com.carrdas.R;
import lhc.com.otherRessources.ApplicationConstants;
import lhc.com.otherRessources.MySingletonRequestQueue;
import lhc.com.carrdas.viewerPage.RankingPageAdapter;

import static android.content.Context.MODE_PRIVATE;
import static lhc.com.otherRessources.ApplicationConstants.CLOSED;
import static lhc.com.otherRessources.ApplicationConstants.GOD;
import static lhc.com.otherRessources.ApplicationConstants.MATCH_CREATOR;
import static lhc.com.otherRessources.ApplicationConstants.MATCH_REF;
import static lhc.com.otherRessources.ApplicationConstants.MATCH_STATUS;
import static lhc.com.otherRessources.ApplicationConstants.MyPREFERENCES_COMPETITION;
import static lhc.com.otherRessources.ApplicationConstants.MyPREFERENCES_CREDENTIALS;
import static lhc.com.otherRessources.ApplicationConstants.OPEN;
import static lhc.com.otherRessources.ApplicationConstants.URL_MATCH_CLOSE;
import static lhc.com.otherRessources.ApplicationConstants.URL_MATCH_OPEN;
import static lhc.com.otherRessources.ApplicationConstants.USERNAME;
import static lhc.com.otherRessources.ApplicationConstants.createURL;


public class RankingFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private String usernameCreator;
    private String usernameConnected;
    private String status;
    private String match_ref;

    private SharedPreferences sharedPreferencesCompetition;
    private SharedPreferences sharedPreferencesCredentials;

    private Button onOpenButton;
    private Button onClosedButton;


    public RankingFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ranking, container, false);


        sharedPreferencesCompetition = getActivity().getSharedPreferences(MyPREFERENCES_COMPETITION, MODE_PRIVATE);
        sharedPreferencesCredentials = getActivity().getSharedPreferences(MyPREFERENCES_CREDENTIALS, MODE_PRIVATE);
        usernameCreator = sharedPreferencesCompetition.getString(MATCH_CREATOR, GOD);
        usernameConnected = sharedPreferencesCredentials.getString(USERNAME, null);
        status = sharedPreferencesCompetition.getString(MATCH_STATUS, OPEN);
        match_ref = sharedPreferencesCompetition.getString(MATCH_REF, null);

        onOpenButton = view.findViewById(R.id.button_on_open);
        onClosedButton = view.findViewById(R.id.button_on_close);

        if(!usernameConnected.equals(usernameCreator)){
            onOpenButton.setVisibility(View.GONE);
            onClosedButton.setVisibility(View.GONE);
        } else {
            if(OPEN.equals(status)){
                onOpenButton.setVisibility(View.GONE);
            } else if (CLOSED.equals(status)){
                onClosedButton.setVisibility(View.GONE);
            }
        }

        onOpenButton.setOnClickListener(openMatch());
        onClosedButton.setOnClickListener(closeMatch());

        RankingPageAdapter mSectionsPagerAdapter = new RankingPageAdapter(getActivity());
        ViewPager mViewPager = view.findViewById(R.id.container_ranking);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        return view;

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

    private View.OnClickListener closeMatch() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postCloseMatch();
            }
        };
    }

    private View.OnClickListener openMatch() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postOpenMatch();
            }
        };
    }

    private void postCloseMatch() {
        ApplicationConstants.Parameter parameter = new ApplicationConstants.Parameter(MATCH_REF, match_ref);
        final String url = createURL(URL_MATCH_CLOSE, parameter);
        RequestQueue requestQueue = MySingletonRequestQueue
                .getInstance(Objects.requireNonNull(getActivity()).getApplicationContext()).getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        onClosedButton.setVisibility(View.GONE);
                        onOpenButton.setVisibility(View.VISIBLE);
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

    private void postOpenMatch() {
        ApplicationConstants.Parameter parameter = new ApplicationConstants.Parameter(MATCH_REF, match_ref);
        final String url = createURL(URL_MATCH_OPEN, parameter);
        RequestQueue requestQueue = MySingletonRequestQueue
                .getInstance(Objects.requireNonNull(getActivity()).getApplicationContext()).getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        onClosedButton.setVisibility(View.VISIBLE);
                        onOpenButton.setVisibility(View.GONE);
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

}
