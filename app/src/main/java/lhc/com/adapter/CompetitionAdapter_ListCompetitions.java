package lhc.com.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
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
        View view = layoutInflater.inflate(R.layout.cell_competition_cardview, null, false);

        //getting the view elements of the list from the view
        TextView name = view.findViewById(R.id.name_competition_cell);
        TextView division = view.findViewById(R.id.division_competition_cell);
        TextView season = view.findViewById(R.id.season_competition_cell);
        ImageView image = view.findViewById(R.id.image_competition);
        ImageView toggleImage = view.findViewById(R.id.imageview);



        //getting the hero of the specified position

        CompetitionDto competitionDto = competitionDtos.get(position);




        //adding values to the list item
        name.setText(competitionDto.getDetails().getName());
        division.setText(competitionDto.getDetails().getDivision());
        season.setText(competitionDto.getDetails().getSeason());
        toggleImage.setOnClickListener(getMenuForToggle());


        String encodedImage = competitionDto.getImageAsBase64();
        if(encodedImage !=null) {
            byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            image.setImageBitmap(decodedByte);
        }
        return view;
    }

    private View.OnClickListener getMenuForToggle() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.imageview:
                        PopupMenu popup = new PopupMenu(mContext.getApplicationContext(), view);
                        popup.setOnMenuItemClickListener(getOnMenuItemClickListener());
                        popup.getMenuInflater().inflate(R.menu.clipboard_popup_competition,popup.getMenu());
                        popup.show();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private PopupMenu.OnMenuItemClickListener getOnMenuItemClickListener() {
        return new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit:
                        break;
                    case R.id.delete:
                        break;
                    default:
                        break;
                }
                return true;
            }
        };
    }
}

