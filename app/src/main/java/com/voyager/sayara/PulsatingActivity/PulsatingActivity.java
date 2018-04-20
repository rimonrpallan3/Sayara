package com.voyager.sayara.PulsatingActivity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.voyager.sayara.MapPlaceSearch.view.IMapPlaceSearchView;
import com.voyager.sayara.PulsatingActivity.presenter.IPulsatingPresenter;
import com.voyager.sayara.PulsatingActivity.presenter.PulsatingPresenter;
import com.voyager.sayara.PulsatingActivity.view.IPulsatingView;
import com.voyager.sayara.R;
import com.voyager.sayara.landingpage.model.OnTripStartUp;

import pl.bclogic.pulsator4droid.library.PulsatorLayout;

/**
 * Created by User on 16-Mar-18.
 */

public class PulsatingActivity  extends AppCompatActivity implements View.OnClickListener {


    ImageView pulseImg;
    OnTripStartUp onTripStartUp;
    String fcmPush = "";
    Bundle bundle;

    IPulsatingPresenter iPulsatingPresenter;

    public PulsatingActivity() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pulse_layout);
        System.out.println("FareEstimate");
        bundle = new Bundle();
        Intent intent =getIntent();
        Bundle bundle = intent.getExtras();
        if(!bundle.isEmpty()){
           // nameStart = bundle.getString("nameStart",nameStart);

            /*System.out.println(
                    "nameStart :"+nameStart+

            );*/
        }


        pulseImg = (ImageView) findViewById(R.id.pulseImg);
        //btnReject.setOnClickListener(this);
        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                pulseImg,
                PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                PropertyValuesHolder.ofFloat("scaleY", 1.2f));
        scaleDown.setDuration(310);

        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);

        scaleDown.start();
        PulsatorLayout pulsator = (PulsatorLayout) findViewById(R.id.pulsator);
        pulsator.start();


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                //finish();
            }
        }, 1500);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        System.out.println("onNewIntent Landing -------------");
        onTripStartUp = (OnTripStartUp) intent.getParcelableExtra("OnTripStartUp");
        fcmPush =  intent.getStringExtra("fcmPush");
        System.out.println("onNewIntent Landing fcmPush-------------  "+fcmPush);
        bundle.putParcelable("OnTripStartUp", onTripStartUp);
        bundle.putString("fcmPush",fcmPush);
        if(onTripStartUp!=null){
            Intent resultIntent = new Intent();
            resultIntent.putExtra("OnTripStartUp", onTripStartUp);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
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
                break;
            case R.id.btnReject:
                finish();
                break;

        }
    }
}
