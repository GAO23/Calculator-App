package io.github.gao23.myapplication;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;

/**
 * Created by GAO on 5/24/2017.
 */

public class TextChangeListener implements TextWatcher{
        private Button button;
    public TextChangeListener(Button button){
        this.button = button;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(s.toString().trim().length()==0){
           button.setEnabled(false);
        } else {
            button.setEnabled(true);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
