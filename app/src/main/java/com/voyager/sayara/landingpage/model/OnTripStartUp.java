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
    private String tripStatus;
    private DriverProfile driverProfile;
    private TripInfo tripInfo;

    public OnTripStartUp() {
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

    public TripInfo getTripInfo() {
        return tripInfo;
    }

    public void setTripInfo(TripInfo tripInfo) {
        this.tripInfo = tripInfo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.driverProfile, flags);
        dest.writeParcelable(this.tripInfo, flags);
        dest.writeString(this.tripStatus);
    }

    protected OnTripStartUp(Parcel in) {
        this.driverProfile = in.readParcelable(DriverProfile.class.getClassLoader());
        this.tripInfo = in.readParcelable(TripInfo.class.getClassLoader());
        this.tripStatus = in.readString();
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
