package lhc.com.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import lhc.com.carrdas.R;

public class VoteAdapter_UserIsVoting extends ArrayAdapter<String> {

    private List<String> userVoteList;
    private Context mContext;

    public VoteAdapter_UserIsVoting(Context context, List<String> userVoteList) {
        super(context, R.layout.vote_user_cell, userVoteList);
        this.userVoteList = userVoteList;
        this.mContext=context;

    }


    //this will return the ListView Item as a View
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            view = layoutInflater.inflate(R.layout.vote_user_cell, null, false);
        }

        TextView numero_de_vote = view.findViewById(R.id.numero_de_vote);
        AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.user_vote_cell_auto_complete);
        //adding values to the list item
        numero_de_vote.setText(String.format("#%s", position+1));

        //finally returning the view
        return view;
    }


}