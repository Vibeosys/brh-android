package com.businessreviewshub.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by shrinivas on 27-12-2016.
 */
public class SessionManager {
    private static final String PROJECT_PREFERENCES = "com.aftersapp";


    private static SessionManager mSessionManager;
    private static SharedPreferences mProjectSharedPref = null;
    private static Context mContext = null;
    private static PropertyFileReader mPropertyFileReader = null;

    public static SessionManager getInstance(Context context) {
        if (mSessionManager != null)
            return mSessionManager;

        mContext = context;
        mPropertyFileReader = mPropertyFileReader.getInstance(context);
        loadProjectSharedPreferences();
        mSessionManager = new SessionManager();

        return mSessionManager;

    }

    public static SessionManager Instance() throws IllegalArgumentException {
        if (mSessionManager != null)
            return mSessionManager;
        else
            throw new IllegalArgumentException("No instance is yet created");
    }

    private static void loadProjectSharedPreferences() {
        if (mProjectSharedPref == null) {
            mProjectSharedPref = mContext.getSharedPreferences(PROJECT_PREFERENCES, Context.MODE_PRIVATE);
        }

        String versionNumber = mProjectSharedPref.getString(PropertyTypeConstants.VERSION_NUMBER, null);
        Float versionNoValue = versionNumber == null ? 0 : Float.valueOf(versionNumber);

        if (mPropertyFileReader.getVersion() > versionNoValue) {
            boolean sharedPrefChange = addOrUdateSharedPreferences();
            if (!sharedPrefChange)
                Log.e("SharedPref", "No shared preferences are changed");
        }
    }

    private static boolean addOrUdateSharedPreferences() {

        SharedPreferences.Editor editor = mProjectSharedPref.edit();
        editor.putString(PropertyTypeConstants.LOGIN_ENDPOINT_URI, mPropertyFileReader.getUserLoginUrl());

        editor.apply();
        return true;
    }

    private static void setValuesInSharedPrefs(String sharedPrefKey, long sharedPrefValue) {
        SharedPreferences.Editor editor = mProjectSharedPref.edit();
        editor.putLong(sharedPrefKey, sharedPrefValue);
        editor.apply();
    }

    private static void setValuesInSharedPrefs(String sharedPrefKey, String sharedPrefValue) {
        SharedPreferences.Editor editor = mProjectSharedPref.edit();
        editor.putString(sharedPrefKey, sharedPrefValue);
        editor.apply();
    }

    public long getEmployeeCode() {
        return mProjectSharedPref.getLong(PropertyTypeConstants.EMPLOYEE_CODE, 0);
    }

    public String getPassword() {
        return mProjectSharedPref.getString(PropertyTypeConstants.EMPLOYEE_PASSWORD, null);
    }

    public String getCompanyCode() {
        return mProjectSharedPref.getString(PropertyTypeConstants.EMPLOYEE_COMPANY_CODE, null);
    }

    public void setCompanyCode(String empCode) {
        setValuesInSharedPrefs(PropertyTypeConstants.EMPLOYEE_COMPANY_CODE, empCode);
    }

    public String getEmployeeName() {
        return mProjectSharedPref.getString(PropertyTypeConstants.EMPLOYEE_NAME, null);
    }

    public void setEmployeeName(String empName) {
        setValuesInSharedPrefs(PropertyTypeConstants.EMPLOYEE_NAME, empName);
    }

    public String getEmployeePhone() {
        return mProjectSharedPref.getString(PropertyTypeConstants.EMPLOYEE_PHONE, null);
    }

    public void setEmployeePhone(String empPhone) {
        setValuesInSharedPrefs(PropertyTypeConstants.EMPLOYEE_PHONE, empPhone);
    }

    public String getEmployeePassword() {
        return mProjectSharedPref.getString(PropertyTypeConstants.EMPLOYEE_PASSWORD, null);
    }

    public void setEmployeePassword(String empPassword) {
        setValuesInSharedPrefs(PropertyTypeConstants.EMPLOYEE_PASSWORD, empPassword);
    }

    public String getLogInUrl() {
        return mProjectSharedPref.getString(PropertyTypeConstants.LOGIN_ENDPOINT_URI, null);
    }
}
