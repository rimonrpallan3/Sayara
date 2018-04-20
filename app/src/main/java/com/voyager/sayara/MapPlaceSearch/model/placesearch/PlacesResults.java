package com.voyager.sayara.MapPlaceSearch.model.placesearch;

import java.util.ArrayList;

/**
 * Created by User on 12-Feb-18.
 */

public class PlacesResults {
    public ArrayList<Predictions> predictions;
    public String status;

    public ArrayList<Predictions> getPredictions() {
        return predictions;
    }

    public void setPredictions(ArrayList<Predictions> predictions) {
        this.predictions = predictions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
