package com.voyager.sayara.otppagesubmit.model;


/**
 * Created by User on 15-Sep-17.
 */

public class OTPModel implements IOTPModel {
    String optNo;
    Boolean checkTermsAndConductionBox;

    public OTPModel(String optNo, Boolean checkTermsAndConductionBox) {
        this.optNo = optNo;
        this.checkTermsAndConductionBox = checkTermsAndConductionBox;
    }

    public String getOptNo() {
        return optNo;
    }

    public void setOptNo(String optNo) {
        this.optNo = optNo;
    }

    public Boolean getCheckTermsAndConductionBox() {
        return checkTermsAndConductionBox;
    }

    public void setCheckTermsAndConductionBox(Boolean checkTermsAndConductionBox) {
        this.checkTermsAndConductionBox = checkTermsAndConductionBox;
    }

    @Override
    public int validateCheckBoxAndOtp(String optNumber, Boolean checkTermsAndConductionBox) {
        if (optNumber.trim().length()==0){
            return -1;
        }
        if(checkTermsAndConductionBox==false){
            return -2;
        }
        return 0;
    }
}