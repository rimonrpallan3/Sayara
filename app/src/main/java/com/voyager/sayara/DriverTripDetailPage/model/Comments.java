package com.voyager.sayara.DriverTripDetailPage.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 06-Feb-18.
 */

public class Comments {

    @SerializedName("tripComment")
    public String tripComment;

    public String getTripComment() {
        return tripComment;
    }

    public void setTripComment(String tripComment) {
        this.tripComment = tripComment;
    }
}
