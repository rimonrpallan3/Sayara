package com.voyager.sayara.registerpage.presenter;

import com.voyager.sayara.registerpage.model.UserDetails;

/**
 * Created by User on 8/30/2017.
 */

public interface IRegisterFetcher {
    void doRegister(String FullName, String Password,String RetypePassword, String email,String phno, String city, String country);
    void onRegisteredSucuess();
}
