package lhc.com.carrdas.viewerPage.FragmentRankingForPageAdapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lhc.com.adapter.ObjectForAdapter.RankingCell;
import lhc.com.adapter.RankingAdapter_TabbedRanking;
import lhc.com.carrdas.R;
import lhc.com.otherRessources.ApplicationConstants;
import lhc.com.volley.JsonArrayRequestGet;
import lhc.com.volley.MySingletonRequestQueue;

import static android.content.Context.MODE_PRIVATE;
import static lhc.com.otherRessources.ApplicationConstants.MATCH_REF;
import static lhc.com.otherRessources.ApplicationConstants.MyPREFERENCES_COMPETITION;
import static lhc.com.otherRessources.ApplicationConstants.TOP;
import static lhc.com.otherRessources.ApplicationConstants.URL_RANKING_TOP;
import static lhc.com.otherRessources.ApplicationConstants.createURL;


public class FragmentTopRanking extends Fragment {

    private ListView rankingListView;
    private SharedPreferences sharedPreferencesCompetition;
    private Context mContext;
    private OnFragmentInteractionListener mListener;

    public FragmentTopRanking() {
        // Required empty public constructor
    }

    public static FragmentTopRanking newInstance() {

        FragmentTopRanking topRanking = new FragmentTopRanking();
        Bundle b = new Bundle();
        topRanking.setArguments(b);

        return topRanking;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tabbed_fragment_ranking, container, false);

        TextView title = view.findViewById(R.id.title_ranking);
        title.setText("TOP RANKING");
        mContext = getActivity();
        rankingListView = view.findViewById(R.id.listViewRanking);
        sharedPreferencesCompetition = mContext.getSharedPreferences(MyPREFERENCES_COMPETITION, MODE_PRIVATE);
        getRanking(URL_RANKING_TOP);

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


    private void getRanking(final String url_topflop) {
        ApplicationConstants.Parameter parameter = new ApplicationConstants.Parameter(MATCH_REF, getMatch_ref());
        final String url = createURL(url_topflop, parameter);
        RequestQueue requestQueue = MySingletonRequestQueue
                .getInstance(mContext).getRequestQueue();
        JsonArrayRequestGet jsonObjectRequest = JsonArrayRequestGet.jsonArrayRequestGet(
                url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<RankingCell> rankingCells = getRankingCells(response);
                        RankingAdapter_TabbedRanking rankingAdapter = new RankingAdapter_TabbedRanking(mContext, rankingCells, TOP);
                        rankingListView.setAdapter(rankingAdapter);
                    }
                }, mContext
        );
        requestQueue.add(jsonObjectRequest);
    }

    private List<RankingCell> getRankingCells(JSONArray response) {
        List<RankingCell> rankingCells = new ArrayList<>();
        JSONObject json_ranking_cell;
        for (int i = 0; i < response.length(); i++) {
            try {

                json_ranking_cell = response.getJSONObject(i);

                ObjectMapper mapper = new ObjectMapper();
                RankingCell rankingCell = mapper.readValue(json_ranking_cell.toString(), RankingCell.class);

                rankingCells.add(rankingCell);
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }
        return rankingCells;
    }


    private String getMatch_ref() {
        return sharedPreferencesCompetition.getString(MATCH_REF, null);
    }

}
