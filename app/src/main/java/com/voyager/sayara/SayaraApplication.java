package com.voyager.sayara;


import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.FirebaseDatabase;
import com.norbsoft.typefacehelper.TypefaceCollection;
import com.norbsoft.typefacehelper.TypefaceHelper;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.voyager.sayara.appconfig.AppConfig;
import com.voyager.sayara.appconfig.Constances;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.voyager.sayara.appconfig.AppConfig.APP_DEBUG;

/*import com.firebase.client.Firebase;*/

/**
 * Created by Rimon on 05/3/18.
 * This class extends Application,
 * The purpose of this class is to initialize the firebase, as this class loads very first when the application starts,
 */
public class SayaraApplication extends Application {

    private static SayaraApplication mInstance;

    private FirebaseAnalytics firebaseAnalytics;
    private String userID = "";
    private String churchId = null;
    FirebaseApp secondary;
    @Override
    public void onCreate() {
        super.onCreate();
        //FacebookSdk.sdkInitialize(this);

        printKeyHash();
        if (!FirebaseApp.getApps(this).isEmpty())
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        appInit();


        TypefaceCollection typeface = new TypefaceCollection.Builder()
                .set(Typeface.NORMAL, Typeface.createFromAsset(getAssets(), Constances.Fonts.REGULAR))
                .create();
        TypefaceHelper.init(typeface);

        /*Firebase.setAndroidContext(this);*/

/*        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(getCacheDir(),250000000));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(false);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);*/

        try{
           // userID = getSharedPreferences(Config.FaithApp_PREFERENCES, MODE_PRIVATE).getString("userid", "NLI");
            firebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
        }catch (Exception e){
            e.printStackTrace();
        }

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId("1:26302240802:android:4deba93122375d93")
                .setApiKey("AIzaSyAepUY58DHLFMu6o0n4oynp5U9rAGkn94c") // Required for Auth.
                .setDatabaseUrl("https://sayara-f796c.firebaseio.com/") // Required for RTDB.
                .build();

        secondary = FirebaseApp.initializeApp(getApplicationContext(), options, "secondary");
        FirebaseApp.initializeApp(this);

    }

    public static synchronized SayaraApplication getInstance() {
        return mInstance;
    }



    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    private Tracker mTracker;

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(getString(R.string.analytics));
        }
        return mTracker;
    }



    /**
     * This method extracts and prints the Hash Key.
     */
    public void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {

        }
    }

    private void appInit(){
        mInstance=this;
        parseAppConfig();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
        if(APP_DEBUG) { Log.e("Application","Memory  cleaned !!"); }
    }





    private void parseAppConfig(){

        //others
        AppConfig.ABOUT_CONTENT=getResources().getString(R.string.ABOUT_CONTENT);
        AppConfig.ADDRESS_CONTACT=getResources().getString(R.string.ADDRESS_CONTACT);
        AppConfig.PHONE=getResources().getString(R.string.PHONE);

        AppConfig.SHOW_ADS = Boolean.parseBoolean(getResources().getString(R.string.SHOW_ADS));
        AppConfig.SHOW_ADS_IN_EVENT = Boolean.parseBoolean(getResources().getString(R.string.SHOW_ADS_IN_EVENT));
        AppConfig.SHOW_ADS_IN_HOME = Boolean.parseBoolean(getResources().getString(R.string.SHOW_ADS_IN_HOME));
        AppConfig.SHOW_INTERSTITIAL_ADS_IN_STARTUP = Boolean.parseBoolean(getResources().getString(R.string.SHOW_INTERSTITIAL_ADS_IN_STARTUP));
        AppConfig.SHOW_ADS_IN_STORE = Boolean.parseBoolean(getResources().getString(R.string.SHOW_ADS_IN_STORE));
        AppConfig.SHOW_ADS_IN_OFFER = Boolean.parseBoolean(getResources().getString(R.string.SHOW_ADS_IN_OFFER));

        AppConfig.BASE_URL = getResources().getString(R.string.BASE_URL);

        AppConfig.ENABLE_CHAT= Boolean.parseBoolean(getResources().getString(R.string.ENABLE_CHAT));
        AppConfig.CHAT_WITH_FIREBASE = Boolean.parseBoolean(getResources().getString(R.string.CHAT_WITH_FIREBASE));

        AppConfig.ENABLE_WEB_DASHBOARD= Boolean.parseBoolean(getResources().getString(R.string.ENABLE_WEB_DASHBOARD));
        AppConfig.RATE_US_FORCE = Boolean.parseBoolean(getResources().getString(R.string.RATE_US_ON_PLAY_STORE_FORCE));
        AppConfig.CRYPTO_KEY=getResources().getString(R.string.crypt_key);

        //tabs




        //chat config
        Constances.BASE_URL=getResources().getString(R.string.BASE_URL);
        Constances.SERVER_ADDRESS_IP=getResources().getString(R.string.SERVER_ADDRESS_IP);
        Constances.SOCKET_SERVER_VERSION=getResources().getString(R.string.SOCKET_SERVER_VERSION);

    }


}
