package com.voyager.sayara.updateprofile;

import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.voyager.sayara.R;
import com.voyager.sayara.common.Helper;
import com.voyager.sayara.costom.CircleImageView;
import com.voyager.sayara.registerpage.model.UserDetails;
import com.voyager.sayara.updateprofile.presenter.UpProfilePresenter;
import com.voyager.sayara.updateprofile.view.IUpProfileView;
import com.voyager.sayara.updateprofilecontent.UpdateProfileContentContent;

import java.io.File;


/**
 * Created by User on 28-Sep-17.
 */

 public class UpdateProfile extends AppCompatActivity implements View.OnClickListener,IUpProfileView {

    CircleImageView updateProfileImg;
    CircleImageView updateProfileImgBtn;
    TextView updateTName;
    TextView updateCName;
    TextView updateTPhoneNo;
    TextView updateCPhoneNo;
    TextView updateTPass;
    TextView updateCPass;
    TextView updateTCity;
    TextView updateCCity;
    TextView updateCEmail;
    public Toolbar updateToolbar;

    String fNmae ="";
    String pnoneNo ="";
    String pswd ="";
    String city ="";
    String country ="";
    String email ="";
    int userID;


    UpProfilePresenter upProfilePresenter;

    String contentType = "";

    String mCurrentPhotoPath = "";

    File photoFile = null;

    Uri photoURI;

    String TAG = "UpdateProfile";

    File imageFile = null;

    String imgPath;

    UserDetails userDetails;




    public UpdateProfile() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_profile);
        updateProfileImg = (CircleImageView)findViewById(R.id.updateProfileImg);
        updateProfileImgBtn = (CircleImageView)findViewById(R.id.updateProfileImgBtn);
        updateTName = (TextView) findViewById(R.id.updateTName);
        updateCName = (TextView)findViewById(R.id.updateCName);
        updateTPhoneNo = (TextView)findViewById(R.id.updateTPhoneNo);
        updateCPhoneNo = (TextView)findViewById(R.id.updateCPhoneNo);
        updateTPass = (TextView)findViewById(R.id.updateTPass);
        updateCPass = (TextView)findViewById(R.id.updateCPass);
        updateTCity = (TextView)findViewById(R.id.updateTCity);
        updateCCity = (TextView)findViewById(R.id.updateCCity);
        updateCEmail = (TextView)findViewById(R.id.updateCEmail);

        updateToolbar = (Toolbar) findViewById(R.id.updateToolbar);
        setSupportActionBar(updateToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        updateToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black));
        Intent intent = getIntent();
        userDetails = (UserDetails) intent.getParcelableExtra("UserDetails");
        if (userDetails != null) {
            Gson gson = new Gson();
            String json = gson.toJson(userDetails);
            System.out.println("UpdateProfile -- UserDetails- name : " + json);
        }

        updateCName.setOnClickListener(this);
        updateCPhoneNo.setOnClickListener(this);
        updateCPass.setOnClickListener(this);
        updateCCity.setOnClickListener(this);
        updateCEmail.setOnClickListener(this);
        updateProfileImg.setOnClickListener(this);
        updateProfileImgBtn.setOnClickListener(this);

        upProfilePresenter = new UpProfilePresenter(this,this,userDetails);
        //uploadProfileName();
        System.out.println("UpdateProfile");

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

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.updateProfileImgBtn:
                upProfilePresenter.getProfilePic();
                break;
            case R.id.toolbar:
                finish();
                break;
            case R.id.updateCPhoneNo:

                break;
            case R.id.updateCPass:
                contentType="pass";
                upProfilePresenter.changeContent(updateCPass.getText().toString(),userID,contentType);
                System.out.println("------updateCName name: "+updateCName.getText().toString()+", userID: "+userID);
                break;
            case R.id.updateCCity:
                contentType="city";
                upProfilePresenter.changeContent(updateCCity.getText().toString(),userID,contentType);
                System.out.println("------updateCName name: "+updateCName.getText().toString()+", userID: "+userID);
                break;

        }
    }


    @Override
    public void setUserDetails(int userID, String fName, String phoneNo, String city, String email, String pswd, String country,String imgPath) {
        this.fNmae = fName;
        this.pnoneNo =phoneNo;
        this.pswd = pswd;
        this.city = city;
        this.country = country;
        this.email = email;
        this.userID =userID;
        this.imgPath = imgPath;
        updateCName.setText(fName);
        updateCPhoneNo.setText(phoneNo);
        updateCPass.setText(pswd);
        updateCCity.setText(city);
        updateCEmail.setText(email);
        try{
            if(imgPath !=null){
                try{
                    Picasso.with(this)
                            .load(userDetails.getImgPath())
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .resize(0, 200)
                            .into(updateProfileImg, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {
                                    //Try again online if cache failed
                                    Picasso.with(getParent())
                                            .load(userDetails.getImgPath())
                                            .error(R.drawable.profile)
                                            .resize(0, 200)
                                            .into(updateProfileImg, new Callback() {
                                                @Override
                                                public void onSuccess() {

                                                }

                                                @Override
                                                public void onError() {
                                                    Log.v("Picasso","Could not fetch image");
                                                }
                                            });
                                }
                            });
                }catch(Exception e){
                    e.printStackTrace();
                }

                //updateProfileImg.setImageDrawable(convertFileToImg(imgPath));
            }else {
                System.out.println("ProfileImg IS null");
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public Drawable convertFileToImg(String mCurrentPhotoPath) {
        File file = new File(mCurrentPhotoPath);
        Drawable d = null;
        System.out.println("UpdateProfile onActivityResult_fileName : " + file.getName());
        for (int i = 0; i < 100; i++) {
            d = Drawable.createFromPath(mCurrentPhotoPath);
        }
       /* Bitmap b = BitmapFactory.decodeFile(mCurrentPhotoPath);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        b.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        // convert byte array to Bitmap

        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0,
                byteArray.length);*/
        return d;
    }


    @Override
    public void goToEditName(Boolean result, int code, String type) {
        System.out.println("goToEditName");
        if (result) {
            Intent intent = new Intent(this, UpdateProfileContentContent.class);
            intent.putExtra("userID", userID);
            intent.putExtra("pswd", pswd);
            intent.putExtra("city", city);
            intent.putExtra("contentType", type);
            startActivityForResult(intent,Helper.UpdateContent);
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


    private void dispatchTakePictureIntent(File image) {
        System.out.println("-----------dispatchTakePictureIntent image: ---"+image);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            photoFile = image;
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.voyager.sayara.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, Helper.REQUEST_TAKE_PHOTO);
                System.out.println("-----------dispatchTakePictureIntent startActivityForResult image: ---"+image);
            }
        }
    }


    @Override
    public void changedName(String name, int userID) {
        System.out.println("-----------changedName name: ---"+name+", userID: ----"+userID);
        updateCName.setText(name);
    }

    @Override
    public void profileImgBelowKitkat(Uri imageFileUri,String imagePath) {
        System.out.println("-----------profileImgBelowKitkat imageFileUri: ---"+imageFileUri+", imagePath: ----"+imagePath);
        mCurrentPhotoPath=imagePath;
        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                camera_intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri);
                startActivityForResult(camera_intent, Helper.REQUEST_TAKE_PHOTO);
    }

    @Override
    public void profileImgAboveKitkat(File imageFile,String imagePath) {
        System.out.println("-----------profileImgAboveKitkat imageFile: ---"+imageFile+", imagePath: ----"+imagePath);
        mCurrentPhotoPath = imagePath;
        dispatchTakePictureIntent(imageFile);
    }

    @Override
    public void profileImgMAndAbove(File imageFile,String imagePath) {
        System.out.println("-----------profileImgMAndAbove imageFile: ---"+imageFile+", imagePath: ----"+imagePath);
        this.imageFile = imageFile;
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG,"Permission is granted");
            //File write logic here
            mCurrentPhotoPath=imagePath;
            dispatchTakePictureIntent(imageFile);
        }else{
            System.out.println("-----------profileImgMAndAbove imageFile: ---"+"Permisiion not granted ");
            //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Helper.STORAGE_PERMISSION);
            requestPermissions(new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA},
                    Helper.CAMERA_STORAGE_PERMISSION);
        }
    }
    @Override
    public void setImgPath(String imgPath) {
        System.out.println("UpdateProfile onActivityResult_profileImg : " + imgPath);
        if (imgPath != null) {
            System.out.println("UpdateProfile onActivityResult_profileImg : ");
            updateProfileImg.setImageDrawable(convertFileToImg(imgPath));
        } else {
            System.out.println("ProfileImg IS null");
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == Helper.REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
                System.out.println("UpdateProfile onActivityResult_ imagePath : "+mCurrentPhotoPath);
                upProfilePresenter.uploadProfileImg(mCurrentPhotoPath);
            }else if(requestCode == Helper.UpdateContent && resultCode == RESULT_OK){
                String storedContent = (String) data.getExtras().getString("storedContent");
                String userID = (String) data.getExtras().getString("userID");
                String contentType = (String) data.getExtras().getString("contentType");
                if(contentType.equals("city")){
                    updateCCity.setText(storedContent);
                }
                if(contentType.equals("pass")){
                    updateCPass.setText(storedContent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "UpdateProfile Please try After SomeTimes", Toast.LENGTH_LONG)
                    .show();
        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case Helper.CAMERA_CAPTURE_PERMISSION:

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    dispatchTakePictureIntent(imageFile);
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "UpdateProfile Permission denied to Access your Camera", Toast.LENGTH_SHORT).show();
                }
                break;
            case Helper.UpdateContent:

                break;

        }
    }

}