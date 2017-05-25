package io.github.gao23.myapplication;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;


public class NewTipActivity extends AppCompatActivity {
    private Button saveButton;
    private EditText entry;
    private EditText subEntry;
    private double customerPaynment = 0.00;
    private CheckBox cashTip;
    private RelativeLayout view;
    private LayoutParams lp1;
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
            entry.setId(1);
            entry.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
            entry.setHint("Enter amount charged to customer");
            subEntry = new EditText(this);
            subEntry.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
            subEntry.setHint("Enter your order number");
            view.addView(entry);
            this.setPara();
            view.addView(subEntry, lp1);
            entry.addTextChangedListener(new TextChangeListener(saveButton));
            subEntry.addTextChangedListener(new TextChangeListener(this.saveButton));
        }
        else{
            entry.setText("");
            subEntry.setText("");
            entry.setHint("Enter the amount charged to customer");
            subEntry.setHint("Enter your order number");
        }

    }

    public void newPaidOrderClicked(View v){
         if(entry== null) {
             entry = new EditText(this);
             entry.setId(1);
             entry.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
             entry.setHint("Enter your tip");
             subEntry = new EditText(this);
             subEntry.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
             subEntry.setHint("Enter your order address");
             view.addView(entry);
             this.setPara();
             view.addView(subEntry,lp1);
             entry.addTextChangedListener(new TextChangeListener(this.saveButton));
             subEntry.addTextChangedListener(new TextChangeListener(this.saveButton));
         }
         else{
             entry.setText("");
             subEntry.setText("");
             entry.setHint("Enter your tip");
             subEntry.setHint("Enter your order address");
         }
    }

    public void SaveClicked(View v) {
        entry.setText("Not supported yet but working on it");
    }

    public void setPara(){
        lp1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        lp1.addRule(RelativeLayout.BELOW, entry.getId());
    }



}
