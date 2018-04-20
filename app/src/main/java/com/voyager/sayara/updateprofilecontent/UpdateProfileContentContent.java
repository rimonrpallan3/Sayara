package com.voyager.sayara.updateprofilecontent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.voyager.sayara.R;
import com.voyager.sayara.common.Helper;
import com.voyager.sayara.updateprofilecontent.presenter.UpdateProfileContentPresenter;
import com.voyager.sayara.updateprofilecontent.view.IUpdateProfileContentView;

/**
 * Created by User on 13-Dec-17.
 */

public class UpdateProfileContentContent extends AppCompatActivity implements View.OnClickListener,IUpdateProfileContentView {

    EditText edtUpdateContent;
    TextView tileName;
    TextView tileSubPass;

    public String storedName = "";
    public String currentContent = "";
    public String currentPswd = "";
    public String currentCity = "";
    public String contentType = "";
    public int userID;
    UpdateProfileContentPresenter updateProfilePresenter;

    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    public Toolbar toolbar;



    public UpdateProfileContentContent() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_name);
        System.out.println("UpdateProfileContentContent");
        edtUpdateContent = (EditText) findViewById(R.id.edtUpdateContent);
        tileName = (TextView) findViewById(R.id.tileName);
        tileSubPass = (TextView) findViewById(R.id.tileSubPass);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black));

        sharedPrefs = getSharedPreferences(Helper.UserDetails,
                Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();
        updateProfilePresenter = new UpdateProfileContentPresenter(this,this,sharedPrefs,editor);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (!bundle.isEmpty()) {
            storedName = bundle.getString("storedName");
            userID = bundle.getInt("userID");
            currentPswd = bundle.getString("pswd");
            currentCity = bundle.getString("city");
            contentType = bundle.getString("contentType");
            if(contentType.equals("name")){
                System.out.println("-----Oncreate name data storedName "+storedName+", userID : "+userID+", currentPswd: "+currentPswd+", currentCity: "+currentCity+", contentType: "+contentType);
                tileName.setText("Edit Name");
            }
            if(contentType.equals("city")){
                System.out.println("-----Oncreate city data storedName "+storedName+", userID : "+userID+", currentPswd: "+currentPswd+", currentCity: "+currentCity+", contentType: "+contentType);
                tileName.setText("Edit City");
            }
            if(contentType.equals("pass")){
                System.out.println("-----Oncreate pass data storedName "+storedName+", userID : "+userID+", currentPswd: "+currentPswd+", currentCity: "+currentCity+", contentType: "+contentType);
                tileName.setText("Edit Password");
                tileSubPass.setVisibility(View.VISIBLE);
            }
        }

        System.out.println("-----Oncreate UpdateProfileContentContent data storedName "+storedName+", userID : "+userID+", currentPswd: "+currentPswd+", currentCity: "+currentCity+", contentType: "+contentType);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

    }

    public void btnUpdate(View v){
        currentContent = edtUpdateContent.getText().toString();
        updateProfilePresenter.changeContent(currentContent,userID,contentType);
        System.out.println("----- btnUpdate currentContent: "+ currentContent +", userID: "+userID );
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnUpdateName:
                break;
        }
    }

    @Override
    public void setUserDetails(int userID, String fName, String phoneNo, String city, String email, String pswd, String Country) {

    }

    @Override
    public void checkName(Boolean result, int code) {
        if (result) {

        } else {
            switch (code) {
                case -1:
                    Toast.makeText(this, "Please fill the New Name, code = " + code, Toast.LENGTH_SHORT).show();
                    break;

                default:
                    Toast.makeText(this, "Please try Again Later, code = " + code, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void nameUploadOnSuccess(Boolean result, int code, String content, int userID, String contentType) {
        if (result) {
            Intent intent = new Intent(this, UpdateProfileContentContent.class);
            intent.putExtra("storedContent", content);
            intent.putExtra("userID", userID);
            intent.putExtra("contentType", contentType);
            setResult(Activity.RESULT_OK,intent);
            System.out.println("-----------nameUploadOnSuccess name: ---"+ content +", userID: ----"+userID);
           //updateProfile.changedName(currentName,userID);
            finish();
        } else {
            switch (code) {
                case -2:
                    Toast.makeText(this, "SomeThing Is Wrong With Upload, code = " + code, Toast.LENGTH_SHORT).show();
                    break;

                default:
                    Toast.makeText(this, "Please try Again Later, code = " + code, Toast.LENGTH_SHORT).show();
            }
        }

    }
}
