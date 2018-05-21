package com.voyager.sayara.registerpage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.voyager.sayara.R;
import com.voyager.sayara.common.Helper;
import com.voyager.sayara.common.NetworkDetector;
import com.voyager.sayara.landingpage.LandingPage;
import com.voyager.sayara.loginsignuppage.LoginSignUpPage;
import com.voyager.sayara.registerpage.model.UserDetails;
import com.voyager.sayara.registerpage.presenter.IRegisterFetcher;
import com.voyager.sayara.registerpage.presenter.RegisterPresenter;
import com.voyager.sayara.registerpage.view.IRegisterView;

import static com.voyager.sayara.common.Helper.REQUEST_REGISTERED;

/**
 * Created by User on 8/23/2017.
 */

public class RegisterPage extends AppCompatActivity implements IRegisterView{

    private EditText edtFullName;
    private EditText edtPassword;
    private EditText edtEmailAddress;
    private EditText edtRetypePassword;
    private TextView txtViewPhoneNo;
    private EditText edtCity;
    private Button btnRegister;
    IRegisterFetcher iRegisterFetcher;
    LoginSignUpPage loginSignUpPage;

    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    String country ="";
    String zipCode ="";
    String PhoneNo ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);

        //find view
        edtFullName = (EditText) this.findViewById(R.id.edtFullName);
        edtPassword = (EditText) this.findViewById(R.id.edtPassword);
        edtRetypePassword = (EditText) this.findViewById(R.id.edtRetypePassword);
        edtEmailAddress = (EditText) this.findViewById(R.id.edtEmailAddress);
        txtViewPhoneNo = (TextView) this.findViewById(R.id.txtViewPhoneNo);
        edtCity = (EditText) this.findViewById(R.id.edtCity);
        btnRegister = (Button) this.findViewById(R.id.btnRegister);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            country = bundle.getString("Country");
            zipCode = bundle.getString("ZipCode");
            PhoneNo = bundle.getString("PhoneNo");
            txtViewPhoneNo.setText(zipCode + PhoneNo);
        }

        //init
        sharedPrefs = getSharedPreferences(Helper.UserDetails,
                Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();
        iRegisterFetcher = new RegisterPresenter(this,sharedPrefs,editor);




        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

    }

    public void btnRegister(View v){
        if(NetworkDetector.haveNetworkConnection(this)){
            //Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.snack_error_network_available), Snackbar.LENGTH_SHORT).show();
            btnRegister.setEnabled(false);
            iRegisterFetcher.doRegister(edtFullName.getText().toString(),
                    edtPassword.getText().toString(),
                    edtRetypePassword.getText().toString(),
                    edtEmailAddress.getText().toString(),
                    txtViewPhoneNo.getText().toString(),
                    edtCity.getText().toString(),
                    country.toString());
        }else {
            Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.snack_error_network), Snackbar.LENGTH_LONG).show();

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
       // outState.putParcelable(Helper.UserDetails,);
    }

    @Override
    public void onRegister(Boolean result, int code) {
        edtFullName.setEnabled(true);
        edtPassword.setEnabled(true);
        edtRetypePassword.setEnabled(true);
        txtViewPhoneNo.setEnabled(true);
        edtCity.setEnabled(true);
        //edtCPR.setEnabled(true);
        if (result) {
        } else {
            btnRegister.setEnabled(true);
            switch (code) {
                case -1:
                    Toast.makeText(this, "Please fill all the fields, code = " + code, Toast.LENGTH_SHORT).show();
                    break;
                case -2:
                    Toast.makeText(this, "Please fill a valid First Name, code = " + code, Toast.LENGTH_SHORT).show();
                    break;
                case -3:
                    Toast.makeText(this, "Please fill a valid Password, code = " + code, Toast.LENGTH_SHORT).show();
                    break;
                case -4:
                    Toast.makeText(this, "Please type the Same Password, code = " + code, Toast.LENGTH_SHORT).show();
                    break;
                case -5:
                    Toast.makeText(this, "Please fill a valid email Address, code = " + code, Toast.LENGTH_SHORT).show();
                    break;
                case -6:
                    Toast.makeText(this, "Please fill a valid Phone No, code = " + code, Toast.LENGTH_SHORT).show();
                    break;
                case -7:
                    Toast.makeText(this, "Please fill a valid City Name, code = " + code, Toast.LENGTH_SHORT).show();
                    break;
                case -8:
                    Toast.makeText(this, "Please fill a valid Country, code = " + code, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(this, "Please try Again Later, code = " + code, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRegistered(Boolean result, int code) {
        System.out.println("-----onRegistered second Please see, code = " + code + ", result: " + result);
        if (result) {
            System.out.println("------- inside onRegistered first Please see, code = " + code + ", result: " + result);
            //Toast.makeText(this, "-----onRegistered second Please see, code = " + code + ", result: " + result, Toast.LENGTH_SHORT).show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //re-enable the button
                    btnRegister.setEnabled(true);
                }
            }, 4000);
            iRegisterFetcher.onRegisteredSucuess();
        } else {
            edtFullName.setEnabled(true);
            edtPassword.setEnabled(true);
            edtEmailAddress.setEnabled(true);
            txtViewPhoneNo.setEnabled(true);
            edtCity.setEnabled(true);
            btnRegister.setEnabled(true);
            switch (code) {
                case -9:
                    Toast.makeText(this, "Please Correct the Required fields, code = " + code, Toast.LENGTH_SHORT).show();
                    break;
                case -77:
                    Toast.makeText(this, "SomeThing went Wrong on our end Please try after some time , code = " + code, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(this, "Please try Again Later, code = " + code, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void sendPParcelableObj(UserDetails userDetails) {
        System.out.println("Name : "+userDetails.getFName());
        Intent intent = new Intent(this, LandingPage.class);
        intent.putExtra("UserDetails", userDetails);
        intent.putExtra("LoginDone", "done");
        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        startActivity(intent);
        setResult(REQUEST_REGISTERED);
        finish();
    }

}
