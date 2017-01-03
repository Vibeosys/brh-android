package com.businessreviewshub.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by shrinivas on 03-01-2017.
 */
public class NetworkUtils {


    /* Method to check network availability
    * */
    public static boolean isActiveNetworkAvailable(Context aContext){

        boolean theStatus = false;
        ConnectivityManager theConManager = (ConnectivityManager)aContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo theNetInfo = theConManager.getActiveNetworkInfo();
        if(theNetInfo != null) {
            theStatus = theNetInfo.isConnected();
        }
        return theStatus;

    }
}
