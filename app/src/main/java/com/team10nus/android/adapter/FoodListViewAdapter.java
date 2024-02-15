package com.team10nus.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.team10nus.android.R;

public class FoodListViewAdapter extends ArrayAdapter<String> {

    public FoodListViewAdapter(Context context, String[] items) {
        super(context, R.layout.list_item, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.list_item, parent, false);

        String item = getItem(position);
        TextView textView = (TextView) view.findViewById(R.id.textView);
        ImageView imageView = (ImageView) view.findViewById(R.id.icon);

        textView.setText(item);

        return view;
    }
}

