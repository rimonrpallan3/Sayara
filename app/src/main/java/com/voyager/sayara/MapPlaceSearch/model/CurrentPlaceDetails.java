package com.voyager.sayara.MapPlaceSearch.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by User on 04-May-18.
 */

public class CurrentPlaceDetails implements Parcelable {

    String  placeName;
    Float likehood;
    String lat;
    String lng;
    String placeid;

    public CurrentPlaceDetails() {
    }


    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public Float getLikehood() {
        return likehood;
    }

    public void setLikehood(Float likehood) {
        this.likehood = likehood;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getPlaceid() {
        return placeid;
    }

    public void setPlaceid(String placeid) {
        this.placeid = placeid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.placeName);
        dest.writeValue(this.likehood);
        dest.writeString(this.lat);
        dest.writeString(this.lng);
        dest.writeString(this.placeid);
    }

    protected CurrentPlaceDetails(Parcel in) {
        this.placeName = in.readString();
        this.likehood = (Float) in.readValue(Float.class.getClassLoader());
        this.lat = in.readString();
        this.lng = in.readString();
        this.placeid = in.readString();
    }

    public static final Creator<CurrentPlaceDetails> CREATOR = new Creator<CurrentPlaceDetails>() {
        @Override
        public CurrentPlaceDetails createFromParcel(Parcel source) {
            return new CurrentPlaceDetails(source);
        }

        @Override
        public CurrentPlaceDetails[] newArray(int size) {
            return new CurrentPlaceDetails[size];
        }
    };
}
