package com.voyager.sayara.MapPlaceSearch.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by User on 04-May-18.
 */

public class CurrentPlaceDetails implements Parcelable {

    CharSequence placeName;
    Float likehood;
    LatLng latLng;

    public CurrentPlaceDetails() {
    }

    public CharSequence getPlaceName() {
        return placeName;
    }

    public void setPlaceName(CharSequence placeName) {
        this.placeName = placeName;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.placeName, flags);
        dest.writeParcelable(this.latLng, flags);
        dest.writeValue(this.likehood);
    }

    protected CurrentPlaceDetails(Parcel in) {
        this.placeName = in.readParcelable(CharSequence.class.getClassLoader());
        this.latLng = in.readParcelable(LatLng.class.getClassLoader());
        this.likehood = (Float) in.readValue(Float.class.getClassLoader());
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
