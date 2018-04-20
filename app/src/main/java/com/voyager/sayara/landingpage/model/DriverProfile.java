package com.voyager.sayara.landingpage.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User on 19-Apr-18.
 */

public class DriverProfile implements Parcelable {


    /**
     * carName : Maruti Swift
     * driverPhoto : http://10.1.1.18/sayara/uploads/driver/
     * driverName : Shijo Joseph
     * driverPhone : 8989898989
     * carPhoto : http://10.1.1.18/sayara/uploads/cars/car-344543.jpg
     */

    private String carName;
    private String driverPhoto;
    private String driverName;
    private String driverPhone;
    private String carPhoto;
    private String driverLocation;

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getDriverPhoto() {
        return driverPhoto;
    }

    public void setDriverPhoto(String driverPhoto) {
        this.driverPhoto = driverPhoto;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getCarPhoto() {
        return carPhoto;
    }

    public void setCarPhoto(String carPhoto) {
        this.carPhoto = carPhoto;
    }

    public String getDriverLocation() {
        return driverLocation;
    }

    public void setDriverLocation(String driverLocation) {
        this.driverLocation = driverLocation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.carName);
        dest.writeString(this.driverPhoto);
        dest.writeString(this.driverName);
        dest.writeString(this.driverPhone);
        dest.writeString(this.carPhoto);
        dest.writeString(this.driverLocation);
    }

    public DriverProfile() {
    }

    protected DriverProfile(Parcel in) {
        this.carName = in.readString();
        this.driverPhoto = in.readString();
        this.driverName = in.readString();
        this.driverPhone = in.readString();
        this.carPhoto = in.readString();
        this.driverLocation = in.readString();
    }

    public static final Creator<DriverProfile> CREATOR = new Creator<DriverProfile>() {
        @Override
        public DriverProfile createFromParcel(Parcel source) {
            return new DriverProfile(source);
        }

        @Override
        public DriverProfile[] newArray(int size) {
            return new DriverProfile[size];
        }
    };

    public static Creator<DriverProfile> getCREATOR() {
        return CREATOR;
    }
}
