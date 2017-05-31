package io.github.gao23.myapplication;

/**
 * Created by GAO on 5/29/2017.
 */

public class Entry {
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
}
