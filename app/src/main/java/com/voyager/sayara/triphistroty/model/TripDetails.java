package com.voyager.sayara.triphistroty.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.voyager.sayara.registerpage.model.UserDetails;

/**
 * Created by User on 26-Dec-17.
 */

public class TripDetails implements Parcelable {
    @SerializedName("trip_id")
    int tripId;
    @SerializedName("car")
    String driverCarName;
    @SerializedName("driver_name")
    String driverName;
    @SerializedName("time")
    String dateTime;
    @SerializedName("pay_type")
    String cashMode;
    @SerializedName("amount")
    String cash;
    @SerializedName("trip_image")
    String tripImgUrl;
    @SerializedName("driver_photo")
    String driverPhotoUrl;
    @SerializedName("driver_id")
    int driverId;
    @SerializedName("driver_rating")
    String starRate;
    int userId;


    public TripDetails(){

    }

    public TripDetails(String date, String cash, String driverCarName, String starRate,String cashMode,String tripImgUrl) {
        this.dateTime = date;
        this.cash = cash;
        this.driverCarName = driverCarName;
        this.starRate = starRate;
        this.cashMode = cashMode;
        this.tripImgUrl = tripImgUrl;
    }

    protected TripDetails(Parcel in) {
        tripId = in.readInt();
        driverCarName = in.readString();
        driverName = in.readString();
        dateTime = in.readString();
        cashMode = in.readString();
        cash = in.readString();
        tripImgUrl = in.readString();
        driverPhotoUrl = in.readString();
        driverId = in.readInt();
        starRate = in.readString();
        userId = in.readInt();
    }

    @Override
    public int describeContents() {
        System.out.println("Describe the content TripDetails");
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        System.out.println("writeToParcel to TripDetails ");
        dest.writeInt(tripId);
        dest.writeString(driverCarName);
        dest.writeString(driverName);
        dest.writeString(dateTime);
        dest.writeString(cashMode);
        dest.writeString(cash);
        dest.writeString(tripImgUrl);
        dest.writeString(driverPhotoUrl);
        dest.writeInt(driverId);
        dest.writeString(starRate);
        dest.writeInt(userId);
    }

    public static final Parcelable.Creator<TripDetails> CREATOR = new Parcelable.Creator<TripDetails>() {
        @Override
        public TripDetails createFromParcel(Parcel in) {
            return new TripDetails(in);
        }

        @Override
        public TripDetails[] newArray(int size) {
            return new TripDetails[size];
        }
    };

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPhotoUrl() {
        return driverPhotoUrl;
    }

    public void setDriverPhotoUrl(String driverPhotoUrl) {
        this.driverPhotoUrl = driverPhotoUrl;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public String getDriverCarName() {
        return driverCarName;
    }

    public void setDriverCarName(String driverCarName) {
        this.driverCarName = driverCarName;
    }

    public String getTripImgUrl() {
        return tripImgUrl;
    }

    public void setTripImgUrl(String tripImgUrl) {
        this.tripImgUrl = tripImgUrl;
    }

    public static Creator<TripDetails> getCREATOR() {
        return CREATOR;
    }

    public String getCashMode() {
        return cashMode;
    }

    public void setCashMode(String cashMode) {
        this.cashMode = cashMode;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getDriverCar() {
        return driverCarName;
    }

    public void setDriverCar(String driverCar) {
        this.driverCarName = driverCar;
    }

    public String getStarRate() {
        return starRate;
    }

    public void setStarRate(String starRate) {
        this.starRate = starRate;
    }
}
