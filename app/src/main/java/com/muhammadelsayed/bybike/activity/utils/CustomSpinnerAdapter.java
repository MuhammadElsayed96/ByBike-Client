package com.muhammadelsayed.bybike.activity.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.TransitionRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.muhammadelsayed.bybike.R;
import com.muhammadelsayed.bybike.activity.model.Transportation;

import java.util.List;

public class CustomSpinnerAdapter extends ArrayAdapter<Transportation> {


    private final LayoutInflater mInflater;
    private final Context mContext;
    private final List<Transportation> trans;
    private final int mResource;

    public CustomSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<Transportation> objects) {
        super(context, resource, objects);

        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        trans = objects;
    }



    private View createItemView(int position, View convertView, ViewGroup parent){

        final View view = mInflater.inflate(mResource, parent, false);
        TextView txtTrans = view.findViewById(R.id.txtTrans);
        TextView txtCost = view.findViewById(R.id.txtCost);
        TextView txtDistance = view.findViewById(R.id.txtDistance);
        ImageView imgTrans = view.findViewById(R.id.imgTrans);


        Transportation transData = trans.get(position);

        txtTrans.setText(transData.getTransType());
        txtCost.setText(transData.getTransCost());
        txtDistance.setText(transData.getTransDistance());
        imgTrans.setImageResource(transData.getTransImg());

        return view;
    }


    @Override
    public View getDropDownView(int position, @Nullable View convertView,
                                @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }
}
