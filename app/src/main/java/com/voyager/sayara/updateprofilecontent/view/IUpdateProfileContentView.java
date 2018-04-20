package com.voyager.sayara.updateprofilecontent.view;

/**
 * Created by User on 8/29/2017.
 */

public interface IUpdateProfileContentView {
    void setUserDetails(int userID,String fName,String phoneNo,String city, String email, String pswd, String Country);
    void checkName(Boolean result, int code);
    void nameUploadOnSuccess(Boolean result, int code, String content,int userID, String contentType);
}
