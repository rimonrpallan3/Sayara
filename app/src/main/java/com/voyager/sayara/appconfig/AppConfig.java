package com.voyager.sayara.appconfig;


import java.util.List;

public class AppConfig {

    public static String BASE_URL = "";

    //Set the link to the app store account
    public static final String PLAY_STORE_URL = "";


    // To verify if the app is build on Debug r Release Mode
    public static boolean APP_DEBUG = false;


    public static boolean SAFE_MODE = false;

    // Set to true if you want to display ads in all views.
    public static  boolean SHOW_ADS = false ;
    public static  boolean SHOW_ADS_IN_STORE = false ;
    public static  boolean SHOW_ADS_IN_EVENT = false ;
    public static  boolean SHOW_ADS_IN_OFFER = false ;
    public static  boolean SHOW_ADS_IN_HOME = false  ;
    public static  boolean SHOW_INTERSTITIAL_ADS_IN_STARTUP = false ;

    public static  boolean ENABLE_CHAT =false ;
    public static  boolean CHAT_WITH_FIREBASE =false ;
    public static  String CRYPTO_KEY ="" ;


    public static  boolean ENABLE_WEB_DASHBOARD =false ;
    public static  boolean RATE_US_FORCE =true ;

    public static  String ADDRESS_CONTACT= "";
    public static  String PHONE= "";


    public static String ABOUT_CONTENT = "";


    public static class  GCMConfig {
        // flag to identify whether to show single line
        // or multi line text in push notification tray
        public static boolean appendNotificationMessages = false;

    }
}
