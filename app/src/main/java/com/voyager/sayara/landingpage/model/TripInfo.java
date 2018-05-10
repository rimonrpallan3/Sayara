package com.voyager.sayara.landingpage.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User on 21-Apr-18.
 */

public class TripInfo implements Parcelable {


    /**
     * pickupAddress : Infopark Campus
     * tripAmount : 0 DB
     * tripId : 100035
     * pickupLocation : 9.732418700000002,76.3537334
     * dropAddress : Infopark Campus
     * tripStatus : Accepted
     */

    private String pickupAddress;
    private String tripAmount;
    private String tripId;
    private String pickupLocation;
    private String dropAddress;
    private String tripStatus;
    private String dropLoc;

    public String getDropLoc() {
        return dropLoc;
    }

    public void setDropLoc(String dropLoc) {
        this.dropLoc = dropLoc;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public String getTripAmount() {
        return tripAmount;
    }

    public void setTripAmount(String tripAmount) {
        this.tripAmount = tripAmount;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getDropAddress() {
        return dropAddress;
    }

    public void setDropAddress(String dropAddress) {
        this.dropAddress = dropAddress;
    }

    public String getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(String tripStatus) {
        this.tripStatus = tripStatus;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.pickupAddress);
        dest.writeString(this.tripAmount);
        dest.writeString(this.tripId);
        dest.writeString(this.pickupLocation);
        dest.writeString(this.dropAddress);
        dest.writeString(this.tripStatus);
        dest.writeString(this.dropLoc);
    }

    public TripInfo() {
    }

    protected TripInfo(Parcel in) {
        this.pickupAddress = in.readString();
        this.tripAmount = in.readString();
        this.tripId = in.readString();
        this.pickupLocation = in.readString();
        this.dropAddress = in.readString();
        this.tripStatus = in.readString();
        this.dropLoc = in.readString();
    }

    public static final Parcelable.Creator<TripInfo> CREATOR = new Parcelable.Creator<TripInfo>() {
        @Override
        public TripInfo createFromParcel(Parcel source) {
            return new TripInfo(source);
        }

        @Override
        public TripInfo[] newArray(int size) {
            return new TripInfo[size];
        }
    };
}
