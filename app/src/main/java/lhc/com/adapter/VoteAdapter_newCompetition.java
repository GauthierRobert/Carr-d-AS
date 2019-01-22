package lhc.com.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import java.util.List;

import lhc.com.carrdas.R;

public class VoteAdapter_newCompetition extends ArrayAdapter<String> {

    private List<String> numberVoteList;
    private Context mContext;

    public VoteAdapter_newCompetition(Context context, List<String> numberVoteList) {
        super(context, R.layout.cell_vote, numberVoteList);
        this.numberVoteList = numberVoteList;
        this.mContext=context;

    }

    //this will return the ListView Item as a View
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            view = layoutInflater.inflate(R.layout.cell_vote, null, false);
        }
        //getting the view elements of the list from the view
        EditText pointEditText = view.findViewById(R.id.point_vote_cell);

        String numberVote = numberVoteList.get(position);

        //adding values to the list item
        pointEditText.setHint(numberVote);

        //finally returning the view
        return view;
    }

}

