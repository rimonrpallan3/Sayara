package com.voyager.sayara.firstotppage.presenter;

import android.content.Context;

import com.voyager.sayara.firstotppage.model.FirstOTPModel;
import com.voyager.sayara.firstotppage.view.IFirstOTPView;


/**
 * Created by User on 8/30/2017.
 */

public class FirstOTPPresenter implements IFirstOTPControler {

    IFirstOTPView iotpView;
    FirstOTPModel user;
    String contry;
    String zipCode;
    String phno;

    public FirstOTPPresenter(IFirstOTPView iotpView) {
        this.iotpView = iotpView;
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

    private void initUser(){
        user = new FirstOTPModel(contry,zipCode,phno);
    }

}
