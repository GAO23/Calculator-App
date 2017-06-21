package io.github.gao23.myapplication;

import android.content.Intent;
import android.content.pm.ActivityInfo;
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
    private EditText customerPayment;
    private CheckBox cashTip;
    private RelativeLayout view;
    private LayoutParams lp1;
    private LayoutParams lp2;
    private LayoutParams lp3;
    private CheckBox receiptBack;
    private boolean viewAddedFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
        lp1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp1.addRule(RelativeLayout.BELOW, entry.getId());
        lp2 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp2.addRule(RelativeLayout.BELOW, subEntry.getId());
        lp3 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        this.getSupportActionBar().setTitle("New Tip");
    }

    public void newUnpaidOrderClicked(View v) {
        entry.setText("");
        subEntry.setText("");
        entry.setHint("Enter amount charged to customer");
        subEntry.setHint("Enter your order number");
        if (!viewAddedFlag) {
            view.addView(entry);
            view.addView(subEntry, lp1);
            this.viewAddedFlag = true;
        }
        if (cashTip != null) {
            view.removeView(cashTip);
            cashTip = null;
        }
        if (customerPayment == null) {
            customerPayment = new EditText(this);
            customerPayment.setHint("Enter customer payment amount");
            customerPayment.addTextChangedListener(new TextChangeListener(this.saveButton, entry, subEntry, customerPayment, this));
            customerPayment.setId(3);
            view.addView(customerPayment, lp2);
            receiptBack = new CheckBox(this);
            receiptBack.setText("Check this box if you did not bring back the receipt.");
            lp3.addRule(RelativeLayout.BELOW, customerPayment.getId());
            view.addView(receiptBack, lp3);
        }
        if (customerPayment != null) {
            customerPayment.setText("");
            customerPayment.setHint("Enter customer payment amount");
            receiptBack.setChecked(false);
        }
    }

    public void newPaidOrderClicked(View v) {
        entry.setText("");
        subEntry.setText("");
        entry.setHint("Enter your tip amount");
        subEntry.setHint("Enter your order address");

        if (!viewAddedFlag) {
            view.addView(entry);
            view.addView(subEntry, lp1);
            this.viewAddedFlag = true;
        }
        if (customerPayment != null) {
            view.removeView(customerPayment);
            customerPayment = null;
            view.removeView(receiptBack);
            receiptBack = null;
        }
        if (cashTip == null) {
            cashTip = new CheckBox(this);
            cashTip.setText("Check the box if this is cash tip");
            view.addView(cashTip, lp2);
        }
        if (cashTip != null) {
            cashTip.setChecked(false);
        }
    }

    public boolean isCashTipNull() {
        return cashTip == null;
    }

    public void SaveClicked(View v) {
        Entry result;
        try {
            if (isCashTipNull()) {
                double chargedAmount;
                int orderNum;
                double payment;
                try {
                    chargedAmount = Double.parseDouble(entry.getText().toString());
                    if (!decimalCheck(chargedAmount)) {
                        throw new InvalidInputException(5);
                    }
                    if(chargedAmount<0){
                        throw new InvalidInputException(8);
                    }
                } catch (NumberFormatException e) {
                    throw new InvalidInputException(1);
                }
                try {
                    orderNum = Integer.parseInt(subEntry.getText().toString());
                    if(orderNum<0){
                        throw new InvalidInputException(8);
                    }
                }
                catch (NumberFormatException e) {
                    throw new InvalidInputException(2);
                }
                try {
                    payment = Double.parseDouble(customerPayment.getText().toString());
                    if (!decimalCheck(payment)) {
                        throw new InvalidInputException(6);
                    }
                    if(payment<0){
                        throw new InvalidInputException(8);
                    }
                } catch (NumberFormatException e) {
                    throw new InvalidInputException(3);
                }
                result = new Entry(chargedAmount, orderNum, payment, receiptBack.isChecked());
                this.terminate(result);
            } else {
                double tipAmount;
                String orderAddress = subEntry.getText().toString();
                boolean isCashTip = cashTip.isChecked();
                try {
                    tipAmount = Double.parseDouble(entry.getText().toString());
                    if (!decimalCheck(tipAmount)) {
                        throw new InvalidInputException(7);
                    }
                    if(tipAmount<0){
                        throw new InvalidInputException(8);
                    }
                } catch (NumberFormatException e) {
                    throw new InvalidInputException(4);
                }
                result = new Entry(tipAmount, orderAddress, isCashTip);
                this.terminate(result);
            }
        } catch (InvalidInputException e) {
            int errorCode = e.getErrorCode();
            switch (errorCode) {
                case 1:
                    Toast.makeText(this.getApplicationContext(), "Invalid charged amount", Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(this.getApplicationContext(), "Order number needs to be a whole number", Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    Toast.makeText(this.getApplicationContext(), "Invalid payment amount", Toast.LENGTH_LONG).show();
                    break;
                case 4:
                    Toast.makeText(this.getApplicationContext(), "Invalid tip amount", Toast.LENGTH_LONG).show();
                    break;
                case 5:
                    Toast.makeText(this.getApplicationContext(), "Make sure charged amount has two decimals only for cents", Toast.LENGTH_LONG).show();
                    break;
                case 6:
                    Toast.makeText(this.getApplicationContext(), "Make sure payment amount has two decimals only for cents", Toast.LENGTH_LONG).show();
                    break;
                case 7:
                    Toast.makeText(this.getApplicationContext(), "Make sure tip amount has two decimals only for cents", Toast.LENGTH_LONG).show();
                    break;
                case 8: Toast.makeText(this.getApplicationContext(), "One of the field is a negative value. Only positive is allowed.", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    private boolean decimalCheck(double test) {
        String num =  Double.toString(test);
        int i = num.lastIndexOf('.');
        if(test % 1 == 0){
            return true;
        }
        else if(i != -1 && (num.substring(i + 1).length() == 2 || num.substring(i + 1).length() == 1)) {
            return true;
        }
        else{
            return false;
        }

}


    @Override
    public void onBackPressed() {
        this.setResult(intentCode.INVALID);
        finish();
    }

    private void terminate(Entry result){
          Intent intent = new Intent();
          intent.putExtra(intentCode.parb, result);
          this.setResult(intentCode.CHECK, intent);
          this.finish();
      }


}
