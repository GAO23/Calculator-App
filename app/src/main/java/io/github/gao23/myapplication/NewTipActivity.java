package io.github.gao23.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    private EditText customerPayment;
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
        entry.addTextChangedListener(new TextChangeListener(saveButton, entry, subEntry, customerPayment, this));
        subEntry.addTextChangedListener(new TextChangeListener(this.saveButton, entry, subEntry, customerPayment, this));
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
        if(customerPayment==null) {
            customerPayment = new EditText(this);
            customerPayment.setHint("Enter customer payment amount");
            customerPayment.addTextChangedListener(new TextChangeListener(this.saveButton, entry, subEntry, customerPayment, this));
            view.addView(customerPayment, lp2);
        }
        if(customerPayment!=null){
            customerPayment.setText("");
            customerPayment.setHint("Enter customer payment amount");
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
        if(customerPayment!=null){
            view.removeView(customerPayment);
            customerPayment = null;
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
        Entry result;
        try {
            if(isCashTipNull()) {
                double chargedAmount;
                int orderNum;
                double payment;
                try {
                    chargedAmount = Double.parseDouble(entry.getText().toString());
                    if(!decimalCheck(chargedAmount)){
                        throw new InvalidInputException(5);
                    }
                } catch (NumberFormatException e) {
                   throw new InvalidInputException(1);
                }
                try {
                    orderNum = Integer.parseInt(subEntry.getText().toString());
                } catch (NumberFormatException e) {
                    throw new InvalidInputException(2);
                }
                try {
                    payment = Double.parseDouble(customerPayment.getText().toString());
                    if(!decimalCheck(payment)){
                        throw new InvalidInputException(6);
                    }
                } catch (NumberFormatException e) {
                    throw new InvalidInputException(3);
                }
                result = new Entry(chargedAmount,orderNum,payment);
               this.terminate(result);
            }
            else{
                double tipAmount;
                String orderAddress = subEntry.getText().toString();
                boolean isCashTip = cashTip.isChecked();
                try {
                    tipAmount = Double.parseDouble(entry.getText().toString());
                    if(!decimalCheck(tipAmount)){
                        throw new InvalidInputException(7);
                    }
                }catch (NumberFormatException e){
                    throw new InvalidInputException(4);
                }
                result = new Entry(tipAmount,orderAddress,isCashTip);
                this.terminate(result);
            }
        }
        catch (InvalidInputException e){
            int errorCode = e.getErrorCode();
            switch(errorCode) {
                case 1: Toast.makeText(this.getApplicationContext(), "Invalid charged amount", Toast.LENGTH_LONG).show();
                    break;
                case 2: Toast.makeText(this.getApplicationContext(), "Order number needs to be a whole number", Toast.LENGTH_LONG).show();
                    break;
                case 3: Toast.makeText(this.getApplicationContext(), "Invalid payment amount", Toast.LENGTH_LONG).show();
                    break;
                case 4: Toast.makeText(this.getApplicationContext(), "Invalid tip amount", Toast.LENGTH_LONG).show();
                    break;
                case 5 : Toast.makeText(this.getApplicationContext(), "Make sure tip amount has two decimals only for cents", Toast.LENGTH_LONG).show();
                    break;
                case 6 : Toast.makeText(this.getApplicationContext(), "Make sure payment amount has two decimals only for cents", Toast.LENGTH_LONG).show();
                    break;
                case 7 : Toast.makeText(this.getApplicationContext(), "Make sure tip amount has two decimals for only cents", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean decimalCheck(double test) {
        String text = Double.toString(Math.abs(test));
        int integerPlaces = text.indexOf('.');
        int decimalPlaces = text.length() - integerPlaces - 1;
       return decimalPlaces == 2;
    }


      private void terminate(Entry result){
          Intent intent = new Intent();
          intent.putExtra(intentCode.parb, result);
          this.setResult(intentCode.CHECK, intent);
          this.finish();
      }


}
