package com.example.myiotdevice;

import android.app.Activity;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.ArrayList;


public class SensorsAdapter extends ArrayAdapter<Sensors> {

    public SparseBooleanArray checkboxStatus;
    public CheckBox cb;

    public SensorsAdapter(Activity context, ArrayList<Sensors> sensors, SparseBooleanArray checkboxStatus){
        super(context,0,sensors);
        this.checkboxStatus=checkboxStatus;
    }

    public View getView(int position, View view, ViewGroup parent){
        Sensors sensors = getItem(position);


        if (view==null) {
            if (sensors.splittedItemHeader1==null){
                view = LayoutInflater.from(getContext()).inflate(R.layout.full_width_item, parent, false);
                cb = (CheckBox) view.findViewById(R.id.checkbox);
                cb.setChecked(checkboxStatus.get(position));
            }
            else if (sensors.splittedItemHeader1!=null){
                view = LayoutInflater.from(getContext()).inflate(R.layout.splitted_width_item, parent, false);
                cb = (CheckBox) view.findViewById(R.id.checkbox);
                cb.setChecked(checkboxStatus.get(position));
            }
        }
        else {
            if (sensors.splittedItemHeader1==null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.full_width_item, parent, false);
                cb = (CheckBox) view.findViewById(R.id.checkbox);
                cb.setChecked(checkboxStatus.get(position));
            }
            else if (sensors.splittedItemHeader1!=null){
                view = LayoutInflater.from(getContext()).inflate(R.layout.splitted_width_item, parent, false);
                cb = (CheckBox) view.findViewById(R.id.checkbox);
                cb.setChecked(checkboxStatus.get(position));
            }
        }

        if (sensors.splittedItemHeader1==null) {
            TextView itemHeaderView = (TextView) view.findViewById(R.id.item_header);
            TextView itemTextView1 = (TextView) view.findViewById(R.id.item_text_1);
            TextView itemTextView2 = (TextView) view.findViewById(R.id.item_text_2);
            TextView itemTextView3 = (TextView) view.findViewById(R.id.item_text_3);
            TextView itemValueView1 = (TextView) view.findViewById(R.id.item_value_1);
            TextView itemValueView2 = (TextView) view.findViewById(R.id.item_value_2);
            TextView itemValueView3 = (TextView) view.findViewById(R.id.item_value_3);

            itemHeaderView.setText(sensors.itemHeader);
            itemTextView1.setText(sensors.itemText1);
            itemTextView2.setText(sensors.itemText2);
            itemTextView3.setText(sensors.itemText3);
            itemValueView1.setText(sensors.itemValue1);
            itemValueView2.setText(sensors.itemValue2);
            itemValueView3.setText(sensors.itemValue3);
        }
        else {
            TextView splittedItemHeaderView1 = (TextView) view.findViewById(R.id.item_splitted_header_1);
            TextView splittedItemHeaderView2 = (TextView) view.findViewById(R.id.item_splitted_header_2);
            TextView splittedItemValueView1 = (TextView) view.findViewById(R.id.item_splitted_value_1);
            TextView splittedItemValueView2 = (TextView) view.findViewById(R.id.item_splitted_value_2);

            splittedItemHeaderView1.setText(sensors.splittedItemHeader1);
            splittedItemHeaderView2.setText(sensors.splittedItemHeader2);
            splittedItemValueView1.setText(sensors.splittedItemValue1);
            splittedItemValueView2.setText(sensors.splittedItemValue2);
        }

        return view;
    }
}
