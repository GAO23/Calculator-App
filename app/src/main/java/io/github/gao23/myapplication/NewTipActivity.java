package io.github.gao23.myapplication;

import android.content.Context;
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
import android.widget.Toast;


public class NewTipActivity extends AppCompatActivity {
    private Button saveButton;
    private EditText entry;
    private EditText subEntry;
    private EditText customerPaynment;
    private CheckBox cashTip;
    private RelativeLayout view;
    private LayoutParams lp1;
    private LayoutParams lp2;
    private boolean viewAddedFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_tip_layout);
        saveButton = (Button) findViewById(R.id.Save);
        this.saveButton.setEnabled(false);
        view = (RelativeLayout) findViewById(R.id.RelativeLayout01);
        entry = new EditText(this);
        entry.setId(1);
        entry.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
        subEntry = new EditText(this);
        subEntry.setId(2);
        subEntry.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
        entry.addTextChangedListener(new TextChangeListener(saveButton, entry, subEntry, customerPaynment, this));
        subEntry.addTextChangedListener(new TextChangeListener(this.saveButton, entry, subEntry, customerPaynment, this));
        lp1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        lp1.addRule(RelativeLayout.BELOW, entry.getId());
        lp2 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        lp2.addRule(RelativeLayout.BELOW, subEntry.getId());
     }

    public void newUnpaidOrderClicked(View v){
        entry.setText("");
        subEntry.setText("");
        entry.setHint("Enter amount charged to customer");
        subEntry.setHint("Enter your order number");
        if(!viewAddedFlag){
            view.addView(entry);
            view.addView(subEntry, lp1);
            this.viewAddedFlag = true;
        }
        if(cashTip!= null){
            view.removeView(cashTip);
            cashTip=null;
        }
        if(customerPaynment==null) {
            customerPaynment = new EditText(this);
            customerPaynment.setHint("Enter customer payment amount");
            customerPaynment.addTextChangedListener(new TextChangeListener(this.saveButton, entry, subEntry, customerPaynment, this));
            view.addView(customerPaynment, lp2);
        }
        if(customerPaynment!=null){
            customerPaynment.setText("");
            customerPaynment.setHint("Enter customer payment amount");
        }
    }

    public void newPaidOrderClicked(View v){
        entry.setText("");
        subEntry.setText("");
        entry.setHint("Enter your tip amount");
        subEntry.setHint("Enter your order address");

        if(!viewAddedFlag){
            view.addView(entry);
            view.addView(subEntry, lp1);
            this.viewAddedFlag = true;
        }
        if(customerPaynment!=null){
            view.removeView(customerPaynment);
            customerPaynment = null;
        }
        if(cashTip== null) {
            cashTip = new CheckBox(this);
            cashTip.setText("Check the box if this is cash tip");
            view.addView(cashTip, lp2);
        }
        if(cashTip!=null){
            cashTip.setChecked(false);
        }
    }

    public boolean isCashTipNull(){
        return cashTip == null;
    }

    public void SaveClicked(View v) {
       if(isCashTipNull()){
           double chargedAmount;
           int orderNum;
           double payment;
           try {
               chargedAmount = Double.parseDouble(entry.getText().toString());
           }
           catch (NumberFormatException e){
               Toast.makeText(this.getApplicationContext(), "Invalid charged amount", Toast.LENGTH_LONG).show();
           }
           try {
               orderNum = Integer.parseInt(subEntry.getText().toString());
           }
           catch (NumberFormatException e){
               Toast.makeText(this.getApplicationContext(), "Invalid order number", Toast.LENGTH_LONG).show();
           }
           try {
               payment = Double.parseDouble(customerPaynment.getText().toString());
           }
           catch (NumberFormatException e){
               Toast.makeText(this.getApplicationContext(), "Invalid customer payment", Toast.LENGTH_LONG).show();
           }
       }

       else{

       }
    }





}
