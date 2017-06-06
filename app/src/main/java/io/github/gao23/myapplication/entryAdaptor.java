package io.github.gao23.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
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
        double customerTip = entry.getEntry();
        String address = entry.getPaidSubEntry();
        String isCashTip = "";
        if(entry.isCashiTip()){
            isCashTip = "Yes";
        }
        else{
            isCashTip = "No";
        }
        String zero = "";
        zero = this.checkIfZeroNeeded(Double.parseDouble(new DecimalFormat("#.##").format(customerTip)));
        view.setText("Computer Tip: $" +  new DecimalFormat("#.##").format(customerTip) + zero + "\nAddress: " + address + "\nCash Tip: " + isCashTip);
    }

    public void setUnpaidEntry(TextView view, Entry entry){
        double charge = entry.getEntry();
        int orderNum = entry.getUnpaidSubEntry();
        double payment = entry.getCustomerPayment();
        DecimalFormat df = new DecimalFormat("#.##");
        String zero = "";
        zero = this.checkIfZeroNeeded(Double.parseDouble(new DecimalFormat("#.##").format(payment-charge)));

        view.setText("Cash Tip: $" + new DecimalFormat("#.##").format(payment-charge) + zero + "\nOrder Number: #" + orderNum);
    }
public String checkIfZeroNeeded(double test){
    String num =  Double.toString(test);
    int i = num.lastIndexOf('.');
        if(test % 1 == 0){
            return ".00";
        } if(i != -1 && num.substring(i + 1).length() == 1){
        return "0";
    }
    else{
        return "";
    }

}

}
