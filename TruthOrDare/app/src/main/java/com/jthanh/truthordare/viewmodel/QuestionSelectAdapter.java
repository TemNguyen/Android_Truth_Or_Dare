package com.jthanh.truthordare.viewmodel;


import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jthanh.truthordare.model.QuestionSelect;

public class QuestionSelectAdapter extends ArrayAdapter<QuestionSelect> {
    private Context context;
    private QuestionSelect[] values;

    public QuestionSelectAdapter(Context context, int textViewResourceId, QuestionSelect[] values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount(){
        return values.length;
    }

    @Override
    public QuestionSelect getItem(int position){
        return values[position];
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.WHITE);
        label.setText(values[position].getName());
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.WHITE);
        label.setText(values[position].getName());
        return label;
    }
}
