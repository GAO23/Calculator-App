package io.github.gao23.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by GAO on 5/29/2017.
 */

public class Entry implements Parcelable{
    private double unpaidEntry;
    private int unpaidSubEntry;
    private double customerPayment;
    private double paidEntry;
    private String paidSubEntry;
    private boolean cashTip = false;
    private boolean paidFlag = false;

    public Entry (double unpaidEntry, int unpaidSubEntry, double customerPayment){
         this.unpaidEntry = unpaidEntry;
         this.unpaidSubEntry = unpaidSubEntry;
         this.customerPayment = customerPayment;
    }

    public Entry(double paidEntry, String paiSubEntry, boolean cashTip){
        this.paidEntry = paidEntry;
        this.paidSubEntry = paiSubEntry;
        this.cashTip = cashTip;
        paidFlag = true;
    }

    public double getEntry(){
        if(paidFlag) {
            return paidEntry;
        }
        else{
            return unpaidEntry;
        }
    }

    public int getUnpaidSubEntry() {
       if(paidFlag){
           throw new PaidUnpaidException();
       }
       return unpaidSubEntry;
    }

    public String getPaidSubEntry(){
        if(!paidFlag){
            throw new PaidUnpaidException();
        }
        return paidSubEntry;
    }

    public double getCustomerPayment() {
        if(paidFlag){
            throw new PaidUnpaidException();
        }
        return customerPayment;
    }

    public boolean isCashiTip(){
        if(!paidFlag){
            throw new PaidUnpaidException();
        }
        return cashTip;
    }

    public boolean isPaid(){
        return isPaid();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.unpaidEntry);
        dest.writeInt(this.unpaidSubEntry);
        dest.writeDouble(this.customerPayment);
        dest.writeDouble(this.paidEntry);
        dest.writeString(this.paidSubEntry);
        dest.writeByte(this.cashTip ? (byte) 1 : (byte) 0);
        dest.writeByte(this.paidFlag ? (byte) 1 : (byte) 0);
    }

    protected Entry(Parcel in) {
        this.unpaidEntry = in.readDouble();
        this.unpaidSubEntry = in.readInt();
        this.customerPayment = in.readDouble();
        this.paidEntry = in.readDouble();
        this.paidSubEntry = in.readString();
        this.cashTip = in.readByte() != 0;
        this.paidFlag = in.readByte() != 0;
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
}
