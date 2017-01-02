package com.businessreviewshub.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by shrinivas on 28-12-2016.
 */
public class DateUtils {
    final SimpleDateFormat dateWithTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public java.util.Date getFormattedDate(String strDate) {
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        df2.setTimeZone(TimeZone.getTimeZone("UTC"));
        java.util.Date date = null;
        try {
            date = df2.parse(strDate);
            Log.d("TAG", "TAG");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
