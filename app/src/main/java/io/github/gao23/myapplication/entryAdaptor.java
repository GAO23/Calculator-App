package io.github.gao23.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by GAO on 6/4/2017.
 */

public class entryAdaptor extends ArrayAdapter<Entry> {
    public entryAdaptor(Context context, ArrayList<Entry> todayEntry){
        super(context, 0, todayEntry);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // entry for this position
        Entry entry = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }
        TextView view = (TextView) convertView.findViewById(android.R.id.text1);

        view.setText(Double.toString(entry.getEntry()));

        return convertView;
    }
}
