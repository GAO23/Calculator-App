package io.github.gao23.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
    Button delete;
    Entry initialEntry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_computer_edit);
        initialEntry = this.getIntent().getParcelableExtra(intentCode.parb2);
        entry = (EditText) findViewById(R.id.computerEditText1);
        entry.setHint("Enter new tip amount");
        subEntry = (EditText) findViewById(R.id.computerEditTExt2);
        subEntry.setHint("Enter new address");
        cashTip = (CheckBox) findViewById(R.id.computerCheckBox1);
        save = (Button) findViewById(R.id.computerSave);
        delete = (Button) findViewById(R.id.computerDelete);
        intialSetUp();
        subEntry.addTextChangedListener(new editTextChangeListener(save, entry, subEntry));
        entry.addTextChangedListener(new editTextChangeListener(save, entry, subEntry));
    }

    private void intialSetUp(){
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
        this.setResult(intentCode.INVALID);
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
            this.terminate(result);
        }catch (InvalidInputException e) {
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
                    Toast.makeText(this.getApplicationContext(), "Make sure tip amount has two decimals only for cents", Toast.LENGTH_LONG).show();
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

    public void computerDelete(View view) {
    }

    private void terminate(Entry result){
        Intent intent = new Intent();
        intent.putExtra(intentCode.parb, result);
        this.setResult(intentCode.CHECK2, intent);
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


}
