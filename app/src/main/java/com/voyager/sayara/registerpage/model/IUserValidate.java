package com.voyager.sayara.registerpage.model;

/**
 * Created by User on 8/30/2017.
 */

public interface IUserValidate {
    int validateUserDetails(String FName, String password,String RetypePassword,String email, String phno, String city, String country);
    int validateRegisterResponseError(String errorMsg);

    public String getEmail();

    public String getPasswd();

    int checkUserValidity(String name, String passwd);
    int checkNameID(int userID, String FName);
    int checkUpdateNameApi(Boolean isError);
    int validateLoginResponseError(String errorMsg);
}
