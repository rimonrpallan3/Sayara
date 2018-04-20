package com.voyager.sayara.triphistroty;

import android.content.Intent;
import android.os.Bundle;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.voyager.sayara.R;
import com.voyager.sayara.registerpage.model.UserDetails;
import com.voyager.sayara.triphistroty.adapter.TripAdapter;
import com.voyager.sayara.triphistroty.model.TripDetails;
import com.voyager.sayara.triphistroty.presenter.ITripPresenter;
import com.voyager.sayara.triphistroty.presenter.TripPresenter;
import com.voyager.sayara.triphistroty.view.ITripView;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;


/**
 * Created by User on 26-Dec-17.
 */

public class TripHistory extends AppCompatActivity implements View.OnClickListener,ITripView {

    // List<TripDetails>
    private List<TripDetails> tripList = new ArrayList<>();
    private RecyclerView tripRecycleView;
    private TripAdapter tripAdapter;
    public ITripPresenter iTripPresenter;

    Toolbar tripHistoryToolbar;
    ImageView mapImageView;
    UserDetails userDetails;
    int userID;
    Intent intent;
    GifImageView loader;

    public TripHistory() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_history);
        System.out.println("TripHistory");
        tripHistoryToolbar = (Toolbar) findViewById(R.id.tripHistoryToolbar);
        setSupportActionBar(tripHistoryToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        tripHistoryToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black));
        loader = (GifImageView) findViewById(R.id.loaderImageView);

        tripRecycleView = (RecyclerView) findViewById(R.id.tripRecycleView);
        iTripPresenter = new TripPresenter(this);
        iTripPresenter.getUserDetailParcelable();

        intent = getIntent();
    }

    @Override
    public void onDataSet(List<TripDetails> tripDetails, int userID) {
        tripAdapter = new TripAdapter(tripDetails, this, userID);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        tripRecycleView.setLayoutManager(mLayoutManager);
        tripRecycleView.setItemAnimator(new DefaultItemAnimator());
        tripRecycleView.setAdapter(tripAdapter);
        loader.setVisibility(View.GONE);

    }

    @Override
    public void getParcelable() {
        userDetails = (UserDetails) intent.getParcelableExtra("UserDetails");
        if (userDetails != null) {
            Gson gson = new Gson();
            String json = gson.toJson(userDetails);
            userID = userDetails.getUserID();
            iTripPresenter.loadData(userID);
            System.out.println("TripHistory -- TripHistory- name : " + json);
        }
    }

    private void prepareTripDetailsData() {
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAccept:
                Toast.makeText(this, "Please wait for this feature.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnReject:
                finish();
                break;
        }
    }


}
