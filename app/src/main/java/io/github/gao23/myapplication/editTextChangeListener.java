package io.github.gao23.myapplication;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by GAO on 6/8/2017.
 */

public class editTextChangeListener implements TextWatcher {
    private boolean isComputer = false;
    private EditText entry;
    private EditText subEntry;
    private EditText customerPayment;
    private Button save;

    public editTextChangeListener (Button save, EditText entry, EditText subEntry){
        this.save = save;
        this.entry = entry;
        this.subEntry = subEntry;
        isComputer = true;
    }

    public editTextChangeListener (Button save, EditText entry, EditText subEntry, EditText customerPayment){
        this.save = save;
        this.entry = entry;
        this.subEntry = subEntry;
        this.customerPayment = customerPayment;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (entry.getText().length() == 0 || subEntry.getText().length() == 0) {
            save.setEnabled(false);
        } else {
            save.setEnabled(true);
        }
        if(!isComputer){
            if(customerPayment.getText().length() == 0){
                save.setEnabled(false);
            }
            else {
                save.setEnabled(true);
            }
        }


    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
