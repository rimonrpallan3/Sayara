package com.voyager.sayara.splashscreen;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.voyager.sayara.R;
import com.voyager.sayara.SayaraApplication;
import com.voyager.sayara.common.Helper;
import com.voyager.sayara.landingpage.LandingPage;
import com.voyager.sayara.loginsignuppage.LoginSignUpPage;
import com.voyager.sayara.splashscreen.presenter.SplashPresenter;
import com.voyager.sayara.splashscreen.view.ISplashView;

import java.util.ArrayList;
import java.util.List;

import static com.voyager.sayara.common.Helper.REQUEST_PHONE_STATE;
import static com.voyager.sayara.common.Helper.REQUEST_READ_PHONE_STATE;


/**
 * Created by User on 8/23/2017.
 */

public class SplashScreen extends AppCompatActivity implements ISplashView{

    private SplashPresenter mPresenter;
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    String imei = "";
    String meid = "";
    private FirebaseAuth mAuth;
    String fireBaseToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        mAuth = FirebaseAuth.getInstance();
        sharedPrefs = getSharedPreferences(Helper.UserDetails,
                Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();
        mPresenter = new SplashPresenter(this,this,this,sharedPrefs,editor);
        mPresenter.load();
        fireBaseToken = FirebaseInstanceId.getInstance().getToken();
        System.out.println("----------- onCreate ----------fireBaseToken: "+fireBaseToken);
        // getDeviceIMEI();
        try{
//
           /* FirebaseApp.initializeApp(this);
            FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
            Toast.makeText(this, "" + currentFirebaseUser.getUid(), Toast.LENGTH_SHORT).show();
            System.out.println("----------- onCreate ----------currentFirebaseUser: "+currentFirebaseUser.getUid());*/
        }catch (Exception e){
            e.printStackTrace();
        }
       /* try{
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_PHONE_STATE);
            } else {
                TelephonyManager tm = (TelephonyManager)
                        getSystemService(this.TELEPHONY_SERVICE);
                try{
                    imei = tm.getImei();
                }catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    meid=tm.getMeid();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }*/
        System.out.println("----------- onCreate ----------imei: "+imei+", meid: "+meid);
    }



    /**
     * Returns the unique identifier for the device
     *
     * @return unique identifier for the device
     */
    public String getDeviceIMEI() {
        String deviceUniqueIdentifier = null;
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (null != tm) {
            deviceUniqueIdentifier = tm.getDeviceId();
            System.out.println("----------- getDeviceIMEI ----------deviceUniqueIdentifier: "+deviceUniqueIdentifier);
        }
        if (null == deviceUniqueIdentifier || 0 == deviceUniqueIdentifier.length()) {
            deviceUniqueIdentifier = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return deviceUniqueIdentifier;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PHONE_STATE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    TelephonyManager tm = (TelephonyManager)
                            getSystemService(this.TELEPHONY_SERVICE);
                    String imei = tm.getImei();
                    String meid=tm.getMeid();
                    System.out.println("-----------onRequestPermissionsResult ----------imei: "+imei+", meid: "+meid);
                } else {

                }
                return;
            }
        }
    }

    @Override
    public void moveToSignUpLogin() {
        Intent intent = new Intent(this, LoginSignUpPage.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void moveToLanding() {
        Intent intent = new Intent(this, LandingPage.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Helper.REQUEST_LOCATION_CHECK_SETTINGS) {
            mPresenter.load();
        }
    }
}