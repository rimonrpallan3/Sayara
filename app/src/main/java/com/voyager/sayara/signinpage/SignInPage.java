package com.voyager.sayara.signinpage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.voyager.sayara.R;
import com.voyager.sayara.common.Helper;
import com.voyager.sayara.firstotppage.FirstOTPPage;
import com.voyager.sayara.landingpage.LandingPage;
import com.voyager.sayara.registerpage.model.UserDetails;
import com.voyager.sayara.signinpage.presenter.ILoginPresenter;
import com.voyager.sayara.signinpage.presenter.SignInPresenter;
import com.voyager.sayara.signinpage.view.ISignInView;


/**
 * Created by User on 8/23/2017.
 */

public class SignInPage extends AppCompatActivity implements ISignInView {

    private EditText edtEmailPhno;
    private EditText edtPswd;
    private Button btnSubmit;

    private ProgressBar progressBar;
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;

    ILoginPresenter iLoginPresenter;
    String fireBaseToken="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_page);

        fireBaseToken = FirebaseInstanceId.getInstance().getToken();
        sharedPrefs = getSharedPreferences(Helper.UserDetails,
                Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();


        //find view
        edtEmailPhno = (EditText) this.findViewById(R.id.edtEmailPhno);
        edtPswd = (EditText) this.findViewById(R.id.edtPswd);
        btnSubmit = (Button) this.findViewById(R.id.btnSubmit);
        progressBar = (ProgressBar) this.findViewById(R.id.progressBar);

        //init
        iLoginPresenter = new SignInPresenter(this,sharedPrefs,editor);
        iLoginPresenter.setProgressBarVisiblity(View.INVISIBLE);
   }

   public void btnSubmit(View v){
       Helper.hideKeyboard(this);
       iLoginPresenter.setProgressBarVisiblity(View.VISIBLE);
       btnSubmit.setEnabled(false);
       iLoginPresenter.doLogin(edtEmailPhno.getText().toString(), edtPswd.getText().toString(),fireBaseToken);
   }

    public void SignUp(View v){
        Intent intent = new Intent(this, FirstOTPPage.class);
        startActivity(intent);
        finish();
    }
    
    @Override
    public void onClearText() {
        edtEmailPhno.setText("");
        edtPswd.setText("");
    }

    @Override
    public void onLoginResult(Boolean result, int code) {
        iLoginPresenter.setProgressBarVisiblity(View.INVISIBLE);
        edtEmailPhno.setEnabled(true);
        edtPswd.setEnabled(true);
        if (result){
        }
        else {
            Toast.makeText(this, "Please input Values, code = " + code, Toast.LENGTH_SHORT).show();
            btnSubmit.setEnabled(true);
            iLoginPresenter.clear();
        }
    }

    @Override
    public void onSetProgressBarVisibility(int visibility) {
        progressBar.setVisibility(visibility);
    }

    @Override
    public void onLoginResponse(Boolean result, int code) {
        iLoginPresenter.setProgressBarVisiblity(View.INVISIBLE);
        edtEmailPhno.setEnabled(true);
        edtPswd.setEnabled(true);
        if (result){
            iLoginPresenter.onLoginSucuess();
        }
        else {
            Toast.makeText(this, "Please input correct UserName and Password, code = " + code, Toast.LENGTH_SHORT).show();
            btnSubmit.setEnabled(true);
            iLoginPresenter.clear();
        }
    }

    @Override
    public void sendPParcelableObj(UserDetails userDetails) {
        System.out.println("UserDetails ----- FName: "+userDetails.getFName());
        Intent intent = new Intent(this, LandingPage.class);
        intent.putExtra("LoginDone", "Done");
        setResult(Helper.REQUEST_LOGEDIN,intent);
        intent.putExtra("UserDetails", userDetails);
        startActivity(intent);
        finish();
    }
}
