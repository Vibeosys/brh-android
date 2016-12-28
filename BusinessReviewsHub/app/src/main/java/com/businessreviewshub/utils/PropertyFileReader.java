package com.businessreviewshub.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by shrinivas on 27-12-2016.
 */
public class PropertyFileReader {
    private static PropertyFileReader mPropertyFileReader = null;
    private static Context mContext;
    protected static Properties mProperties;
    public static PropertyFileReader getInstance(Context context) {
        if (mPropertyFileReader != null)
        return mPropertyFileReader;

        mContext = context;
        mProperties = getProperties();
        mPropertyFileReader = new PropertyFileReader();
        return mPropertyFileReader;
    }
    protected static Properties getProperties() {
        try {
            AssetManager assetManager = mContext.getAssets();
            InputStream inputStream = assetManager.open("app.properties");
            mProperties = new Properties();
            mProperties.load(inputStream);

        } catch (Exception e) {
            System.out.print(e.getMessage());
        }

        return mProperties;
    }
    protected String getEndPointUri() {
        return mProperties.getProperty(PropertyTypeConstants.API_ENDPOINT_URI);
    }
    public float getVersion() {
        String versionNumber = mProperties.getProperty(PropertyTypeConstants.VERSION_NUMBER);
        return Float.valueOf(versionNumber);
    }


    public String getDatabaseDirPath() {
        return mProperties.getProperty(PropertyTypeConstants.DATABASE_DIR_PATH);
    }

    public String getDatabaseFileName() {
        return mProperties.getProperty(PropertyTypeConstants.DATABASE_FILE_NAME);
    }
    public String getUserLoginUrl() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.LOGIN_ENDPOINT_URI);
    }
    public String getSendSmsUrl() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.SEND_SMS);
    }
    public String getSmsHistoryUrl() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.SMS_HISTORY);
    }
    public String getEditProfilerUrl() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.EDIT_PROFILE);
    }
}
