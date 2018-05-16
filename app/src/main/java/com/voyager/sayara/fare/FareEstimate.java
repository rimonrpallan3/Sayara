package com.voyager.sayara.fare;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.voyager.sayara.PulsatingActivity.PulsatingActivity;
import com.voyager.sayara.R;
import com.voyager.sayara.common.Helper;
import com.voyager.sayara.fare.presenter.FarePresenter;
import com.voyager.sayara.fare.presenter.IFarePresenter;
import com.voyager.sayara.fare.view.IFareView;
import com.voyager.sayara.fragments.map.model.PlaceDetailModel;
import com.voyager.sayara.registerpage.model.UserDetails;


/**
 * Created by User on 03-Oct-17.
 */

public class FareEstimate extends AppCompatActivity implements View.OnClickListener,IFareView {

    TextView startOrgin;
    TextView endDestin;
    TextView startAddress;
    TextView endAddress;
    TextView startDistanceKm;
    TextView endDistanceKm;
    TextView fairDistKm;
    TextView fairCost;

    Button btnAccept;
    Button btnReject;
    Toolbar fareToolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    PlaceDetailModel placeDetailModel;

    public String nameStart = "";
    public String nameStartLoc = "";
    public String nameEnd = "";
    public String nameEndLoc = "";
    public String distanceKm = "";
    public String addressStart = "";
    public String addressEnd = "";
    public String driveClassType = "";
    public String costFair = "";
    public String costFairSet = "";
    public String driveCarType = "";
    public String driveCarId = "";
    IFarePresenter iFarePresenter;
    int userId;
    UserDetails userDetails;

    public FareEstimate() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fare_estimate);
        System.out.println("FareEstimate");

        Intent intent =getIntent();
        Bundle bundle = intent.getExtras();
        if(!bundle.isEmpty()){
            nameStart = bundle.getString("nameStart",nameStart);
            nameEnd = bundle.getString("nameEnd",nameEnd);
            nameStartLoc = bundle.getString("nameStartLoc",nameStartLoc);
            nameEndLoc = bundle.getString("nameEndLoc",nameEndLoc);
            distanceKm = bundle.getString("distanceKm",distanceKm);
            driveClassType = bundle.getString("driveClassType",driveClassType);
            costFair = bundle.getString("cost",costFair);
            driveCarType = bundle.getString("driveCarType",driveCarType);
            driveCarId = bundle.getString("driveCarId", driveCarId);
            userId = bundle.getInt("userId", 0);
            userDetails = bundle.getParcelable("UserDetails");
            costFairSet ="BD "+costFair;
            System.out.println(
                    "nameStart :"+nameStart+
                    "username :"+userDetails.getFName()+
                    " ,nameEnd :"+nameEnd+
                    " ,nameStartLoc :"+nameStartLoc+
                    " ,nameEndLoc :"+nameEndLoc+
                    " ,distanceKm :"+distanceKm+
                    " ,driveClassType :"+driveClassType+
                    " ,costFairSet :"+costFairSet+
                    " ,userId :"+userDetails.getUserID()+
                    " ,driveCarType :"+driveCarType
            );
        }


        startOrgin = (TextView) findViewById(R.id.startOrgin);
        endDestin = (TextView) findViewById(R.id.endDestin);
        startAddress = (TextView) findViewById(R.id.startAddress);
        endAddress = (TextView) findViewById(R.id.endAddress);
        startDistanceKm = (TextView) findViewById(R.id.startDistanceKm);
        endDistanceKm = (TextView) findViewById(R.id.endDistanceKm);
        fairDistKm = (TextView) findViewById(R.id.fairDistKm);
        fairCost = (TextView) findViewById(R.id.fairCost);
        btnAccept = (Button) findViewById(R.id.btnAccept);
        btnReject = (Button) findViewById(R.id.btnReject);
        fareToolbar = (Toolbar) findViewById(R.id.fareToolbar);
        setSupportActionBar(fareToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.fare));
        fareToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black));
        btnAccept.setOnClickListener(this);
        btnReject.setOnClickListener(this);
        startOrgin.setText(nameStart);
        endDestin.setText(nameEnd);
        startAddress.setText(addressStart);
        endAddress.setText(addressEnd);
        startDistanceKm.setText(addressEnd + " away");
        endDistanceKm.setText(distanceKm);
        fairDistKm.setText(distanceKm);
        fairCost.setText(costFairSet);
        iFarePresenter = new FarePresenter(this);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAccept:
                Toast.makeText(this, "Please wait for this feature.", Toast.LENGTH_SHORT).show();
                iFarePresenter.startTrip(userDetails.getUserID(),userDetails.getFName(),nameStartLoc,nameStart,nameEndLoc,nameEnd,distanceKm,costFairSet,driveCarId,"online");
                break;
            case R.id.btnReject:
                finish();
                break;

        }
    }

    @Override
    public void waitToGetDriver() {
        Intent resultIntent = new Intent(this, PulsatingActivity.class);
        resultIntent.putExtra("waitingForDriver", "true");
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        setResult(RESULT_OK, resultIntent);
        startActivity (resultIntent);
        finish();
    }
}