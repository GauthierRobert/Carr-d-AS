package lhc.com.viewerPage;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import lhc.com.carrdas.R;

public class VoteDetailsPageAdapter extends PagerAdapter {

    private Context mContext;
    private String jsonArrayString;

    public VoteDetailsPageAdapter(Context context, String jsonArrayString) {
        this.mContext = context;
        this.jsonArrayString = jsonArrayString;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.tabbed_fragment_detail_page, collection, false);
        TextView textView = layout.findViewById(R.id.tempText);
        try {
            textView.setText(getVoteFrom(position));
        } catch (JSONException e) {
            e.printStackTrace();
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

    private int numberPage(){
        if (jsonArrayString!=null){
            try {
                JSONArray jsonArray = new JSONArray(jsonArrayString);
                return jsonArray.length();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return 0 ;
    }

    private String getVoteFrom(int i) throws JSONException {

        JSONArray jsonArray = new JSONArray(jsonArrayString);
        return jsonArray.get(i).toString();

    }
}