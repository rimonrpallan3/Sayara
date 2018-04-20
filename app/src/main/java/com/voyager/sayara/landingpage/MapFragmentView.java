package com.voyager.sayara.landingpage;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
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
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.voyager.sayara.MapPlaceSearch.MapPlaceSearch;
import com.voyager.sayara.PulsatingActivity.PulsatingActivity;
import com.voyager.sayara.PulsatingActivity.view.IPulsatingView;
import com.voyager.sayara.R;
import com.voyager.sayara.common.Helper;
import com.voyager.sayara.fare.FareEstimate;
import com.voyager.sayara.landingpage.model.Cars;
import com.voyager.sayara.landingpage.model.OnTripStartUp;
import com.voyager.sayara.landingpage.model.geogetpath.Route;
import com.voyager.sayara.landingpage.presenter.IMapFragmentPresenter;
import com.voyager.sayara.landingpage.presenter.MapFragmentPresenter;
import com.voyager.sayara.landingpage.view.ILandingView;
import com.voyager.sayara.landingpage.view.IMapFragmentView;
import com.voyager.sayara.registerpage.model.UserDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by User on 19-Jan-18.
 */

public class MapFragmentView extends Fragment implements
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
    int cashPerKM = 10;
    int cashMinimum = 20;
    int cash;
    String distance = "";
    double distanceCal = 0.0;
    double sourceLat = 0.0;
    double sourceLong = 0.0;
    double destLat = 0.0;
    double destLong = 0.0;
    PolylineOptions polylineOptions;
    String username;
    SharedPreferences sharedPreferences;
    // ----------- Newly added
    Activity activity;
    LinearLayout tvMapSourceDestination;
    LocationRequest mLocationRequest;
    public MapView mMapView;
    String mprovider;
    View rootView;
    FloatingActionButton fabLoc;
    private static final String TAG = MapFragmentView.class.getSimpleName();
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

    String sourceLocationTxt = "";
    String sourcePlaceId = "";
    String destinationLocationTxt = "";
    String destinationPlaceId = "";
    String originLat = "";
    String originLng = "";
    String destinationLat = "";
    String destinationLng = "";
    String driveCarType = "carMini";
    String driveClassType = "Diamond";
    String ApiKey = "";

    String driveCarId ="1";
    IMapFragmentPresenter iMapFragmentPresenter;
    ImageButton choseTripBackPress;
    ILandingView iLandingView;
    LinearLayout carMiniLayout;
    LinearLayout carTruck;
    UserDetails userDetails;
    OnTripStartUp onTripStartUp;

    String findDriver ="";


    public MapFragmentView(Activity activity) {
        this.activity = activity;
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
        ApiKey = getString(R.string.place_api_key);
        tvMapSourceDestination = (LinearLayout) rootView.findViewById(R.id.tvMapSourceDestination);
        //------------ Car type Selection 1----------------
        carMiniLayout = (LinearLayout) rootView.findViewById(R.id.carMiniLayout);
        carImg = (ImageView) rootView.findViewById(R.id.carImg);
        cashAmt = (TextView) rootView.findViewById(R.id.cashAmt);
        carTypeTxt = (TextView) rootView.findViewById(R.id.carTypeTxt);

        //------------ Car type Selection 2----------------
        carTruck = (LinearLayout) rootView.findViewById(R.id.carTruck);
        truckImg = (ImageView) rootView.findViewById(R.id.truckImg);
        tCashAmt = (TextView) rootView.findViewById(R.id.tCashAmt);
        truckTypeTxt = (TextView) rootView.findViewById(R.id.truckTypeTxt);

        choseTripBackPress = (ImageButton) ((AppCompatActivity) getActivity()).findViewById(R.id.choseTripBackPress);
        choseTrip = (FrameLayout) rootView.findViewById(R.id.choseTrip);
        serviceUnavailable = (FrameLayout) rootView.findViewById(R.id.serviceUnavailable);
        btnRideNow = (Button) rootView.findViewById(R.id.btnRideNow);
        fabLoc = (FloatingActionButton) rootView.findViewById(R.id.fabLoc);
        frameLoaderLayout = (FrameLayout) rootView.findViewById(R.id.frameLoaderLayout);
        mMapView = (MapView) rootView.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            userDetails = bundle.getParcelable("UserDetails");
                System.out.println("MapFragmentView -- UserDetails- userId : " + userDetails.getUserID());
        }
        try {
            mprovider = locationManager.getBestProvider(criteria, false);
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }
            location = locationManager.getLastKnownLocation(locationManager
                    .getBestProvider(criteria, false));
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }

        tvMapSourceDestination.setOnClickListener(this);
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
        iMapFragmentPresenter = new MapFragmentPresenter(this);

        /*Intent resultIntent = new Intent(getContext(), PulsatingActivity.class);
        resultIntent.putExtra("waitingForDriver", "true");
        startActivity(resultIntent);*/

        return rootView;
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

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {

            }
        });

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            mMap.setMyLocationEnabled(true);
        }*/
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(rootView.findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }


    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(activity,
                        android.Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");

            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(activity,
                                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });

        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(activity,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
        if (!checkPermissions()) {
            requestPermissions();
        } else {
        }
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
                    sourcePlaceId = data.getStringExtra("sourcePlaceId");
                    destinationLocationTxt = data.getStringExtra("destinationLocationTxt");
                    destinationPlaceId = data.getStringExtra("destinationPlaceId");
                    originLat = data.getStringExtra("originLat");
                    originLng = data.getStringExtra("originLng");
                    destinationLat = data.getStringExtra("destinationLat");
                    destinationLng = data.getStringExtra("destinationLng");
                    Integer userId = data.getIntExtra("userId", 0);
                    System.out.println("MapFragmentView onActivityResult sourceLocationTxt : " + sourceLocationTxt +
                            " sourcePlaceId : " + sourcePlaceId +
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
                    iMapFragmentPresenter.getTripDirection(originLat, originLng, destinationLat, destinationLng, false, ApiKey,userId);
                    Toast.makeText(getContext(), "sourceLocationTxt : " + sourceLocationTxt + ", destinationLocationTxt : " + destinationLocationTxt, Toast.LENGTH_SHORT).show();

                }


                break;

            case Helper.GET_DRIVER:
                if (resultCode == Activity.RESULT_OK) {
                    onTripStartUp = data.getParcelableExtra("OnTripStartUp");
                    Gson gson = new Gson();
                    String jsonString = gson.toJson(onTripStartUp);
                    System.out.println("MapFragmentView onActivityResult GET_DRIVER onTripStartUp json : " + jsonString );
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabLoc:
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
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
                bundle.putString("nameStartLoc", sourceLat+","+sourceLong);
                bundle.putString("nameEnd", destinationLocationTxt);
                bundle.putString("nameEndLoc", destinationLat+","+destinationLng);
                bundle.putString("distanceKm", distance);
                bundle.putString("driveClassType", driveClassType);
                bundle.putString("driveCarType", driveCarType);
                bundle.putString("driveCarId", driveCarId);
                bundle.putParcelable("UserDetails", userDetails);
                bundle.putInt("cost", cash);
                intent.putExtras(bundle);
                startActivityForResult(intent, Helper.GET_DRIVER);
                onBackPressFun();
                break;
            case R.id.tvMapSourceDestination:
                intent = new Intent(getActivity(), MapPlaceSearch.class);
                bundle = new Bundle();
                intent.putExtra("UserDetails", userDetails);
                intent.putExtras(bundle);
                startActivityForResult(intent, Helper.SEARCH_MAP_API_TRIP);
               // Toast.makeText(getContext(),"This Features Ui is under Construction  ",Toast.LENGTH_LONG).show();
                break;
            case R.id.carMiniLayout:
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
        }
    }

    private Bitmap getMarkerBitmapFromView(String locationNAme) {

        View customMarkerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker, null);
        TextView locationName = (TextView) customMarkerView.findViewById(R.id.locationName);
        locationName.setText(locationNAme);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
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
     * @param googleMap: instance of GoogleMap
     * @param lstLatLngRoute: list of LatLng forming Route
     */
    public void zoomRoute(GoogleMap googleMap, List<LatLng> lstLatLngRoute) {

        if (googleMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty()) return;

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng latLngPoint : lstLatLngRoute)
            boundsBuilder.include(latLngPoint);

        int routePadding = 300;
        LatLngBounds latLngBounds = boundsBuilder.build();

        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding));
        //googleMap.setPadding(0, 0, 0, 30);
    }

    public void onBackPressFun(){
        iLandingView.hideVisibilityLandingItems(View.VISIBLE,"toolbar");
        iMapFragmentPresenter.hideVisibilityLayoutItems(View.VISIBLE);
        iLandingView.hideVisibilityLandingItems(View.GONE,"backImg");
        choseTrip.setVisibility(View.GONE);
        serviceUnavailable.setVisibility(View.GONE);

        mMap.addMarker(new MarkerOptions().position(new LatLng(lat, log)).title("Marker"));
        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(lat, log));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(13);
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
    }

    @Override
    public void setRoutes(List<List<HashMap<String, String>>> route, List<Route> routes,String tripDist) {
        distance = tripDist;
        System.out.println("MapFragmentView onActivityResult sourceLocationTxt : "+ sourceLocationTxt +
                " destLat : "+destLat+
                " destLong : "+destLong+
                " sourceLat : "+sourceLat+
                " sourceLong : "+sourceLong+
                " originLng : "+originLng);
        if (destLat > 0.0 && destLong > 0.0 && sourceLat > 0.0 && sourceLong > 0.0) {
            System.out.println("MapFragmentView---IF");
            if (mMap != null) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(sourceLat, sourceLong))
                        .title("From"))
                        .setIcon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(sourceLocationTxt)));
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(destLat, destLong))
                        .title("To"))
                        .setIcon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(destinationLocationTxt)));
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
                    zoomRoute(mMap,points);
                    polyLineOptions.addAll(points);
                    polyLineOptions.width(10);
                    polyLineOptions.color(Color.DKGRAY);
                }
                frameLoaderLayout.setVisibility(View.GONE);


                iMapFragmentPresenter.hideVisibilityLayoutItems(View.INVISIBLE);
                if (polyLineOptions != null) {
                    System.out.println("MapFragmentView---polyLineOptions --IF");
                    mMap.addPolyline(polyLineOptions);
                    distanceCal = Math.floor((dist / 1000));
                    Log.e("distance: ", distanceCal + "");
                    if (distanceCal <= 1) {
                        cash = cashMinimum;
                    } else {
                        cash = cashMinimum + (int) Math.floor((distanceCal - 1) * cashPerKM);
                    }
                    cash = (int) Math.floor((dist/1000) * cashPerKM);
                    cashAmt.setText("BD : " + cash);
                } else {
                    Toast.makeText(getActivity(), "Could not find path", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void doVisibilityLayoutItems(int visibility) {
        tvMapSourceDestination.setVisibility(visibility);
        fabLoc.setVisibility(visibility);
    }

    @Override
    public void getDriverCarDetails(List<Cars> cares,Boolean state) {
        if(state){
            serviceUnavailable.setVisibility(View.VISIBLE);
        }else {
            choseTrip.setVisibility(View.VISIBLE);
        }
        System.out.println("getDriverCarDetails ---carType");
        for(int i=0;i<cares.size();i++) {
            String carType=cares.get(i).getName();
            int carId=cares.get(i).getId();
            System.out.println("carType"+carType);
            System.out.println("driveCarId"+carId);
        }

    }
}
