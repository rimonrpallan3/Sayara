package com.voyager.sayara.yourtripdetail;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.voyager.sayara.DriverTripDetailPage.DriverDetailPage;
import com.voyager.sayara.R;
import com.voyager.sayara.costom.CircleImageView;
import com.voyager.sayara.triphistroty.model.TripDetails;


/**
 * Created by User on 10-Jan-18.
 */

public class YourTripDetail extends AppCompatActivity implements View.OnClickListener {

    ImageView mapYTD;

    Toolbar YTDToolbar;
    Intent intent;
    TripDetails tripDetails;

    public TextView YTDDate;
    public TextView YTDTime;
    public TextView YTDDBAmount;
    public TextView YTDDriverName;
    public TextView YTDCarName;
    public TextView YTDPaymentMode;
    public ImageView YTDStar1;
    public ImageView YTDStar2;
    public ImageView YTDStar3;
    public ImageView YTDStar4;
    public CircleImageView YTDDriverProfileImg;
    public CardView YTDDriverProfileCard;
    String time;
    String date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yout_trip_detail);

        //find view
        YTDDate = (TextView) findViewById(R.id.YTDDate);
        YTDTime = (TextView) findViewById(R.id.YTDTime);
        YTDDBAmount = (TextView) findViewById(R.id.YTDDBAmount);
        YTDCarName = (TextView) findViewById(R.id.YTDCarName);
        YTDDriverName = (TextView) findViewById(R.id.YTDDriverName);
        YTDPaymentMode = (TextView) findViewById(R.id.YTDPaymentMode);
        YTDStar1 = (ImageView) findViewById(R.id.YTDStar1);
        YTDStar2 = (ImageView) findViewById(R.id.YTDStar2);
        YTDStar3 = (ImageView) findViewById(R.id.YTDStar3);
        YTDStar4 = (ImageView) findViewById(R.id.YTDStar4);
        YTDDriverProfileCard = (CardView) findViewById(R.id.YTDDriverProfileCard);
        mapYTD = (ImageView) this.findViewById(R.id.mapYTD);
        YTDDriverProfileImg = (CircleImageView) this.findViewById(R.id.YTDDriverProfileImg);
        YTDToolbar = (Toolbar) this.findViewById(R.id.YTDToolbar);

        mapYTD.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.map));

        setSupportActionBar(YTDToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        intent = getIntent();
        tripDetails = (TripDetails) intent.getParcelableExtra("TripDetails");
        if (tripDetails != null) {
            Gson gson = new Gson();
            String json = gson.toJson(tripDetails);
            System.out.println("YourTripDetail -- YourTripDetail- name : " + json);
            //---------------------------------------------------------
            String[] DateTime = tripDetails.getDateTime().split("\\s");
            for (int x = 0; x < DateTime.length; x++) {
                System.out.println("x : " + x + " = " + DateTime[x]);
                date = DateTime[0];
                YTDDate.setText(date);
                time = DateTime[1];
                YTDTime.setText(time);
            }
            YTDDBAmount.setText(tripDetails.getCash());
            YTDCarName.setText(tripDetails.getDriverCar());
            YTDPaymentMode.setText(tripDetails.getCashMode());
            YTDDriverName.setText(tripDetails.getDriverName());
            Picasso.with(this)
                    .load(tripDetails.getTripImgUrl())
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .resize(0, 200)
                    .into(mapYTD, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            //Try again online if cache failed
                            Picasso.with(getParent())
                                    .load(tripDetails.getTripImgUrl())
                                    .error(R.drawable.profile)
                                    .resize(0, 200)
                                    .into(mapYTD, new Callback() {
                                        @Override
                                        public void onSuccess() {

                                        }

                                        @Override
                                        public void onError() {
                                            Log.v("Picasso", "Could not fetch image");
                                        }
                                    });
                        }
                    });
            Picasso.with(this)
                    .load(tripDetails.getDriverPhotoUrl())
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .resize(0, 200)
                    .into(YTDDriverProfileImg, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            //Try again online if cache failed
                            Picasso.with(getParent())
                                    .load(tripDetails.getDriverPhotoUrl())
                                    .error(R.drawable.profile)
                                    .resize(0, 200)
                                    .into(YTDDriverProfileImg, new Callback() {
                                        @Override
                                        public void onSuccess() {

                                        }

                                        @Override
                                        public void onError() {
                                            Log.v("Picasso", "Could not fetch image");
                                        }
                                    });
                        }
                    });

        }
        YTDDriverProfileCard.setOnClickListener(this);


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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.YTDDriverProfileCard:
                Intent intent = new Intent(this, DriverDetailPage.class);
                intent.putExtra("TripDetails", tripDetails);
                startActivity(intent);
                break;
        }
    }
}
