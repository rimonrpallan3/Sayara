package com.voyager.sayara.MapPlaceSearch;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.gson.Gson;
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
    float maxLikeHood = 0;
    CurrentPlaceDetails maxCurrentPlaceDetails;



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
        apiKey = getString(R.string.map_api_key);
        impsPresenter = new MapPlaceSearchPresenter(this);
        Intent intent = getIntent();
        bundle = new Bundle();
        userDetails = (UserDetails) intent.getParcelableExtra("UserDetails");
        maxCurrentPlaceDetails = (CurrentPlaceDetails) intent.getParcelableExtra("CurrentPlaceDetails");
        if (userDetails != null) {
            System.out.println("MapPlaceSearch -- UserDetails- name : " + userDetails.getFName());
            System.out.println("MapPlaceSearch -- UserDetails- userId : " + userDetails.getUserID());
        }
        if(maxCurrentPlaceDetails.getPlaceName()!=null){
            tvMapSource.setText(maxCurrentPlaceDetails.getPlaceName());
            impsPresenter.setCurrentLoc(maxCurrentPlaceDetails.getLat(),maxCurrentPlaceDetails.getLng());
            sourcePlaceId = maxCurrentPlaceDetails.getPlaceid();
            sourceLocationTxt = maxCurrentPlaceDetails.getPlaceName();
            sourceSet = true;
            tvMapDestination.requestFocus();
            Gson gson = new Gson();
            String json2 = gson.toJson(maxCurrentPlaceDetails);
            System.out.println("-----------MapPlaceSearch onCreate CurrentPlaceDetails : " + json2);
            System.out.println("MapPlaceSearch onCreate -- maxCurrentPlaceDetails - not Null: ");
        }else{
            tvMapSource.requestFocus();
            System.out.println("MapPlaceSearch onCreate -- maxCurrentPlaceDetails - Null: ");
        }
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


    }


    @Override
    public void onStart() {
        super.onStart();
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
