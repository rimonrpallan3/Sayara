package com.voyager.sayara.landingpage;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
 import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.voyager.sayara.MapPlaceSearch.MapPlaceSearch;
import com.voyager.sayara.MapPlaceSearch.model.CurrentPlaceDetails;
import com.voyager.sayara.R;
import com.voyager.sayara.common.Helper;
import com.voyager.sayara.costom.CircleImageView;
import com.voyager.sayara.fare.FareEstimate;
import com.voyager.sayara.landingpage.helper.BackHandledFragment;
import com.voyager.sayara.landingpage.model.Cars;
import com.voyager.sayara.landingpage.model.DriverProfile;
import com.voyager.sayara.landingpage.model.OnTripStartUp;
import com.voyager.sayara.landingpage.model.TripInfo;
import com.voyager.sayara.landingpage.model.geogetpath.Route;
import com.voyager.sayara.landingpage.presenter.IMapFragmentPresenter;
import com.voyager.sayara.landingpage.presenter.MapFragmentPresenter;
import com.voyager.sayara.landingpage.view.ILandingView;
import com.voyager.sayara.landingpage.view.IMapFragmentView;
import com.voyager.sayara.registerpage.model.UserDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.voyager.sayara.common.Helper.REQUEST_PHONE_CUSTOMER_CALL;
import static com.voyager.sayara.common.Helper.REQUEST_PHONE_SUPPORT_CALL;


/**
 * Created by User on 19-Jan-18.
 */

public class MapFragmentView extends BackHandledFragment implements
        OnMapReadyCallback,
        LocationListener,
        NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener,
        IMapFragmentView {

    TextView cashTextView;
    private GoogleMap mMap;
    EditText sourceEditText;
    EditText destEditText;
    LocationManager locationManager;
    String provider;
    double cashPerKM = 0.650;
    double cashMinimum = 0.200;
    double cash;
    String distance = "";
    double distanceCal = 0.0;
    double distancePerLit = 0.0;
    double sourceLat = 0.0;
    double sourceLong = 0.0;
    double destLat = 0.0;
    double destLong = 0.0;
    PolylineOptions polylineOptions;
    // ----------- Newly added
    LinearLayout tvMapSourceDestination;
    LocationRequest mLocationRequest;
    public MapView mMapView;
    String mprovider;
    View rootView;
    FloatingActionButton fabLoc;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private FusedLocationProviderClient mFusedLocationClient;
    double lat = 26.2285;
    double log = 50.5860;
    Location location;
    FrameLayout frameLoaderLayout;
    FrameLayout choseTrip;
    FrameLayout serviceUnavailable;
    Button btnRideNow;
    ImageView truckImg;
    TextView cashAmt;
    TextView carTypeTxt;
    ImageView carImg;
    TextView tCashAmt;
    TextView truckTypeTxt;
    public final static String TAG =  MapFragmentView.class.getSimpleName();

    String sourceLocationTxt = "";
    String sourcePlaceId = "";
    String destinationLocationTxt = "";
    String destinationPlaceId = "";
    String originLat = "";
    String originLng = "";
    String destinationLat = "";
    String destinationLng = "";
    String destinationLatLng = "";
    String driveCarType = "carMini";
    String driveClassType = "Diamond";
    String ApiKey = "";

    String driveCarId = "1";
    IMapFragmentPresenter iMapFragmentPresenter;
    ImageButton choseTripBackPress;
    ILandingView iLandingView;
    LinearLayout carMiniLayout;
    LinearLayout carTruck;
    UserDetails userDetails;
    OnTripStartUp onTripStartUp;

    @BindView(R.id.driverHeaderLayout)
    LinearLayout driverHeaderLayout;
    @BindView(R.id.driverBodyLayout)
    LinearLayout driverBodyLayout;
    @BindView(R.id.layoutTripSupportCall)
    LinearLayout layoutTripSupportCall;
    @BindView(R.id.layoutTripCancel)
    LinearLayout layoutTripCancel;
    @BindView(R.id.layoutTripEmergencyCallOngoing)
    LinearLayout layoutTripEmergencyCallOngoing;
    @BindView(R.id.layoutTripCustomerCallOngoing)
    LinearLayout layoutTripCustomerCallOngoing;
    @BindView(R.id.layoutOngoingTripCancel)
    LinearLayout layoutTripCancelOngoing;
    @BindView(R.id.layoutTripOngoing)
    LinearLayout layoutTripOngoing;
    @BindView(R.id.layoutDriverWaiting)
    LinearLayout layoutDriverWaiting;
    @BindView(R.id.driverCircleImageView)
    CircleImageView driverCircleImageView;
    @BindView(R.id.driverName)
    TextView driverName;
    @BindView(R.id.driverRating)
    TextView driverRating;
    @BindView(R.id.carName)
    TextView carName;
    @BindView(R.id.driverCity)
    TextView driverCity;
    @BindView(R.id.carNo)
    TextView carNo;
    @BindView(R.id.tripStartOrgin)
    TextView tripStartOrgin;
    @BindView(R.id.tripEndDestin)
    TextView tripEndDestin;
    @BindView(R.id.tripAmount)
    TextView tripAmount;
    @BindView(R.id.imgTripOnStartCancel)
    ImageView imgTripOnStartCancel;
    @BindView(R.id.imgTripSupportCall)
    ImageView imgTripSupportCall;
    @BindView(R.id.imgTripOngoingCustomer)
    ImageView imgTripOngoingCustomer;
    @BindView(R.id.imgTripOngoingSupport)
    ImageView imgTripOngoingSupport;
    @BindView(R.id.imgTripOngoingCancel)
    ImageView imgTripOngoingCancel;
    @BindView(R.id.layoutOnTripStartUp)
    LinearLayout layoutOnTripStartUp;

    Boolean clicked = true;
    Bundle bundle;
    String fcmPush = "";
    int screenHeight = 0;
    int screenWidth = 0;

    CurrentPlaceDetails maxCurrentPlaceDetails;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    boolean isMarkerRotating = false;
    Marker mSourceMarker = null;
    Marker mDestinationMarker = null;
    LatLng sourceLatLng;
    LatLng destLatLng;

    public MapFragmentView() {

    }


    @Override
    public String getTagText() {
        return null;
    }

    @Override
    public boolean onBackPressed() {
        if (onTripStartUp.getTripStatus().equals("Stoped")) {
            getActivity().getFragmentManager().popBackStack();
            return true; // event consumed
        } else {
            // event not consumed, let Activity handle it.
            return false;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            iLandingView = (ILandingView) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement ILandingView");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.map_ft_v1, container, false);
        System.out.println("MapFragmentView");
        this.rootView = rootView;
        ButterKnife.bind(this, rootView);
        bundle = this.getArguments();
        ApiKey = getString(R.string.map_api_key);
        sharedPrefs = getActivity().getSharedPreferences(Helper.OnTripStartUp,
                Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();
        tvMapSourceDestination = (LinearLayout) rootView.findViewById(R.id.tvMapSourceDestination);
        //------------ Car type Selection 1----------------
        carMiniLayout = (LinearLayout) rootView.findViewById(R.id.layoutCarMini);
        carImg = (ImageView) rootView.findViewById(R.id.carImg);
        cashAmt = (TextView) rootView.findViewById(R.id.cashAmt);
        carTypeTxt = (TextView) rootView.findViewById(R.id.carTypeTxt);

        //------------ Car type Selection 2----------------
        carTruck = (LinearLayout) rootView.findViewById(R.id.carTruck);
        truckImg = (ImageView) rootView.findViewById(R.id.truckImg);
        tCashAmt = (TextView) rootView.findViewById(R.id.tCashAmt);
        truckTypeTxt = (TextView) rootView.findViewById(R.id.truckTypeTxt);

        //------------ On Trip StatUp  ----------------
        layoutOnTripStartUp = (LinearLayout) rootView.findViewById(R.id.layoutOnTripStartUp);
        layoutTripCancel = (LinearLayout) rootView.findViewById(R.id.layoutTripCancel);
        layoutTripCustomerCallOngoing = (LinearLayout) rootView.findViewById(R.id.layoutTripCustomerCallOngoing);
        layoutTripEmergencyCallOngoing = (LinearLayout) rootView.findViewById(R.id.layoutTripEmergencyCallOngoing);
        layoutTripCancelOngoing = (LinearLayout) rootView.findViewById(R.id.layoutOngoingTripCancel);
        layoutTripOngoing = (LinearLayout) rootView.findViewById(R.id.layoutTripOngoing);
        layoutDriverWaiting = (LinearLayout) rootView.findViewById(R.id.layoutDriverWaiting);

        choseTripBackPress = ((AppCompatActivity) getActivity()).findViewById(R.id.choseTripBackPress);
        choseTrip = (FrameLayout) rootView.findViewById(R.id.choseTrip);
        serviceUnavailable = (FrameLayout) rootView.findViewById(R.id.serviceUnavailable);
        btnRideNow = (Button) rootView.findViewById(R.id.btnRideNow);
        fabLoc = (FloatingActionButton) rootView.findViewById(R.id.fabLoc);
        frameLoaderLayout = (FrameLayout) rootView.findViewById(R.id.frameLoaderLayout);
        mMapView = (MapView) rootView.findViewById(R.id.mapViewFrg);
        MapsInitializer.initialize(this.getActivity());
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);


        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        if (bundle != null) {
            try {
                userDetails = bundle.getParcelable("UserDetails");
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("MapFragmentView -- UserDetails- userId : " + userDetails.getUserID());
            try {
                onTripStartUp = bundle.getParcelable("OnTripStartUp");
                if(onTripStartUp==null){
                    onTripStartUp = getTripDetails();
                    if(onTripStartUp!=null){
                        setTripStatuses();
                    }
                }else {
                    setTripStatuses();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            mprovider = locationManager.getBestProvider(criteria, false);
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                return null;
            }
            location = locationManager.getLastKnownLocation(locationManager
                    .getBestProvider(criteria, false));
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }

        final Drawable callIcon = new IconicsDrawable(getActivity())
                .icon(CommunityMaterial.Icon.cmd_phone)
                .color(ResourcesCompat.getColor(getResources(), R.color.iconColor, null))
                .sizeDp(10);
        final Drawable cancelIcon = new IconicsDrawable(getActivity())
                .icon(CommunityMaterial.Icon.cmd_cancel)
                .color(ResourcesCompat.getColor(getResources(), R.color.red, null))
                .sizeDp(10);
        imgTripOnStartCancel.setImageDrawable(cancelIcon);
        imgTripOngoingCancel.setImageDrawable(cancelIcon);
        imgTripSupportCall.setImageDrawable(callIcon);
        imgTripOngoingCustomer.setImageDrawable(callIcon);
        imgTripOngoingSupport.setImageDrawable(callIcon);

        tvMapSourceDestination.setOnClickListener(this);
        layoutOnTripStartUp.setOnClickListener(this);
        layoutTripSupportCall.setOnClickListener(this);
        layoutTripEmergencyCallOngoing.setOnClickListener(this);
        layoutTripCustomerCallOngoing.setOnClickListener(this);
        layoutTripCancelOngoing.setOnClickListener(this);
        layoutTripOngoing.setOnClickListener(this);
        layoutDriverWaiting.setOnClickListener(this);
        layoutTripCancel.setOnClickListener(this);
        fabLoc.setOnClickListener(this);
        btnRideNow.setOnClickListener(this);
        choseTripBackPress.setOnClickListener(this);
        carMiniLayout.setOnClickListener(this);
        carTruck.setOnClickListener(this);
        choseTrip.setOnClickListener(this);
        serviceUnavailable.setOnClickListener(this);
        carImg.setOnClickListener(this);
        cashAmt.setOnClickListener(this);
        MapsInitializer.initialize(this.getActivity());
        iMapFragmentPresenter = new MapFragmentPresenter(this, getActivity(), TAG);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
        return rootView;
    }

    public void setTripStatuses(){
        fcmPush = bundle.getString("fcmPush");
        System.out.println(" ----------- MapFragmentView fcmPush : " + fcmPush);
        if (fcmPush != null && fcmPush.length() > 0 && onTripStartUp.getTripStatus().equals("Stoped")) {
            if (layoutOnTripStartUp.getVisibility() == View.VISIBLE) {
                layoutDriverWaiting.setVisibility(View.VISIBLE);
                layoutTripOngoing.setVisibility(View.GONE);
                layoutOnTripStartUp.setVisibility(View.GONE);
                iLandingView.hideVisibilityLandingItems(View.VISIBLE, "toolbar");
                iMapFragmentPresenter.hideVisibilityLayoutItems(View.VISIBLE);
                editor.clear();
            }
        }else if(fcmPush != null && fcmPush.length() > 0 && onTripStartUp.getTripStatus().equals("Accepted")){
            System.out.println("MapFragmentView onActivityResult GET_DRIVER : OK");
            //iMapFragmentPresenter.hideVisibilityLayoutItems(View.GONE);
            final DriverProfile driverProfile = onTripStartUp.getDriverProfile();
            final TripInfo tripInfo = onTripStartUp.getTripInfo();
            Gson gson = new Gson();
            String jsonString = gson.toJson(onTripStartUp);
            System.out.println("MapFragmentView onActivityResult GET_DRIVER onTripStartUp json : " + jsonString);
                setTripStartDetails();
                //iMapFragmentPresenter.getTripDriverToPickUp(driverProfile.getDriverLocation(), tripInfo.getPickupLocation(), false, ApiKey, tripInfo.getPickupAddress());
           }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (mMap != null) {
                    mMap.setMyLocationEnabled(true);
                }
                locationManager.requestLocationUpdates(provider, 0, 0, (android.location.LocationListener) getActivity());
            }
        }
        switch (requestCode) {
            case REQUEST_PHONE_SUPPORT_CALL: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + getResources().getString(R.string.phone_number_support)));
                    startActivity(intent);
                } else {

                }
                return;
            }
            case REQUEST_PHONE_CUSTOMER_CALL: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + getResources().getString(R.string.phone_number_customer)));
                    startActivity(intent);
                } else {

                }
                return;
            }
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (mMap != null) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
            mMap.animateCamera(cameraUpdate);
            locationManager.removeUpdates((android.location.LocationListener) getActivity());
        }
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (fcmPush != null && onTripStartUp.getTripStatus() != null && fcmPush.length() > 1 && onTripStartUp.getTripStatus().equals("Started")) {
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(mLocationRequest);
            mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getActivity(), R.raw.map_json));
            setTripStartDetails();
            layoutDriverWaiting.setVisibility(View.GONE);
            layoutTripOngoing.setVisibility(View.VISIBLE);
            final DriverProfile driverProfile = onTripStartUp.getDriverProfile();
            final TripInfo tripInfo = onTripStartUp.getTripInfo();
            Gson gson = new Gson();
            String jsonString = gson.toJson(onTripStartUp);
            System.out.println("MapFragmentView push GET_DRIVER onTripStartUp json : " + jsonString);
            googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                public void onMapLoaded() {
                    //do stuff here
                    iMapFragmentPresenter.getTripDriverToPickUp(driverProfile.getDriverLocation(), tripInfo.getDropLoc(), false, ApiKey, tripInfo.getDropAddress());

                }
            });
        } else if (fcmPush != null && fcmPush.length() > 0 && onTripStartUp.getTripStatus().equals("Stoped")) {
            System.out.println("MapFragmentView push Trip Stoped : ");
            mMap.clear();
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(mLocationRequest);
            mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getActivity(), R.raw.map_json));
            if (location == null) {

                mMap.addMarker(new MarkerOptions().position(new LatLng(lat, log)).title("Marker"));
                CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(lat, log));
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(13);
                mMap.moveCamera(center);
                mMap.animateCamera(zoom);
            }
            layoutDriverWaiting.setVisibility(View.VISIBLE);
            layoutTripOngoing.setVisibility(View.GONE);
            layoutOnTripStartUp.setVisibility(View.GONE);
            iLandingView.hideVisibilityLandingItems(View.VISIBLE, "toolbar");
            iMapFragmentPresenter.hideVisibilityLayoutItems(View.VISIBLE);
        } else if (location == null) {
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(mLocationRequest);
            mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getActivity(), R.raw.map_json));
            System.out.println("MapFragmentView push when null : " + location);
            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, log)).title("Marker"));
            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(lat, log));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(13);
            mMap.moveCamera(center);
            mMap.animateCamera(zoom);

            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(final LatLng latLng) {

                }
            });
        }
        System.out.println("MapFragmentView map Location : " + location);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Helper.SEARCH_MAP_API_TRIP:
                if (resultCode == Activity.RESULT_OK) {
                    iLandingView.hideVisibilityLandingItems(View.GONE, "toolbar");
                    iMapFragmentPresenter.hideVisibilityLayoutItems(View.INVISIBLE);
                    iLandingView.hideVisibilityLandingItems(View.VISIBLE, "backImg");
                    choseTrip.setVisibility(View.VISIBLE);
                    sourceLocationTxt = data.getStringExtra("sourceLocationTxt");
                    sourcePlaceId = data.getStringExtra("currentPlaceId");
                    destinationLocationTxt = data.getStringExtra("destinationLocationTxt");
                    destinationPlaceId = data.getStringExtra("destinationPlaceId");
                    originLat = data.getStringExtra("originLat");
                    originLng = data.getStringExtra("originLng");
                    destinationLat = data.getStringExtra("destinationLat");
                    destinationLng = data.getStringExtra("destinationLng");
                    destinationLatLng = destinationLat + "," + destinationLng;
                    Integer userId = data.getIntExtra("userId", 0);
                    System.out.println("MapFragmentView onActivityResult sourceLocationTxt : " + sourceLocationTxt +
                            " currentPlaceId : " + sourcePlaceId +
                            " destinationLocationTxt : " + destinationLocationTxt +
                            " destinationPlaceId : " + destinationPlaceId +
                            " originLat : " + originLat +
                            " originLng : " + originLng +
                            " destinationLat : " + destinationLat +
                            " destinationLng : " + destinationLng);
                    destLat = Double.parseDouble(destinationLat);
                    destLong = Double.parseDouble(destinationLng);
                    sourceLat = Double.parseDouble(originLat);
                    sourceLong = Double.parseDouble(originLng);
                    frameLoaderLayout.setVisibility(View.VISIBLE);
                    iMapFragmentPresenter.getTripDirection(originLat, originLng, destinationLat, destinationLng, false, ApiKey, userId);
                    Toast.makeText(getActivity(), "sourceLocationTxt : " + sourceLocationTxt + ", destinationLocationTxt : " + destinationLocationTxt, Toast.LENGTH_SHORT).show();

                }


                break;

            case Helper.GET_DRIVER:
                System.out.println("MapFragmentView onActivityResult GET_DRIVER : ");
                if (resultCode == Activity.RESULT_OK) {
                    System.out.println("MapFragmentView onActivityResult GET_DRIVER : OK");
                    onTripStartUp = data.getParcelableExtra("OnTripStartUp");
                    iMapFragmentPresenter.hideVisibilityLayoutItems(View.GONE);
                    final DriverProfile driverProfile = onTripStartUp.getDriverProfile();
                    final TripInfo tripInfo = onTripStartUp.getTripInfo();
                    Gson gson = new Gson();
                    String jsonString = gson.toJson(onTripStartUp);
                    System.out.println("MapFragmentView onActivityResult GET_DRIVER onTripStartUp json : " + jsonString);
                    setTripStartDetails();
                    iMapFragmentPresenter.getTripDriverToPickUp(driverProfile.getDriverLocation(), tripInfo.getPickupLocation(), false, ApiKey, tripInfo.getPickupAddress());
                } else if (resultCode != Activity.RESULT_OK) {
                    System.out.println("MapFragmentView onActivityResult GET_DRIVER : NILL ");
                    onTripStartUp = getTripDetails();
                    if (onTripStartUp != null) {
                        iMapFragmentPresenter.hideVisibilityLayoutItems(View.GONE);
                        final DriverProfile driverProfile = onTripStartUp.getDriverProfile();
                        final TripInfo tripInfo = onTripStartUp.getTripInfo();
                        Gson gson = new Gson();
                        String jsonString = gson.toJson(onTripStartUp);
                        System.out.println("MapFragmentView onActivityResult GET_DRIVER onTripStartUp json : " + jsonString);
                        setTripStartDetails();
                        iMapFragmentPresenter.getTripDriverToPickUp(driverProfile.getDriverLocation(), tripInfo.getPickupLocation(), false, ApiKey, tripInfo.getPickupAddress());
                    }
                    System.out.println("MapFragmentView onActivityResult GET_DRIVER requestCode : " + requestCode + ", resultCode : " + resultCode);
                }
                break;

            default:
                break;
        }
    }

    private OnTripStartUp getTripDetails() {
        Gson gson = new Gson();
        String json = sharedPrefs.getString("OnTripStartUp", null);
        if (json != null) {
            System.out.println("-----------MapFragmentView getTripDetails OnTripStartUp : " + json);
            onTripStartUp = gson.fromJson(json, OnTripStartUp.class);
            //emailAddress = userDetails.getEmail();
            return onTripStartUp;
        }
        return onTripStartUp;
    }

    public void setTripStartDetails() {
        layoutOnTripStartUp.setVisibility(View.VISIBLE);
        iMapFragmentPresenter.hideVisibilityLayoutItems(View.GONE);
        final DriverProfile driverProfile = onTripStartUp.getDriverProfile();
        final TripInfo tripInfo = onTripStartUp.getTripInfo();
        try {
            Picasso.with(getActivity())
                    .load(driverProfile.getDriverPhoto())
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .resize(0, 200)
                    .into(driverCircleImageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            //Try again online if cache failed
                            Picasso.with(getActivity())
                                    .load(driverProfile.getDriverPhoto())
                                    .error(R.drawable.profile)
                                    .resize(0, 200)
                                    .into(driverCircleImageView, new Callback() {
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
        driverName.setText(driverProfile.getDriverName());
        //driverRating;
        carName.setText(driverProfile.getCarName());
        driverCity.setText(driverProfile.getDriverCity());
        carNo.setText(driverProfile.getCarNumber());
        tripStartOrgin.setText(tripInfo.getPickupAddress());
        tripEndDestin.setText(tripInfo.getDropAddress());
        tripAmount.setText(tripInfo.getTripAmount());
    }

    private double bearingBetweenLocations(LatLng latLng1,LatLng latLng2) {

        double PI = 3.14159;
        double lat1 = latLng1.latitude * PI / 180;
        double long1 = latLng1.longitude * PI / 180;
        double lat2 = latLng2.latitude * PI / 180;
        double long2 = latLng2.longitude * PI / 180;

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;

        return brng;
    }

    private void rotateMarker(final Marker marker, final float toRotation) {
        if(!isMarkerRotating) {
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            final float startRotation = marker.getRotation();
            final long duration = 1000;

            final Interpolator interpolator = new LinearInterpolator();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    isMarkerRotating = true;

                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = interpolator.getInterpolation((float) elapsed / duration);

                    float rot = t * toRotation + (1 - t) * startRotation;

                    marker.setRotation(-rot > 180 ? rot / 2 : rot);
                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    } else {
                        isMarkerRotating = false;
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabLoc:
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mFusedLocationClient.getLastLocation()
                        .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    // Logic to handle location object
                                    mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("Marker"));
                                    CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
                                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(13);
                                    mMap.moveCamera(center);
                                    mMap.animateCamera(zoom);
                                }
                            }
                        });
                break;
            case R.id.btnRideNow:
                Intent intent = new Intent(getActivity(), FareEstimate.class);
                Bundle bundle = new Bundle();
                bundle.putString("nameStart", sourceLocationTxt);
                bundle.putString("nameStartLoc", sourceLat + "," + sourceLong);
                bundle.putString("nameEnd", destinationLocationTxt);
                bundle.putString("nameEndLoc", destinationLat + "," + destinationLng);
                bundle.putString("distanceKm", distance);
                bundle.putString("driveClassType", driveClassType);
                bundle.putString("driveCarType", driveCarType);
                bundle.putString("driveCarId", driveCarId);
                bundle.putParcelable("UserDetails", userDetails);
                bundle.putString("cost", String.format("%.3f", cash));
                intent.putExtras(bundle);
                startActivityForResult(intent, Helper.GET_DRIVER);
                onBackPressFun();
                break;
            case R.id.tvMapSourceDestination:
                tvMapSourceDestination.setEnabled(false);
                iMapFragmentPresenter.getCurrentLocDetails();

                // Toast.makeText(getContext(),"This Features Ui is under Construction  ",Toast.LENGTH_LONG).show();
                break;
            case R.id.layoutCarMini:
                truckImg.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.pick_up_grey));
                tCashAmt.setTextColor(ContextCompat.getColor(getActivity(), R.color.hintHighLightText));
                truckTypeTxt.setTextColor(ContextCompat.getColor(getActivity(), R.color.hintHighLightText));
                carImg.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.sedan_orange));
                cashAmt.setTextColor(ContextCompat.getColor(getActivity(), R.color.highLightText));
                carTypeTxt.setTextColor(ContextCompat.getColor(getActivity(), R.color.highLightText));
                driveCarType = "carMini";
                break;
            case R.id.carTruck:
                carImg.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.sedan_grey));
                cashAmt.setTextColor(ContextCompat.getColor(getActivity(), R.color.hintHighLightText));
                carTypeTxt.setTextColor(ContextCompat.getColor(getActivity(), R.color.hintHighLightText));
                truckImg.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.pick_up_orange));
                tCashAmt.setTextColor(ContextCompat.getColor(getActivity(), R.color.highLightText));
                truckTypeTxt.setTextColor(ContextCompat.getColor(getActivity(), R.color.highLightText));
                driveCarType = "Truck";
                break;
            case R.id.choseTripBackPress:
                onBackPressFun();
                break;
            case R.id.layoutTripSupportCall:
                System.out.println("callCustomerCare -- -  : ");
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + getResources().getString(R.string.phone_number_support)));
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_SUPPORT_CALL);
                } else {
                    startActivity(callIntent);
                }
                break;
            case R.id.layoutTripCancel:
                cancelChoiceDialog("layoutTripCancel");
                editor.clear();
                break;
            case R.id.layoutOnTripStartUp:
                System.out.println("clicked: " + clicked);
                if (clicked) {
                    driverBodyLayout.setVisibility(View.VISIBLE);
                    clicked = false;
                } else if (!clicked) {
                    driverBodyLayout.setVisibility(View.GONE);
                    clicked = true;
                }
                break;
            case R.id.layoutTripCustomerCallOngoing:
                System.out.println("callCustomerCare -- -  : ");
                callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + getResources().getString(R.string.phone_number_customer)));
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CUSTOMER_CALL);
                } else {
                    startActivity(callIntent);
                }
                break;
            case R.id.layoutTripEmergencyCallOngoing:
                System.out.println("callCustomerCare -- -  : ");
                callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + getResources().getString(R.string.phone_number_emergency)));
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_SUPPORT_CALL);
                } else {
                    startActivity(callIntent);
                }
                break;
            case R.id.layoutOngoingTripCancel:
                cancelChoiceDialog("layoutOngoingTripCancel");
                editor.clear();
                break;
        }
    }

    private Bitmap getMarkerBitmapFromView(String locationNAme,String point) {

        View customMarkerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker, null);
        TextView locationName = (TextView) customMarkerView.findViewById(R.id.locationName);
        ImageView markerIcon = (ImageView) customMarkerView.findViewById(R.id.markerIcon);
        if(point.equals("startPoint")){
            markerIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.location_pointer_icons_orange));
        }else if(point.equals("endPoint")){
            markerIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.location_pointer_icons_orange_square));
        }else {
            markerIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.location_pointer_icons_black));
        }
        locationName.setText(locationNAme);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        //customMarkerView.setPadding((int) getResources().getDimension(R.dimen._24),0,0,0);
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

    /**
     * Zooms a Route (given a List of LalLng) at the greatest possible zoom level.
     *
     * @param googleMap:      instance of GoogleMap
     * @param lstLatLngRoute: list of LatLng forming Route
     */
    public void zoomRoute(GoogleMap googleMap, List<LatLng> lstLatLngRoute) {
        int routePadding = 300;
        if (googleMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty()) return;

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng latLngPoint : lstLatLngRoute)
            boundsBuilder.include(latLngPoint);

        if (screenHeight < 500 && screenWidth < 400) {
            routePadding = 100;
        } else {
            routePadding = 300;
        }

        LatLngBounds latLngBounds = boundsBuilder.build();

        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding));
        //googleMap.setPadding(0, 0, 0, 30);
    }


    public void cancelChoiceDialog(final String function) {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.your_custom_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final Button btnYes = (Button) promptsView.findViewById(R.id.btn_yes);
        final Button btnNo = (Button) promptsView.findViewById(R.id.btn_no);

        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iMapFragmentPresenter.dialogueYesClickListener(function);
                alertDialog.cancel();
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        alertDialog.show();
    }

    public void onBackPressFun() {
        iLandingView.hideVisibilityLandingItems(View.VISIBLE, "toolbar");
        iMapFragmentPresenter.hideVisibilityLayoutItems(View.VISIBLE);
        iLandingView.hideVisibilityLandingItems(View.GONE, "backImg");
        choseTrip.setVisibility(View.GONE);
        serviceUnavailable.setVisibility(View.GONE);
        setLocMethod();
    }

    public void setLocMethod() {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(new LatLng(lat, log)).title("Marker"));
        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(lat, log));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(13);
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
    }

    @Override
    public void setRoutesTrip(List<List<HashMap<String, String>>> route, List<Route> routes, String tripDist) {
        BitmapDescriptor iconStart = BitmapDescriptorFactory.fromResource(R.drawable.location_pointer_icons_orange);
        BitmapDescriptor iconEnd = BitmapDescriptorFactory.fromResource(R.drawable.location_pointer_icons_orange_square);
        distance =tripDist;
        double presentTripLat = 0;
        double presentTripLng = 0;
        double endTripLat = 0;
        double endTripLng = 0;
        double mMarkerRotations = 0;

        if (destLat > 0.0 && destLong > 0.0 && sourceLat > 0.0 && sourceLong > 0.0) {
             sourceLatLng = new LatLng(sourceLat,sourceLong);
             destLatLng = new LatLng(destLat,destLong);
            System.out.println("MapFragmentView---IF");
            if (mMap != null) {
                mMap.clear();
                mSourceMarker = null;
                /*mSourceMarker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(sourceLat, sourceLong))
                        .title("From"));
                mSourceMarker.setIcon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(sourceLocationTxt,"startPoint")));
                LatLng sourseLatLng = new LatLng(sourceLat, sourceLong);
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(destLat, destLong))
                        .title("To"))
                        .setIcon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(destinationLocationTxt,"endPoint")));
                LatLng destLatLng = new LatLng(destLat, destLong);*/
                ArrayList<LatLng> points = null;
                PolylineOptions polyLineOptions = null;
                // traversing through routes
                double dist = 0;
                Location srcLoc = new Location("");
                Location destLoc = new Location("");
                for (int i = 0; i < routes.size(); i++) {
                    points = new ArrayList<LatLng>();
                    polyLineOptions = new PolylineOptions();
                    List<HashMap<String, String>> path = route.get(i);
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);
                        HashMap<String, String> point2 = path.get(j);
                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        if (destLoc.getLatitude() > 0) {
                            destLoc.setLatitude(srcLoc.getLatitude());
                            destLoc.setLongitude(srcLoc.getLongitude());
                        } else {
                            presentTripLat =lat;
                            presentTripLng= lng;
                            destLoc.setLatitude(lat);
                            destLoc.setLongitude(lng);
                        }
                        srcLoc.setLatitude(lat);
                        srcLoc.setLongitude(lng);
                        dist = dist + destLoc.distanceTo(srcLoc);
                        LatLng position = new LatLng(lat, lng);
                        points.add(position);
                        if(j==path.size()-1){
                            endTripLat = Double.parseDouble(point.get("lat"));
                            endTripLng = Double.parseDouble(point.get("lng"));
                        }else if(j==0){
                            presentTripLat = Double.parseDouble(point.get("lat"));
                            presentTripLng = Double.parseDouble(point.get("lng"));
                        }
                    }
                    zoomRoute(mMap, points);
                    polyLineOptions.addAll(points);
                    int lineWidth = 10;
                    if (screenHeight < 500 && screenWidth < 400) {
                        lineWidth = 5;
                    } else {
                        lineWidth = 10;
                    }
                    polyLineOptions.width(lineWidth);
                    polyLineOptions.color(Color.DKGRAY);
                    //points.get()
                    //presentTripLat =polyLineOptions.getPoints().get(path.size()-1).latitude;
                    //presentTripLng =polyLineOptions.getPoints().get(path.size()-1).longitude;
                }
                //mMarkerRotations = bearingBetweenLocations(sourseLatLng,destLatLng);
                //rotateMarker(mSourceMarker,Float.valueOf(String.valueOf(mMarkerRotations)));
               Marker startPoint =  mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(presentTripLat,presentTripLng)));
                        startPoint.setIcon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(sourceLocationTxt,"startPoint")));
               Marker endPoint= mMap.addMarker(new MarkerOptions()
                        .position(new LatLng( endTripLat,endTripLng)));
                        endPoint.setIcon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(destinationLocationTxt,"endPoint")));

                frameLoaderLayout.setVisibility(View.GONE);


                iMapFragmentPresenter.hideVisibilityLayoutItems(View.INVISIBLE);
                if (polyLineOptions != null) {
                    System.out.println("MapFragmentView---polyLineOptions --IF");
                    mMap.addPolyline(polyLineOptions);
                    distanceCal = Math.floor((dist / 1000));
                    Log.e("distance: ", distanceCal + "");
                    if (distanceCal < 1) {
                        cash = cashMinimum;
                    } else {
                        cash = cashMinimum + (double) Math.floor((distanceCal - 1) * cashPerKM);
                    }
                    cash = (double) Math.floor((dist / 1000) * cashPerKM);
                    cashAmt.setText("BD : " + String.format("%.3f", cash));
                } else {
                    Toast.makeText(getActivity(), "Could not find path", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    public void setRoutesDriverToPickUp(List<List<HashMap<String, String>>> route, List<Route> routes, String driverLat, String driverLng, String pickUpLat, String pickUpLng, String pickUpLocName) {
        double driverLatD = Double.parseDouble(driverLat);
        double driverLngD = Double.parseDouble(driverLng);
        double pickUpLatD = Double.parseDouble(pickUpLat);
        double pickUpLngD = Double.parseDouble(pickUpLng);

        if (driverLatD > 0.0 && driverLngD > 0.0 && pickUpLatD > 0.0 && pickUpLngD > 0.0) {
            if (mMap != null) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(driverLatD, driverLngD))
                        .title("From"))
                        .setIcon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView("Driver Location","startPoint")));
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(pickUpLatD, pickUpLngD))
                        .title("To"))
                        .setIcon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(pickUpLocName,"endPoint")));
                ArrayList<LatLng> points = null;
                PolylineOptions polyLineOptions = null;
                // traversing through routes
                double dist = 0;
                Location srcLoc = new Location("");
                Location destLoc = new Location("");
                for (int i = 0; i < routes.size(); i++) {
                    points = new ArrayList<LatLng>();
                    polyLineOptions = new PolylineOptions();
                    List<HashMap<String, String>> path = route.get(i);
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);
                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        if (destLoc.getLatitude() > 0) {
                            destLoc.setLatitude(srcLoc.getLatitude());
                            destLoc.setLongitude(srcLoc.getLongitude());
                        } else {
                            destLoc.setLatitude(lat);
                            destLoc.setLongitude(lng);
                        }
                        srcLoc.setLatitude(lat);
                        srcLoc.setLongitude(lng);
                        dist = dist + destLoc.distanceTo(srcLoc);
                        LatLng position = new LatLng(lat, lng);
                        points.add(position);
                    }
                    zoomRoute(mMap, points);
                    polyLineOptions.addAll(points);
                    int lineWidth = 10;
                    if (screenHeight < 500 && screenWidth < 400) {
                        lineWidth = 5;
                    } else {
                        lineWidth = 10;
                    }
                    polyLineOptions.width(lineWidth);
                    polyLineOptions.color(Color.DKGRAY);
                }
                frameLoaderLayout.setVisibility(View.GONE);
                layoutOnTripStartUp.setVisibility(View.VISIBLE);
                iMapFragmentPresenter.hideVisibilityLayoutItems(View.INVISIBLE);
                if (polyLineOptions != null) {
                    System.out.println("MapFragmentView---polyLineOptions --IF");
                    mMap.addPolyline(polyLineOptions);
                } else {
                    System.out.println("MapFragmentView---Could not find path --IF");
                    Toast.makeText(getActivity(), "Could not find path", Toast.LENGTH_SHORT).show();
                }
            } else {
                System.out.println("MapFragmentView---Map null --IF");
            }
        }
    }

    @Override
    public void doVisibilityLayoutItems(int visibility) {
        tvMapSourceDestination.setVisibility(visibility);
        fabLoc.setVisibility(visibility);
    }

    @Override
    public void getDriverCarDetails(List<Cars> cares, Boolean state) {
        if (state) {
            serviceUnavailable.setVisibility(View.VISIBLE);
        } else {
            choseTrip.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < cares.size(); i++) {
            String carType = cares.get(i).getName();
            int carId = cares.get(i).getId();
        }

    }

    @Override
    public void dialogueYesClickListener(String function) {
        if (function.equals("layoutTripCancel")) {
            driverBodyLayout.setVisibility(View.GONE);
            layoutOnTripStartUp.setVisibility(View.GONE);
            clicked = true;
            iMapFragmentPresenter.tripCancelOnStart(userDetails.getUserID(), Integer.valueOf(onTripStartUp.getTripInfo().getTripId()));
        } else if (function.equals("layoutOngoingTripCancel")) {
            layoutDriverWaiting.setVisibility(View.VISIBLE);
            layoutTripOngoing.setVisibility(View.GONE);
            layoutOnTripStartUp.setVisibility(View.GONE);
            iLandingView.hideVisibilityLandingItems(View.VISIBLE, "toolbar");
            iMapFragmentPresenter.hideVisibilityLayoutItems(View.VISIBLE);
            iMapFragmentPresenter.tripOngoingEnded(userDetails.getUserID(), Integer.valueOf(onTripStartUp.getTripInfo().getTripId()));
        }
    }

    @Override
    public void tripCanceled() {
        iLandingView.hideVisibilityLandingItems(View.VISIBLE, "toolbar");
        iMapFragmentPresenter.hideVisibilityLayoutItems(View.VISIBLE);
        setLocMethod();
        Toast.makeText(getActivity(), "Trip Canceled...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void tripOnGoingEnded() {
        iLandingView.hideVisibilityLandingItems(View.VISIBLE, "toolbar");
        iMapFragmentPresenter.hideVisibilityLayoutItems(View.VISIBLE);
        setLocMethod();
        Toast.makeText(getActivity(), "Trip Ended...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void highLikeHoodCurrentPlace(CurrentPlaceDetails maxCurrentPlaceDetails) {
        this.maxCurrentPlaceDetails = maxCurrentPlaceDetails;
        Intent intent = new Intent(getActivity(), MapPlaceSearch.class);
        bundle = new Bundle();
        intent.putExtra("UserDetails", userDetails);
        intent.putExtra("CurrentPlaceDetails", maxCurrentPlaceDetails);
        intent.putExtras(bundle);
        startActivityForResult(intent, Helper.SEARCH_MAP_API_TRIP);
        tvMapSourceDestination.setEnabled(true);
    }
}
