package com.businessreviewshub.data.requestDataDTO;

/**
 * Created by shrinivas on 02-03-2017.
 */
public class ForgotPasswordDTO {
    private String companyCode;
    private String email;

    public ForgotPasswordDTO(String companyCode, String email) {
        this.companyCode = companyCode;
        this.email = email;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
