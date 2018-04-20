package com.voyager.sayara.fragments.map;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.voyager.sayara.R;
import com.voyager.sayara.fragments.map.adapter.PlaceAutoCompleteAdapter;
import com.voyager.sayara.fragments.map.helper.MapStateManager;
import com.voyager.sayara.fragments.map.helper.Util;
import com.voyager.sayara.fragments.map.helper.onSomeEventListener;
import com.directions.route.RoutingListener;
import com.voyager.sayara.fragments.map.model.PlaceDetailModel;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by User on 28-Sep-17.
 */

public class MapFragment extends Fragment implements
        RoutingListener,
        GoogleMap.OnMarkerClickListener,
        OnMapReadyCallback, LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        OnCompleteListener<Void>,
        View.OnClickListener {


    private GoogleMap googleMap;
    private MapView mMapView;
    LocationRequest mLocationRequest;
    LocationManager locationManager;
    double lat = 26.2285;
    double log = 50.5860;
    String mprovider;
    private FusedLocationProviderClient mFusedLocationClient;


    public LinearLayout serviceUnavailable;
    public LinearLayout serviceAvailable;
    Button btnRideNow;
    AutoCompleteTextView autotxtOrigin;
    AutoCompleteTextView autotxtDestination;

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    View rootView;

    FrameLayout searchLocation;

    private static final String TAG = MapFragment.class.getSimpleName();

    Activity activity;
    public Toolbar mapToolbar;

    public int x = 0;
    public int i = 0;

    int PLACE_PICKER_REQUEST = 1;

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 2;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 3;

    public PlaceAutoCompleteAdapter PlaceAutoCompleteAdapter;
    private List<Polyline> polylines;

    private GoogleApiClient mGoogleApiClient;
    private static final int[] COLORS = new int[]{R.color._1, R.color._2, R.color._3, R.color._4, R.color._5};
    private ProgressDialog progressDialog;

    private LatLng start;
    private LatLng end;

    private static final LatLngBounds BOUNDS_MANAMA = new LatLngBounds(new LatLng(25.7083335, 47.9116665),
            new LatLng(26.416667, 50.823333));



    Boolean mRequestingLocationUpdates = true;

    String REQUESTING_LOCATION_UPDATES_KEY = "Update_KEY";

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    Bundle mapViewBundle = null;

    private static final String ADDRESS_REQUESTED_KEY = "address-request-pending";
    private static final String LOCATION_ADDRESS_KEY = "location-address";

    /**
     * The formatted location address.
     */
    private String mAddressOutput;

    /**
     * Tracks whether the user has requested an address. Becomes true when the user requests an
     * address and false when the address (or an error message) is delivered.
     */
    private boolean mAddressRequested;


    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location = null; // location
    double latitude; // latitude
    double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    private LocationCallback mLocationCallback;


    /*Auto Complete Place Apis*/
    public String placeNameStart = "";
    public String placeIdStart = "";
    public String placeAddressStart = "";
    public String placePhoneStart = "";
    public String placeWebsiteStart = "";
    public LatLng placeLocationStart;

    /*Auto Complete Place Apis*/
    public String placeNameStop = "";
    public String placeIdStop = "";
    public String placeAddressStop = "";
    public String placePhoneStop = "";
    public String placeWebsiteStop = "";
    public LatLng placeLocationStop;


    public String placeDistanceKm = "";
    public String placeCountry = "";
    public String placeAdress = "";

    PlaceDetailModel placeDetailModel;
    public Boolean isMapLoc = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    onSomeEventListener someEventListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.map_fragment, container, false);
        System.out.println("MapFragmentView");
        this.rootView = rootView;
        activity = getActivity();


        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        updateValuesFromBundle(savedInstanceState);



        serviceUnavailable = (LinearLayout) rootView.findViewById(R.id.serviceUnavailable);
        serviceAvailable = (LinearLayout) rootView.findViewById(R.id.serviceAvailable);
        btnRideNow = (Button) rootView.findViewById(R.id.btnRideNow);


        mMapView = rootView.findViewById(R.id.map);

        locationManager = (LocationManager) activity.getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        mprovider = locationManager.getBestProvider(criteria, false);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        MapsInitializer.initialize(this.getActivity());

        searchLocation.setOnClickListener(this);
        serviceUnavailable.setOnClickListener(this);
        serviceAvailable.setOnClickListener(this);
        btnRideNow.setOnClickListener(this);
        autotxtOrigin.setOnClickListener(this);
        autotxtDestination.setOnClickListener(this);
        someEventListener.someEvent(0, serviceAvailable, searchLocation);


        polylines = new ArrayList<>();
        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        MapsInitializer.initialize(activity);
        mGoogleApiClient.connect();

        // Set defaults, then update using values stored in the Bundle.
        mAddressRequested = false;
        mAddressOutput = "";
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);

        return rootView;
    }


    private void openAutocompleteActivityStart() {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(activity);
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(activity, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Log.e(TAG, message);
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        }
    }

    private void openAutocompleteActivityEnd() {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(activity);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(activity, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Log.e(TAG, message);
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        }
    }

    public void route() {
        System.out.println("MapFragment_rote");

        if (start == null || end == null) {
            if (start == null) {
                System.out.println("MapFragment_rote_start_null");
                if (autotxtOrigin.getText().length() > 0) {
                    autotxtOrigin.setError("Choose location from dropdown.");
                } else {
                    Toast.makeText(activity, "Please choose a autotxtOrigin point.", Toast.LENGTH_SHORT).show();
                }
            }
            if (end == null) {
                if (autotxtDestination.getText().length() > 0) {
                    autotxtDestination.setError("Choose location from dropdown.");
                } else {
                    Toast.makeText(activity, "Please choose a destination.", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            progressDialog = ProgressDialog.show(activity, "Please wait.",
                    "Fetching route information.", true);
            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .alternativeRoutes(true)
                    .waypoints(start, end)
                    .build();
            routing.execute();
        }
    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            System.out.println("updateValuesFromBundle_NonNull----------------------------1");

            // Check savedInstanceState to see if the address was previously requested.
            if (savedInstanceState.keySet().contains(ADDRESS_REQUESTED_KEY)) {
                mAddressRequested = savedInstanceState.getBoolean(ADDRESS_REQUESTED_KEY);
            }
            // Check savedInstanceState to see if the location address string was previously found
            // and stored in the Bundle. If it was found, display the address string in the UI.
            if (savedInstanceState.keySet().contains(LOCATION_ADDRESS_KEY)) {
                mAddressOutput = savedInstanceState.getString(LOCATION_ADDRESS_KEY);
                //displayAddressOutput();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        autotxtOrigin.setEnabled(true);
        autotxtDestination.setEnabled(true);
        System.out.println("MapFragment_onActivityResult_data" + data.toString());
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(getContext(), data);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(getContext(), toastMsg, Toast.LENGTH_LONG).show();
            }
        }

        // Check that the result was from the autocomplete widget.
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                System.out.println("MapFragment_RESULT_OK_data" + data.toString());
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(activity, data);
                Log.i(TAG, "Place Selected: " + place.getName());

                // Format the place's details and display them in the TextView.
              /*  autotxtOrigin.setText(formatPlaceDetails(getResources(), place.getName(),
                        place.getId(), place.getAddress(), place.getPhoneNumber(),
                        place.getWebsiteUri()));*/

                placeNameStart = (String) place.getName();
                if (placeNameStart != null) {
                    System.out.println("placeNameStart: " + placeNameStart);
                    //placeDetailModel.setNameStart(placeNameStart);
                }
                placeIdStart = place.getId();
                placeAddressStart = (String) place.getAddress();
                if (placeAddressStart != null) {
                    System.out.println("placeAddressStart: " + placeAddressStart);
                    // placeDetailModel.setAddressStart(placeAddressStart);
                }

                placePhoneStart = (String) place.getPhoneNumber();
                placeWebsiteStart = String.valueOf(place.getWebsiteUri());
                placeLocationStart = place.getLatLng();
                autotxtOrigin.setText(placeNameStart);
                System.out.println("MapFragment_placeLocationStart_out - " + "latitude: " + placeLocationStart.latitude + " longitude: " + placeLocationStart.longitude);

                if (data.toString() != null) {
                    System.out.println("MapFragment_placeLocationStart_in_ :" + data.toString());
                    //setLocation(placeLocationStart.latitude,placeLocationStart.longitude);
                    googleMap.addMarker(new MarkerOptions().position(new LatLng(lat, log)).title("A"));
                    CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(placeLocationStart.latitude, placeLocationStart.longitude));
                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(13);

                    googleMap.moveCamera(center);
                    googleMap.animateCamera(zoom);
                    start = placeLocationStart;
                }

                // Display attributions if required.
                CharSequence attributions = place.getAttributions();
                if (!TextUtils.isEmpty(attributions)) {
                    System.out.println("MapFragment_attributions_data" + data.toString());
                    autotxtOrigin.setText(Html.fromHtml(attributions.toString()));
                } else {
                    System.out.println("MapFragment_attributions_null_data" + data.toString());
                    //autotxtOrigin.setText("");
                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(activity, data);
                Log.e(TAG, "Error: Status = " + status.toString());
            } else if (resultCode == RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if
                // the user pressed the back button.
            }
        }

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(activity, data);
                Log.i(TAG, "Place: " + place.getName());

                placeNameStop = (String) place.getName();
                // placeDetailModel.setNameEnd(placeNameStop);
                placeIdStop = place.getId();
                placeAddressStop = (String) place.getAddress();
                //  placeDetailModel.setAddressEnd(placeAddressStop);
                placePhoneStop = (String) place.getPhoneNumber();
                placeWebsiteStop = String.valueOf(place.getWebsiteUri());
                placeLocationStop = place.getLatLng();
                autotxtDestination.setText(placeNameStop);
                System.out.println("MapFragment_placeLocationEnd_out - " + "latitude: " + placeLocationStop.latitude + " longitude: " + placeLocationStop.longitude);

                end = placeLocationStop;
                // Display attributions if required.
                CharSequence attributions = place.getAttributions();
                if (!TextUtils.isEmpty(attributions)) {
                    System.out.println("MapFragment_attributionsEnd_data" + data.toString());
                    autotxtDestination.setText(Html.fromHtml(attributions.toString()));
                } else {
                    System.out.println("MapFragment_attributions_null_data" + data.toString());
                    //autotxtDestination.setText("");
                }


            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(activity, data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    /**
     * Helper method to format information about a place nicely.
     */
    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        Log.e(TAG, res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));
        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));

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
        MapStateManager mgr = new MapStateManager(getActivity());
        mgr.saveMapState(googleMap);
        Toast.makeText(getContext(), "Map State has been save?", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            mMapView.onDestroy();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
           /* case R.id.searchLocation:
                if (Util.Operations.isOnline(activity.getApplicationContext())) {
                    route();
                    if (polylines != null) {
                        //someEventListener.someEvent(1, serviceUnavailable, searchLocation);
                    }
                } else {
                    Toast.makeText(activity.getApplicationContext(), "No internet connectivity", Toast.LENGTH_SHORT).show();
                }
               *//* for (i = x; i < 3; i++) {
                    if (i == 0) {
                        Toast.makeText(getContext(), "Please Click again to see the next view", Toast.LENGTH_SHORT).show();
                        x = i + 1;
                        System.out.println("----------------------second" + x);
                        serviceUnavailable.setVisibility(View.VISIBLE);
                        someEventListener.someEvent(1, serviceUnavailable, searchLocation);
                        break;
                    } else if (i == 1) {
                        if (serviceUnavailable.getVisibility() == View.GONE && serviceAvailable.getVisibility() == View.GONE) {
                            x = 0;
                            searchLocation.setEnabled(true);
                        } else {
                            Toast.makeText(getContext(), "Please Click Ride Now Button", Toast.LENGTH_SHORT).show();
                            x = i + 1;
                            System.out.println("----------------------third" + x);
                            serviceUnavailable.setVisibility(View.GONE);
                            serviceAvailable.setVisibility(View.VISIBLE);
                            someEventListener.someEvent(2, serviceAvailable, searchLocation);
                            searchLocation.setEnabled(false);
                        }
                        break;
                    } else if (i == 2) {
                        x = i + 1;
                        System.out.println("----------------------fourth" + x);
                        x = 0;
                        if (serviceUnavailable.getVisibility() == View.GONE && serviceAvailable.getVisibility() == View.GONE) {
                            Toast.makeText(getContext(), "Please Click Again", Toast.LENGTH_SHORT).show();
                            x = 1;
                            System.out.println("----------------------second" + x);
                            serviceUnavailable.setVisibility(View.VISIBLE);
                            someEventListener.someEvent(1, serviceUnavailable, searchLocation);
                        }
                        break;
                    }
                }*/


            case R.id.btnRideNow:
                System.out.println("MapFragment_btmRideNow");
                someEventListener.someEvent(3, serviceAvailable, searchLocation);
                serviceAvailable.setVisibility(View.GONE);
                searchLocation.setEnabled(true);
               /* if(start!=null&&end!=null) {
                    System.out.println("MapFragment_has_start_end");
                    route();
                }else {
                    System.out.println("MapFragment_start_and_end_is_null");
                    Toast.makeText(getContext(),"Please select Start and End Points ", Toast.LENGTH_SHORT).show();
                }*/
                break;
            case R.id.autotxtOrigin:
                openAutocompleteActivityStart();
                autotxtOrigin.setEnabled(false);
                break;
           /* case R.id.autotxtDestination:
                openAutocompleteActivityEnd();
                autotxtDestination.setEnabled(false);
                break;*/
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            someEventListener = (onSomeEventListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onSomeEventListener");
        }
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        //System.out.println("GoogleMap_CTL_lat-------" + start + ",  CTL_log--------" + end);
        googleMap = map;
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);


        if (!isMapLoc) {

            map.addMarker(new MarkerOptions().position(new LatLng(lat, log)).title("Marker"));
            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(lat, log));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(13);
            googleMap.moveCamera(center);
            googleMap.animateCamera(zoom);
            isMapLoc = true;
        }
      /*  if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 5000, 0,
                new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        lat = location.getLatitude();
                        log = location.getLongitude();
                        //System.out.println("NETWORK_PROVIDER_CTL_lat-------" + lat + ",  CTL_log--------" + log);
                        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(lat, log));
                        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
                        googleMap.moveCamera(center);
                        googleMap.animateCamera(zoom);
                    }
                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }
                    @Override
                    public void onProviderEnabled(String provider) {
                    }
                    @Override
                    public void onProviderDisabled(String provider) {
                    }
                });
        if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                3000, 0, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        lat = location.getLatitude();
                        log = location.getLongitude();
                        //System.out.println("GPS_PROVIDER_CTL_lat-------" + lat + ",  CTL_log--------" + log);
                        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(lat, log));
                        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
                        googleMap.moveCamera(center);
                        googleMap.animateCamera(zoom);
                    }
                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }
                    @Override
                    public void onProviderEnabled(String provider) {
                    }
                    @Override
                    public void onProviderDisabled(String provider) {
                    }
                });*/

        if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        PlaceAutoCompleteAdapter = new PlaceAutoCompleteAdapter(activity, android.R.layout.simple_list_item_1,
                mGoogleApiClient, BOUNDS_MANAMA, null);

        googleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                LatLngBounds bounds = googleMap.getProjection().getVisibleRegion().latLngBounds;
                PlaceAutoCompleteAdapter.setBounds(bounds);
            }
        });

        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                // Cleaning all the markers.
            }
        });
        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition position) {

            }
        });

        autotxtOrigin.setAdapter(PlaceAutoCompleteAdapter);
        autotxtDestination.setAdapter(PlaceAutoCompleteAdapter);
        googleMap.setMyLocationEnabled(true);
        mMapView.getMapAsync(this);

    }


    public void setLocation(final double latitude, final double longitude) {

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


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY,
                mRequestingLocationUpdates);
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);

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
    public void onRoutingFailure(RouteException e) {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int i) {
        progressDialog.dismiss();
        //iMapFare = (IMapFare) getContext();
        serviceAvailable.setVisibility(View.VISIBLE);
        CameraUpdate center = CameraUpdateFactory.newLatLng(start);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);

        googleMap.moveCamera(center);


        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (i = 0; i < route.size(); i++) {

            //In case of more than 5 alternative routes
            try{
                int colorIndex = i % COLORS.length;

                PolylineOptions polyOptions = new PolylineOptions();
                polyOptions.color(getResources().getColor(COLORS[colorIndex]));
                polyOptions.width(10 + i * 3);
                polyOptions.addAll(route.get(i).getPoints());
                Polyline polyline = googleMap.addPolyline(polyOptions);
                polylines.add(i, polyline);
                placeCountry = route.get(i).getCountry();
                placeDistanceKm = route.get(i).getDistanceText();
                if (placeDistanceKm != null) {
                    System.out.println("placeDistanceKm: " + placeDistanceKm);
                    // placeDetailModel.setDistanceKm(placeDistanceKm);
                }

            }catch (Exception e){
                e.printStackTrace();
            }


            Toast.makeText(getContext(), "Route " + (i + 1) + ": distance - " + route.get(i).getDistanceValue() + ": duration - " + route.get(i).getDurationValue() + ": Km - " + route.get(i).getDistanceText(), Toast.LENGTH_SHORT).show();

        }
        //iMapFare.mapDetailBuff(placeNameStart,placeNameStop,placeDistanceKm,placeAddressStart,placeAddressStop);
        someEventListener.mapDetailBuff(placeNameStart, placeNameStop, placeDistanceKm, placeAddressStart, placeAddressStop);

        // Start marker
        MarkerOptions options = new MarkerOptions();
        options.position(start);
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue));
        googleMap.addMarker(options);

        // End marker
        options = new MarkerOptions();
        options.position(end);
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green));
        googleMap.addMarker(options);

    }

    @Override
    public void onRoutingCancelled() {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.v(TAG, connectionResult.toString());
        System.out.println("---------------connection Lost Error " + connectionResult.toString());

    }

    @Override
    public void onComplete(@NonNull Task<Void> task) {

    }
}