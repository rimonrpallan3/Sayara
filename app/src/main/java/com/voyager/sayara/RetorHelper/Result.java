package com.voyager.sayara.RetorHelper;

import java.util.HashMap;

/**
 * Created by User on 10-Apr-18.
 */

public class Result {
    HashMap<String, MainOffers> firstOffer;


    public Result() {
    }

    public Result(HashMap<String, MainOffers> firstOffer) {
        this.firstOffer = firstOffer;
    }

    public HashMap<String, MainOffers> getFirstOffer() {
        return firstOffer;
    }

    public void setFirstOffer(HashMap<String, MainOffers> firstOffer) {
        this.firstOffer = firstOffer;
    }
}
