package com.voyager.sayara.DriverTripDetailPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.voyager.sayara.DriverTripDetailPage.adapter.DTDCommentAdapter;
import com.voyager.sayara.DriverTripDetailPage.model.DTDModel;
import com.voyager.sayara.DriverTripDetailPage.model.Comments;
import com.voyager.sayara.DriverTripDetailPage.presenter.DTDPresenter;
import com.voyager.sayara.DriverTripDetailPage.presenter.IDTDControler;
import com.voyager.sayara.DriverTripDetailPage.view.IDTDView;
import com.voyager.sayara.R;
import com.voyager.sayara.costom.CircleImageView;
import com.voyager.sayara.triphistroty.model.TripDetails;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;


/**
 * Created by User on 8/30/2017.
 */

 public class DriverDetailPage extends AppCompatActivity implements IDTDView {


    Toolbar DRPToolbar;
    GifImageView loader;
    CircleImageView driverImg;
    private RecyclerView userComplementRecycle;
    private DTDCommentAdapter dtdCommentAdapter;
    public IDTDControler idtdControler;
    LinearLayout loaderImageViewLayout;
    private List<Comments> userCommentlist = new ArrayList<>();
    List<DTDModel> dtdModel = new ArrayList<>();
    TripDetails tripDetails;
    TextView driverName;
    TextView driverRating;
    TextView driverWorkDays;
    TextView knowLanguages;
    Intent intent;
    String rating;
    String[] ratingArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_detail_page);
        DRPToolbar = (Toolbar) this.findViewById(R.id.DTDToolbar);
        setSupportActionBar(DRPToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));

        DRPToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black));
        driverImg = (CircleImageView)findViewById(R.id.driverImg);
        driverRating = (TextView)findViewById(R.id.driverRating);
        driverName = (TextView) findViewById(R.id.driverName);
        driverWorkDays = (TextView) findViewById(R.id.driverWorkDays);
        knowLanguages = (TextView) findViewById(R.id.knowLanguages);
        loader = (GifImageView) findViewById(R.id.loaderImageView);
        loaderImageViewLayout = (LinearLayout) findViewById(R.id.loaderImageViewLayout);

        userComplementRecycle = (RecyclerView) findViewById(R.id.userComplementRecycle);
        idtdControler = new DTDPresenter(this);
        idtdControler.getParcelable();

        intent = getIntent();
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
    public void getParcelable() {
        tripDetails = (TripDetails) intent.getParcelableExtra("TripDetails");
        if (tripDetails != null) {
            Gson gson = new Gson();
            String json = gson.toJson(tripDetails);
            idtdControler.loadData(tripDetails.getUserId(),tripDetails.getDriverId());
            driverName.setText(tripDetails.getDriverName());
            Picasso.with(this)
                    .load(tripDetails.getDriverPhotoUrl())
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .resize(0, 200)
                    .into(driverImg, new Callback() {
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
                                    .into(driverImg, new Callback() {
                                        @Override
                                        public void onSuccess() {

                                        }

                                        @Override
                                        public void onError() {
                                            Log.v("Picasso","Could not fetch image");
                                        }
                                    });
                        }
                    });

            System.out.println("DriverDetailPage -- DriverDetailPage- name : " + json);
        }
    }

    @Override
    public void getCommentListAndDetails(List<Comments> commentses, DTDModel dtdModel) {
        rating =(dtdModel.getDriverRating());
        ratingArray =rating.split("(?<=\\.\\d{1})");
        for (int i=0; i<(ratingArray.length); i++ ) {
                System.out.println("getCommentListAndDetails values :"+ ratingArray[i]);
        }
        driverRating.setText(ratingArray[0]);
        driverWorkDays.setText(dtdModel.getWorkDays());
        knowLanguages.setText(dtdModel.getLanguagesKnown());

        dtdCommentAdapter = new DTDCommentAdapter(commentses);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        userComplementRecycle.setLayoutManager(mLayoutManager);
        userComplementRecycle.setItemAnimator(new DefaultItemAnimator());
        userComplementRecycle.setAdapter(dtdCommentAdapter);
        loaderImageViewLayout.setVisibility(View.GONE);

    }
}
