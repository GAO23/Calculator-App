package io.github.gao23.myapplication;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * Created by GAO on 5/24/2017.
 */

public class TextChangeListener implements TextWatcher{
        private EditText entry;
        private Button button;
        private EditText subentry;
        private EditText customerPayment;
        private NewTipActivity act;

    public TextChangeListener(Button button, EditText entry, EditText subentery, EditText customerPayment, NewTipActivity act){
        this.button = button;
        this.entry = entry;
        this.subentry = subentery;
        this.customerPayment = customerPayment;
        this.act = act;
    }



    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!act.isCashTipull()) {
            if (entry.getText().length() == 0 || subentry.getText().length() == 0) {
                button.setEnabled(false);
            } else {
                button.setEnabled(true);
            }
        }
        else {
            if (entry.getText().length() == 0 || subentry.getText().length() == 0) {
                button.setEnabled(false);
            } else if (customerPayment != null && customerPayment.getText().length() == 0) {
                button.setEnabled(false);
            } else {
                button.setEnabled(true);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
