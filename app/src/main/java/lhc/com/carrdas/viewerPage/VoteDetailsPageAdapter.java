package lhc.com.carrdas.viewerPage;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lhc.com.carrdas.R;
import lhc.com.dtos.BallotDto;
import lhc.com.dtos.RuleDto;
import lhc.com.dtos.VoteDto;

import static android.content.Context.MODE_PRIVATE;
import static lhc.com.otherRessources.ApplicationConstants.FLOP;
import static lhc.com.otherRessources.ApplicationConstants.MyPREFERENCES_COMPETITION;
import static lhc.com.otherRessources.ApplicationConstants.NUMBER_VOTE_FLOP;
import static lhc.com.otherRessources.ApplicationConstants.NUMBER_VOTE_TOP;
import static lhc.com.otherRessources.ApplicationConstants.RULES;
import static lhc.com.otherRessources.ApplicationConstants.TOP;
import static lhc.com.otherRessources.ApplicationConstants.WITH_COMMENTS;

public class VoteDetailsPageAdapter extends PagerAdapter {

    private Context mContext;
    private String jsonArrayString;

    int numberTop;
    int numberFlop;
    boolean withComment;

    public VoteDetailsPageAdapter(Context context, String jsonArrayString) {
        this.mContext = context;
        this.jsonArrayString = jsonArrayString;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.tabbed_fragment_detail_page, collection, false);
        SharedPreferences sharedPreferencesCompetition = mContext.getSharedPreferences(MyPREFERENCES_COMPETITION, MODE_PRIVATE);

        numberTop = sharedPreferencesCompetition.getInt(NUMBER_VOTE_TOP, 0);
        numberFlop = sharedPreferencesCompetition.getInt(NUMBER_VOTE_FLOP, 0);
        withComment = sharedPreferencesCompetition.getBoolean(WITH_COMMENTS, false);

        if(numberTop == 0) {
            LinearLayout layout_top = layout.findViewById(R.id.layout_top_vote_vote_details);
            layout_top.setVisibility(View.GONE);
        }
        if(numberFlop == 0) {
            LinearLayout layout_flop = layout.findViewById(R.id.layout_flop_vote_vote_details);
            layout_flop.setVisibility(View.GONE);
        }
        if(!withComment) {
            LinearLayout layout_comment = layout.findViewById(R.id.layout_comments_vote_details);
            layout_comment.setVisibility(View.GONE);
        }


        TextView overviewOfTopVotes = layout.findViewById(R.id.overviewOfTopVotes_vote_details);
        TextView overviewOfFlopVotes = layout.findViewById(R.id.overviewOfFlopVotes_vote_details);
        TextView comments_vote_details = layout.findViewById(R.id.comments_vote_details);
        BallotDto ballotDto = null;

        try {
            ObjectMapper mapper = new ObjectMapper();
            String ballotString = getVoteFrom(position).toString();
            ballotDto = mapper.readValue(ballotString, BallotDto.class);

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        overviewOfTopVotes.setText(generateOverView(ballotDto,TOP));
        overviewOfFlopVotes.setText(generateOverView(ballotDto,FLOP));
        comments_vote_details.setText(ballotDto.getComment());
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

    private JSONObject getVoteFrom(int i) throws JSONException {
        JSONArray jsonArray = new JSONArray(jsonArrayString);
        return jsonArray.getJSONObject(i);
    }

    private String generateOverView(BallotDto ballotDto, String constantVote) {
        String[] strings = null;
        if (ballotDto !=null) {
            if (ballotDto.getVoteDtos() != null) {
                strings = new String[ballotDto.getVoteDtos().size()];
                for (VoteDto voteDto : ballotDto.getVoteDtos()) {
                    int indication = voteDto.getIndication();
                    if(TOP.equals(constantVote)) {
                        if (indication > 0) {
                            strings[indication-1] = voteDto.getName();
                        }
                    } else if (FLOP.equals(constantVote)){
                        if (indication < 0) {
                            strings[(-(1+indication))] = voteDto.getName();
                        }
                    }
                }
            }
        }

        return createOverviewWithArray(strings);
    }

    private String createOverviewWithArray(String[] flopVoteString) {
        if (flopVoteString !=null) {
            if (flopVoteString.length != 0) {
                String overview = "#1 : " + flopVoteString[0];
                String nl = System.getProperty("line.separator");

                for (int i = 2; i <= flopVoteString.length; i++) {
                    if (flopVoteString[i - 1] !=null)
                        overview = overview + nl + "#" + i + " : " + flopVoteString[i - 1];
                }
                return overview;
            }
        }
        return null;
    }

}