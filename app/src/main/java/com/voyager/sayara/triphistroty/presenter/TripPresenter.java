package com.voyager.sayara.triphistroty.presenter;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.voyager.sayara.triphistroty.model.TripDetails;
import com.voyager.sayara.triphistroty.view.ITripView;
import com.voyager.sayara.webservices.ApiClient;
import com.voyager.sayara.webservices.WebServices;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by User on 26-Dec-17.
 */

public class TripPresenter implements ITripPresenter {

    ITripView iTripView;
    List<TripDetails> tripDetails;
    Handler handler;

    public TripPresenter(ITripView iTripView){
        this.iTripView = iTripView;
        handler = new Handler(Looper.getMainLooper());
    }


    @Override
    public void loadData(final int userId) {
        System.out.println("TripPresenter -- getTripDirection- userId : " + userId);
        Retrofit retrofit = new ApiClient().getRetrofitClient();
        WebServices webServices = retrofit.create(WebServices.class);

        Call<List<TripDetails>> call = webServices.getTripHistory(21,1);
        call.enqueue(new Callback<List<TripDetails>>() {
            @Override
            public void onResponse(Call<List<TripDetails>> call, Response<List<TripDetails>> response) {
                tripDetails = response.body();
                for(int i=0;i<tripDetails.size();i++){
                    System.out.println(tripDetails.get(i));
                    System.out.println("getTripDirection -- getTripDirection- TripId : " + tripDetails.get(i).getTripId()+
                            " driverCarName : " + tripDetails.get(i).getDriverCarName() +
                                    " dateTime : " + tripDetails.get(i).getDateTime() +
                                    " cashMode : " + tripDetails.get(i).getCashMode() +
                                    " cash : " + tripDetails.get(i).getCash() +
                                    " tripImgUrl : " + tripDetails.get(i).getTripImgUrl());
                }
                iTripView.onDataSet(tripDetails,userId);
            }

            @Override
            public void onFailure(Call<List<TripDetails>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText((Context) iTripView, "ErrorMessage"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void getUserDetailParcelable() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                iTripView.getParcelable();
            }
        }, 2000);
    }
}
