package io.github.gao23.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
    private ArrayList<Entry> savedEntry;
    private entryAdaptor entryArrayAdapter;

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void fieldReset(){
        totalCashNum = 0;
        totalComputerNum = 0;
        forgottenReceipt = 0;
        cashTipNum = 0;
        totalCashEarning = 0;
        totalComputerEarning = 0;
    }

    private void savePerferences (){
        SharedPreferences save = getSharedPreferences("entry_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = save.edit();
        editor.putInt("totalCashNum", totalCashNum);
        editor.putInt("totalComputerNum", totalComputerNum);
        editor.putInt("forgottenReceipt", forgottenReceipt);
        editor.putInt("cashTipNum", cashTipNum);
        editor.putLong("totalComputerEarning",Double.doubleToRawLongBits(totalComputerEarning));
        editor.putLong("totalCashEarning",Double.doubleToRawLongBits(totalCashEarning));
        ComplexPreferences comPer = ComplexPreferences.getComplexPreferences(this, "entry_prefs", 0);
        savedEntry = new ArrayList<Entry>();
        savedEntry.addAll(todayEntry);
        comPer.putObject("entry_value", savedEntry);
        comPer.commit();
        editor.commit();
    }

    private void restorePerferences(){
        SharedPreferences restore = getSharedPreferences("entry_data", Context.MODE_PRIVATE);
        totalCashNum = restore.getInt("totalCashNum", 0);
        totalComputerNum = restore.getInt("totalComputerNum", 0);
        forgottenReceipt = restore.getInt("forgottenReceipt", 0);
        cashTipNum = restore.getInt("cashTipNum", 0);
        totalComputerEarning =  Double.longBitsToDouble(restore.getLong("totalComputerEarning", Double.doubleToRawLongBits(0)));
        totalCashEarning =  Double.longBitsToDouble(restore.getLong("totalCashEarning", Double.doubleToRawLongBits(0)));
        ComplexPreferences comPer = ComplexPreferences.getComplexPreferences(this, "entry_prefs", 0);
        savedEntry = comPer.getObject("entry_value",ArrayList.class);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        list = (ListView) findViewById(R.id.listView);
        todayEntry = new ArrayList<Entry>();
        this.restorePerferences();
        entryArrayAdapter = new entryAdaptor(this, todayEntry);
        list.setAdapter(entryArrayAdapter);
        list.setOnItemClickListener(new itemListener());
        if(savedEntry!=null){
            todayEntry.addAll(savedEntry);
        }
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.getSupportActionBar().setIcon(R.mipmap.ic_launcher);
    }

    public void onClick(View v){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, NewTipActivity.class);
        startActivityForResult(intent, intentCode.GENERAL_NEW_ACTIVITY_INTENT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == intentCode.VALID_RESULT_INTENT_CODE) {
            Entry entry = data.getParcelableExtra(intentCode.ENTRY_PARCEL);
            if(entry.isPaid()){
                this.addComputerTip(entry);
                totalComputerNum +=1;
                if(entry.isCashTip()){
                    cashTipNum += 1;
                }
                totalComputerEarning += entry.getEntry();
                this.updateComputerSummary();
                this.savePerferences();
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
                this.savePerferences();
                Toast.makeText(this.getApplicationContext(), "New cash entry saved", Toast.LENGTH_LONG).show();
                this.savePerferences();
            }
            entryArrayAdapter.notifyDataSetChanged();
        }

        else if(resultCode==intentCode.COMPUTER_EDIT_RESULT_INTENT_CODE){
            double tipDifferences = data.getDoubleExtra(intentCode.compTipDifferences,0.0);
            int isCashDifferences = data.getIntExtra(intentCode.isCashTipDifferences,0);
            totalComputerEarning += tipDifferences;
            cashTipNum += isCashDifferences;
            int position = data.getIntExtra("position",0);
            this.todayEntry.remove(position);
            this.todayEntry.add(position, (Entry) data.getParcelableExtra(intentCode.EDITED_ENTRY_PARCEL));
            this.updateComputerSummary();
            this.entryArrayAdapter.notifyDataSetChanged();
            Toast.makeText(this.getApplicationContext(), "Entry Modified", Toast.LENGTH_LONG).show();
            this.savePerferences();
        }

       else if(resultCode == intentCode.COMPUTER_DELETE_RESULT_INTENT_CODE){
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
            this.savePerferences();
        }

        else if(resultCode == intentCode.CASH_EDIT_RESULT_INTENT_CODE){
            double tipDifferences = data.getDoubleExtra(intentCode.cashTipDifferences,0.0);
            int forgottenDifferences = data.getIntExtra(intentCode.forgottenDifferences,0);
            totalCashEarning += tipDifferences;
            forgottenReceipt += forgottenDifferences;
            int position = data.getIntExtra("position",0);
            this.todayEntry.remove(position);
            this.addCashTip((Entry)data.getParcelableExtra(intentCode.EDITED_ENTRY_PARCEL));
            this.updateCashSummary();
            this.entryArrayAdapter.notifyDataSetChanged();
            Toast.makeText(this.getApplicationContext(), "Entry Modified", Toast.LENGTH_LONG).show();
            this.savePerferences();
        }

        else if(resultCode == intentCode.CASH_DELETE_RESULT_INTENT_CODE){
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
            this.savePerferences();
        }


        else if(resultCode==intentCode.INVALID_RESULT_INTENT_CODE){
            Toast.makeText(this.getApplicationContext(), "Action Canceled", Toast.LENGTH_LONG).show();
            return;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.calculate:{
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, calculateActivity.class);
                intent.putParcelableArrayListExtra("list",todayEntry);
                intent.putExtra("Total", totalCashNum + totalComputerNum);
                startActivity(intent);
                break;
            }
            case R.id.clear :{
                if(todayEntry.isEmpty()) {
                    AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("Entry is already empty");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                else {
                    this.clearConfirmation();
                }
                break;
            }
            case R.id.exit:{
                 onBackPressed();
                break;
            }
            case R.id.about:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    private void clearConfirmation() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        todayEntry.clear();
                        fieldReset();
                        entryArrayAdapter.notifyDataSetChanged();
                        savePerferences();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }

        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Confirm clear?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
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
                      intent.putExtra(intentCode.ENTRY_PARCEL, entryArrayAdapter.getItem(position));
                      intent.setClass(MainActivity.this, computerEditActivity.class);
                      startActivityForResult(intent, intentCode.GENERAL_NEW_ACTIVITY_INTENT_CODE);
                  }
                else if(!entryArrayAdapter.getItem(position).getSummaryFlag() && !entryArrayAdapter.getItem(position).isPaid()){
                      Intent intent = new Intent();
                      intent.putExtra("position", position);
                      intent.putExtra(intentCode.ENTRY_PARCEL, entryArrayAdapter.getItem(position));
                      intent.setClass(MainActivity.this, cashEditActivity.class);
                     startActivityForResult(intent, intentCode.GENERAL_NEW_ACTIVITY_INTENT_CODE);
                }
                else if(!entryArrayAdapter.getItem(position).getSummaryFlag()){
                      return;
                  }
            }
        }


}
