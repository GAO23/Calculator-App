package io.github.gao23.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class cashEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_edit);

    }

    @Override
    public void onBackPressed() {
        this.setResult(intentCode.INVALID);
        finish();
    }
}
