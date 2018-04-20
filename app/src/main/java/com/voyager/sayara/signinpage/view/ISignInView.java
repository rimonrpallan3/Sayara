package com.voyager.sayara.signinpage.view;

import com.voyager.sayara.registerpage.model.UserDetails;

/**
 * Created by User on 8/29/2017.
 */

public interface ISignInView {
    void onClearText();
    void onLoginResult(Boolean result, int code);
    void onSetProgressBarVisibility(int visibility);
    void onLoginResponse(Boolean result, int code);
    void sendPParcelableObj(UserDetails userDetails);
}
