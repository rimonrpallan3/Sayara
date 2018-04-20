package com.voyager.sayara.MapPlaceSearch.model.placedetail;

import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 12-Feb-18.
 */

public class Result {
    @SerializedName("formatted_address")
    String formattedAddress;
    public Geometry geometry;

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }
}
