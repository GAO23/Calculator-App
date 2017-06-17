package io.github.gao23.myapplication;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class calculateActivity extends AppCompatActivity {
    private TextView text;
    private Button ok;
    private ArrayList<Entry> todayEntery;
    private int cashTipNum = 0;
    private double totalEarning = 0;
    private double totalComputer = 0;
    private double totalCash = 0;
    private double cashTip = 0;
    private double totalOwed = 0;
    private int total;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_calculate);
        text = (TextView) findViewById(R.id.calculateResultText);
        ok = (Button) findViewById(R.id.buttonOK);
        todayEntery = this.getIntent().getParcelableArrayListExtra("list");
        total = getIntent().getIntExtra("Total",1);
        for(Entry entry: todayEntery){
            if(!entry.getSummaryFlag()){
               if(entry.isPaid() && entry.isCashTip()){
                   cashTipNum+=1;
                   cashTip+=entry.getEntry();
                   totalComputer+=entry.getEntry();
                   totalEarning+=entry.getEntry();
               }
               else if(entry.isPaid() && !entry.isCashTip()){
                   totalComputer+=entry.getEntry();
                   totalEarning+=entry.getEntry();
               }

               else if(!entry.isPaid()){
                   totalEarning+=(entry.getCustomerPayment()-entry.getEntry());
                   totalCash+=(entry.getCustomerPayment()-entry.getEntry());
                   totalOwed += entry.getEntry();
               }
            }
        }
        DecimalFormat df = new DecimalFormat("#.##");
        String zero = "";
        String result = "";
        zero = this.checkIfZeroNeeded(totalComputer);
        result += "Today's total computer earning is $" + df.format(totalComputer) + zero + "\nToday's total computer cash tip is " + cashTipNum;
        zero = this.checkIfZeroNeeded(cashTip);
        result+="\nToday's total computer cash tip earning amount is $" + df.format(cashTip) + zero+"\n";
        zero = this.checkIfZeroNeeded(totalCash);
        result+="\nToday's total cash earning is $ " + df.format(totalCash) + zero;
        zero = this.checkIfZeroNeeded(totalOwed);
        result +="\nToday's total customer charged amount is $" + df.format(totalOwed) + zero+"\n";
        zero = this.checkIfZeroNeeded(totalOwed-(totalComputer-cashTip));
        if(totalOwed-(totalComputer-cashTip)<0){
            result += "\nStore owes you $" + df.format((totalOwed - (totalComputer - cashTip))*-1)+ zero+"\n";
        }
        else {
            result += "\nTotal owed to the store is customer charged amount - (computer earnings - total computer cash tip earnings) = $" + df.format((totalOwed - (totalComputer - cashTip))) + zero+"\n";
        }
        zero = this.checkIfZeroNeeded(totalEarning);
        result += "\nToday's total earning is $" + df.format(totalEarning) + zero;
        zero = this.checkIfZeroNeeded(totalEarning/total);
        result += "\nTip per delivery is $" + df.format(totalEarning/total) + zero;
        if(todayEntery.isEmpty()){
            text.setText("There is no entry");
        }
        else{
            text.setText(result);
        }
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
        finish();
    }

    public void okClicked(View view) {
        this.onBackPressed();
    }
}
