package com.voyager.sayara.updateprofile.presenter;

import android.graphics.drawable.Drawable;

import java.io.File;

/**
 * Created by User on 11-Dec-17.
 */

public interface IUpProfilePresenter {
    void changeContent(String name, int userID ,String Type);
    void newName(String name, int userID );
    void getProfilePic();
    public void uploadProfileImg(String imagePath);

}
