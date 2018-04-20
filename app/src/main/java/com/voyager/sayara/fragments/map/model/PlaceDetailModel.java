package com.voyager.sayara.fragments.map.model;


import android.net.Uri;

/**
 * Created by User on 15-Sep-17.
 */

public class PlaceDetailModel implements IPlaceDetailModel {

    String nameStart;
    String nameEnd;
    String id;
    String distanceKm;
    String addressStart;
    String addressEnd;
    String phoneNo;
    Uri webUri;

    public PlaceDetailModel(String nameStart, String nameEnd, String distanceKm, String addressStart, String addressEnd) {
        this.nameStart = nameStart;
        this.nameEnd = nameEnd;
        this.distanceKm = distanceKm;
        this.addressStart = addressStart;
        this.addressEnd = addressEnd;
    }

    public String getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(String distanceKm) {
        this.distanceKm = distanceKm;
    }

    public String getNameStart() {
        return nameStart;
    }

    public void setNameStart(String nameStart) {
        this.nameStart = nameStart;
    }

    public String getNameEnd() {
        return nameEnd;
    }

    public void setNameEnd(String nameEnd) {
        this.nameEnd = nameEnd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getAddressStart() {
        return addressStart;
    }

    public void setAddressStart(String addressStart) {
        this.addressStart = addressStart;
    }

    public String getAddressEnd() {
        return addressEnd;
    }

    public void setAddressEnd(String addressEnd) {
        this.addressEnd = addressEnd;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public Uri getWebUri() {
        return webUri;
    }

    public void setWebUri(Uri webUri) {
        this.webUri = webUri;
    }
}