package io.github.gao23.myapplication;

import android.content.Context;
import android.graphics.Color;
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
    private LayoutInflater mInflator;
    public entryAdaptor(Context context, ArrayList<Entry> todayEntry){
        super(context, 0, todayEntry);
        mInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Entry entry = getItem(position);
        ViewHolder holder = null;
        int type = this.getItemViewType(position);
        if (convertView == null) {
            switch(type){
            case 0:
                holder = new ViewHolder();
                convertView = mInflator.inflate(R.layout.placeholderlistlayout, parent, false);
                holder.textView = (TextView)convertView.findViewById(R.id.holderText);
                 break;
                case 1: holder = new ViewHolder();
                convertView = mInflator.inflate(R.layout.listlayout, parent, false);
                holder.textView = (TextView)convertView.findViewById(R.id.text1);
                    break;
            }
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }

        if(type == 1) {
            if (entry.isPaid()) {
                this.setPaidEntry(holder.textView, entry);
            } else {
                this.setUnpaidEntry(holder.textView, entry);
            }
        }
        else{
            holder.textView.setText(entry.getSummaryMessage());
        }

        return convertView;
    }

    public void setPaidEntry(TextView view, Entry entry){
        double customerTip = entry.getEntry();
        String address = entry.getPaidSubEntry();
        String zero = "";
        zero = this.checkIfZeroNeeded(Double.parseDouble(new DecimalFormat("#.##").format(customerTip)));
        if(entry.isCashTip()){
            view.setText("Computer Tip: $" +  new DecimalFormat("#.##").format(customerTip) + zero + "\nAddress: " + address + "\nThis is cash tip!");
            view.setTextColor(Color.BLUE);
        }
        else{
            view.setText("Computer Tip: $" +  new DecimalFormat("#.##").format(customerTip) + zero + "\nAddress: " + address);
            view.setTextColor(Color.BLACK);
        }
    }

    public void setUnpaidEntry(TextView view, Entry entry){
        double charge = entry.getEntry();
        int orderNum = entry.getUnpaidSubEntry();
        double payment = entry.getCustomerPayment();
        DecimalFormat df = new DecimalFormat("#.##");
        String zero = "";
        zero = this.checkIfZeroNeeded(Double.parseDouble(new DecimalFormat("#.##").format(payment-charge)));
        if(entry.isReceiptForgot()) {
            view.setText("Cash Tip: $" + new DecimalFormat("#.##").format(payment - charge) + zero + "\nOrder Number: #" + orderNum + "\nYou forgot the receipt OMG!");
            view.setTextColor(Color.RED);
        }
        else {
            view.setText("Cash Tip: $" + new DecimalFormat("#.##").format(payment - charge) + zero + "\nOrder Number: #" + orderNum);
            view.setTextColor(Color.BLACK);
        }
        if(payment-charge < 0) {
            view.setText(view.getText() + "\nDid you undercharged the customer or something?? O.o");
            view.setTextColor(Color.parseColor("#800080"));
        }
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

    @Override
    public int getItemViewType(int position) {
        if(this.getItem(position).getSummaryFlag()){
            return 0;
        }
        else{
            return 1;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }


    public static class ViewHolder {
        public TextView textView;
    }

}
