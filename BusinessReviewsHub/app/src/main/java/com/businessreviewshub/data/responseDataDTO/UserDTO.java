package com.businessreviewshub.data.responseDataDTO;

/**
 * Created by shrinivas on 27-12-2016.
 */
public class UserDTO {
    private String EmpCode;
    private String EmpName;
    private String PhoneNo;
    private String EmpPwd;
    private String companyCode;
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

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getEmpPwd() {
        return EmpPwd;
    }

    public void setEmpPwd(String empPwd) {
        EmpPwd = empPwd;
    }

    public String getEmpCode() {
        return EmpCode;
    }

    public void setEmpCode(String empCode) {
        EmpCode = empCode;
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
}
