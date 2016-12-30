package com.businessreviewshub.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.businessreviewshub.activities.LoginActivity;
import com.businessreviewshub.data.responseDataDTO.UserDTO;

/**
 * Created by shrinivas on 27-12-2016.
 */
public class UserAuth {
    public static boolean isUserLoggedIn(Context context, String userName, String password) {
        if (password == null || password == "" || userName == null || userName == "") {
            Intent theLoginIntent = new Intent(context, LoginActivity.class);
            //theLoginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            theLoginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            theLoginIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(theLoginIntent);
            return false;
        }
        return true;
    }

    public static boolean isUserLoggedIn() {

        String theUserCompanyCode = SessionManager.Instance().getCompanyCode();
        String theUserName = SessionManager.Instance().getEmployeeName();

        if (theUserCompanyCode == null || theUserCompanyCode == "") {
            return false;
        }
        return true;
    }

    public void saveAuthenticationInfo(UserDTO userInfo, final Context context) {
        if (userInfo == null)
            return;

        if (userInfo.getEmpCode() == null || userInfo.getEmpCode() == "" ||
                userInfo.getEmpName() == null || userInfo.getEmpName() == "" ||
                userInfo.getEmpPwd() == null || userInfo.getEmpPwd() == "")
            return;

        SessionManager theSessionManager = SessionManager.getInstance(context);
        theSessionManager.setCompanyCode(userInfo.getEmpCode());
        theSessionManager.setEmployeeName(userInfo.getEmpName());
        theSessionManager.setEmployeePhone(userInfo.getPhoneNo());
        theSessionManager.setEmployeePassword(userInfo.getEmpPwd());
        theSessionManager.setEmployeeCoded(userInfo.getCompanyCode());
        theSessionManager.setEmployeeCompanyName(userInfo.getCompanyName());
        theSessionManager.setEmployeeCompanyLogoUrl(userInfo.getCompanyLogo());
        theSessionManager.setEmployeeProfileUrl(userInfo.getProfileImage());
        Log.d("TAG", "TAG");
        Log.d("TAG", "TAG");
        Log.d("TAG", "TAG");

    }

    public static boolean CleanAuthenticationInfo() {

        SessionManager theSessionManager = SessionManager.Instance();
        theSessionManager.setCompanyCode(null);
        theSessionManager.setEmployeeName(null);
        theSessionManager.setEmployeePhone(null);
        theSessionManager.setEmployeePassword(null);
        theSessionManager.setEmployeeCoded(null);
        theSessionManager.setEmployeeCompanyName(null);
        theSessionManager.setEmployeeCompanyLogoUrl(null);
        theSessionManager.setEmployeeProfileUrl(null);
        return true;
    }
}
