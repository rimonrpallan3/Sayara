package com.voyager.sayara.StartTrip;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.voyager.sayara.R;
import com.voyager.sayara.common.Helper;

/**
 * Created by User on 18-Dec-17.
 */

public class StartTrip extends AppCompatActivity implements View.OnClickListener{

    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    Toolbar startTripToolbar;
    LinearLayout callDriver;
    LinearLayout showLandMark;
    LinearLayout cancelRide;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_trip);
        sharedPrefs = getSharedPreferences(Helper.UserDetails,
                Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();

        startTripToolbar = (Toolbar) findViewById(R.id.startTripToolbar);
        callDriver = (LinearLayout) findViewById(R.id.callDriver);
        showLandMark = (LinearLayout) findViewById(R.id.showLandMark);
        cancelRide = (LinearLayout) findViewById(R.id.cancelRide);
        callDriver.setOnClickListener(this);
        showLandMark.setOnClickListener(this);
        cancelRide.setOnClickListener(this);
        setSupportActionBar(startTripToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.fare));
        startTripToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.callDriver:
                Toast.makeText(this, "Please wait for this feature.callDriver", Toast.LENGTH_SHORT).show();
                break;
            case R.id.showLandMark:
                Toast.makeText(this, "Please wait for this feature.showLandMark", Toast.LENGTH_SHORT).show();
                break;
            case R.id.cancelRide:
                Toast.makeText(this, "Please wait for this feature.cancelRide", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}