package io.github.gao23.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DecimalFormat;

public class computerEditActivity extends AppCompatActivity {
    EditText entry;
    EditText subEntry;
    CheckBox cashTip;
    Button save;
    Entry initialEntry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_computer_edit);
        initialEntry = this.getIntent().getParcelableExtra(intentCode.ENTRY_PARCEL);
        entry = (EditText) findViewById(R.id.computerEditText1);
        entry.setHint("Enter new tip amount");
        subEntry = (EditText) findViewById(R.id.computerEditTExt2);
        subEntry.setHint("Enter new address");
        cashTip = (CheckBox) findViewById(R.id.computerCheckBox1);
        save = (Button) findViewById(R.id.computerSave);
        initialSetUp();
        subEntry.addTextChangedListener(new editTextChangeListener(save, entry, subEntry));
        entry.addTextChangedListener(new editTextChangeListener(save, entry, subEntry));
        this.getSupportActionBar().setTitle("Edit Computer Tip");
    }

    private void initialSetUp(){
       DecimalFormat df = new DecimalFormat("#.##");
        String zero = "";
        zero = checkIfZeroNeeded(initialEntry.getEntry());
        entry.setText(new DecimalFormat("#.##").format(initialEntry.getEntry()) + zero);
        subEntry.setText(initialEntry.getPaidSubEntry());
        cashTip.setText("Check the box if this is cash tip");
        cashTip.setChecked(initialEntry.isCashTip());
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

    @Override
    public void onBackPressed() {
        this.setResult(intentCode.INVALID_RESULT_INTENT_CODE);
        finish();
    }

    public void computerSave(View view) {
        try {
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
            Entry result = new Entry(tipAmount, orderAddress, isCashTip);
            if(this.modified(result)){
                this.Confirmation(result);
            }
            else{
                this.unmodifiedSaved();
            }
        }catch (InvalidInputException e) {
            int errorCode = e.getErrorCode();
            switch (errorCode) {
                case 4:
                    Toast.makeText(this.getApplicationContext(), "Invalid tip amount", Toast.LENGTH_LONG).show();
                    break;
                case 7:
                    Toast.makeText(this.getApplicationContext(), "Make sure tip amount has two decimals only for cents", Toast.LENGTH_LONG).show();
                    break;
                case 8: Toast.makeText(this.getApplicationContext(), "One of the field is a negative value. Only positive is allowed.", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    public boolean modified (Entry result){
        boolean isModified = false;
        if (result.getEntry() != initialEntry.getEntry()){
            isModified = true;
        }
        else if(isModified != true && !result.getPaidSubEntry().equals(initialEntry.getPaidSubEntry())){
            isModified = true;
        }
        else if(isModified != true && !result.isCashTip()==initialEntry.isCashTip()){
            isModified = true;
        }
        return isModified;
    }

    public void computerDelete(View view) {
        this.deleteConfirmed();

    }

    private void terminate(Entry result){
        Intent intent = new Intent();
        intent.putExtra(intentCode.EDITED_ENTRY_PARCEL, result);
        int position = this.getIntent().getIntExtra("position",0)
;        intent.putExtra("position", position);
        double compTipDifferences = result.getEntry()-initialEntry.getEntry();
        int compCashTipDifferences = 0;
        if(initialEntry.isCashTip() && !result.isCashTip()){
            compCashTipDifferences = -1;
        }
        if(!initialEntry.isCashTip() && result.isCashTip()){
            compCashTipDifferences = 1;
        }
        intent.putExtra(intentCode.compTipDifferences, compTipDifferences);
        intent.putExtra(intentCode.isCashTipDifferences, compCashTipDifferences);
        this.setResult(intentCode.COMPUTER_EDIT_RESULT_INTENT_CODE, intent);
        this.finish();
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
    private void Confirmation(final Entry result) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        terminate(result);
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
        this.setResult(intentCode.COMPUTER_DELETE_RESULT_INTENT_CODE, intent);
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


}
