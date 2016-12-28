package com.businessreviewshub.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by shrinivas on 28-12-2016.
 */
public class DateUtils {
    final SimpleDateFormat dateWithTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    public java.util.Date getFormattedDate(String strDate) {
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        java.util.Date date = null;
        try {
            date = df2.parse(strDate);
            String stringMonth = (String) android.text.format.DateFormat.format("MMM", date);
            String day = (String) android.text.format.DateFormat.format("dd", date);
            String timeStr = (String) android.text.format.DateFormat.format("h:mm a", date);
            Log.d("TAG","TAG");
            Log.d("TAG","TAG");
            Log.d("TAG","TAG");

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
