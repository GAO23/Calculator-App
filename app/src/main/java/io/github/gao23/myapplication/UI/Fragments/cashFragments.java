package io.github.gao23.myapplication.UI.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import io.github.gao23.myapplication.Logic.Entry;
import io.github.gao23.myapplication.R;

/**
 * Created by xgao on 4/19/18.
 */

public class cashFragments extends Fragment{

    private EditText edit1;
    private EditText edit2;
    private EditText edit3;

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public cashFragments() {

    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static cashFragments newInstance(int sectionNumber) {
        cashFragments fragment = new cashFragments();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.cash_fragment, container, false);
        edit1 = (EditText) rootView.findViewById(R.id.amountCharged);
        edit2 = (EditText) rootView.findViewById(R.id.customerPayment);
        edit3 = (EditText) rootView.findViewById(R.id.cashTipIdentifier);
        return rootView;
    }

    /***
     * this checks if the user had inputed anything
     * @return false if user had not inputed any valid entry
     */
    public boolean isValid(){
        if(TextUtils.isEmpty(edit1.getText())){
            return false;
        }

        if(TextUtils.isEmpty(edit2.getText())){
            return false;
        }

        if(TextUtils.isEmpty(edit3.getText())){
            return  false;
        }

        if(!Entry.decimalCheck(Double.parseDouble(edit1.getText().toString()))){
            return false;
        }

        if(!Entry.decimalCheck(Double.parseDouble(edit2.getText().toString()))){
            return false;
        }
        return true;
    }

    /***
     * gets the user inputed charged amount
     * @return the user inputed amount charged
     */
    public double getAmountCharged(){
        return Double.parseDouble(edit1.getText().toString());
    }

    /***
     * returns the user inputed payment amount
     * @return the user inputed payment
     */
    public double getPaymentAmount(){
        return Double.parseDouble(edit2.getText().toString());
    }

    /***
     * returns the user inputed user ID
     * @return the user inputed user ID
     */
    public String getOrderID(){
        return edit3.getText().toString();
    }

    /***
     * returns if the user clicked on the forgotten receipt check box
     * @return if the user forgot to bring back the receipt
     */
    public boolean isRecieptForgotten(){
        CheckBox cashTipCheckBox = (CheckBox) getView().findViewById(R.id.recieptBackCheckBox);
        return cashTipCheckBox.isChecked();
    }
}
