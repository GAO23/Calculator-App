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
    boolean cashiTip = false;
    boolean paidFlag = false;

    public Entry (double unpaidEntry, int unpaidSubEntry, double customerPayment){
         this.unpaidEntry = unpaidEntry;
         this.unpaidSubEntry = unpaidSubEntry;
         this.customerPayment = customerPayment;
    }

    public Entry(double paidEntry, String paiSubEntry, boolean cashiTip){
        this.paidEntry = paidEntry;
        this.paidSubEntry = paiSubEntry;
        this.cashiTip = cashiTip;
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

    public int getUnpaidSubEntry() throws PaidUnpaidException{
       if(paidFlag){
           throw new PaidUnpaidException();
       }
       return unpaidSubEntry;
    }

    public String getPaidSubEntry() throws PaidUnpaidException{
        if(!paidFlag){
            throw new PaidUnpaidException();
        }
        return paidSubEntry;
    }

    public double getCustomerPayment() throws  PaidUnpaidException{
        if(paidFlag){
            throw new PaidUnpaidException();
        }
        return customerPayment;
    }

    public boolean isCashiTip() throws PaidUnpaidException{
        if(!paidFlag){
            throw new PaidUnpaidException();
        }
        return cashiTip;
    }
}
