package io.github.gao23.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private int totalCashNum = 0;
    private int totalComputerNum = 0;
    private int forgottenReceipt = 0;
    private int cashTipNum = 0;
    private double totalComputerEarning = 0.0;
    private double totalCashEarning = 0.0;
    private  ListView list;
    private ArrayList<Entry> todayEntry;
    private entryAdaptor entryArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = (ListView) findViewById(R.id.listView);
        todayEntry = new ArrayList<Entry>();
        entryArrayAdapter = new entryAdaptor(this, todayEntry);
        list.setAdapter(entryArrayAdapter);
        list.setOnItemClickListener(new itemListener());
    }

    public void onClick(View v){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, NewTipActivity.class);
        startActivityForResult(intent, intentCode.PASS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == intentCode.CHECK) {
            Entry entry = data.getParcelableExtra(intentCode.parb);
            if(entry.isPaid()){
                this.addComputerTip(entry);
                totalComputerNum +=1;
                if(entry.isCashTip()){
                    cashTipNum += 1;
                }
                totalComputerEarning += entry.getEntry();
                this.updateComputerSummary();
                Toast.makeText(this.getApplicationContext(), "New computer entry saved", Toast.LENGTH_LONG).show();
            }
            else{
                this.addCashTip(entry);
                totalCashNum += 1;
                if(entry.isReceiptForgot()){
                    forgottenReceipt += 1;
                }
                totalCashEarning += entry.getCustomerPayment()-entry.getEntry();
                this.updateCashSummary();
                Toast.makeText(this.getApplicationContext(), "New cash entry saved", Toast.LENGTH_LONG).show();
            }
            entryArrayAdapter.notifyDataSetChanged();
        }

        else if(resultCode==intentCode.COMPUTERCHECK){
            double tipDifferences = data.getDoubleExtra(intentCode.compTipDifferences,0.0);
            int isCashDifferences = data.getIntExtra(intentCode.isCashTipDifferences,0);
            totalComputerEarning += tipDifferences;
            cashTipNum += isCashDifferences;
            int position = data.getIntExtra("position",0);
            this.todayEntry.remove(position);
            this.todayEntry.add(position, (Entry) data.getParcelableExtra(intentCode.parb2));
            this.updateComputerSummary();
            this.entryArrayAdapter.notifyDataSetChanged();
            Toast.makeText(this.getApplicationContext(), "Entry Modified", Toast.LENGTH_LONG).show();
        }

       else if(resultCode == intentCode.COMPUTERDELETE){
            int position = data.getIntExtra("position",0);
            totalComputerEarning -= this.todayEntry.get(position).getEntry();
            totalComputerNum -=1;
            if( this.todayEntry.get(position).isCashTip()){
                cashTipNum -= 1;
            }
            this.todayEntry.remove(position);
            if(totalComputerNum == 0){
                todayEntry.remove(0);
            }
            else {
                this.updateComputerSummary();
            }
            entryArrayAdapter.notifyDataSetChanged();
            Toast.makeText(this.getApplicationContext(), "Successfully Removed", Toast.LENGTH_LONG).show();
        }

        else if(resultCode == intentCode.CASHCHECK){
            double tipDifferences = data.getDoubleExtra(intentCode.cashTipDifferences,0.0);
            int forgottenDifferences = data.getIntExtra(intentCode.forgottenDifferences,0);
            totalCashEarning += tipDifferences;
            forgottenReceipt += forgottenDifferences;
            int position = data.getIntExtra("position",0);
            this.todayEntry.remove(position);
            this.addCashTip((Entry)data.getParcelableExtra(intentCode.parb2));
            this.updateCashSummary();
            this.entryArrayAdapter.notifyDataSetChanged();
            Toast.makeText(this.getApplicationContext(), "Entry Modified", Toast.LENGTH_LONG).show();
        }

        else if(resultCode == intentCode.CASHDELETE){
            int position = data.getIntExtra("position",0);
            totalCashEarning -= (this.todayEntry.get(position).getCustomerPayment() - this.todayEntry.get(position).getEntry());
            totalCashNum -=1;
            if( this.todayEntry.get(position).isReceiptForgot()){
                forgottenReceipt -= 1;
            }
            this.todayEntry.remove(position);
            if(totalCashNum == 0){
                todayEntry.remove(todayEntry.size()-1);
            }
            else {
                this.updateCashSummary();
            }
            entryArrayAdapter.notifyDataSetChanged();
            Toast.makeText(this.getApplicationContext(), "Successfully Removed", Toast.LENGTH_LONG).show();
        }


        else if(resultCode==intentCode.INVALID){
            Toast.makeText(this.getApplicationContext(), "Action Canceled", Toast.LENGTH_LONG).show();
            return;
        }
    }


    public void addComputerTip(Entry entry){
        if(totalComputerNum == 0){
            this.todayEntry.add(0,new Entry(""));
        }
        todayEntry.add(1, entry);
    }

    public void addCashTip(Entry entry) {
        if (totalCashNum == 0) {
            this.todayEntry.add(new Entry(""));
        }
            for (int i = totalComputerNum + 1; i < todayEntry.size(); i++) {
                if (entry.getUnpaidSubEntry() < todayEntry.get(i).getUnpaidSubEntry()) {
                    todayEntry.add(i, entry);
                    break;
                }
            }


        if (!this.todayEntry.contains(entry)) {
            this.todayEntry.add(entry);
        }

    }

    public void updateComputerSummary(){
        String zero = "";
        zero = this.entryArrayAdapter.checkIfZeroNeeded(Double.parseDouble(new DecimalFormat("#.##").format(totalComputerEarning)));
        String result = new DecimalFormat("#.##").format(totalComputerEarning) + zero;
        entryArrayAdapter.getItem(0).setSummaryMessage("Total Computer Orders: " + Integer.toString(totalComputerNum)  + "\nCash Tip: " + Integer.toString(cashTipNum) + "\nTotal Computer Earnings: $" + result);
    }

    public void updateCashSummary(){
        if(totalComputerNum == 0){
            String zero = "";
            zero = this.entryArrayAdapter.checkIfZeroNeeded(Double.parseDouble(new DecimalFormat("#.##").format(totalCashEarning)));
            String result = new DecimalFormat("#.##").format(totalCashEarning) + zero;
            entryArrayAdapter.getItem(0).setSummaryMessage("Total Cash Orders: " + Integer.toString(totalCashNum) + "\nForgotten Receipt: " + Integer.toString(forgottenReceipt) +"\nTotal Cash Earnings: $" + result);
        }
        else{
            String zero = "";
            zero = this.entryArrayAdapter.checkIfZeroNeeded(Double.parseDouble(new DecimalFormat("#.##").format(totalCashEarning)));
            String result = new DecimalFormat("#.##").format(totalCashEarning) + zero;
            entryArrayAdapter.getItem(totalComputerNum+1).setSummaryMessage("Total Cash Orders: " + Integer.toString(totalCashNum) + "\nForgotten Receipt: " + Integer.toString(forgottenReceipt)  +"\nTotal Cash Earnings: $" + result);
        }
    }

        private  class itemListener implements AdapterView.OnItemClickListener{
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                  if(!entryArrayAdapter.getItem(position).getSummaryFlag() && entryArrayAdapter.getItem(position).isPaid()){
                      Intent intent = new Intent();
                      intent.putExtra("position", position);
                      intent.putExtra(intentCode.parb, entryArrayAdapter.getItem(position));
                      intent.setClass(MainActivity.this, computerEditActivity.class);
                      startActivityForResult(intent, intentCode.PASS);
                  }
                else if(!entryArrayAdapter.getItem(position).getSummaryFlag() && !entryArrayAdapter.getItem(position).isPaid()){
                      Intent intent = new Intent();
                      intent.putExtra("position", position);
                      intent.putExtra(intentCode.parb, entryArrayAdapter.getItem(position));
                      intent.setClass(MainActivity.this, cashEditActivity.class);
                     startActivityForResult(intent, intentCode.PASS);
                }
                else if(!entryArrayAdapter.getItem(position).getSummaryFlag()){
                      return;
                  }
            }
        }


}
