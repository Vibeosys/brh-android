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
    private String EmpCode;
    private String EmpName;
    private String PhoneNo;

    public String getGetEmpCode() {
        return EmpCode;
    }

    public void setGetEmpCode(String getEmpCode) {
        this.EmpCode = getEmpCode;
    }

    public String getEmpName() {
        return EmpName;
    }

    public void setEmpName(String empName) {
        EmpName = empName;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
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
