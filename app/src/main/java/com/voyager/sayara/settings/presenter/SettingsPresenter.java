package com.voyager.sayara.settings.presenter;

import com.voyager.sayara.landingpage.model.Cars;
import com.voyager.sayara.landingpage.model.TripCarDetails;
import com.voyager.sayara.registerpage.model.UserDetails;
import com.voyager.sayara.settings.view.ISettingsView;
import com.voyager.sayara.webservices.ApiClient;
import com.voyager.sayara.webservices.WebServices;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by User on 28-May-18.
 */

public class SettingsPresenter implements ISettingsPresenter{

    ISettingsView iSettingsView;

    public SettingsPresenter(ISettingsView iSettingsView) {
        this.iSettingsView = iSettingsView;
    }

    @Override
    public void logout(int userId,int logOut) {
        Retrofit retrofit = new ApiClient().getRetrofitClient();
        WebServices webServices = retrofit.create(WebServices.class);
        Call<UserDetails> call = webServices.logOut(userId, userId);
        call.enqueue(new Callback<UserDetails>() {
            @Override
            public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                UserDetails userDetails = response.body();
                System.out.println("-------SettingsPresenter -- logout: ");
                iSettingsView.logedOut();
            }

            @Override
            public void onFailure(Call<UserDetails> call, Throwable t) {

                t.printStackTrace();
                //Toast.makeText((Context) iRegisterView, "ErrorMessage"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
