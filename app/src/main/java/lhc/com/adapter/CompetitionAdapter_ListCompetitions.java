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
import lhc.com.dtos.CompetitionDto;

public class CompetitionAdapter_ListCompetitions extends ArrayAdapter<CompetitionDto> {

    private List<CompetitionDto> competitionDtos;
    private Context mContext;

    public CompetitionAdapter_ListCompetitions(Context context, List<CompetitionDto> competitionDtos) {
        super(context, R.layout.cell_competition, competitionDtos);
        this.competitionDtos = competitionDtos;
        this.mContext=context;

    }

    //this will return the ListView Item as a View
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //we need to get the view of the xml for our list item
        //And for this we need a layoutinflater
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        //getting the view
        View view = layoutInflater.inflate(R.layout.cell_competition, null, false);

        //getting the view elements of the list from the view
        TextView name = view.findViewById(R.id.name_competition_cell);
        TextView division = view.findViewById(R.id.division_competition_cell);
        TextView season = view.findViewById(R.id.season_competition_cell);


        //getting the hero of the specified position

        CompetitionDto competitionDto = competitionDtos.get(position);

        //adding values to the list item
        name.setText(competitionDto.getName());
        division.setText(competitionDto.getDivision());
        season.setText(competitionDto.getSeason());

        //finally returning the view
        return view;
    }

}

