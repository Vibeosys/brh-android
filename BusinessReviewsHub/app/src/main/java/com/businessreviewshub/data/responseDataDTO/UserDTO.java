package com.businessreviewshub.data.responseDataDTO;

/**
 * Created by shrinivas on 27-12-2016.
 */
public class UserDTO {
    private String EmpCode;
    private String EmpName;
    private String PhoneNo;
    private String EmpPwd;

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
