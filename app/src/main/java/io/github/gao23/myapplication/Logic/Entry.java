package io.github.gao23.myapplication.Logic;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by GAO on 5/29/2017.
 */

public class Entry implements Parcelable{
    private double entry;
    private int unpaidSubEntry;
    private double customerPayment;
    private String paidSubEntry;
    private boolean cashTip = false;
    private boolean paidFlag = false;
    private boolean receiptForgot = false;
    private boolean summaryFlag = false;
    private String summaryMessage;

    public Entry (double unpaidEntry, int unpaidSubEntry, double customerPayment, boolean receiptForgot){
         this.entry = unpaidEntry;
         this.unpaidSubEntry = unpaidSubEntry;
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

    public int getUnpaidSubEntry() {
       return unpaidSubEntry;
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
        dest.writeInt(this.unpaidSubEntry);
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
        this.unpaidSubEntry = in.readInt();
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
}
