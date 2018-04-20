package com.voyager.sayara.fare.presenter;

import com.voyager.sayara.fare.model.TripResponse;
import com.voyager.sayara.fare.view.IFareView;
import com.voyager.sayara.landingpage.model.geogetpath.GetPaths;
import com.voyager.sayara.webservices.ApiClient;
import com.voyager.sayara.webservices.WebServices;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by User on 13-Nov-17.
 */

public class FarePresenter implements IFarePresenter{

    IFareView iFareView;
    TripResponse tripResponse;

    public FarePresenter(IFareView iFareView) {
        this.iFareView = iFareView;
    }

    @Override
    public void startTrip(int userId,String userName,String nameStartLoc,String nameStart,String nameEndLoc,String nameEnd,String distanceKm,String costFairSet,String driveCarId,String paymentType) {
        System.out.println("-------FarePresenter -- getTripDirection");
        Retrofit retrofit = new ApiClient().getRetrofitClient();
        WebServices webServices = retrofit.create(WebServices.class);
        Call<TripResponse> call = webServices.startTrip(userId ,userName,nameStartLoc, nameStart,nameEndLoc, nameEnd,distanceKm,costFairSet,driveCarId,paymentType);
        call.enqueue(new Callback<TripResponse>() {
            @Override
            public void onResponse(Call<TripResponse> call, Response<TripResponse> response) {
                System.out.println("-------FarePresenter -- startTrip---------------");
                tripResponse  = (TripResponse) response.body();
                System.out.println("-------startTrip  error : " + tripResponse.getError() +
                        " tripId : " + tripResponse.getTripId() +
                        " userId : " + tripResponse.getUserId() +
                        " createdAt : " + tripResponse.getCreatedAt() );

                iFareView.waitToGetDriver();
            }

            @Override
            public void onFailure(Call<TripResponse> call, Throwable t) {
                System.out.println("---FarePresenter ----startTrip  Error Report : " + t.getMessage() );

                t.printStackTrace();
                //Toast.makeText((Context) iRegisterView, "ErrorMessage"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
