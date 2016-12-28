package com.businessreviewshub.data.responseDataDTO;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;

/**
 * Created by shrinivas on 28-12-2016.
 */
public class SmsHistoryResponseDTO {
    private String smsId;
    private String smsDate;
    private String customerName;
    private String phoneNo;

    public SmsHistoryResponseDTO(String smsId, String smsDate, String customerName, String phoneNo) {
        this.smsId = smsId;
        this.smsDate = smsDate;
        this.customerName = customerName;
        this.phoneNo = phoneNo;
    }

    public String getSmsId() {
        return smsId;
    }

    public void setSmsId(String smsId) {
        this.smsId = smsId;
    }

    public String getSmsDate() {
        return smsDate;
    }

    public void setSmsDate(String smsDate) {
        this.smsDate = smsDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public static ArrayList<SmsHistoryResponseDTO> deserializeToArray(String stringJson)
    {
        Gson gson =new Gson();
        ArrayList<SmsHistoryResponseDTO> smsHistoryResponseDTOs=null;
        try
        {
            SmsHistoryResponseDTO[] deserializeObject =gson.fromJson(stringJson,SmsHistoryResponseDTO[].class);
            smsHistoryResponseDTOs =new ArrayList<>();
            for(SmsHistoryResponseDTO singleDTOelement:deserializeObject)
            {
                smsHistoryResponseDTOs.add(singleDTOelement);
            }
        }catch (JsonSyntaxException e)
        {
            Log.e("deserialize", "SMS History DTO error" + e.toString());
        }

        return smsHistoryResponseDTOs;
    }
}
