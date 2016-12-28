package com.businessreviewshub.data.responseDataDTO;

import android.util.Log;

import com.businessreviewshub.data.BaseDTO;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

/**
 * Created by shrinivas on 27-12-2016.
 */
public class LoginResponseDTO extends BaseDTO {
    private static final String TAG = LoginResponseDTO.class.getSimpleName();
    private String empCode;
    private String empName;
    private String phoneNo;
    private String companyLogo;
    private String companyName;
    private String profileImage;

    public String getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getGetEmpCode() {
        return empCode;
    }

    public void setGetEmpCode(String getEmpCode) {
        this.empCode = getEmpCode;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public LoginResponseDTO() {
    }




    public static LoginResponseDTO deserializeJson(String serializedString) {
        Gson gson = new Gson();
        LoginResponseDTO responseDTO = null;
        try {
            responseDTO = gson.fromJson(serializedString, LoginResponseDTO.class);
        } catch (JsonParseException e) {
            Log.d(TAG, "Exception in deserialization RegisterResponseData" + e.toString());
        }
        return responseDTO;
    }
}
