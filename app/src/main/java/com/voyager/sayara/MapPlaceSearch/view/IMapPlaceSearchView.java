package com.voyager.sayara.MapPlaceSearch.view;

import com.voyager.sayara.MapPlaceSearch.model.placesearch.Predictions;
import com.voyager.sayara.landingpage.model.OnTripStartUp;

import java.util.List;

/**
 * Created by User on 8/29/2017.
 */

public interface IMapPlaceSearchView {
    void loadData(List<Predictions> predictionses, String input,String valueType);
    void sourceText(String sourceLocation,String placeId);
    void destinationText(String destinationLocation,String placeId);
    void tripLatLng(String originLat,String originLng,String destinationLat,String destinationLng);
}
