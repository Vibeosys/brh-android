package com.businessreviewshub.data.requestDataDTO;

import com.businessreviewshub.data.BaseDTO;

/**
 * Created by shrinivas on 27-12-2016.
 */
public class UserRequestDTO extends BaseDTO {
    private long empCode;
    private String password;
    private String companyCode;

    public UserRequestDTO(long empCode, String password, String companyCode) {
        this.empCode = empCode;
        this.password = password;
        this.companyCode = companyCode;
    }

    public long getEmpCode() {
        return empCode;
    }

    public void setEmpCode(long empCode) {
        this.empCode = empCode;
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
