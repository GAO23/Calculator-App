package io.github.gao23.myapplication.UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DecimalFormat;

import io.github.gao23.myapplication.Logic.Entry;
import io.github.gao23.myapplication.Logic.InvalidInputException;
import io.github.gao23.myapplication.Logic.editTextChangeListener;
import io.github.gao23.myapplication.Logic.intentCode;
import io.github.gao23.myapplication.R;

public class cashEditActivity extends AppCompatActivity {
    private EditText entry;
    private EditText subEntry;
    private EditText customerPayment;
    private CheckBox forgottenTip;
    private Entry initialEntry;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_cash_edit);
        entry = (EditText) findViewById(R.id.cashEditText1);
        subEntry = (EditText) findViewById(R.id.cashEditText2);
        customerPayment = (EditText) findViewById(R.id.cashEditText3);
        forgottenTip = (CheckBox) findViewById(R.id.cashCheckBox);
        forgottenTip.setText("Check this box if you did not bring back the receipt.");
        initialEntry = this.getIntent().getParcelableExtra(intentCode.ENTRY_PARCEL);
        save = (Button) findViewById(R.id.cashSaveButton);
        this.initialSetUp();
        entry.addTextChangedListener(new editTextChangeListener(save,entry,subEntry,customerPayment));
        subEntry.addTextChangedListener(new editTextChangeListener(save,entry,subEntry,customerPayment));
        customerPayment.addTextChangedListener(new editTextChangeListener(save,entry,subEntry,customerPayment));
        this.getSupportActionBar().setTitle("Edit Cash Tip");
    }

    @Override
    public void onBackPressed() {
        this.setResult(intentCode.INVALID_RESULT_INTENT_CODE);
        finish();
    }

    public String checkIfZeroNeeded(double test){
        String num =  Double.toString(test);
        int i = num.lastIndexOf('.');
        if(test % 1 == 0){
            return ".00";
        } if(i != -1 && num.substring(i + 1).length() == 1){
            return "0";
        }
        else{
            return "";
        }

    }

    public void saveTermination(Entry result){
        Intent intent = new Intent();
        intent.putExtra(intentCode.EDITED_ENTRY_PARCEL, result);
        int position = this.getIntent().getIntExtra("position",0);
        intent.putExtra("position", position);
        double cashTipDifferences = (result.getCustomerPayment()-result.getEntry()) - (initialEntry.getCustomerPayment()-initialEntry.getEntry());
        int cashForgottenDifferences = 0;
        if(initialEntry.isReceiptForgot() && !result.isReceiptForgot()){
            cashForgottenDifferences = -1;
        }
        if(!initialEntry.isReceiptForgot() && result.isReceiptForgot()){
            cashForgottenDifferences = 1;
        }
        intent.putExtra(intentCode.cashTipDifferences, cashTipDifferences);
        intent.putExtra(intentCode.forgottenDifferences, cashForgottenDifferences);
        this.setResult(intentCode.CASH_EDIT_RESULT_INTENT_CODE, intent);
        this.finish();
    }

    public boolean modified (Entry result){
        boolean isModified = false;
        if (result.getEntry() != initialEntry.getEntry()){
            isModified = true;
        }
        else if(isModified != true && !(result.getUnpaidSubEntry() == initialEntry.getUnpaidSubEntry())){
            isModified = true;
        }
        else if(isModified != true && !(result.getCustomerPayment() == initialEntry.getCustomerPayment())){
            isModified = true;
        }

        else if(isModified != true && !result.isReceiptForgot()==initialEntry.isReceiptForgot()){
            isModified = true;
        }
        return isModified;
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

    private void initialSetUp(){
        DecimalFormat df = new DecimalFormat("#.##");
        String zero = "";
        zero = checkIfZeroNeeded(initialEntry.getEntry());
        entry.setText(new DecimalFormat("#.##").format(initialEntry.getEntry()) + zero);
        subEntry.setText(Integer.toString(initialEntry.getUnpaidSubEntry()));
        zero = checkIfZeroNeeded(initialEntry.getCustomerPayment());
        customerPayment.setText(new DecimalFormat("#.##").format(initialEntry.getCustomerPayment()) + zero);
        forgottenTip.setChecked(initialEntry.isReceiptForgot());
    }



    private void Confirmation(final Entry result) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        saveTermination(result);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }

        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Confirm save?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void deleteTermination(){
        Intent intent = new Intent();
        int position = this.getIntent().getIntExtra("position",0);
        intent.putExtra("position", position);
        this.setResult(intentCode.CASH_DELETE_RESULT_INTENT_CODE, intent);
        finish();
    }

    private void deleteConfirmed() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        deleteTermination();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }

        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Confirm Deletion?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }


    public void unmodifiedSaved() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Nothing is modified");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        onBackPressed();
                    }
                });
        alertDialog.show();
    }

    public void cashSave(View view) {
        double chargedAmount;
        int orderNum;
        double payment;
        try {
            try {
                chargedAmount = Double.parseDouble(entry.getText().toString());
                if (!decimalCheck(chargedAmount)) {
                    throw new InvalidInputException(5);
                }
                if (chargedAmount < 0) {
                    throw new InvalidInputException(8);
                }
            } catch (NumberFormatException e) {
                throw new InvalidInputException(1);
            }
            try {
                orderNum = Integer.parseInt(subEntry.getText().toString());
                if (orderNum < 0) {
                    throw new InvalidInputException(8);
                }
            } catch (NumberFormatException e) {
                throw new InvalidInputException(2);
            }
            try {
                payment = Double.parseDouble(customerPayment.getText().toString());
                if (!decimalCheck(payment)) {
                    throw new InvalidInputException(6);
                }
                if (payment < 0) {
                    throw new InvalidInputException(8);
                }
            } catch (NumberFormatException e) {
                throw new InvalidInputException(3);
            }
            Entry result = new Entry(chargedAmount, orderNum, payment, forgottenTip.isChecked());
            if (this.modified(result)) {
                this.Confirmation(result);
            } else {
                this.unmodifiedSaved();
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
                case 5:
                    Toast.makeText(this.getApplicationContext(), "Make sure charged amount has two decimals only for cents", Toast.LENGTH_LONG).show();
                    break;
                case 6:
                    Toast.makeText(this.getApplicationContext(), "Make sure payment amount has two decimals only for cents", Toast.LENGTH_LONG).show();
                    break;
                case 8:
                    Toast.makeText(this.getApplicationContext(), "One of the field is a negative value. Only positive is allowed.", Toast.LENGTH_LONG).show();
                    break;
            }

        }
    }

    public void cashDelete(View view) {
        this.deleteConfirmed();
    }
}
