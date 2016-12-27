package com.businessreviewshub.data.requestDataDTO;

/**
 * Created by shrinivas on 27-12-2016.
 */
public class SendSMSRequestDTO {
    private String phoneNo;
    private String customerName;

    public SendSMSRequestDTO(String phoneNo, String customerName) {
        this.phoneNo = phoneNo;
        this.customerName = customerName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
