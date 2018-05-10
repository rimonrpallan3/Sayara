package com.voyager.sayara.landingpage.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.voyager.sayara.MapPlaceSearch.model.CurrentPlaceDetails;
import com.voyager.sayara.R;
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

public class MapFragmentPresenter implements IMapFragmentPresenter {

    IMapFragmentView iMapFragmentView;
    LatLng orginLatLng;
    double originLatD;
    double originLngD;
    String tripDist = "";
    String driverLat = "";
    String driverlng = "";
    String pickUpLat = "";
    String pickUplng = "";

    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;
    List<CurrentPlaceDetails> currentPlaceDetailsList = new ArrayList<>();
    float maxLikeHood = 0;
    CurrentPlaceDetails maxCurrentPlaceDetails;
    Activity activity;
    String TAG;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;


    public MapFragmentPresenter(IMapFragmentView iMapFragmentView, Activity activity, String TAG) {
        this.iMapFragmentView = iMapFragmentView;
        this.activity = activity;
        this.TAG = TAG;
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
    public void getTripDirection(final String originLat, final String originLng, String destinationLat, String destinationLng, Boolean sensor, String ApiKey, final Integer userId) {
        System.out.println("-------MapFragmentPresenter -- getTripDirection");
        originLatD = Double.parseDouble(originLat);
        originLngD = Double.parseDouble(originLng);
        orginLatLng = new LatLng(originLatD, originLngD);
        Retrofit retrofit = new ApiClient().getRetrofitClientPath();
        WebServices webServices = retrofit.create(WebServices.class);
        Call<GetPaths> call = webServices.getPaths(originLat + "," + originLng, destinationLat + "," + destinationLng, sensor, ApiKey);
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
                iMapFragmentView.setRoutesTrip(route, routes, tripDist);
            }

            @Override
            public void onFailure(Call<GetPaths> call, Throwable t) {

                t.printStackTrace();
                //Toast.makeText((Context) iRegisterView, "ErrorMessage"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getBokingDetails(int userId) {
        Retrofit retrofit = new ApiClient().getRetrofitClient();
        WebServices webServices = retrofit.create(WebServices.class);
        String latlong = originLatD + "," + originLngD;
        System.out.println("-------MapFragmentPresenter -- getBokingDetails: " + latlong);
        Call<TripCarDetails> call = webServices.getTripDetails(latlong, userId);
        call.enqueue(new Callback<TripCarDetails>() {
            @Override
            public void onResponse(Call<TripCarDetails> call, Response<TripCarDetails> response) {
                TripCarDetails tripCarDetails = response.body();
                Boolean state = tripCarDetails.getError();
                System.out.println("-------MapFragmentPresenter -- onResponse: " + state);
                List<Cars> cars = tripCarDetails.getCars();
                iMapFragmentView.getDriverCarDetails(cars, state);
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
    public void getTripDriverToPickUp(String driverLocation, String pickUpLocation, Boolean sensor, String ApiKey, final String pickUpLocName) {
        System.out.println("-------MapFragmentPresenter -- getTripDriverToPickUp: ");
        String[] driveLoc = driverLocation.split(",");
        String[] pickUpLoc = pickUpLocation.split(",");
        for (int x = 0; x < driveLoc.length; x++) {
            System.out.println("x : " + x + " = " + driveLoc[x]);
            driverLat = driveLoc[0];
            driverlng = driveLoc[1];
        }
        for (int x = 0; x < pickUpLoc.length; x++) {
            System.out.println("x : " + x + " = " + pickUpLoc[x]);
            pickUpLat = pickUpLoc[0];
            pickUplng = pickUpLoc[1];
        }

        Retrofit retrofit = new ApiClient().getRetrofitClientPath();
        WebServices webServices = retrofit.create(WebServices.class);
        Call<GetPaths> call = webServices.getPaths(driverLocation, pickUpLocation, sensor, ApiKey);
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
                iMapFragmentView.setRoutesDriverToPickUp(route, routes, driverLat, driverlng, pickUpLat, pickUplng, pickUpLocName);
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
        Call<EndTrip> call = webServices.stopStartUpTrip(userId, tripid);
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
        Call<EndTrip> call = webServices.endOnGoingTrip(userId, tripid);
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

    @Override
    public void getCurrentLocDetails() {
        if (!checkPermissions()) {
            requestPermissions();
        } else {
            maxCurrentPlaceDetails = new CurrentPlaceDetails();

            // Construct a GeoDataClient.
            mGeoDataClient = Places.getGeoDataClient(activity, null);

            // Construct a PlaceDetectionClient.
            mPlaceDetectionClient = Places.getPlaceDetectionClient(activity, null);
            try {
                int permissionState = ActivityCompat.checkSelfPermission(activity.getApplicationContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION);
                LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
                // get the last know location from your location manager.
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                // now get the lat/lon from the location and do something with it.
                if (location == null) {
                    iMapFragmentView.highLikeHoodCurrentPlace(maxCurrentPlaceDetails);
                } else {

                    try {
                        int permissionState2 = ActivityCompat.checkSelfPermission(activity.getApplicationContext(),
                                android.Manifest.permission.ACCESS_FINE_LOCATION);
                        Task<PlaceLikelihoodBufferResponse> placeResult = mPlaceDetectionClient.getCurrentPlace(null);
                        placeResult.addOnCompleteListener(new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
                            @Override
                            public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                                PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();
                                currentPlaceDetailsList.clear();
                                maxLikeHood = 0;
                                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                                    CurrentPlaceDetails currentPlaceDetails = new CurrentPlaceDetails();
                                    Log.i(TAG, String.format("Place '%s' has likelihood: %g",
                                            placeLikelihood.getPlace().getName(),
                                            placeLikelihood.getLikelihood()));
                                    currentPlaceDetails.setLikehood(placeLikelihood.getLikelihood());
                                    currentPlaceDetails.setLat(String.valueOf(placeLikelihood.getPlace().getLatLng().latitude));
                                    currentPlaceDetails.setLng(String.valueOf(placeLikelihood.getPlace().getLatLng().longitude));
                                    currentPlaceDetails.setPlaceid(placeLikelihood.getPlace().getId());
                                    currentPlaceDetails.setPlaceName(placeLikelihood.getPlace().getName().toString());
                                    System.out.println("Name : " + placeLikelihood.getPlace().getName()
                                            + ", Like hood : " + placeLikelihood.getLikelihood()
                                            + ", LatLog : " + placeLikelihood.getPlace().getLatLng());
                                    currentPlaceDetailsList.add(currentPlaceDetails);
                                }
                                likelyPlaces.release();
                                Gson gson = new Gson();
                                String json = gson.toJson(currentPlaceDetailsList);
                                System.out.println("-----------MapFragmentPresenter PlaceLikelihoodBufferResponse currentPlaceDetailsList : " + json);
                                for (int i = 0; i < currentPlaceDetailsList.size(); i++) {
                                    CurrentPlaceDetails currentPlaceDetails = currentPlaceDetailsList.get(i);
                                    float mostLikeHood = currentPlaceDetails.getLikehood();
                                    System.out.println("-----------MapFragmentPresenter mostLikeHood : " + mostLikeHood);
                                    if (maxLikeHood < mostLikeHood) {
                                        maxLikeHood = mostLikeHood;
                                        maxCurrentPlaceDetails = currentPlaceDetails;
                                    }

                                }
                                iMapFragmentView.highLikeHoodCurrentPlace(maxCurrentPlaceDetails);
                                String json2 = gson.toJson(maxCurrentPlaceDetails);
                                System.out.println("----------- MapFragmentPresenter PlaceLikelihoodBufferResponse CurrentPlaceDetails : " + json2);
                            }
                        });
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                        System.out.println("----------- MapFragmentPresenter PlaceLikelihoodBufferResponse Error  : " + e.getMessage().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("----------- MapFragmentPresenter PlaceLikelihoodBufferResponse Error  : " + e.getMessage().toString());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("----------- MapFragmentPresenter PlaceLikelihoodBufferResponse Error  : " + e.getMessage().toString());
            }
        }
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
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(activity.findViewById(android.R.id.content),
                activity.getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(activity.getString(actionStringId), listener).show();
    }


    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(activity,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

}
