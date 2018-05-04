package com.voyager.sayara.MapPlaceSearch.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by User on 04-May-18.
 */

public class CurrentLocDetails implements Parcelable {

    public List<CurrentPlaceDetails> currentPlaceDetailsList;

    public CurrentLocDetails() {
    }

    public List<CurrentPlaceDetails> getCurrentPlaceDetailsList() {
        return currentPlaceDetailsList;
    }

    public void setCurrentPlaceDetailsList(List<CurrentPlaceDetails> currentPlaceDetailsList) {
        this.currentPlaceDetailsList = currentPlaceDetailsList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.currentPlaceDetailsList);
    }

    protected CurrentLocDetails(Parcel in) {
        this.currentPlaceDetailsList = in.createTypedArrayList(CurrentPlaceDetails.CREATOR);
    }

    public static final Parcelable.Creator<CurrentLocDetails> CREATOR = new Parcelable.Creator<CurrentLocDetails>() {
        @Override
        public CurrentLocDetails createFromParcel(Parcel source) {
            return new CurrentLocDetails(source);
        }

        @Override
        public CurrentLocDetails[] newArray(int size) {
            return new CurrentLocDetails[size];
        }
    };
}
