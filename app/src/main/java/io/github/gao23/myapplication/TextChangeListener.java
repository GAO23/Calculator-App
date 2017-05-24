package io.github.gao23.myapplication;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by GAO on 5/24/2017.
 */

public class TextChangeListener implements TextWatcher{
        private NewTipActivity act;
    public TextChangeListener(NewTipActivity act){
        this.act = act;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(s.toString().trim().length()==0){
           act.setDisable();
        } else {
            act.setEnable();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
