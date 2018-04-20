package com.voyager.sayara.updateprofilecontent.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.voyager.sayara.registerpage.model.UserDetails;
import com.voyager.sayara.updateprofilecontent.view.IUpdateProfileContentView;
import com.voyager.sayara.webservices.ApiClient;
import com.voyager.sayara.webservices.WebServices;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by User on 8/29/2017.
 */

public class UpdateProfileContentPresenter implements IUpdateProfileContentFetcher {

    IUpdateProfileContentView iUpdateProfile;
    Context context;
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    UserDetails user;

    String fNmae ="";
    String pnoneNo ="";
    String pswd ="";
    String city ="";
    String country ="";
    String email ="";
    String imgPath ="";
    int userID;


    public UpdateProfileContentPresenter(){
    }

    public UpdateProfileContentPresenter(IUpdateProfileContentView iUpdateProfile, Context context, SharedPreferences sharedPrefs, SharedPreferences.Editor editor) {
        this.iUpdateProfile = iUpdateProfile;
        this.sharedPrefs = sharedPrefs;
        this.context =context;
        this.editor = editor;
        getUserSDetails();
    }



    private void getUserSDetails(){
        Gson gson = new Gson();
        String json = sharedPrefs.getString("UserDetails", "");
        System.out.println("-----------uploadProfileName UserDetails"+json);
        user = gson.fromJson(json,UserDetails.class);
        fNmae = user.getFName().toString();
        pnoneNo =user.getPhno().toString();
        pswd = user.getPswd().toString();
        city = user.getCity().toString();
        country =  user.getCountry().toString();
        email = user.getEmail().toString();
        userID =user.getUserID();

        iUpdateProfile.setUserDetails(userID,fNmae,pnoneNo,city,email,pswd,country);

    }

    private void addUserGsonInSharedPrefrences(){
        Gson gson = new Gson();
        String jsonString = gson.toJson(user);
        //UserModel user1 = gson.fromJson(jsonString,UserModel.class);
        if(jsonString!=null) {
            editor.putString("UserDetails", jsonString);
            editor.commit();
            System.out.println(" -----------uploadProfileName UserDetails"+jsonString);

        }

    }

    public void uploadProfileName(final String content, final int userID, final String contentType) {
        //System.out.println("------- onActivityResult : mCurrentPhotoPath - " + mCurrentPhotoPath);

        if(contentType.equals("name")){
            fNmae =content;
            user.setFName(fNmae);
        }
        if(contentType.equals("city")){
            city =content;
            user.setCity(city);
        }
        if(contentType.equals("pass")){
            pswd =content;
            user.setPswd(pswd);
        }
        System.out.println("------- IUpProfileView : " +
                "fNmae:  " +fNmae+
                ", pnoneNo: "+pnoneNo+
                ", userID: "+ userID +
                ", pswd: "+ pswd +
                ", city: "+ city +
                ", country: " + country);

        Retrofit retrofit = new ApiClient().getRetrofitClient();
        WebServices webServices = retrofit.create(WebServices.class);

        Call<UserDetails> call = webServices.updateProfileName(fNmae,userID,pswd,city);
        call.enqueue(new Callback<UserDetails>() {
            @Override
            public void onResponse(Call<UserDetails> call,
                                   Response<UserDetails> response) {

                UserDetails user = response.body();
                System.out.println("----- uploadProfileName isError: "+user.isError +" user_error: "+user.error_msg );

                final int code =user.checkUpdateNameApi(user.isError);
                Boolean isLoginSuccess =true;
                if (code != 0) {
                    isLoginSuccess = false;
                    Toast.makeText(context, user.getError_msg(), Toast.LENGTH_SHORT).show();
                    System.out.println("-----uploadProfileName  data unSuccess ");
                } else {
                    user.setFName(content);
                    Toast.makeText(context, "Register Successful", Toast.LENGTH_SHORT).show();
                    addUserGsonInSharedPrefrences();
                    System.out.println("----- uploadProfileName data Successful ");
                }
                Boolean result = isLoginSuccess;
                System.out.println("----- uploadProfileName second Data Please see, code = " + code + ", result: " + result);
                iUpdateProfile.nameUploadOnSuccess(result, code, content, userID, contentType);
            }

            @Override
            public void onFailure(Call<UserDetails> call, Throwable t) {
                Toast.makeText(context.getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("uploadProfileName error:", t.getMessage());
            }
        });


    }

    @Override
    public void changeContent(String content, int userID, String contentType) {
        System.out.println("----- changeContent data name "+content+", userID : "+userID);
        user.checkNameID(userID,content);

        Boolean isLoginSuccess = true;
        final int code =  user.checkNameID(userID,content);
        if (code != 0) {
            isLoginSuccess = false;
        } else {
            uploadProfileName(content,userID,contentType);
        }
        Boolean result = isLoginSuccess;
        iUpdateProfile.checkName(result, code);
    }


}
