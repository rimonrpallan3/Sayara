package com.voyager.sayara.firstotppage.model;


/**
 * Created by User on 15-Sep-17.
 */

public class FirstOTPModel implements IFirstOTPModel {
    String contry;
    String contryCode;
    String phno;

    public FirstOTPModel(){

    }

    public FirstOTPModel(String contry, String zipCode, String phno) {
        this.contry = contry;
        this.contryCode = zipCode;
        this.phno = phno;
    }

    public String getContry() {
        return contry;
    }

    public void setContry(String contry) {
        this.contry = contry;
    }

    public String getContryCode() {
        return contryCode;
    }

    public void setContryCode(String contryCode) {
        this.contryCode = contryCode;
    }

    public String getPhno() {
        return phno;
    }

    public void setPhno(String phno) {
        this.phno = phno;
    }

    @Override
    public int validateFirstOTPpage(String contry, String zipCode, String phno) {
        if (contry.trim().length()==0||zipCode.trim().length()==0||phno.trim().length()==0){
            {
                return -1;
            }
        }else {
            for (int i = 0; i < contry.trim().length(); i++) {
                char charAt2 = contry.trim().charAt(i);
                if (!Character.isLetter(charAt2)) {
                    return -2;
                }
            }
            for (int i = 0; i < zipCode.trim().length(); i++) {
                String charAt2 = zipCode.trim().toString();
                if (charAt2==null) {
                    return -3;
                }
            }
            for (int i = 0; i < phno.trim().length(); i++) {
                char charAt2 = phno.trim().charAt(i);
                if (!Character.isDigit(charAt2)) {
                    return -4;
                }else if(phno.trim().length()!=8){
                    return -5;
                }
            }

        }
        return 0;
    }
}