package com.voyager.sayara.MapPlaceSearch;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.voyager.sayara.MapPlaceSearch.Adapter.ListMapApiDirectionDestinationAdapter;
import com.voyager.sayara.MapPlaceSearch.Adapter.ListMapApiDirectionSourceAdapter;
import com.voyager.sayara.MapPlaceSearch.Adapter.SimpleDividerItemDecoration;
import com.voyager.sayara.MapPlaceSearch.model.CurrentPlaceDetails;
import com.voyager.sayara.MapPlaceSearch.model.placesearch.Predictions;
import com.voyager.sayara.MapPlaceSearch.presenter.IMPSPresenter;
import com.voyager.sayara.MapPlaceSearch.presenter.MapPlaceSearchPresenter;
import com.voyager.sayara.MapPlaceSearch.view.IMapPlaceSearchView;
import com.voyager.sayara.R;
import com.voyager.sayara.registerpage.model.UserDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 02-Feb-18.
 */

public class MapPlaceSearch extends AppCompatActivity implements IMapPlaceSearchView{

    Activity activity;
    public Toolbar toolbarMapPlaceSearch;
    EditText tvMapSource;
    EditText tvMapDestination;
    RecyclerView searchMapApiItemListRecycle;
    ListMapApiDirectionSourceAdapter sourceAdapter;
    ListMapApiDirectionDestinationAdapter destinationAdapter;
    private  List<Predictions> predictionses;
    private List<Predictions> filteredList;
    String apiKey;
    IMPSPresenter impsPresenter;
    final static String type ="geocode";
    String sourceLocationTxt ="";
    String sourcePlaceId ="";
    String destinationLocationTxt ="";
    String destinationPlaceId ="";
    RecyclerView.LayoutManager mLayoutManager;
    Boolean sourceSet =false;
    Boolean destinationSet =false;
    Bundle bundle;
    UserDetails userDetails;
    private static final String TAG = MapPlaceSearch.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;
    List<CurrentPlaceDetails> currentPlaceDetailsList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_place_search);
        toolbarMapPlaceSearch = (Toolbar) findViewById(R.id.toolbarMapPlaceSearch);
        setSupportActionBar(toolbarMapPlaceSearch);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        toolbarMapPlaceSearch.setTitleTextColor(ContextCompat.getColor(this, R.color.black));

        tvMapSource = (EditText) findViewById(R.id.tvMapSource);
        tvMapDestination = (EditText) findViewById(R.id.tvMapDestination);
        apiKey = getString(R.string.place_api_key);
        impsPresenter = new MapPlaceSearchPresenter(this);
        Intent intent = getIntent();
        bundle = new Bundle();
        userDetails = (UserDetails) intent.getParcelableExtra("UserDetails");
        if (userDetails != null) {
            System.out.println("LandingPage -- UserDetails- name : " + userDetails.getFName());
            System.out.println("LandingPage -- UserDetails- userId : " + userDetails.getUserID());
        }

        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        /*dictionaryWords = getListItemData();
        filteredList = new ArrayList<>();
        filteredList.addAll(dictionaryWords);
        System.out.println("MapPlaceSearch dictionaryWords places words: "+filteredList.get(0).getWord());*/

        searchMapApiItemListRecycle = (RecyclerView)findViewById(R.id.searchMapApiItemListRecycle);
        searchMapApiItemListRecycle.addItemDecoration(new SimpleDividerItemDecoration(this));
        assert searchMapApiItemListRecycle != null;

        tvMapSource.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0){
                    if(!destinationSet){
                        tvMapDestination.setText("");
                    }
                    sourceSet = false;
                    searchMapApiItemListRecycle.setVisibility(View.VISIBLE);
                    impsPresenter.loadData(s.toString(),type,apiKey,"Source");

                }else {
                    searchMapApiItemListRecycle.setVisibility(View.INVISIBLE);
                }
                System.out.println("MapPlaceSearch has ben onTextChanged ");
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        tvMapDestination.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0){
                    if(!sourceSet){
                        tvMapSource.setText("");
                    }
                    destinationSet = false;
                    searchMapApiItemListRecycle.setVisibility(View.VISIBLE);
                    impsPresenter.loadData(s.toString(),type,apiKey,"Destination");

                }else {
                    searchMapApiItemListRecycle.setVisibility(View.INVISIBLE);
                }
                System.out.println("MapPlaceSearch has ben onTextChanged ");
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });



        tvMapSource.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (tvMapSource.getRight() - tvMapSource.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (!checkPermissions()) {
                            requestPermissions();
                        } else {
                            int permissionState = ActivityCompat.checkSelfPermission(getApplicationContext(),
                                    android.Manifest.permission.ACCESS_FINE_LOCATION);
                            Task<PlaceLikelihoodBufferResponse> placeResult = mPlaceDetectionClient.getCurrentPlace(null);
                            placeResult.addOnCompleteListener(new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
                                @Override
                                public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                                    PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();
                                    for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                                        CurrentPlaceDetails currentPlaceDetails = new CurrentPlaceDetails();
                                        Log.i(TAG, String.format("Place '%s' has likelihood: %g",
                                                placeLikelihood.getPlace().getName(),
                                                placeLikelihood.getPlace().getLatLng(),
                                                placeLikelihood.getLikelihood()));
                                        currentPlaceDetails.setLikehood(placeLikelihood.getLikelihood());
                                        currentPlaceDetails.setLatLng(placeLikelihood.getPlace().getLatLng());
                                        currentPlaceDetails.setPlaceName(placeLikelihood.getPlace().getName());
                                        System.out.println("Name : "+placeLikelihood.getPlace().getName()+", Like hood : "+placeLikelihood.getLikelihood());
                                        currentPlaceDetailsList.add(currentPlaceDetails);
                                    }
                                    likelyPlaces.release();
                                }
                            });

                            return true;
                        }
                    }
                }
                return false;
            }
        });

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
        Snackbar.make(findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }


    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
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
                            ActivityCompat.requestPermissions(getParent(),
                                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });

        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!checkPermissions()) {
            requestPermissions();
        } else {
        }
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void loadData(List<Predictions> predictionses, String input, String valueType) {
        this.predictionses = predictionses;
        searchMapApiItemListRecycle.addItemDecoration(new SimpleDividerItemDecoration(this));
        assert searchMapApiItemListRecycle != null;
        filteredList = new ArrayList<>();
        filteredList.addAll(predictionses);
        if(valueType.equals("Source")) {
            sourceAdapter = new ListMapApiDirectionSourceAdapter(filteredList, this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            searchMapApiItemListRecycle.setLayoutManager(mLayoutManager);
            searchMapApiItemListRecycle.setItemAnimator(new DefaultItemAnimator());
            searchMapApiItemListRecycle.setAdapter(sourceAdapter);
            //sourceAdapter.getFilter().filter(input);
            sourceAdapter.notifyDataSetChanged();
        }else if(valueType.equals("Destination")){
            destinationAdapter = new ListMapApiDirectionDestinationAdapter(filteredList, this);
            mLayoutManager = new LinearLayoutManager(getApplicationContext());
            searchMapApiItemListRecycle.setLayoutManager(mLayoutManager);
            searchMapApiItemListRecycle.setItemAnimator(new DefaultItemAnimator());
            searchMapApiItemListRecycle.setAdapter(destinationAdapter);
            //sourceAdapter.getFilter().filter(input);
            destinationAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void sourceText(String sourceLocation,String sourcePlaceId) {
        this.sourceLocationTxt =sourceLocation;
        this.sourcePlaceId =sourcePlaceId;
        tvMapSource.setText(sourceLocation);
        sourceSet = true;
        tvMapSource.setBackgroundResource(R.drawable.edittext_default);
        tvMapDestination.requestFocus();
        searchMapApiItemListRecycle.setVisibility(View.INVISIBLE);
    }

    @Override
    public void destinationText(String destinationLocation, String destinationPlaceId) {
        this.destinationLocationTxt = destinationLocation;
        this.destinationPlaceId = destinationPlaceId;
        destinationSet = true;
        if(sourceSet&&destinationSet) {
            impsPresenter.getOriginLatLng(sourcePlaceId, destinationPlaceId, apiKey);
        }

    }

    @Override
    public void tripLatLng(String originLat, String originLng, String destinationLat, String destinationLng) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("sourceLocationTxt", sourceLocationTxt);
        resultIntent.putExtra("sourcePlaceId", sourcePlaceId);
        resultIntent.putExtra("destinationLocationTxt", destinationLocationTxt);
        resultIntent.putExtra("destinationPlaceId", destinationPlaceId);
        resultIntent.putExtra("originLat", originLat);
        resultIntent.putExtra("originLng", originLng);
        resultIntent.putExtra("destinationLat", destinationLat);
        resultIntent.putExtra("destinationLng", destinationLng);
        resultIntent.putExtra("userId", userDetails.getUserID());
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
