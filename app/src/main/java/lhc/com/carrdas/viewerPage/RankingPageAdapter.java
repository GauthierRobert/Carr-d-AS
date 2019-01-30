package lhc.com.carrdas.viewerPage;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
import lhc.com.otherRessources.MySingletonRequestQueue;

import static android.content.Context.MODE_PRIVATE;
import static lhc.com.otherRessources.ApplicationConstants.FLOP;
import static lhc.com.otherRessources.ApplicationConstants.MATCH_REF;
import static lhc.com.otherRessources.ApplicationConstants.MyPREFERENCES_COMPETITION;
import static lhc.com.otherRessources.ApplicationConstants.TOP;
import static lhc.com.otherRessources.ApplicationConstants.URL_RANKING_FLOP;
import static lhc.com.otherRessources.ApplicationConstants.URL_RANKING_TOP;
import static lhc.com.otherRessources.ApplicationConstants.createURL;

public class RankingPageAdapter extends PagerAdapter {

    private Context mContext;
    private SharedPreferences sharedPreferencesCompetition;
    List<RankingCell> rankingCellsTop;
    List<RankingCell> rankingCellsFlop;

    private ListView rankingListView;

    public RankingPageAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.tabbed_fragment_ranking, collection, false);
        rankingListView = layout.findViewById(R.id.listViewRanking);
        sharedPreferencesCompetition = mContext.getSharedPreferences(MyPREFERENCES_COMPETITION, MODE_PRIVATE);

        if (position == 0) {
            getRanking(URL_RANKING_TOP);
        } else if (position == 1) {
            getRanking(URL_RANKING_FLOP);
        }

        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return numberPage();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    private int numberPage() {
        return 2;
    }


    private void getRanking(final String url_topflop) {
        ApplicationConstants.Parameter parameter = new ApplicationConstants.Parameter(MATCH_REF, getMatch_ref());
        final String url = createURL(url_topflop, parameter);
        RequestQueue requestQueue = MySingletonRequestQueue
                .getInstance(mContext).getRequestQueue();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                Request.Method.GET, url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONObject json_ranking_cell;
                        rankingCellsTop = new ArrayList<>();
                        rankingCellsFlop = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                json_ranking_cell = response.getJSONObject(i);
                                ObjectMapper mapper = new ObjectMapper();
                                RankingCell rankingCell = mapper.readValue(json_ranking_cell.toString(), RankingCell.class);
                                if (URL_RANKING_TOP.equals(url_topflop)) {
                                    rankingCellsTop.add(rankingCell);
                                } else if (URL_RANKING_FLOP.equals(url_topflop)) {
                                    rankingCellsFlop.add(rankingCell);
                                }
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }
                            if (URL_RANKING_TOP.equals(url_topflop)) {
                                RankingAdapter_TabbedRanking rankingAdapter = new RankingAdapter_TabbedRanking(mContext, rankingCellsTop, TOP);
                                rankingListView.setAdapter(rankingAdapter);
                            } else if (URL_RANKING_FLOP.equals(url_topflop)) {
                                RankingAdapter_TabbedRanking rankingAdapter = new RankingAdapter_TabbedRanking(mContext, rankingCellsFlop, FLOP);
                                rankingListView.setAdapter(rankingAdapter);
                            }
                        }
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
        return sharedPreferencesCompetition.getString(MATCH_REF, null);
    }


}