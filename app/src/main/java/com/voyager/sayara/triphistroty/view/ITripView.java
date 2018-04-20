package com.voyager.sayara.triphistroty.view;

import android.os.Parcelable;

import com.voyager.sayara.triphistroty.model.TripDetails;

import java.util.List;

/**
 * Created by User on 26-Dec-17.
 */

public interface ITripView {
    void onDataSet(List<TripDetails> tripDetails, int userID);
    void getParcelable();
}
