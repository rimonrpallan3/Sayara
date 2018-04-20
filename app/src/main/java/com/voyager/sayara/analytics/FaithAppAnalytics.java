package com.voyager.sayara.analytics;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by rimon on 2/9/16.
 * This class is used to create the fetch analytics log event on events from app and store it in firebase database
 */
public class FaithAppAnalytics {
    private FirebaseAnalytics mFirebaseAnalytics;

    public FaithAppAnalytics(Context context){
        mFirebaseAnalytics= FirebaseAnalytics.getInstance(context);
    }

    /**
     * this method is get event on login
     * @param activity  String Activity name
     */
    public void logActivityEvent(String activity){
        Bundle bundle = new Bundle();
        bundle.putString("activity", activity);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
    /**
     * this method is get event on login
     * @param activity  String Fragment name
     */
    public void logFragmentEvent(String activity){
        Bundle bundle = new Bundle();
        bundle.putString("fragment", activity);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    /**
     * this method is ued for button click event logs
     * @param button  String Button click logs
     */
    public void logButtonEvent(String button){
        Bundle bundle = new Bundle();
        bundle.putString("button", button);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    /**
     * this method is ued for notification click event logs
     * @param notificationType Strings notification logs events
     */
    public void logNotificationEvent(String notificationType){
        Bundle bundle = new Bundle();
        bundle.putString("notification", notificationType);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
}
