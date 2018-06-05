package com.voyager.sayara.PulsatingActivity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.voyager.sayara.PulsatingActivity.presenter.IPulsatingPresenter;
import com.voyager.sayara.R;
import com.voyager.sayara.common.Helper;
import com.voyager.sayara.landingpage.model.OnTripStartUp;

import pl.bclogic.pulsator4droid.library.PulsatorLayout;

import static com.voyager.sayara.gcm.MyFirebaseMessagingService.NOTIFICATION_ONTRIP_ACCEPTED;

/**
 * Created by User on 16-Mar-18.
 */

public class PulsatingActivity  extends AppCompatActivity implements View.OnClickListener {


    ImageView pulseImg;
    OnTripStartUp onTripStartUp;
    String fcmPush = "";
    Bundle bundle;

    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;

    IPulsatingPresenter iPulsatingPresenter;
    NotificationManager notificationManager;

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
        sharedPrefs = getSharedPreferences(Helper.OnTripStartUp,
                Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();
        if(!bundle.isEmpty()){
           // nameStart = bundle.getString("nameStart",nameStart);

            /*System.out.println(
                    "nameStart :"+nameStart+

            );*/
        }
        notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ONTRIP_ACCEPTED);


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
        System.out.println("onNewIntent PulsatingActivity -------------");
        onTripStartUp = (OnTripStartUp) intent.getParcelableExtra("OnTripStartUp");
        fcmPush =  intent.getStringExtra("fcmPush");
        System.out.println("onNewIntent PulsatingActivity fcmPush-------------  "+fcmPush);
        bundle.putParcelable("OnTripStartUp", onTripStartUp);
        bundle.putString("fcmPush",fcmPush);
        if(onTripStartUp!=null){
            System.out.println("onNewIntent PulsatingActivity onTripStartUp not null -------------  ");
            Intent intentParent = new Intent("custom-event-name");
            intentParent.putExtra("OnTripStartUp", onTripStartUp);
            addTripDetailsGsonInSharedPrefrences(onTripStartUp);
            Log.d("sender", "Broadcasting message");
            // You can also include some extra data.
            intent.putExtra("message", "This is my message!");
            setResult(RESULT_OK, intentParent);
            finish();
        }

    }

    private void addTripDetailsGsonInSharedPrefrences(OnTripStartUp onTripStartUp){
        Gson gson = new Gson();
        String jsonString = gson.toJson(onTripStartUp);
        //UserModel user1 = gson.fromJson(jsonString,UserModel.class);
        if(jsonString!=null) {
            editor.putString("OnTripStartUp", jsonString);
            editor.commit();
            System.out.println("-----------PulsatingActivity addTripDetailsGsonInSharedPrefrences  onTripStartUp : "+jsonString);

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
