package io.github.gao23.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.text.DecimalFormat;

public class cashEditActivity extends AppCompatActivity {
    private EditText entry;
    private EditText subEntry;
    private EditText customerPayment;
    private CheckBox forgottenTip;
    private Entry initialEntry;
    private Button save;
    private Button delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_edit);
        entry = (EditText) findViewById(R.id.cashEditText1);
        subEntry = (EditText) findViewById(R.id.cashEditText2);
        customerPayment = (EditText) findViewById(R.id.cashEditText3);
        forgottenTip = (CheckBox) findViewById(R.id.cashCheckBox);
        initialEntry = this.getIntent().getParcelableExtra(intentCode.parb2);
        save = (Button) findViewById(R.id.cashSaveButton);
        delete = (Button) findViewById(R.id.cashDeleteButton);
        this.intialSetUp();
        entry.addTextChangedListener(new editTextChangeListener(save,entry,subEntry,customerPayment));
        subEntry.addTextChangedListener(new editTextChangeListener(save,entry,subEntry,customerPayment));
        customerPayment.addTextChangedListener(new editTextChangeListener(save,entry,subEntry,customerPayment));
    }

    @Override
    public void onBackPressed() {
        this.setResult(intentCode.INVALID);
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

    private void intialSetUp(){
        DecimalFormat df = new DecimalFormat("#.##");
        String zero = "";
        zero = checkIfZeroNeeded(initialEntry.getEntry());
        entry.setText(new DecimalFormat("#.##").format(initialEntry.getEntry()) + zero);
        subEntry.setText(Integer.toString(initialEntry.getUnpaidSubEntry()));
        zero = checkIfZeroNeeded(initialEntry.getCustomerPayment());
        customerPayment.setText(new DecimalFormat("#.##").format(initialEntry.getCustomerPayment()) + zero);
        forgottenTip.setChecked(initialEntry.isReceiptForgot());
    }
}
