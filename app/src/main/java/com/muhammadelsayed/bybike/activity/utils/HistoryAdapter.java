package com.muhammadelsayed.bybike.activity.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.muhammadelsayed.bybike.activity.model.Trip;
import com.muhammadelsayed.bybike.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class HistoryAdapter extends BaseAdapter {
    private static final String TAG = HistoryAdapter.class.getSimpleName();
    private List<Trip> tripsList;
    private LayoutInflater inflater;
    private Context context;

    public HistoryAdapter(Context context, List<Trip> tripsList) {
        inflater = LayoutInflater.from(context);
        this.tripsList = tripsList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return tripsList.size();
    }

    @Override
    public Object getItem(int position) {
        return tripsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.wtf(TAG, "getView() has been instantiated");
        final ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.history_list_item_view, null);
            holder = new ViewHolder();
            holder.tripCostTv = convertView.findViewById(R.id.trip_cost_tv);
            holder.tripClientTv = convertView.findViewById(R.id.trip_rider_tv);
            holder.tripDateTv = convertView.findViewById(R.id.trip_date_tv);
            holder.tripStarRatingBar = convertView.findViewById(R.id.trip_rating_bar);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (tripsList.get(position).getOrder_receive() != null) {
            String clientName = tripsList.get(position).getOrder_receive().getRider().getName();
            String tripCost = String.valueOf(tripsList.get(position).getOrder_receive().getTotal_cost());

            holder.tripClientTv.setText(clientName);
            holder.tripCostTv.setText("L.E. " + tripCost);

        } else {
            switch (tripsList.get(position).getStatus()) {
                case 0:
                    holder.tripClientTv.setText("Order Pending");
                    break;
                case 4:
                    holder.tripClientTv.setText("Order Canceled By Rider");
                    break;
                case 5:
                    holder.tripClientTv.setText("Order Canceled By You");
                    break;
                case 1:
                    holder.tripClientTv.setText("Order Has Been Accepted");
                    break;
                case 2:
                    holder.tripClientTv.setText("Order Has Been Taken");
                    break;

            }

            holder.tripCostTv.setText("");
        }


        if (tripsList.get(position).getRate() != null) {

            String riderRate = String.valueOf(tripsList.get(position).getRate().getRate());
            holder.tripStarRatingBar.setRating(Float.valueOf("5"));

        } else {
            holder.tripStarRatingBar.setRating(0);

        }

        String tripTime = tripsList.get(position).getCreated_at();

        try {
            holder.tripDateTv.setText(formatTime(tripTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    static class ViewHolder {
        TextView tripCostTv;   //trip_cost_tv
        TextView tripClientTv; //trip_client_tv
        TextView tripDateTv;   //trip_date_tv
        MaterialRatingBar tripStarRatingBar; // trip_rating_bar
    }


    private static String formatTime(String tripTime) throws ParseException {
        DateFormat formatter
                = new SimpleDateFormat("MM/dd/yy 'at' h:mm a");
        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        Date date = originalFormat.parse(tripTime);
        return formatter.format(date);
    }

}



