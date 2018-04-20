package com.voyager.sayara.registerpage.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.gson.Gson;
import com.voyager.sayara.loginsignuppage.view.ILoginSignupView;
import com.voyager.sayara.registerpage.model.IUserValidate;
import com.voyager.sayara.registerpage.model.UserDetails;
import com.voyager.sayara.registerpage.view.IRegisterView;
import com.voyager.sayara.webservices.ApiClient;
import com.voyager.sayara.webservices.WebServices;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by User on 8/29/2017.
 */

public class RegisterPresenter implements IRegisterFetcher{

    IRegisterView iRegisterView;
    ILoginSignupView iLoginSignupView;
    IUserValidate user;
    UserDetails userDetails;
    String FullName;
    String Password;
    String RetypePassword;
    String email;
    String phno;
    String city;
    String country;
    String Imei;


    int num =1;
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    Context context;

    public RegisterPresenter(ILoginSignupView iLoginSignupView){
        this.iLoginSignupView = iLoginSignupView;
    }

    public RegisterPresenter(IRegisterView iRegisterView,SharedPreferences sharedPrefs,SharedPreferences.Editor editor) {
        this.iRegisterView = iRegisterView;
        this.sharedPrefs = sharedPrefs;
        this.editor = editor;

    }

    @Override
    public void doRegister(String FullName, String Password,String RetypePassword,String email, String phno, String city, String country) {
        System.out.println("FullName : "+FullName+" Password : "+Password+" RetypePassword : "+RetypePassword+" email : "+email+" phno : "+phno+" city : "+city+" country : "+country);
        this.FullName = FullName;
        this.Password = Password;
        this.RetypePassword = RetypePassword;
        this.email = email;
        this.phno = phno;
        this.country = country;
        this.city = city;
        initUser();
        Boolean isLoginSuccess = true;
        final int code = user.validateUserDetails(FullName,Password,RetypePassword,email,phno,city,country);
        if (code != 0) {
            isLoginSuccess = false;
        } else {
            sendRegisteredDataAndValidateResponse();
        }
        Boolean result = isLoginSuccess;
        iRegisterView.onRegister(result, code);
    }

    @Override
    public void onRegisteredSucuess() {
        iRegisterView.sendPParcelableObj(userDetails);
    }

    public void sendRegisteredDataAndValidateResponse(){
        Retrofit retrofit = new ApiClient().getRetrofitClient();
        WebServices webServices = retrofit.create(WebServices.class);
        Call<UserDetails> call = webServices.registerUser(FullName,Password,email,phno,country,city);
        call.enqueue(new Callback<UserDetails>() {
            @Override
            public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                userDetails  = (UserDetails) response.body();
                userDetails.setFName(FullName);
                userDetails.setPswd(Password);
                userDetails.setEmail(email);
                userDetails.setPhno(phno);
                userDetails.setCountry(country);
                userDetails.setCity(city);
                System.out.println("-------sendRegisteredDataAndValidateResponse  FullName : " + FullName +
                        " Password : " + Password +
                        " email Address : " + email +
                        " phno : " + phno +
                        " city : " + city +
                        " country : " + country);
                final int code =user.validateRegisterResponseError(userDetails.error_msg);
                System.out.println("-----sendRegisteredDataAndValidateResponse  data code :"+code);
                Boolean isLoginSuccess =true;
                if (code != 0) {
                    isLoginSuccess = false;
                    Toast.makeText((Context) iRegisterView, userDetails.getError_msg(), Toast.LENGTH_SHORT).show();
                    System.out.println("----- sendRegisteredDataAndValidateResponse isError: "+userDetails.isError +" userID: "+userDetails.userID +" created_at: "+userDetails.created_at);
                    System.out.println("-----sendRegisteredDataAndValidateResponse  data unSuccess ");
                } else {
                    Toast.makeText((Context) iRegisterView, "Register Successful", Toast.LENGTH_SHORT).show();
                    addUserGsonInSharedPrefrences(userDetails);
                    System.out.println("--------- sendRegisteredDataAndValidateResponse isError: "+userDetails.isError +" Error message: "+userDetails.error_msg);
                    System.out.println("----- sendRegisteredDataAndValidateResponse data Successful ");
                }
                Boolean result = isLoginSuccess;
                System.out.println("----- sendRegisteredDataAndValidateResponse second Data Please see, code = " + code + ", result: " + result);
                iRegisterView.onRegistered(result, code);
            }

            @Override
            public void onFailure(Call<UserDetails> call, Throwable t) {
                Boolean isLoginSuccess =false;
                Boolean result = isLoginSuccess;
                int code = -77;
                iRegisterView.onRegistered(result, code);
                t.printStackTrace();
                //Toast.makeText((Context) iRegisterView, "ErrorMessage"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void initUser(){
        user = new UserDetails(FullName,Password,RetypePassword,email,phno,city,country);
    }


    private void addUserGsonInSharedPrefrences(UserDetails userDetails){
        Gson gson = new Gson();
        String jsonString = gson.toJson(userDetails);
        //UserModel user1 = gson.fromJson(jsonString,UserModel.class);
        if(jsonString!=null) {
            editor.putString("UserDetails", jsonString);
            editor.commit();
            System.out.println("-----------sendRegisteredDataAndValidateResponse  UserDetails"+jsonString);

        }

    }


    private void addUserListInSharedPrefrences(){
        sharedPrefs.getString("list",null);
        //create json object
        JSONObject jsonObject = new JSONObject();
        try {
            // add the data into jsonObject
            jsonObject.put("id", num);
            jsonObject.put("FullName", FullName);
            jsonObject.put("Password", Password);
            jsonObject.put("phno",phno);
            jsonObject.put("city",city);
            jsonObject.put("Imei",Imei);
        } catch (Exception e) {

            e.printStackTrace();
        }
        // check key(list) exist into sharedPreferences
        if (sharedPrefs.contains("list")) {
            /**
             * key exist in the sharedPreferences.
             * get the data from the key (list) and new data into list .
             */
            String storedCarList = sharedPrefs.getString("list", null);
            try {

                JSONArray jsonArray = new JSONArray(storedCarList);
                jsonArray.put(jsonObject);
                editor.putString("list", jsonArray.toString());
                editor.commit();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            // key is not exist in the sharedPreferences.
            JSONArray jsonArrays = new JSONArray();
            jsonArrays.put(jsonObject);
            editor.putString("list", jsonArrays.toString());
            editor.commit();
        }

        Toast.makeText(context.getApplicationContext(), "Saved: " +
                        sharedPrefs.getString("list",null),
                Toast.LENGTH_SHORT).show();

        num = num +1;
    }


}
