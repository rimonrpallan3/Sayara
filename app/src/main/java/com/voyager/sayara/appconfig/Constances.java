package com.voyager.sayara.appconfig;


import com.voyager.sayara.BuildConfig;

import java.util.List;

public class Constances {


    public static int DISTANCE_CONST = 1024;
    //Change the url depending on the name of your web hosting
   // public static String BASE_URL = "https://droideve.com/smartgeostore";
    public static String BASE_URL = AppConfig.BASE_URL;

    //CHAT CONFIG SERVER
    public static String SERVER_ADDRESS_IP;
    public static String SOCKET_SERVER_VERSION;


    //Set the link to the app store account
    public static String PLAY_STORE_URL = AppConfig.PLAY_STORE_URL;
    //Your AdMob Banner Unit ID
    //public static final String BANNER_UNIT_ID = Config.BANNER_UNIT_ID;
    //public static final String ADS_UNIT_ID = Config.ADS_UNIT_ID;
    //Your maps api key
    //public static final String MAPS_API_ID = Config.MAPS_API_ID;
    //You Can download other fonts , place it in the app and change the variables
    public static class Fonts{
        public final static String BOLD = "fonts/OpenSans-Bold.ttf";
        public final static String REGULAR = "fonts/OpenSans-Regular.ttf";
    }

    //Set SQLLite database  information
    public static final  String DB_NAME= BuildConfig.APPLICATION_ID+".db";
    public static final int DB_VERISON = 2;

    //WARNING :  DO NOT EDIT THIS
    public static class API{

        private static String API_VERSION = "1.0";
        public final static String BASE_IMAGES_URL = BASE_URL+"/uploads/images/";
        public static String API_USER_UPLOAD = BASE_URL+"/"+API_VERSION+"/webservice/uploadImage";
        public static String API_USER_GET_STORES = BASE_URL+"/"+API_VERSION+"/webservice/getStores";
        public static String API_USER_GET_EVENTS = BASE_URL+"/"+API_VERSION+"/webservice/getEvent";
        public static String API_USER_GET_REVIEWS = BASE_URL+"/"+API_VERSION+"/webservice/getComments";
        public static String API_USER_GET_CATEGORY = BASE_URL+"/"+API_VERSION+"/webservice/getCategories";
        public static String API_USER_GCM = BASE_URL+"/"+API_VERSION+"/webservice/register";
        public static String API_USER_UPLOAD64 = BASE_URL+"/"+API_VERSION+"/webservice/uploadImage64";
        public static String API_USER_CREATE_STORE = BASE_URL+"/"+API_VERSION+"/webservice/createStore";
        public static String API_USER_UPDATE_STORE = BASE_URL+"/"+API_VERSION+"/webservice/updateStore";
        public static String API_USER_LOGIN = BASE_URL+"/"+API_VERSION+"/webservice/signIn";
        public static String API_USER_SIGNUP = BASE_URL+"/"+API_VERSION+"/webservice/signUp";
        public static String API_USER_CHECK_CONNECTION = BASE_URL+"/"+API_VERSION+"/webservice/checkUserConnection";
        public static String API_USER_CHECK = BASE_URL+"/"+API_VERSION+"/webservice/checkUserConnection";
        public static String API_APP_INIT = BASE_URL+"/"+API_VERSION+"/webservice/app_initialization";
        public static String API_RATING_STORE = BASE_URL+"/"+API_VERSION+"/webservice/rate";
        public static String API_USER_REGISTER_TOKEN = BASE_URL+"/"+API_VERSION+"/webservice/registerToken";
        public static String API_SAVE_STORE= BASE_URL+"/"+API_VERSION+"/webservice/saveStore";
        public static String API_REMOVE_STORE= BASE_URL+"/"+API_VERSION+"/webservice/removeStore";
        public static String API_LOAD_MESSAGES= BASE_URL+"/"+API_VERSION+"/webservice/loadMessages";
        public static String API_LOAD_DISCUSSION= BASE_URL+"/"+API_VERSION+"/webservice/loadDiscussion";
        public static String API_INBOX_LOADED= BASE_URL+"/"+API_VERSION+"/webservice/inboxLoaded";
        public static String API_INBOX_MARK_AS_SEEN= BASE_URL+"/"+API_VERSION+"/webservice/markMessagesAsSeen";
        public static String API_INBOX_MARK_AS_LOADED= BASE_URL+"/"+API_VERSION+"/webservice/markMessagesAsLoaded";
        public static String API_SEND_MESSAGE= BASE_URL+"/"+API_VERSION+"/webservice/sendMessage";
        public static String API_SEND_NEW_PASSWD= BASE_URL+"/"+API_VERSION+"/webservice/sendNewPassword";
        public static String API_CHANGE_USER_STATUS= BASE_URL+"/"+API_VERSION+"/webservice/changeUserStatus";
        public static String API_BLOCK_USER= BASE_URL+"/"+API_VERSION+"/webservice/blockUser";
        public static String API_GET_USERS= BASE_URL+"/"+API_VERSION+"/webservice/getUsers";
        public static String API_UPDATE_ACCOUNT= BASE_URL+"/"+API_VERSION+"/webservice/updateAccount";
        public static String API_GET_OFFERS= BASE_URL+"/"+API_VERSION+"/webservice/getOffers";
        public static String API_GET_FILTERS_OFFER_LIST= BASE_URL+"/"+API_VERSION+"/webservice/getOffertypes";
        public static String API_REFRESH_POSITION= BASE_URL+"/"+API_VERSION+"/webservice/refreshPosition";
        public static String API_MARK_VIEW= BASE_URL+"/"+API_VERSION+"/webservice/markView";

    }


    public static  class initConfig{

        public static class fonts {
            public static final String  FONTSTYLE = "";
        }

        //WARNING :  DO NOT EDIT THIS
        public static class Tabs{

            public static final int HOME = 0;
            public static final int NEARBY = 0;
            public static final int BOOKMAKRS = -1;
            public static final int MOST_RATED = -2;
            public static final int MOST_RECENT = -3;
            public static final int EVENTS = -4;
            public static final int CHAT = -5;
            public static final int NEARBY_OFFERS = -6;
            public static final int NEARBY_SHOPS = -7;
        }


        public static class AppInfos{

                // set the description
                public static String ABOUT_CONTENT = AppConfig.ABOUT_CONTENT;

                // Your email that you wish that users on your app will contact you.
                public static String ADDRESS_CONTACT= AppConfig.ADDRESS_CONTACT;
                public static String PHONE= AppConfig.PHONE;

        }

        //WARNING :  DO NOT EDIT THIS
        public static int Numboftabs;
    }


}
