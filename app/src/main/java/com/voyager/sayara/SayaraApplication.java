package com.voyager.sayara;


import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.multidex.MultiDexApplication;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*import com.firebase.client.Firebase;*/

/**
 * Created by Rimon on 05/3/18.
 * This class extends Application,
 * The purpose of this class is to initialize the firebase, as this class loads very first when the application starts,
 */
public class SayaraApplication extends MultiDexApplication {

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

}
