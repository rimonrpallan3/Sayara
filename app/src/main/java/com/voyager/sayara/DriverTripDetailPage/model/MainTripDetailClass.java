package com.voyager.sayara.DriverTripDetailPage.model;


import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by User on 06-Feb-18.
 */

public class MainTripDetailClass {
    @SerializedName("comments")
    public List<Comments> commentses;
    @SerializedName("driver_info")
    public List<DTDModel> dtdModel;

    public List<Comments> getCommentses() {
        return commentses;
    }

    public void setCommentses(List<Comments> commentses) {
        this.commentses = commentses;
    }

    public List<DTDModel> getDtdModel() {
        return dtdModel;
    }

    public void setDtdModel(List<DTDModel> dtdModel) {
        this.dtdModel = dtdModel;
    }
}
