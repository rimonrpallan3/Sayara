package com.voyager.sayara.firstotppage.presenter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.voyager.sayara.firstotppage.model.CountryDetails;
import com.voyager.sayara.firstotppage.model.FirstOTPModel;
import com.voyager.sayara.firstotppage.view.IFirstOTPView;
import com.voyager.sayara.landingpage.model.OnTripStartUp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by User on 8/30/2017.
 */

public class FirstOTPPresenter implements IFirstOTPControler {

    IFirstOTPView iotpView;
    FirstOTPModel user;
    String contry;
    String zipCode;
    String phno;
    Activity activity;
    String countryJson = "";
    List<CountryDetails> countryDetailsList;
    Gson gson;

    public FirstOTPPresenter(IFirstOTPView iotpView, Activity activity) {
        this.iotpView = iotpView;
        this.activity = activity;
        countryJson = loadJSONFromAsset();
        countryDetailsList = getCountryDetailsList();
        Gson gson = new Gson();
        String jsonString = gson.toJson(countryDetailsList);
        System.out.println("FirstOTPPresenter const countryDetailsList json : " + jsonString);
        iotpView.getCountryDetailList(countryDetailsList);
    }




    @Override
    public void doGetData(String contry, String zipCode, String phno) {
        System.out.println("contry : "+contry+" zipCode : "+zipCode+" phno : "+phno);
        this.contry = contry;
        this.zipCode = zipCode;
        this.phno = phno;
        initUser();
        Boolean isLoginSuccess = true;
        final int code = user.validateFirstOTPpage(contry,zipCode,phno);
        if (code!=0) isLoginSuccess = false;
        final Boolean result = isLoginSuccess;
        iotpView.validatedSendData(result, code);

    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = activity.getAssets().open("data/country_phones.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            System.out.println("-------------loadJSONFromAsset "+json);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public List<CountryDetails> getCountryDetailsList(){
        List<CountryDetails> countryDetailsList = new ArrayList<>();
        try {
            JSONArray m_jArry = new JSONArray(loadJSONFromAsset());
            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                CountryDetails countryDetails = new CountryDetails();
                countryDetails.setName(jo_inside.getString("name"));
                countryDetails.setDial_code(jo_inside.getString("dial_code"));
                countryDetails.setCode(jo_inside.getString("code"));
                countryDetailsList.add(countryDetails);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return countryDetailsList;
    }


    private void initUser(){
        user = new FirstOTPModel(contry,zipCode,phno);
    }

}
