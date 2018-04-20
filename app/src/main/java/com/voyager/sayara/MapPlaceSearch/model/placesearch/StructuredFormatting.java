package com.voyager.sayara.MapPlaceSearch.model.placesearch;

import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 12-Feb-18.
 */

public class StructuredFormatting {
    @SerializedName("main_text")
    String mainText;
    @SerializedName("secondary_text")
    String secondaryText;

    public String getMainText() {
        return mainText;
    }

    public void setMainText(String mainText) {
        this.mainText = mainText;
    }

    public String getSecondaryText() {
        return secondaryText;
    }

    public void setSecondaryText(String secondaryText) {
        this.secondaryText = secondaryText;
    }
}
