package lhc.com.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import lhc.com.carrdas.R;

public class VoteAdapter_VoteFragment extends ArrayAdapter<String> {

    private List<String> userVoteList;
    private Context mContext;
    private String[] users;

    public VoteAdapter_VoteFragment(Context context, List<String> userVoteList, String[] users) {
        super(context, R.layout.cell_vote_user, userVoteList);
        this.userVoteList = userVoteList;
        this.mContext=context;
        this.users = users;
    }


    //this will return the ListView Item as a View
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            view = layoutInflater.inflate(R.layout.cell_vote_user, null, false);
        }

        TextView numero_de_vote = view.findViewById(R.id.numero_de_vote);
        Spinner spinner = view.findViewById(R.id.user_vote_cell_auto_complete);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, R.layout.cell_vote_fragment_spinner, R.id.auto_complete_text,  users);
        spinner.setAdapter(adapter);

        //adding values to the list item
        numero_de_vote.setText(String.format("#%s", position+1));

        //finally returning the view
        return view;
    }


}