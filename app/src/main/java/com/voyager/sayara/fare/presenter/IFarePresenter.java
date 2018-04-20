package com.voyager.sayara.fare.presenter;

/**
 * Created by User on 13-Nov-17.
 */

public interface IFarePresenter {
    public void startTrip(int userId,
                          String userName,
                          String nameStartLoc,
                          String nameStart,
                          String nameEndLoc,
                          String nameEnd,
                          String distanceKm,
                          String costFairSet,
                          String driveCarId,
                          String paymentType);
}
