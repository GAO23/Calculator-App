package io.github.gao23.myapplication;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.util.concurrent.locks.ReadWriteLock;

public class NewTipActivity extends AppCompatActivity {
    private Button saveButton;
    private EditText entry;
    private RelativeLayout view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_tip_layout);
        saveButton = (Button) findViewById(R.id.Save);
        this.saveButton.setEnabled(false);
        view = (RelativeLayout) findViewById(R.id.RelativeLayout01);
     }

    public void newUnpaidOrderClicked(View v){
        if(entry==null) {
            entry = new EditText(this);
            entry.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
            entry.setHint("Enter a new unpaid order amount");
            view.addView(entry);
            entry.addTextChangedListener(new TextChangeListener(this));
        }
        else{
            entry.setText("");
            entry.setHint("Enter a new unpaid order amount");
        }
    }

    public void newPaidOrderClicked(View v){
         if(entry== null) {
             entry = new EditText(this);
             entry.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
             entry.setHint("Enter a new paid order amount");
             view.addView(entry);
             entry.addTextChangedListener(new TextChangeListener(this));
         }
         else{
             entry.setText("");
             entry.setHint("Enter a new paid order amount");
         }
    }

    public void SaveClicked(View v) {
        entry.setText("Not supported yet but working on it");
    }

    public void setEnable(){
        saveButton.setEnabled(true);
    }
    public void setDisable(){
        saveButton.setEnabled(false);
    }


}
