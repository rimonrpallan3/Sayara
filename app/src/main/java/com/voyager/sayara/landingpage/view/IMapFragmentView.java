package com.voyager.sayara.landingpage.view;

import com.voyager.sayara.MapPlaceSearch.model.CurrentPlaceDetails;
import com.voyager.sayara.landingpage.model.Cars;
import com.voyager.sayara.landingpage.model.geogetpath.Route;

import java.util.HashMap;
import java.util.List;

/**
 * Created by User on 15-Feb-18.
 */

public interface IMapFragmentView {
    void setRoutesTrip(List<List<HashMap<String, String>>> route, List<Route> routes, String tripDist);
    void setRoutesDriverToPickUp(List<List<HashMap<String, String>>> route, List<Route> routes, String driverLat, String driverLng, String pickUpLat, String pickUpLng, String pickUpLocName);
    void doVisibilityLayoutItems(int visibility);
    void getDriverCarDetails(List<Cars> cares,Boolean state);
    void dialogueYesClickListener(String function);
    void tripCanceled();
    void tripOnGoingEnded();
    void highLikeHoodCurrentPlace(CurrentPlaceDetails maxCurrentPlaceDetails);
}
