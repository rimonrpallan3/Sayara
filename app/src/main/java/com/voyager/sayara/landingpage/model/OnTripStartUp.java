package com.voyager.sayara.landingpage.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User on 17-Apr-18.
 */

public class OnTripStartUp implements Parcelable {


    /**
     * tripId : 100028
     * tripStatus : Accepted
     * driverProfile : {}
     */

    private String tripId;
    private String tripStatus;
    private DriverProfile driverProfile;

    public OnTripStartUp() {
    }


    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(String tripStatus) {
        this.tripStatus = tripStatus;
    }


    public DriverProfile getDriverProfile() {
        return driverProfile;
    }

    public void setDriverProfile(DriverProfile driverProfile) {
        this.driverProfile = driverProfile;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tripId);
        dest.writeString(this.tripStatus);
        dest.writeParcelable(this.driverProfile, flags);
    }

    protected OnTripStartUp(Parcel in) {
        this.tripId = in.readString();
        this.tripStatus = in.readString();
        this.driverProfile = in.readParcelable(DriverProfile.class.getClassLoader());
    }

    public static final Creator<OnTripStartUp> CREATOR = new Creator<OnTripStartUp>() {
        @Override
        public OnTripStartUp createFromParcel(Parcel source) {
            return new OnTripStartUp(source);
        }

        @Override
        public OnTripStartUp[] newArray(int size) {
            return new OnTripStartUp[size];
        }
    };
}
