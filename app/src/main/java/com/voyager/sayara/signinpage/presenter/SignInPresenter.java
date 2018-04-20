package com.voyager.sayara.signinpage.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.gson.Gson;
import com.voyager.sayara.registerpage.model.IUserValidate;
import com.voyager.sayara.registerpage.model.UserDetails;
import com.voyager.sayara.signinpage.view.ISignInView;
import com.voyager.sayara.webservices.ApiClient;
import com.voyager.sayara.webservices.WebServices;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * Created by User on 8/29/2017.
 */

public class SignInPresenter implements ILoginPresenter {

    ISignInView iSignInView;
    IUserValidate user;
    Handler handler;

    String name;
    String passwd;

    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    String fireBaseToken;

    UserDetails userDetails;

    public SignInPresenter(ISignInView iSignInView,SharedPreferences sharedPrefs,SharedPreferences.Editor editor) {
        this.iSignInView = iSignInView;
        this.sharedPrefs = sharedPrefs;
        this.editor = editor;
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void clear() {
        iSignInView.onClearText();
    }

    @Override
    public void doLogin(String name, String passwd,String fireBaseToken) {
        this.name = name;
        this.passwd = passwd;
        this.fireBaseToken = fireBaseToken;
        System.out.println("-------doLogin  email : " + name +
                " Password : " + passwd);
        initUser();
        Boolean isLoginSuccess = true;
        final int code = user.checkUserValidity(name,passwd);
        if (code!=0) isLoginSuccess = false;
        final Boolean result = isLoginSuccess;
        iSignInView.onLoginResult(result, code);
        validateLoginDataBaseApi();
    }

    public void validateLoginDataBaseApi(){
        System.out.println("-------validateLoginDataBaseApi ");
        Retrofit retrofit = new ApiClient().getRetrofitClient();
        WebServices webServices = retrofit.create(WebServices.class);
        Call<UserDetails> call = webServices.loginUser(name,passwd,fireBaseToken);
        call.enqueue(new Callback<UserDetails>() {
            @Override
            public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
               userDetails  = (UserDetails) response.body();

                System.out.println("-------validateLoginDataBaseApi  email : " + name +
                        " Password : " + passwd +
                        " LName : " + userDetails.getFName()+
                        " phno : " + userDetails.getPhno() +
                        " city : " + userDetails.getCity() +
                        "userID: " + userDetails.getUserID()+
                        "imageUrl"+ userDetails.getImgPath());
                Gson gson = new Gson();
                String jsonString = gson.toJson(userDetails);

                System.out.println(" ----------- getFilters OfferListMap "+jsonString);
                if(jsonString!=null) {
                    System.out.println("-----------getFilters OfferList"+jsonString);
                }

                final int code =user.validateRegisterResponseError(userDetails.getError_msg());
                System.out.println("--------- validateLoginDataBaseApi code: "+code);
                Boolean isLoginSuccess =true;
                if (code != 0) {
                    isLoginSuccess = false;
                    System.out.println("--------- validateLoginDataBaseApi isError: "+userDetails.isError() +" Error message: "+userDetails.getError_msg());
                    Toast.makeText((Context) iSignInView, userDetails.getError_msg(), Toast.LENGTH_SHORT).show();
                    System.out.println("-----validateLoginDataBaseApi  data unSuccess ");
                } else {
                    userDetails.setPswd(passwd);
                    userDetails.setFcm(fireBaseToken);
                    System.out.println("----- validateLoginDataBaseApi isError: "+userDetails.isError() +" userID: "+userDetails.getUserID());
                    Toast.makeText((Context) iSignInView, "Login Successful", Toast.LENGTH_SHORT).show();
                    addUserGsonInSharedPrefrences(userDetails);
                    System.out.println("----- validateLoginDataBaseApi data Successful ");
                }
                Boolean result = isLoginSuccess;
                System.out.println("----- sendRegisteredDataAndValidateResponse second Data Please see, code = " + code + ", result: " + result);
                iSignInView.onLoginResponse(result, code);
            }

            @Override
            public void onFailure(Call<UserDetails> call, Throwable t) {
                Boolean isLoginSuccess = false;
                Boolean result = isLoginSuccess;
                int code = -77;
                iSignInView.onLoginResult(result, code);
                t.printStackTrace();
                //Toast.makeText((Context) iRegisterView, "ErrorMessage"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void addUserGsonInSharedPrefrences(UserDetails UserDetails ){
        Gson gson = new Gson();
        String jsonString = gson.toJson(UserDetails);
        //UserDetails user1 = gson.fromJson(jsonString,UserDetails.class);
        if(jsonString!=null) {
            editor.putString("UserDetails", jsonString);
            editor.commit();
            System.out.println("-----------validateLoginDataBaseApi UserDetails"+jsonString);
        }

    }


    @Override
    public void setProgressBarVisiblity(int visiblity) {
        iSignInView.onSetProgressBarVisibility(visiblity);
    }

    @Override
    public void onLoginSucuess() {
        iSignInView.sendPParcelableObj(userDetails);
    }

    private void initUser(){
        user = new UserDetails(name,passwd);
    }
}
