package com.voyager.sayara.MapPlaceSearch.model.placesearch;

import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 12-Feb-18.
 */

public class Predictions {
    public String description;
    @SerializedName("place_id")
    public String placeId;
    @SerializedName("structured_formatting")
    public StructuredFormatting structuredFormatting;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public StructuredFormatting getStructuredFormatting() {
        return structuredFormatting;
    }

    public void setStructuredFormatting(StructuredFormatting structuredFormatting) {
        this.structuredFormatting = structuredFormatting;
    }
}
