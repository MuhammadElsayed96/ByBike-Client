package com.muhammadelsayed.bybike.activity.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.muhammadelsayed.bybike.R;
import com.muhammadelsayed.bybike.activity.model.PlaceModel;

import java.util.List;

import static com.muhammadelsayed.bybike.activity.PlaceSearchActivity.searchFrom;
import static com.muhammadelsayed.bybike.activity.PlaceSearchActivity.searchTo;

public class ListViewCustomAdapter  extends ArrayAdapter<PlaceModel> {
    private List<PlaceModel> results;
    Context mContext;


    public ListViewCustomAdapter(List<PlaceModel> results, Context mContext) {
        super(mContext, R.layout.place_results_row, results);
        this.results = results;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull final ViewGroup parent) {

        final ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.place_results_row, null);

            viewHolder.row = convertView.findViewById(R.id.row);
            viewHolder.placeName = convertView.findViewById(R.id.place_name);
            viewHolder.placeAddress = convertView.findViewById(R.id.place_address);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        PlaceModel place = getItem(position);
        if (place != null) {
            viewHolder.placeName.setText(place.getPrimaryText());
            viewHolder.placeAddress.setText(place.getSecondaryText());
        }

        viewHolder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (searchFrom.hasFocus()) {
                    searchFrom.setQuery(viewHolder.placeName.getText().toString(), true);

                } else if (searchTo.hasFocus()) {
                    searchTo.setQuery(viewHolder.placeName.getText().toString(), true);
                }
            }
        });

        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        RelativeLayout row;
        TextView placeName;
        TextView placeAddress;
    }
}
