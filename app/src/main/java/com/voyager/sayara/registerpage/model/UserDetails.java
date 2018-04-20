package com.voyager.sayara.registerpage.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 8/30/2017.
 */

public class UserDetails  implements Parcelable,IUserValidate{
    @SerializedName("name")
    String FName;
    @SerializedName("password")
    String pswd;
    String retypePswd;
    @SerializedName("email")
    String email;
    @SerializedName("phone")
    String phno;
    @SerializedName("city")
    String city;
    @SerializedName("country")
    String country;
    @SerializedName("error")
    public boolean isError;
    @SerializedName("id")
    public int userID;
    @SerializedName("created_at")
    public String created_at="";
    @SerializedName("error_msg")
    public String error_msg="";
    @SerializedName("photo")
    public String imgPath;
    String fcm = "";

    public UserDetails(){

    }


    public UserDetails(String email, String pswd) {
        this.email = email;
        this.pswd = pswd;
    }

    public UserDetails(String FName, String pswd,String retypePswd, String email, String phno, String city, String country) {
        this.FName = FName;
        this.pswd = pswd;
        this.retypePswd = retypePswd;
        this.email = email;
        this.phno = phno;
        this.city = city;
        this.country = country;
    }

    protected UserDetails(Parcel in) {
        FName = in.readString();
        pswd = in.readString();
        retypePswd = in.readString();
        email = in.readString();
        phno = in.readString();
        city = in.readString();
        country = in.readString();
        imgPath =in.readString();
        isError = in.readByte() != 0;
        userID = in.readInt();
        created_at = in.readString();
        error_msg = in.readString();
        fcm = in.readString();
    }

    @Override
    public int describeContents() {
        System.out.println("Describe the content UserDetails");
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        System.out.println("writeToParcel to UserDetails ");
        dest.writeString(FName);
        dest.writeString(pswd);
        dest.writeString(retypePswd);
        dest.writeString(email);
        dest.writeString(phno);
        dest.writeString(city);
        dest.writeString(country);
        dest.writeString(imgPath);
        dest.writeByte((byte) (isError ? 1 : 0));
        dest.writeInt(userID);
        dest.writeString(created_at);
        dest.writeString(error_msg);
        dest.writeString(fcm);
    }


    public static final Creator<UserDetails> CREATOR = new Creator<UserDetails>() {
        @Override
        public UserDetails createFromParcel(Parcel in) {
            return new UserDetails(in);
        }

        @Override
        public UserDetails[] newArray(int size) {
            return new UserDetails[size];
        }
    };


    public static Creator<UserDetails> getCREATOR() {
        return CREATOR;
    }

    public String getFcm() {
        return fcm;
    }

    public void setFcm(String fcm) {
        this.fcm = fcm;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getError_msg() {
        return error_msg;
    }


    public String getRetypePswd() {
        return retypePswd;
    }

    public void setRetypePswd(String retypePswd) {
        this.retypePswd = retypePswd;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPasswd() {
        return pswd;
    }

    @Override
    public int checkUserValidity(String name, String passwd) {
        if (email==null||passwd==null||!email.equals(getEmail())||!passwd.equals(getPasswd())){
            return -1;
        }
        return 0;
    }

    @Override
    public int checkNameID(int userID, String FName) {
        if (userID==0||FName.trim().length()==0){
            return -1;
        }else {
            for (int i = 0; i < FName.trim().length(); i++) {
                char charAt2 = FName.trim().charAt(i);
                if (!Character.isLetter(charAt2)) {
                    return -2;
                }
            }
        }
        return 0;
    }

    @Override
    public int checkUpdateNameApi(Boolean isError) {
        System.out.println("---------checkUpdateNameApi isError: "+isError);
        if(isError){
            //if there is no error message then it means that data response is correct.
            return -2;
        }
        return 0;
    }

    @Override
    public int validateLoginResponseError(String errorMsg) {
        if(errorMsg!=null){
            //if there is no error message then it means that data response is correct.
            return -2;
        }
        return 0;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFName() {
        return FName;
    }

    public void setFName(String FName) {
        this.FName = FName;
    }

    public String getPswd() {
        return pswd;
    }

    public void setPswd(String pswd) {
        this.pswd = pswd;
    }

    public String getPhno() {
        return phno;
    }

    public void setPhno(String phno) {
        this.phno = phno;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    @Override
    public int validateUserDetails(String FName,
                                   String password,
                                   String RetypePassword,
                                   String email,
                                   String phno,
                                   String city,
                                   String country) {
        if (FName.trim().length()==0||
                password.trim().length()==0||
                RetypePassword.trim().length()==0||
                email.trim().length()==0||
                phno.trim().length()==0||
                city.trim().length()==0||
                country.trim().length()==0){
            {
                return -1;
            }
        }else {
            for (int i = 0; i < FName.trim().length(); i++) {
                char charAt2 = FName.trim().charAt(i);
                if (!Character.isLetter(charAt2)) {
                    return -2;
                }
            }
            for (int i = 0; i < password.trim().length(); i++) {
                String charAt2 = password.trim().toString();
                if (charAt2==null) {
                    return -3;
                }
            }

            if(!password.equals(RetypePassword)){
                return -4;
            }
            for (int i = 0; i < email.trim().length(); i++) {
                String charAt2 = email.trim().toString();
                if (charAt2==null) {
                    return -5;
                }
            }
            for (int i = 0; i < phno.trim().length(); i++) {
                String charAt2 = phno.trim().toString();
                if (charAt2==null) {
                    return -6;
                }
            }
            for (int i = 0; i < city.trim().length(); i++) {
                char charAt2 = city.trim().charAt(i);
                if (!Character.isLetter(charAt2)) {
                    return -7;
                }
            }
            for (int i = 0; i < country.trim().length(); i++) {
                char charAt2 = country.trim().charAt(i);
                if (!Character.isLetter(charAt2)) {
                    return -8;
                }
            }
        }
        return 0;
    }

    @Override
    public int validateRegisterResponseError(String errorMsg) {
        if(errorMsg!=null&&errorMsg.length()>1){
            //if there is no error message then it means that data response is correct.
            return -9;
        }
        return 0;
    }


}
