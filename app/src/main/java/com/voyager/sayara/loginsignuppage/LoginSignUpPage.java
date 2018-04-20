package com.voyager.sayara.loginsignuppage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.voyager.sayara.R;
import com.voyager.sayara.common.Helper;
import com.voyager.sayara.firstotppage.FirstOTPPage;

import com.voyager.sayara.landingpage.LandingPage;
import com.voyager.sayara.signinpage.SignInPage;

public class LoginSignUpPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_sigup_page);

    }

    public void btnSignIn(View v){
        Intent intent = new Intent(this, SignInPage.class);
        startActivityForResult(intent, Helper.REQUEST_LOGEDIN);
        System.out.println("btnSignIn has ben called ");
    }

    public  void btnSignUp(View v){
        Intent intent = new Intent(this, FirstOTPPage.class);
        startActivity(intent);
    }

    public  void btnHiddenBtn(View v){
        Intent intent = new Intent(this, LandingPage.class);
        intent.putExtra("btnHiddenBtn", "Done");
        startActivity(intent);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Helper.REQUEST_LOGEDIN) {
            try{
                String LoginDone = (String) data.getExtras().getString("LoginDone");
                if(LoginDone!=null) {
                    System.out.println("Onacivity has ben called ");
                    finish();
                }
            }catch (Exception e){
                e.printStackTrace();
            }


        }
    }

}
