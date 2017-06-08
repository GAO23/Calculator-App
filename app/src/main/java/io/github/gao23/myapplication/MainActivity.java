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
            }
            else{
                this.addCashTip(entry);
                totalCashNum += 1;
                if(entry.isReceiptForgot()){
                    forgottenReceipt += 1;
                }
                totalCashEarning += entry.getCustomerPayment()-entry.getEntry();
                this.updateCashSummary();
            }
            entryArrayAdapter.notifyDataSetChanged();
        }
        else {
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
                      intent.putExtra(intentCode.parb2, entryArrayAdapter.getItem(position));
                      intent.setClass(MainActivity.this, computerEditActivity.class);
                      startActivityForResult(intent, intentCode.PASS);
                  }
                else if(!entryArrayAdapter.getItem(position).getSummaryFlag() && !entryArrayAdapter.getItem(position).isPaid()){
                    Intent intent = new Intent();
                    intent.putExtra(intentCode.parb2, entryArrayAdapter.getItem(position));
                    intent.setClass(MainActivity.this, cashEditActivity.class);
                    startActivityForResult(intent, intentCode.PASS);
                }
                else if(!entryArrayAdapter.getItem(position).getSummaryFlag()){
                      return;
                  }
            }
        }


}
