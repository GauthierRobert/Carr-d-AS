package lhc.com.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import lhc.com.adapter.ObjectForAdapter.HasVotedPlayerCell;
import lhc.com.carrdas.R;

public class HasVotedAdapter_InfoMatchActivity extends ArrayAdapter<HasVotedPlayerCell> {

    private List<HasVotedPlayerCell> HasVotedPlayerCells;
    private Context mContext;

    public HasVotedAdapter_InfoMatchActivity(Context context, List<HasVotedPlayerCell> HasVotedPlayerCells) {
        super(context, R.layout.cell_match, HasVotedPlayerCells);
        this.HasVotedPlayerCells = HasVotedPlayerCells;
        this.mContext = context;
    }

    //this will return the ListView Item as a View
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        final View view;
        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.cell_has_voted, parent, false);
        } else {
            view = convertView;
        }

        HasVotedPlayerCell HasVotedPlayerCell = HasVotedPlayerCells.get(position);

        String hasVotedPlayerCellPlayer = HasVotedPlayerCell.getPlayer();
        boolean hasVotedPlayerCellHasVoted = HasVotedPlayerCell.isHasVoted();

        TextView player = view.findViewById(R.id.player_has_voted);
        ImageView check = view.findViewById(R.id.image_view_has_voted);

        player.setText(hasVotedPlayerCellPlayer);
        if(!hasVotedPlayerCellHasVoted) {
            check.setVisibility(View.INVISIBLE);
        } else {
            check.setVisibility(View.VISIBLE);
        }

        return view;

    }

}

