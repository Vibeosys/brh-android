package com.businessreviewshub.data.requestDataDTO;

/**
 * Created by shrinivas on 27-12-2016.
 */
public class LoginRequestDTO {
    private String email;
    private String password;
    private String companyCode;

    public LoginRequestDTO(String email, String password, String companyCode) {
        this.email = email;
        this.password = password;
        this.companyCode = companyCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }
}
