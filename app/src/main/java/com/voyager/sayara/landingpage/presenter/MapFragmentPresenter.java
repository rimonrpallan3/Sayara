package com.voyager.sayara.landingpage.presenter;

import com.google.android.gms.maps.model.LatLng;
import com.voyager.sayara.landingpage.model.TripCarDetails;
import com.voyager.sayara.landingpage.model.geogetpath.Distance;
import com.voyager.sayara.landingpage.model.geogetpath.GetPaths;
import com.voyager.sayara.landingpage.model.geogetpath.Leg;
import com.voyager.sayara.landingpage.model.geogetpath.Route;
import com.voyager.sayara.landingpage.model.geogetpath.Step;
import com.voyager.sayara.landingpage.model.landingModel.EndTrip;
import com.voyager.sayara.landingpage.view.IMapFragmentView;
import com.voyager.sayara.webservices.ApiClient;
import com.voyager.sayara.webservices.WebServices;
import com.voyager.sayara.landingpage.model.Cars;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by User on 15-Feb-18.
 */

public class MapFragmentPresenter implements IMapFragmentPresenter{

    IMapFragmentView iMapFragmentView;
    LatLng orginLatLng;
    double originLatD;
    double originLngD;
    String tripDist ="";
    String driverLat = "";
    String driverlng = "";
    String pickUpLat = "";
    String pickUplng = "";



    public MapFragmentPresenter(IMapFragmentView iMapFragmentView) {
        this.iMapFragmentView = iMapFragmentView;
    }


    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }


    @Override
    public void getTripDirection(final String originLat,final String originLng,  String destinationLat,String destinationLng, Boolean sensor, String ApiKey,final Integer userId) {
        System.out.println("-------MapFragmentPresenter -- getTripDirection");
        originLatD = Double.parseDouble(originLat);
        originLngD = Double.parseDouble(originLng);
        orginLatLng = new LatLng(originLatD,originLngD);
        Retrofit retrofit = new ApiClient().getRetrofitClientPath();
        WebServices webServices = retrofit.create(WebServices.class);
        Call<GetPaths> call = webServices.getPaths(originLat + "," + originLng, destinationLat + "," + destinationLng, sensor,ApiKey);
        call.enqueue(new Callback<GetPaths>() {
            @Override
            public void onResponse(Call<GetPaths> call, Response<GetPaths> response) {
                getBokingDetails(userId);
                GetPaths getPaths = response.body();
                List<List<HashMap<String, String>>> route = new ArrayList<List<HashMap<String, String>>>();
                List<Route> routes = getPaths.getRoutes();
                for (int i = 0; i < routes.size(); i++) {
                    List<HashMap<String, String>> path = new ArrayList<HashMap<String, String>>();
                    List<Leg> legs = routes.get(i).getLegs();
                    Distance distance = legs.get(0).getDistance();
                    tripDist = distance.getText();
                    for (int j = 0; j < legs.size(); j++) {
                        List<Step> steps = legs.get(j).getSteps();
                        for (int k = 0; k < steps.size(); k++) {
                            String polyline = steps.get(k).getPolyline().getPoints();
                            List<LatLng> latLngs = decodePoly(polyline);
                            for (int l = 0; l < latLngs.size(); l++) {
                                HashMap<String, String> hm = new HashMap<String, String>();
                                hm.put("lat",
                                        Double.toString(((LatLng) latLngs.get(l)).latitude));
                                hm.put("lng",
                                        Double.toString(((LatLng) latLngs.get(l)).longitude));
                                path.add(hm);
                            }
                        }
                    }
                    route.add(path);
                }
                iMapFragmentView.setRoutes(route,routes,tripDist);
            }

            @Override
            public void onFailure(Call<GetPaths> call, Throwable t) {

                t.printStackTrace();
                //Toast.makeText((Context) iRegisterView, "ErrorMessage"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getBokingDetails(int userId){
        Retrofit retrofit = new ApiClient().getRetrofitClient();
        WebServices webServices = retrofit.create(WebServices.class);
        String latlong = originLatD+","+originLngD;
        System.out.println("-------MapFragmentPresenter -- getBokingDetails: "+latlong);
        Call<TripCarDetails> call = webServices.getTripDetails(latlong,userId);
        call.enqueue(new Callback<TripCarDetails>() {
            @Override
            public void onResponse(Call<TripCarDetails> call, Response<TripCarDetails> response) {
                TripCarDetails tripCarDetails = response.body();
                Boolean state = tripCarDetails.getError();
                System.out.println("-------MapFragmentPresenter -- onResponse: "+state);
                List<Cars> cars = tripCarDetails.getCars();
                iMapFragmentView.getDriverCarDetails(cars,state);
            }

            @Override
            public void onFailure(Call<TripCarDetails> call, Throwable t) {

                t.printStackTrace();
                //Toast.makeText((Context) iRegisterView, "ErrorMessage"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void hideVisibilityLayoutItems(int visibility) {
        iMapFragmentView.doVisibilityLayoutItems(visibility);
    }

    @Override
    public void setOnTripStartUp(String driverLocation,String pickUpLocation,Boolean sensor,String ApiKey,final String pickUpLocName) {
        String[] driveLoc = driverLocation.split(",");
        String[] pickUpLoc = pickUpLocation.split(",");
        for (int x=0; x<driveLoc.length; x++) {
            System.out.println("x : "+x+" = "+driveLoc[x]);
            driverLat = driveLoc[0];
            driverlng = driveLoc[1];
        }
        for (int x=0; x<pickUpLoc.length; x++) {
            System.out.println("x : "+x+" = "+pickUpLoc[x]);
            pickUpLat = pickUpLoc[0];
            pickUplng = pickUpLoc[1];
        }

        Retrofit retrofit = new ApiClient().getRetrofitClientPath();
        WebServices webServices = retrofit.create(WebServices.class);
        Call<GetPaths> call = webServices.getPaths(driverLocation, pickUpLocation, sensor,ApiKey);
        call.enqueue(new Callback<GetPaths>() {
            @Override
            public void onResponse(Call<GetPaths> call, Response<GetPaths> response) {
                GetPaths getPaths = response.body();
                List<List<HashMap<String, String>>> route = new ArrayList<List<HashMap<String, String>>>();
                List<Route> routes = getPaths.getRoutes();
                for (int i = 0; i < routes.size(); i++) {
                    List<HashMap<String, String>> path = new ArrayList<HashMap<String, String>>();
                    List<Leg> legs = routes.get(i).getLegs();
                    Distance distance = legs.get(0).getDistance();
                    tripDist = distance.getText();
                    for (int j = 0; j < legs.size(); j++) {
                        List<Step> steps = legs.get(j).getSteps();
                        for (int k = 0; k < steps.size(); k++) {
                            String polyline = steps.get(k).getPolyline().getPoints();
                            List<LatLng> latLngs = decodePoly(polyline);
                            for (int l = 0; l < latLngs.size(); l++) {
                                HashMap<String, String> hm = new HashMap<String, String>();
                                hm.put("lat",
                                        Double.toString(((LatLng) latLngs.get(l)).latitude));
                                hm.put("lng",
                                        Double.toString(((LatLng) latLngs.get(l)).longitude));
                                path.add(hm);
                            }
                        }
                    }
                    route.add(path);
                }
                iMapFragmentView.setDriverRoutes(route,routes,driverLat,driverlng,pickUpLat,pickUplng,pickUpLocName);
            }

            @Override
            public void onFailure(Call<GetPaths> call, Throwable t) {

                t.printStackTrace();
                //Toast.makeText((Context) iRegisterView, "ErrorMessage"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void tripCancelOnStart(Integer userId, Integer tripid) {
        Retrofit retrofit = new ApiClient().getRetrofitClient();
        WebServices webServices = retrofit.create(WebServices.class);
        Call<EndTrip> call = webServices.stopStartUpTrip(userId,tripid);
        call.enqueue(new Callback<EndTrip>() {
            @Override
            public void onResponse(Call<EndTrip> call, Response<EndTrip> response) {
                EndTrip endTrip = response.body();
                System.out.println("-------MapFragmentPresenter -- onResponse: ");
                iMapFragmentView.tripCanceled();
            }

            @Override
            public void onFailure(Call<EndTrip> call, Throwable t) {

                t.printStackTrace();
                //Toast.makeText((Context) iRegisterView, "ErrorMessage"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void tripOngoingEnded(Integer userId, Integer tripid) {
        Retrofit retrofit = new ApiClient().getRetrofitClient();
        WebServices webServices = retrofit.create(WebServices.class);
        Call<EndTrip> call = webServices.endOnGoingTrip(userId,tripid);
        call.enqueue(new Callback<EndTrip>() {
            @Override
            public void onResponse(Call<EndTrip> call, Response<EndTrip> response) {
                EndTrip endTrip = response.body();
                System.out.println("-------MapFragmentPresenter -- onResponse: ");
                iMapFragmentView.tripOnGoingEnded();
            }

            @Override
            public void onFailure(Call<EndTrip> call, Throwable t) {

                t.printStackTrace();
                //Toast.makeText((Context) iRegisterView, "ErrorMessage"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
