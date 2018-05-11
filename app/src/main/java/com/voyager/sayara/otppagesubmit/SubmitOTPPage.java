package com.voyager.sayara.otppagesubmit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.voyager.sayara.R;
import com.voyager.sayara.TermsAndConduction.TermsAndConduction;
import com.voyager.sayara.otppagesubmit.presenter.IOTPControler;
import com.voyager.sayara.otppagesubmit.presenter.OTPPresenter;
import com.voyager.sayara.otppagesubmit.view.IOTPView;
import com.voyager.sayara.registerpage.RegisterPage;



/**
 * Created by User on 8/30/2017.
 */

 public class SubmitOTPPage extends AppCompatActivity implements IOTPView {

    IOTPControler iotpControler;
    TextView optSecondMsg;
    EditText edtOPTNo;
    CheckBox checkTermsAndConductionBox;
    Button btnSubmit;
    String country ="";
    String zipCode ="";
    String PhoneNo ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_page);
        optSecondMsg = (TextView) findViewById(R.id.optSecondMsg);
        edtOPTNo = (EditText) findViewById(R.id.edtOPTNo);
        checkTermsAndConductionBox = (CheckBox) findViewById(R.id.checkTermsAndConductionBox);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        iotpControler = new OTPPresenter(this,this);
        iotpControler.setOPTSecondMsg(optSecondMsg);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            Intent i = getIntent();
            country = bundle.getString("Country");
            zipCode = bundle.getString("ZipCode");
            PhoneNo = bundle.getString("PhoneNo");
           // loginSignUpPage = (LoginSignUpPage)i.getSerializableExtra("LoginSignUpPage");
        }

    }

    @Override
    public void onSubmit(Boolean result, int code) {
        edtOPTNo.setEnabled(true);
        btnSubmit.setEnabled(true);
        if (result) {
            Intent oldIntent = getIntent();
            Intent intent = new Intent(this, RegisterPage.class);
            intent.putExtra("Country",country);
            intent.putExtra("ZipCode",zipCode);
            intent.putExtra("PhoneNo",PhoneNo);
           // intent.putExtra("LoginSignUpPage", (Serializable) loginSignUpPage);
            intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            setResult(Activity.RESULT_OK, oldIntent);
            startActivity(intent);
            finish();
        } else {
            btnSubmit.setEnabled(true);
            switch (code) {
                case -1:
                    Toast.makeText(this, "Please type in OTP, code = " + code, Toast.LENGTH_SHORT).show();
                    break;
                case -2:
                    Toast.makeText(this, "Please Agree the Terms and Conduction, code = " + code, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(this, "Please try Again Later, code = " + code, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void moveToTermsAndConductionPage() {
        Intent intent = new Intent(SubmitOTPPage.this, TermsAndConduction.class);
        startActivity(intent);
    }

    public void btnSubmit(View v){
        edtOPTNo.setEnabled(false);
        btnSubmit.setEnabled(false);
        iotpControler.doOTPValidationAndCheck(edtOPTNo.getText().toString(),checkTermsAndConductionBox.isChecked());
    }
 }
