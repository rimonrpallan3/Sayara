package com.voyager.sayara.PulsatingActivity.presenter;

import com.voyager.sayara.PulsatingActivity.view.IPulsatingView;
import com.voyager.sayara.landingpage.model.OnTripStartUp;

/**
 * Created by User on 19-Apr-18.
 */

public class PulsatingPresenter implements IPulsatingPresenter {

    IPulsatingView iPulsatingView;

    public PulsatingPresenter(IPulsatingView iPulsatingView) {
        this.iPulsatingView = iPulsatingView;
    }

    @Override
    public void setOnTripStartData(OnTripStartUp onTripStartData) {
        iPulsatingView.getDriverDetails(onTripStartData);
    }
}
