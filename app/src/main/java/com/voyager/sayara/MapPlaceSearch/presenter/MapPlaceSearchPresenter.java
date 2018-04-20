package com.voyager.sayara.MapPlaceSearch.presenter;

import com.voyager.sayara.MapPlaceSearch.model.placedetail.Geometry;
import com.voyager.sayara.MapPlaceSearch.model.placedetail.Location;
import com.voyager.sayara.MapPlaceSearch.model.placedetail.PlaceDetail;
import com.voyager.sayara.MapPlaceSearch.model.placedetail.Result;
import com.voyager.sayara.MapPlaceSearch.model.placesearch.PlacesResults;
import com.voyager.sayara.MapPlaceSearch.model.placesearch.Predictions;
import com.voyager.sayara.MapPlaceSearch.model.placesearch.StructuredFormatting;
import com.voyager.sayara.MapPlaceSearch.view.IMapPlaceSearchView;
import com.voyager.sayara.webservices.ApiClient;
import com.voyager.sayara.webservices.WebServices;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by User on 8/29/2017.
 */

public class MapPlaceSearchPresenter implements IMPSPresenter{

    IMapPlaceSearchView iMapPlaceSearchView;
    String originLat;
    String originLng;
    String destinationLat;
    String destinationLng;


    public MapPlaceSearchPresenter(IMapPlaceSearchView iMapPlaceSearchView) {
        this.iMapPlaceSearchView =iMapPlaceSearchView;
    }

    @Override
    public void loadData(final String input, String type, String key, final String valueType) {
        System.out.println("-------EarningPresenter");
        Retrofit retrofit = new ApiClient().getRetrofitClientPath();
        WebServices webServices = retrofit.create(WebServices.class);
        Call<PlacesResults> call = webServices.getPlaceSearch(input,type,key);
        call.enqueue(new Callback<PlacesResults>() {
            @Override
            public void onResponse(Call<PlacesResults> call, Response<PlacesResults> response) {
                PlacesResults placesResults  = response.body();
                List<Predictions> predictionses = placesResults.getPredictions();
                for(int i = 0; i < predictionses.size(); i++){
                    Predictions predictions1 = predictionses.get(i);
                    StructuredFormatting structuredFormatting = (StructuredFormatting) predictions1.structuredFormatting;
                    System.out.println("-------MapPlaceSearchPresenter getTripDirection MainText : " + structuredFormatting.getMainText() +
                            " SecondaryText : " + structuredFormatting.getSecondaryText());
                }

                iMapPlaceSearchView.loadData(predictionses,input,valueType);

            }

            @Override
            public void onFailure(Call<PlacesResults> call, Throwable t) {

                t.printStackTrace();
                //Toast.makeText((Context) iRegisterView, "ErrorMessage"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void getOriginLatLng(String sourcePlaceId, final String destinationPlaceId,final String key) {
        Retrofit retrofit = new ApiClient().getRetrofitClientPath();
        WebServices webServices = retrofit.create(WebServices.class);
        Call<PlaceDetail> call = webServices.getPlaceDetail(sourcePlaceId,key);
        call.enqueue(new Callback<PlaceDetail>() {
            @Override
            public void onResponse(Call<PlaceDetail> call, Response<PlaceDetail> response) {
                PlaceDetail placeDetail  = response.body();
                Result result = placeDetail.getResult();
                Geometry geometry =result.getGeometry();
                Location location = geometry.getLocation();
                originLat = location.getLat();
                originLng = location.getLng();
                System.out.println("-------MapPlaceSearchPresenter getOriginLatLng originLat : " + originLat +
                        " originLng : " + originLng);
                getDestinationLatLng(destinationPlaceId,key);

            }

            @Override
            public void onFailure(Call<PlaceDetail> call, Throwable t) {

                t.printStackTrace();
                //Toast.makeText((Context) iRegisterView, "ErrorMessage"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getDestinationLatLng(String destinationPlaceId,String key){
        Retrofit retrofit = new ApiClient().getRetrofitClientPath();
        WebServices webServices = retrofit.create(WebServices.class);
        Call<PlaceDetail> call = webServices.getPlaceDetail(destinationPlaceId,key);
        call.enqueue(new Callback<PlaceDetail>() {
            @Override
            public void onResponse(Call<PlaceDetail> call, Response<PlaceDetail> response) {
                PlaceDetail placeDetail  = response.body();
                Result result = placeDetail.getResult();
                Geometry geometry =result.getGeometry();
                Location location = geometry.getLocation();
                destinationLat = location.getLat();
                destinationLng = location.getLng();
                System.out.println("-------MapPlaceSearchPresenter getDestinationLatLng destinationLat : " + destinationLat +
                        " destinationLng : " + destinationLng);
                iMapPlaceSearchView.tripLatLng(originLat,originLng,destinationLat,destinationLng);
            }

            @Override
            public void onFailure(Call<PlaceDetail> call, Throwable t) {

                t.printStackTrace();
                //Toast.makeText((Context) iRegisterView, "ErrorMessage"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
