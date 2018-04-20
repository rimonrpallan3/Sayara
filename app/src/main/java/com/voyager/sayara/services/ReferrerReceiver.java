package com.voyager.sayara.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.voyager.sayara.SayaraApplication;

import java.util.HashMap;

/**
 * Created by rimon on 05/03/18.
 */

public class ReferrerReceiver extends BroadcastReceiver {

    private String SOURCE   = "utm_source";
    private String CAMPAIGN = "utm_campaign";
    private String MEDIUM   = "utm_medium";
    private String CONTENT  = "utm_content";
    private String TERM     = "utm_term";
    @Override
    public void onReceive(Context context, Intent intent) {

        try{
            String referrer = intent.getStringExtra("referrer");
            HashMap<String, String> param_map = new HashMap<>();
            for (String param : referrer.split("&")) {
                String[] param_array = param.split("=");
                param_map.put(param_array[0], param_array[1]);
            }

            SayaraApplication mApplication = ((SayaraApplication) context.getApplicationContext());


        }catch (Exception e){
            e.printStackTrace();
        }
    }

}