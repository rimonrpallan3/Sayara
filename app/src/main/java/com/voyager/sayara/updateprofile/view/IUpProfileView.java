package com.voyager.sayara.updateprofile.view;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import java.io.File;

/**
 * Created by User on 11-Dec-17.
 */

public interface IUpProfileView {

    void setUserDetails(int userID,String fName,String phoneNo,String city, String email, String pswd, String Country,String imgPath);
    void goToEditName(Boolean result, int code, String type);
    void changedName(String name, int userID);
    void profileImgBelowKitkat(Uri imageFileUri,String imagePath);
    void profileImgAboveKitkat(File imageFile,String imagePath);
    void profileImgMAndAbove(File imageFile,String imagePath);
    void setImgPath(String imgPath);

}
