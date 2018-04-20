package com.voyager.sayara.DriverTripDetailPage.view;

import com.voyager.sayara.DriverTripDetailPage.model.DTDModel;
import com.voyager.sayara.DriverTripDetailPage.model.Comments;

import java.util.List;

/**
 * Created by User on 8/30/2017.
 */

public interface IDTDView {
    void getParcelable();
    void getCommentListAndDetails(List<Comments> commentses, DTDModel dtdModel);


}
