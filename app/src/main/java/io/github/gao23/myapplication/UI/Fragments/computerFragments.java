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

 public class computerFragments extends Fragment {

     private EditText text1;
     private EditText text2;
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public computerFragments() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static computerFragments newInstance(int sectionNumber) {
        computerFragments fragment = new computerFragments();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.computer_fragment, container, false);
        text1 = (EditText) rootView.findViewById(R.id.tipAmount);
        text2 = (EditText) rootView.findViewById(R.id.computerTipIdentifier);
        return rootView;
    }

    /***
     * gets the tip amount
     * @return the user input tip amount
     */
    public double getTipAmount() {
        double tipAmpunt = Double.parseDouble(text1.getText().toString());
        return tipAmpunt;
    }

    /***
     * gets the order ID
     * @return the user inputed user ID
     */
    public String getOrderID() {
        return text2.getText().toString();
    }

    /***
     * returns whether user checks the is cash tip box
     * @return if user inputed this is cash tip
     */
    public boolean isCashTip() {
        CheckBox isCashCheckBox = (CheckBox) getView().findViewById(R.id.cashTipCheckBox);
        return isCashCheckBox.isChecked();
    }

    /***
     * this checks if user inputed anything
     * @return false if user has not inputed anything
     */
    public boolean isValid (){
        if (TextUtils.isEmpty(text1.getText())){
            return false;
        }

        if(TextUtils.isEmpty(text2.getText())){
            return false;
        }
        if(!Entry.decimalCheck(Double.parseDouble(text1.getText().toString()))){
            return false;
        }
        return true;
    }



}
