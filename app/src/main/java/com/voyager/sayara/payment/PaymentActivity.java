package com.voyager.sayara.payment;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.voyager.sayara.R;

import butterknife.BindView;

/**
 * Created by User on 05-Jun-18.
 */

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.paymentToolbar)
    Toolbar paymentToolbar;
    @BindView(R.id.paymentCash)
    LinearLayout paymentCash;


    public PaymentActivity() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        System.out.println("PaymentActivity");
        setSupportActionBar(paymentToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.payment));
        paymentCash.setOnClickListener(this);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.paymentCash:
                Toast.makeText(this, "Please wait for this feature ", Toast.LENGTH_SHORT).show();
                break;

        }
    }
}