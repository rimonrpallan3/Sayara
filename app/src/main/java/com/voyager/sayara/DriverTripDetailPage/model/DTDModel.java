package com.voyager.sayara.DriverTripDetailPage.model;


import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by User on 15-Sep-17.
 */

public class DTDModel implements IDTDModel {

    @SerializedName("driverName")
    String driverName;
    @SerializedName("driverRating")
    String driverRating;
    @SerializedName("languagesKnown")
    String languagesKnown;
    @SerializedName("driverId")
    public int driverID;
    @SerializedName("driverPhoto")
    public String imgPath;
    @SerializedName("memberFrom")
    String workDays;
    @SerializedName("comments")
    public List<Comments> comments;


    public List<Comments> getComments() {
        return comments;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }

    public String getWorkDays() {
        return workDays;
    }

    public void setWorkDays(String workDays) {
        this.workDays = workDays;
    }

    public int getDriverID() {
        return driverID;
    }

    public void setDriverID(int driverID) {
        this.driverID = driverID;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverRating() {
        return driverRating;
    }

    public void setDriverRating(String driverRating) {
        this.driverRating = driverRating;
    }

    public String getLanguagesKnown() {
        return languagesKnown;
    }

    public void setLanguagesKnown(String languagesKnown) {
        this.languagesKnown = languagesKnown;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

}