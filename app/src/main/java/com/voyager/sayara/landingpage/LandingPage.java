package com.voyager.sayara.landingpage;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.mikepenz.iconics.context.IconicsLayoutInflater2;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.voyager.sayara.R;
import com.voyager.sayara.common.Helper;
import com.voyager.sayara.costom.CircleImageView;
import com.voyager.sayara.drawerfragments.help.HelpFragment;
import com.voyager.sayara.landingpage.adapter.DrawerListAdapter;
import com.voyager.sayara.landingpage.helper.BackHandledFragment;
import com.voyager.sayara.landingpage.model.OnTripStartUp;
import com.voyager.sayara.landingpage.model.drawerHeader.HeaderItem;
import com.voyager.sayara.landingpage.model.drawerList.DrawerItems;
import com.voyager.sayara.landingpage.presenter.ILandingPresenter;
import com.voyager.sayara.landingpage.presenter.LandingPresenter;
import com.voyager.sayara.landingpage.view.ILandingView;
import com.voyager.sayara.landingpage.view.IMapFragmentView;
import com.voyager.sayara.registerpage.model.UserDetails;
import com.voyager.sayara.triphistroty.TripHistory;
import com.voyager.sayara.updateprofile.UpdateProfile;
import com.voyager.sayara.updateprofile.presenter.UpProfilePresenter;
import com.voyager.sayara.fare.FareEstimate;
import com.voyager.sayara.fragments.map.helper.onSomeEventListener;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by User on 8/30/2017.
 */

public class LandingPage extends AppCompatActivity implements View.OnClickListener,
        onSomeEventListener,
        ILandingView,
        BackHandledFragment.BackHandlerInterface, DrawerListAdapter.ClickListener {

    Activity activity;
    public Toolbar toolbar;

    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView navigationView;
    private DrawerLayout mDrawerLayout;

    CircleImageView customerProfileDrawerImg;
    TextView customerProfileDrawerTitle;
    private static final String TAG = LandingPage.class.getSimpleName();

    public int S = 0;
    public String nameStart = "";
    public String nameEnd = "";
    public String distanceKm = "";
    public String addressStart = "";
    public String addressEnd = "";
    private final static double DRAWER_COVER_ASPECT_RATIO = 9d / 14d;

    UpProfilePresenter upProfilePresenter;

    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;

    UserDetails userDetails;

    FrameLayout landingContainer;
    ImageButton choseTripBackPress;

    String sourceLocation = "";
    String currentPlaceId = "";
    String destinationLocation = "";
    String destinationPlaceId = "";

    Bundle bundle;

    OnTripStartUp onTripStartUp;
    String fcmPush = "";

    MapFragmentView mapFragmentView;


    DrawerListAdapter drawerListAdapter;

    IMapFragmentView iMapFragmentView;
    private Fragment fragment;

    ILandingPresenter iLandingPresenter;
    private BackHandledFragment selectedFragment;
    RecyclerView drawerList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Do something for lollipop and above versions
            LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));
        } else {
            LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));
            // do something for phones running an SDK before lollipop
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_page);
        activity = this;

        sharedPrefs = getSharedPreferences(Helper.UserDetails,
                Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();


        FirebaseMessaging.getInstance().subscribeToTopic("test");
        FirebaseInstanceId.getInstance().getToken();
        System.out.println("LandingPage -- FCm- Id : " + FirebaseInstanceId.getInstance().getToken().toString());

        Intent intent = getIntent();
        bundle = new Bundle();
        String hiddenBtn = intent.getStringExtra("btnHiddenBtn");
        userDetails = (UserDetails) intent.getParcelableExtra("UserDetails");
        if (userDetails != null) {
            System.out.println("LandingPage -- UserDetails- name : " + userDetails.getFName());
            System.out.println("LandingPage -- UserDetails- Id : " + userDetails.getUserID());
            System.out.println("LandingPage -- UserDetails- fcm : " + userDetails.getFcm());
        } else if (hiddenBtn != null) {
            // do nothing //
        } else {
            getUserSDetails();
        }


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black));
        landingContainer = (FrameLayout) findViewById(R.id.landingContainer);


        //  Navigation Drawer
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        customerProfileDrawerImg = (CircleImageView) findViewById(R.id.customerProfileDrawerImg);
        customerProfileDrawerTitle = (TextView) findViewById(R.id.customerProfileDrawerTitle);
        drawerList = (RecyclerView) findViewById(R.id.drawerList);

       /* Menu menu = navigationView.getMenu();
        MenuItem profileImg = menu.findItem(R.id.updateProfile);
        MenuItem infoTripImg = menu.findItem(R.id.infoTrip);
        MenuItem helpImg = menu.findItem(R.id.help);*//*
        ImageView infoImg = (ImageView) hView1.findViewById(R.id.infoTrip);
        ImageView helpImg = (ImageView) hView2.findViewById(R.id.help);*//*
        final Drawable profileIcon = new IconicsDrawable(this)
                .icon(CommunityMaterial.Icon.cmd_account)
                .color(ResourcesCompat.getColor(getResources(),R.color.iconColor,null))
                .sizeDp(12);
        profileImg.setIcon(profileIcon);
        final Drawable infoIcon = new IconicsDrawable(this)
                .icon(CommunityMaterial.Icon.cmd_information)
                .color(ResourcesCompat.getColor(getResources(),R.color._1,null))
                .sizeDp(12);
        infoTripImg.setIcon(infoIcon);
        final Drawable questionIcon = new IconicsDrawable(this)
                .icon(CommunityMaterial.Icon.cmd_comment_question_outline)
                .color(ResourcesCompat.getColor(getResources(),R.color._1,null))
                .sizeDp(12);
        helpImg.setIcon(questionIcon);*/

        //addDrawerItems();
        setupDrawer();
        iLandingPresenter = new LandingPresenter(this, TAG, this);
        //------------ End of Navigation Drawer-----------------------------
        mapFragmentView = new MapFragmentView();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.landingContainer, mapFragmentView);
        bundle.putParcelable("UserDetails", userDetails);

        mapFragmentView.setArguments(bundle);
        fragmentTransaction.commit();
        int width = (getResources().getDisplayMetrics().widthPixels);
        DrawerLayout.LayoutParams params = (android.support.v4.widget.DrawerLayout.LayoutParams) navigationView.getLayoutParams();
        params.width = (int) (width * DRAWER_COVER_ASPECT_RATIO);
        navigationView.setLayoutParams(params);

        upProfilePresenter = new UpProfilePresenter();
        /*View hView =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.customerProfileDrawerTitle);*/
        if (userDetails != null) {
            System.out.println("LandingPage -- UserDetails Share- name : " + userDetails.getFName());
            System.out.println("LandingPage -- UserDetails Share- Id : " + userDetails.getUserID());
            System.out.println("LandingPage -- UserDetails Share- fcm : " + userDetails.getFcm());
            //nav_user.setText(userDetails.getFName());
        }
        drawerListAdapter = new DrawerListAdapter(getData(), this);
        drawerList.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        drawerList.setLayoutManager(mLayoutManager);
        drawerList.setItemAnimator(new DefaultItemAnimator());
        drawerList.setAdapter(drawerListAdapter);
        drawerListAdapter.setClickListener(this);


    }

    public void passVal(IMapFragmentView iMapFragmentView) {
        this.iMapFragmentView = iMapFragmentView;

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        System.out.println("onNewIntent Landing -------------");
        onTripStartUp = (OnTripStartUp) intent.getParcelableExtra("OnTripStartUp");
        fcmPush = intent.getStringExtra("fcmPush");
        System.out.println("onNewIntent Landing fcmPush-------------  " + fcmPush);
        bundle.putParcelable("OnTripStartUp", onTripStartUp);
        bundle.putString("fcmPush", fcmPush);
        if (onTripStartUp != null) {
            if (onTripStartUp.getTripStatus().equals("Started")) {
                iMapFragmentView.pushTripStarted(onTripStartUp);
            } else if (onTripStartUp.getTripStatus().equals("Stoped")) {

            }
            System.out.println("onNewIntent Landing ------------- inside  ");
            Toast.makeText(getApplicationContext(), "Home Selected", Toast.LENGTH_SHORT).show();
            mapFragmentView = new MapFragmentView();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.landingContainer, mapFragmentView);
            mapFragmentView.setArguments(bundle);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:

                if (mDrawerToggle.isDrawerIndicatorEnabled()) {
                    mDrawerLayout.openDrawer(GravityCompat.START);

                 /*   customerProfileDrawerTitle.setText(userDetails.getFName());


                    try {
                        Picasso.with(this)
                                .load(userDetails.getImgPath())
                                .networkPolicy(NetworkPolicy.OFFLINE)
                                .resize(0, 200)
                                .into(customerProfileDrawerImg, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        //Try again online if cache failed
                                        Picasso.with(getParent())
                                                .load(userDetails.getImgPath())
                                                .error(R.drawable.profile)
                                                .resize(0, 200)
                                                .into(customerProfileDrawerImg, new Callback() {
                                                    @Override
                                                    public void onSuccess() {

                                                    }

                                                    @Override
                                                    public void onError() {
                                                        Log.v("Picasso", "Could not fetch image");
                                                    }
                                                });
                                    }
                                });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    onBackPressed();
                }*/
                }
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * this method is used to set the Boolean Check in what state its current in
     *
     * @param isEnabled Boolean value to state open or close
     */
    public void setDrawerState(boolean isEnabled) {
        if (isEnabled) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            mDrawerToggle.onDrawerStateChanged(DrawerLayout.LOCK_MODE_UNLOCKED);
            mDrawerToggle.setDrawerIndicatorEnabled(true);
            mDrawerToggle.syncState();
            this.getSupportActionBar().setHomeButtonEnabled(true);

        } else {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            mDrawerToggle.onDrawerStateChanged(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            mDrawerToggle.setDrawerIndicatorEnabled(false);
            mDrawerToggle.syncState();
            this.getSupportActionBar().setHomeButtonEnabled(false);
        }
    }

    List<DrawerItems> drawerItems = Arrays.asList();

    public List<DrawerItems> getData() {

        drawerItems = new ArrayList<DrawerItems>();
        drawerItems.clear();

        HeaderItem headerItem = new HeaderItem();
        headerItem.setImageUrl(userDetails.getImgPath());
        headerItem.setUserName(userDetails.getFName());
        headerItem.setEnabled(true);

        DrawerItems yourTripItem = new DrawerItems();
        yourTripItem.setName(getResources().getString(R.string.drawer_your_trips));
        yourTripItem.setIconDraw(MaterialDrawableBuilder.IconValue.INFORMATION);
        yourTripItem.setID(1);
        yourTripItem.setEnabled(true);
        drawerItems.add(yourTripItem);

        DrawerItems paymentItem = new DrawerItems();
        paymentItem.setName(getResources().getString(R.string.drawer_update_profile));
        paymentItem.setIconDraw(MaterialDrawableBuilder.IconValue.CASH);
        paymentItem.setID(2);
        paymentItem.setEnabled(true);
        drawerItems.add(paymentItem);

        DrawerItems hlpItem = new DrawerItems();
        //savesItem.setName(getResources().getString(R.string.Favoris)+" /*("+bookmaeks_count+")*/");
        hlpItem.setName(getResources().getString(R.string.drawer_help));
        hlpItem.setIconDraw(MaterialDrawableBuilder.IconValue.HELP);
        hlpItem.setID(3);
        drawerItems.add(hlpItem);

        DrawerItems termAndCondItem = new DrawerItems();
        //savesItem.setName(getResources().getString(R.string.Favoris)+" /*("+bookmaeks_count+")*/");
        termAndCondItem.setName(getResources().getString(R.string.drawer_help));
        termAndCondItem.setIconDraw(MaterialDrawableBuilder.IconValue.FILE_DOCUMENT);
        termAndCondItem.setID(4);
        drawerItems.add(termAndCondItem);

        DrawerItems settingItem = new DrawerItems();
        //savesItem.setName(getResources().getString(R.string.Favoris)+" /*("+bookmaeks_count+")*/");
        settingItem.setName(getResources().getString(R.string.drawer_help));
        settingItem.setIconDraw(MaterialDrawableBuilder.IconValue.SETTINGS);
        settingItem.setID(4);
        drawerItems.add(settingItem);

        return drawerItems;
    }

    /**
     * this method is used to set up navigation drawer Close and open states
     */
    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //mDrawerLayout.openDrawer(Gravity.RIGHT);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }


        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    /**
     * this method redirects the user to the app page in play store
     */
    private void launchMarket() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, getString(R.string.play_store_warning), Toast.LENGTH_LONG).show();
        }
    }

    private void getUserSDetails() {
        Gson gson = new Gson();
        String json = sharedPrefs.getString("UserDetails", null);
        if (json != null) {
            System.out.println("-----------LandingPage uploadProfileName UserDetails" + json);
            userDetails = gson.fromJson(json, UserDetails.class);
            //emailAddress = userDetails.getEmail();
        }

    }


    /**
     * this method is used to set the drawer item and initializes its click event in navigation drawer.
     */
    private void addDrawerItems() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(false);
                //Closing drawer on item click
                mDrawerLayout.closeDrawers();
                //Check to see which item was being clicked and perform appropriate action
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                switch (menuItem.getItemId()) {

                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.infoTrip:
                        Intent intent = new Intent(LandingPage.this, TripHistory.class);
                        intent.putExtra("UserDetails", userDetails);
                        startActivity(intent);
                        //   getSupportActionBar().setTitle(getString(R.string.profile));
                        //  faith_main_activity_following_tab_layout.setVisibility(View.INVISIBLE);
                        return true;

                    // For rest of the options we just show a toast on click

                    case R.id.help:
                        getSupportActionBar().setTitle(getResources().getString(R.string.help));
                        HelpFragment helpFragment = new HelpFragment();
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.landingContainer, helpFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        return true;

                    case R.id.updateProfile:
                        intent = new Intent(LandingPage.this, UpdateProfile.class);
                        intent.putExtra("UserDetails", userDetails);
                        startActivity(intent);
                        // ((FaithApplication) activity.getApplication()).logEvent("navigation_drawer_rate");
                        // launchMarket();

                        return true;

                    default:

                        return true;

                }
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
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
    public void onBackPressed() {
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            System.out.println("back stack entry count : " + getSupportFragmentManager().getBackStackEntryCount());
            setDrawerState(true);
        }
        if (selectedFragment == null || !selectedFragment.onBackPressed()) {
            // Selected fragment did not consume the back press event.
            super.onBackPressed();
        }
        super.onBackPressed();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // case R.id.driverSwitch:

        }
    }

    @Override
    public void someEvent(int s, final LinearLayout serviceLayout, final FrameLayout searchLoc) {
        S = s;
        if (S == 3) {
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            mDrawerToggle.setDrawerIndicatorEnabled(true);
            mDrawerToggle.setToolbarNavigationClickListener(null);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setSupportActionBar(toolbar);
            Intent intent = new Intent(this, FareEstimate.class);
            Bundle bundle = new Bundle();
            bundle.putString("nameStart", nameStart);
            bundle.putString("nameEnd", nameEnd);
            bundle.putString("distanceKm", distanceKm);
            bundle.putString("addressStart", addressStart);
            bundle.putString("addressEnd", addressEnd);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (S == 1) {
            mDrawerToggle.setDrawerIndicatorEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.search_loc));
            setDrawerState(false);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    mDrawerToggle.setDrawerIndicatorEnabled(true);
                    getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
                    mDrawerToggle.setToolbarNavigationClickListener(null);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    setSupportActionBar(toolbar);
                    serviceLayout.setVisibility(View.GONE);
                    setDrawerState(true);
                }

            });
        } else if (S == 2) {
            mDrawerToggle.setDrawerIndicatorEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.search_loc));
            setDrawerState(false);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    mDrawerToggle.setDrawerIndicatorEnabled(true);
                    getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
                    mDrawerToggle.setToolbarNavigationClickListener(null);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    setSupportActionBar(toolbar);
                    serviceLayout.setVisibility(View.GONE);
                    setDrawerState(true);
                    searchLoc.setEnabled(true);
                }
            });

        }
    }

    @Override
    public void mapDetailBuff(String nameStart, String nameEnd, String distanceKm, String addressStart, String addressEnd) {
        this.nameStart = nameStart;
        this.nameEnd = nameEnd;
        this.distanceKm = distanceKm;
        this.addressStart = addressStart;
        this.addressEnd = addressEnd;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.landingContainer);
        fragment.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Helper.GET_DRIVER:
                if (resultCode == Activity.RESULT_OK) {
                    System.out.println("LandingPage onActivityResult GET_DRIVER : ");
                }
                break;

            default:
                break;
        }
    }


    @Override
    public void hideVisibilityLandingItems(int visibility, String value) {
        if (value.equals("toolbar")) {
            toolbar.setVisibility(visibility);
        } else if (value.equals("backImg")) {
            choseTripBackPress.setVisibility(visibility);
        }
    }

    @Override
    public void setSelectedFragment(BackHandledFragment backHandledFragment) {
        this.selectedFragment = selectedFragment;
    }

    @Override
    public void itemClicked(View view, int position) {
        DrawerItems drawerItems = drawerListAdapter.getData().get(position);
        if (drawerItems instanceof DrawerItems) {
            switch (drawerItems.getID()) {
                case Menu.YOUR_TRIPS:

                    if (mDrawerLayout != null)
                        mDrawerLayout.closeDrawers();


                    break;
                case Menu.PAYMENTS:

                    if (mDrawerLayout != null)
                        mDrawerLayout.closeDrawers();


                    break;
                case Menu.HELP:

                    if (mDrawerLayout != null)
                        mDrawerLayout.closeDrawers();


                    break;
                case Menu.TERMS_AND_CONDITIONS:

                    if (mDrawerLayout != null)
                        mDrawerLayout.closeDrawers();


                    break;
                case Menu.SETTINGS:

                    if (mDrawerLayout != null)
                        mDrawerLayout.closeDrawers();


                    break;


            }
        }

    }

    private static class Menu {
        public static final int YOUR_TRIPS = 1;
        public static final int PAYMENTS = 2;
        public static final int HELP = 3;
        public static final int TERMS_AND_CONDITIONS = 4;
        public static final int SETTINGS = 5;
    }

}
