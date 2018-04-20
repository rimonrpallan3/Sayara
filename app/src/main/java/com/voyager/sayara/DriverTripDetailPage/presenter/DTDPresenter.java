package com.voyager.sayara.DriverTripDetailPage.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.voyager.sayara.DriverTripDetailPage.model.DTDModel;
import com.voyager.sayara.DriverTripDetailPage.model.MainTripDetailClass;
import com.voyager.sayara.DriverTripDetailPage.model.Comments;
import com.voyager.sayara.DriverTripDetailPage.view.IDTDView;
import com.voyager.sayara.webservices.ApiClient;
import com.voyager.sayara.webservices.WebServices;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * Created by User on 8/30/2017.
 */

public class DTDPresenter implements IDTDControler {
    IDTDView idtdView;
    DTDModel dtdModel;
    List<Comments> commentsArrayList = new ArrayList<>();
    Handler handler;
    MainTripDetailClass mainTripDetailClass;

    public DTDPresenter(IDTDView idtdView) {
        this.idtdView =idtdView;
        handler = new Handler(Looper.getMainLooper());
    }


    @Override
    public void getParcelable() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                idtdView.getParcelable();
            }
        }, 2000);
    }

    @Override
    public void loadData(int userId, int driverId) {
        Retrofit retrofit = new ApiClient().getRetrofitClient();
        WebServices webServices = retrofit.create(WebServices.class);
        Call<DTDModel> call = webServices.getDriverProfileDetail(89,52);
        call.enqueue(new Callback<DTDModel>() {
            @Override
            public void onResponse(Call<DTDModel> call, Response<DTDModel> response) {
                dtdModel = response.body();
                System.out.println("-------DTDPresenter  driverName : " + dtdModel.getDriverName() +
                        " driverRating : " + dtdModel.getDriverRating() +
                        " languagesKnown : " + dtdModel.getLanguagesKnown() +
                        " driverID : " + dtdModel.getDriverID() +
                        " imgPath : " + dtdModel.getImgPath() +
                        " workDays : " + dtdModel.getWorkDays());
                commentsArrayList = dtdModel.getComments();
                idtdView.getCommentListAndDetails(commentsArrayList,dtdModel);
            }

            @Override
            public void onFailure(Call<DTDModel> call, Throwable t) {
                System.out.println("-------DTDPresenter getTripDirection onFailure ErrorMessage : " +t.getMessage());
                Toast.makeText((Context) idtdView, "ErrorMessage"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
