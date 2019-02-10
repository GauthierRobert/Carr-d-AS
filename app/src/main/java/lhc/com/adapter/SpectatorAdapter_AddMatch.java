package lhc.com.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import lhc.com.carrdas.R;

public class SpectatorAdapter_AddMatch extends ArrayAdapter<String> {

    private List<String> list;
    private Context mContext;

    public SpectatorAdapter_AddMatch(Context context, List<String> list) {
        super(context, R.layout.cell_match, list);
        this.list = list;
        this.mContext = context;
    }

    //this will return the ListView Item as a View
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        final View result;
        if (convertView == null) {
            result = layoutInflater.inflate(R.layout.cell_spectator, parent, false);
        } else {
            result = convertView;
        }

        String spectator = list.get(position);

        TextView spectatorText = result.findViewById(R.id.spectator_name);
        spectatorText.setText(spectator);

        return result;

    }

}

