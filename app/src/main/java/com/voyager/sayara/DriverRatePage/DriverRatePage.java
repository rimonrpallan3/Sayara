package com.voyager.sayara.DriverRatePage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.voyager.sayara.R;


/**
 * Created by User on 8/30/2017.
 */

 public class DriverRatePage extends AppCompatActivity  {


    Toolbar DRPToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_rate_page);
        DRPToolbar = (Toolbar) this.findViewById(R.id.DRPToolbar);
        setSupportActionBar(DRPToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));

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
