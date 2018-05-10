package com.voyager.sayara.MapPlaceSearch.presenter;

/**
 * Created by User on 12-Feb-18.
 */

public interface IMPSPresenter {
    void loadData(String input,String type, String key,String valueType);
    void getOriginLatLng(String sourcePlaceId, String destinationPlaceId,String Key);
    void setCurrentLoc(String pickUpLat, String pickUpLng);
}
