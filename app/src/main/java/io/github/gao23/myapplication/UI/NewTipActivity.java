package io.github.gao23.myapplication.UI;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.content.Context;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.content.res.Resources.Theme;
import android.widget.TextView;
import io.github.gao23.myapplication.Logic.Entry;
import io.github.gao23.myapplication.Logic.intentCode;
import io.github.gao23.myapplication.R;
import io.github.gao23.myapplication.UI.Fragments.*;


public class NewTipActivity extends AppCompatActivity  {
    private cashFragments cashFrag;
    private computerFragments computerFrag;

    /***
     * this is the old setup, it adds the background and everything
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_tip_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        this.setUpSpinner(toolbar);

        computerFrag = computerFragments.newInstance(0);
        cashFrag = cashFragments.newInstance(1);


    }

    /***
     * I am not too sure about what these code exactly does, I will need to learn spinner a bit
     * @param toolbar is the action bar that is created in the oncreate. It is a component of the new tip layout
     */
    private void setUpSpinner(Toolbar toolbar){
        // Setup spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(new MyAdapter(
                toolbar.getContext(),
                new String[]{
                        "Computer Tip",
                        "Cash Tip"
                }));

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // When the given dropdown item is selected, show its contents in the
                // container view.
                // container seems to be an empty view and the fragment manager seemed to replace everything inside the container with the fragment
                if (position == 0) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, computerFrag)
                            .commit();
                }

                else if(position == 1){
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, cashFrag)
                            .commit();
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    /* we don't need menu but lets keep it just in case
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.spinner_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    } */


    /***
     * this is similar to the entry adapter, all it does is add the item in a scroll like view to the spinner
     * we need to use it to show the title of the item on the spinner. The title of the spinner is set in the setup spinner method. Get item position returns the title
     */
    private static class MyAdapter extends ArrayAdapter<String> implements ThemedSpinnerAdapter {
        private final ThemedSpinnerAdapter.Helper mDropDownHelper;

        public MyAdapter(Context context, String[] objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
            mDropDownHelper = new ThemedSpinnerAdapter.Helper(context);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                // Inflate the drop down using the helper's LayoutInflater
                LayoutInflater inflater = mDropDownHelper.getDropDownViewInflater();
                view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            } else {
                view = convertView;
            }

            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(getItem(position));

            return view;
        }

        @Override
        public Theme getDropDownViewTheme() {
            return mDropDownHelper.getDropDownViewTheme();
        }

        @Override
        public void setDropDownViewTheme(Theme theme) {
            mDropDownHelper.setDropDownViewTheme(theme);
        }
    }






    /***
     * this is called whenever the user tapped on back pressed
     * The result call passed back is invalid which will do nothing but showing a toast
     */
    @Override
    public void onBackPressed() {
        this.setResult(intentCode.INVALID_RESULT_INTENT_CODE);
        finish();
    }

    /***
     * this returns the new user inputed entry back to the main activity to be added to the entry adaptor list
     * @param result is the final new entry
     */
    private void terminate(Entry result){
          Intent intent = new Intent();
          intent.putExtra(intentCode.ENTRY_PARCEL, result);
          this.setResult(intentCode.VALID_RESULT_INTENT_CODE, intent);
          this.finish();
      }


 }



