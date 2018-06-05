package com.voyager.sayara.firstotppage.view;

import com.voyager.sayara.firstotppage.model.CountryDetails;

import java.util.List;

/**
 * Created by User on 8/30/2017.
 */

public interface IFirstOTPView {
    public void validatedSendData(Boolean result, int code);
    public void getCountryDetailList(List<CountryDetails> countryDetailsList);
}
