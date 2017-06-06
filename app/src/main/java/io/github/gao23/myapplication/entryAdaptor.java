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
        Entry entry = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listlayout, parent, false);
        }
        TextView view = (TextView) convertView.findViewById(R.id.text1);
        if(entry.isPaid()){
            this.setPaidEntry(view, entry);
        }
        else{
            this.setUnpaidEntry(view,entry);
        }

        return convertView;
    }

    public void setPaidEntry(TextView view, Entry entry){
        double customerTip = Math.round(entry.getEntry()*100)/100;
        String address = entry.getPaidSubEntry();
        String isCashTip = "";
        if(entry.isCashiTip()){
            isCashTip = "Yes";
        }
        else{
            isCashTip = "No";
        }
        view.setText("Computer Tip: $" + Double.toString(customerTip) + "\nAddress: " + address + "\nCash Tip: " + isCashTip);
    }

    public void setUnpaidEntry(TextView view, Entry entry){
        double charge = Math.round(entry.getEntry()*100)/100;
        int orderNum = entry.getUnpaidSubEntry();
        double payment = entry.getCustomerPayment();
        view.setText("Cash Tip: $" + Double.toString(payment - charge) + "\nOrder Number: #" + orderNum);
    }


}
