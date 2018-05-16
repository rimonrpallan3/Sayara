package com.voyager.sayara.splashscreen;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.voyager.sayara.R;
import com.voyager.sayara.appconfig.AppConfig;
import com.voyager.sayara.common.Helper;
import com.voyager.sayara.landingpage.LandingPage;
import com.voyager.sayara.loginsignuppage.LoginSignUpPage;
import com.voyager.sayara.splashscreen.presenter.SplashPresenter;
import com.voyager.sayara.splashscreen.view.ISplashView;

import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static com.voyager.sayara.common.Helper.REQUEST_PHONE_STATE;


/**
 * Created by User on 8/23/2017.
 */

public class SplashScreen extends AppCompatActivity implements ISplashView,EasyPermissions.PermissionCallbacks{

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
        //SayaraApplication application = (SayaraApplication) getApplication();
        System.out.println("----------- onCreate ----------BaseUrl: " + AppConfig.BASE_URL);
        mAuth = FirebaseAuth.getInstance();
        sharedPrefs = getSharedPreferences(Helper.UserDetails,
                Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();
        mPresenter = new SplashPresenter(this, this, this, sharedPrefs, editor);
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mPresenter.load();
        }else {
            mPresenter.load();
        }
        fireBaseToken = FirebaseInstanceId.getInstance().getToken();
        System.out.println("----------- onCreate ----------fireBaseToken: " + fireBaseToken);


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
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }
            deviceUniqueIdentifier = tm.getDeviceId();
            System.out.println("----------- getDeviceIMEI ----------deviceUniqueIdentifier: " + deviceUniqueIdentifier);
        }
        if (null == deviceUniqueIdentifier || 0 == deviceUniqueIdentifier.length()) {
            deviceUniqueIdentifier = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return deviceUniqueIdentifier;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        // EasyPermissions handles the request result.
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        switch (requestCode) {
            case REQUEST_PHONE_STATE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    TelephonyManager tm = (TelephonyManager)
                            getSystemService(this.TELEPHONY_SERVICE);
                    String imei = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        imei = tm.getImei();
                        String meid=tm.getMeid();
                    }

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
            if ( Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                mPresenter.load();
            }else {
                mPresenter.load();
            }

        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }
}