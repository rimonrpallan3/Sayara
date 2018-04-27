package com.voyager.sayara.landingpage.presenter;

import java.util.List;

/**
 * Created by User on 15-Feb-18.
 */

public interface IMapFragmentPresenter {
    void getTripDirection(final String originLat,final String originLng,  String destinationLat,String destinationLng, Boolean sensor, String ApiKey,Integer userId);
    void hideVisibilityLayoutItems(int visibility);
    void setOnTripStartUp(String driverLocation,String pickUpLoc,Boolean sensor,String ApiKey, String pickUpLocName);
    void tripCancelOnStart(Integer userId, Integer tripid);
    void tripOngoingEnded(Integer userId, Integer tripid);

}
