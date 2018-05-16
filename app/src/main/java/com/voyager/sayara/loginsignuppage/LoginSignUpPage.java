package com.voyager.sayara.loginsignuppage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.voyager.sayara.R;
import com.voyager.sayara.common.Helper;
import com.voyager.sayara.firstotppage.FirstOTPPage;

import com.voyager.sayara.landingpage.LandingPage;
import com.voyager.sayara.signinpage.SignInPage;

import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static com.voyager.sayara.common.Helper.RC_LOCATION_PERM_SIGIN;
import static com.voyager.sayara.common.Helper.RC_LOCATION_PERM_SIGUP;

public class LoginSignUpPage extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_sigup_page);

    }

    public void btnSignIn(View v){
        if (EasyPermissions.hasPermissions(this, Helper.PERMISSIONS_LOCATION_COARSE,Helper.PERMISSIONS_LOCATION_FINE)) {
            Intent intent = new Intent(this, SignInPage.class);
            startActivityForResult(intent, Helper.REQUEST_LOGEDIN);
            System.out.println("btnSignIn has ben called ");
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(this, getString(R.string.permission_location_check),
                    RC_LOCATION_PERM_SIGIN, Helper.PERMISSIONS_LOCATION_COARSE,Helper.PERMISSIONS_LOCATION_FINE);
        }
    }

    public  void btnSignUp(View v){
        if (EasyPermissions.hasPermissions(this, Helper.PERMISSIONS_LOCATION_COARSE,Helper.PERMISSIONS_LOCATION_FINE)) {
            Intent intent = new Intent(this, FirstOTPPage.class);
            startActivityForResult(intent,Helper.REQUEST_LOGEDIN);
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(this, getString(R.string.permission_location_check),
                    RC_LOCATION_PERM_SIGUP, Helper.PERMISSIONS_LOCATION_COARSE,Helper.PERMISSIONS_LOCATION_FINE);
        }

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
                if(data!=null) {
                    String LoginDone = (String) data.getExtras().getString("LoginDone");
                    if (LoginDone != null) {
                        System.out.println("Onacivity has ben called ");
                        finish();
                    }
                }else {
                    System.out.println("LoginSignUpPage  onActivityResult null ");
                }
            }catch (Exception e){
                e.printStackTrace();
            }


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        // EasyPermissions handles the request result.
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        switch (requestCode) {
            case Helper.RC_LOCATION_PERM_SIGIN:
                Intent intent = new Intent(this, SignInPage.class);
                startActivityForResult(intent, Helper.REQUEST_LOGEDIN);
                System.out.println("btnSignIn has ben called ");
                break;
            case Helper.RC_LOCATION_PERM_SIGUP:
                intent = new Intent(this, FirstOTPPage.class);
                startActivityForResult(intent,Helper.REQUEST_LOGEDIN);
                break;
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

}
