package com.voyager.sayara.landingpage.model.landingModel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 26-Apr-18.
 */

public class EndTrip {
    @SerializedName("error")
    public boolean isError;

    @SerializedName("error_msg")
    public String error_msg="";

    public EndTrip() {
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }
}

