package io.github.gao23.myapplication.Logic;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by GAO on 5/29/2017.
 */

public class Entry implements Parcelable{
    private double entry;
    private String cashOrderID;
    private double customerPayment;
    private String paidSubEntry;
    private boolean cashTip = false;
    private boolean paidFlag = false;
    private boolean receiptForgot = false;
    private boolean summaryFlag = false;
    private String summaryMessage;

    public Entry (double unpaidEntry, String cashOrderID, double customerPayment, boolean receiptForgot){
         this.entry = unpaidEntry;
         this.cashOrderID = cashOrderID;
         this.customerPayment = customerPayment;
         this.receiptForgot = receiptForgot;
    }

    public Entry(double paidEntry, String paiSubEntry, boolean cashTip){
        this.entry = paidEntry;
        this.paidSubEntry = paiSubEntry;
        this.cashTip = cashTip;
        paidFlag = true;
    }

    public Entry (String summaryMessage){
       this.summaryFlag = true;
        this.summaryMessage = summaryMessage;
    }

    public double getEntry(){
       return entry;
    }

    public String getUnpaidSubEntry() {
       return cashOrderID;
    }

    public String getPaidSubEntry(){
        return paidSubEntry;
    }

    public double getCustomerPayment() {
        return this.customerPayment;
    }

    public boolean isCashTip(){
        return this.cashTip;
    }

    public boolean isPaid(){
        return paidFlag;
    }

    public boolean isReceiptForgot() {
        return this.receiptForgot;
    }
    public String getSummaryMessage(){
        return this.summaryMessage;
    }

    public boolean getSummaryFlag(){return this.summaryFlag;}


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.entry);
        dest.writeString(this.cashOrderID);
        dest.writeDouble(this.customerPayment);
        dest.writeDouble(this.entry);
        dest.writeString(this.paidSubEntry);
        dest.writeByte(this.cashTip ? (byte) 1 : (byte) 0);
        dest.writeByte(this.paidFlag ? (byte) 1 : (byte) 0);
        dest.writeByte(this.receiptForgot ? (byte) 1 : (byte) 0);
        dest.writeByte(this.summaryFlag ? (byte) 1 : (byte) 0);
        dest.writeString(this.summaryMessage);
    }

    protected Entry(Parcel in) {
        this.entry = in.readDouble();
        this.cashOrderID = in.readString();
        this.customerPayment = in.readDouble();
        this.entry = in.readDouble();
        this.paidSubEntry = in.readString();
        this.cashTip = in.readByte() != 0;
        this.paidFlag = in.readByte() != 0;
        this.receiptForgot = in.readByte() != 0;
        this.summaryFlag = in.readByte() != 0;
        this.summaryMessage = in.readString();
    }

    public static final Creator<Entry> CREATOR = new Creator<Entry>() {
        @Override
        public Entry createFromParcel(Parcel source) {
            return new Entry(source);
        }

        @Override
        public Entry[] newArray(int size) {
            return new Entry[size];
        }
    };

    public void setSummaryMessage(String message){
        this.summaryMessage = message;
    }


    /***
     * helper function to add the zeros if the decials places are less than two
     * @param test is the number being tested
     * @return the number of zeros needed to append to a decimal
     */
    public static String checkIfZeroNeeded(double test){
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

    /***
     * this checks if the value has the right decimal, use as helper function to make sure the number user entered has decimal and two digits after it
     * @param test is the value being checked
     * @return
     */
    public static boolean decimalCheck(double test) {
        String num =  Double.toString(test);
        int i = num.lastIndexOf('.');
        if(test % 1 == 0){
            return true;
        }
        else if(i != -1 && (num.substring(i + 1).length() == 2 || num.substring(i + 1).length() == 1)) {
            return true;
        }
        else{
            return false;
        }

    }


}


