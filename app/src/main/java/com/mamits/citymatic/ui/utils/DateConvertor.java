package com.mamits.citymatic.ui.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateConvertor {

    public final static String FORMAT_dd_MM_yyyy = "dd-MM-yyyy";
    public final static String FORMAT_dd_MM_yyyy_HH_mm_ss = "dd-MM-yyyy HH:mm:ss";
    public final static String FORMAT_MMM_dd_yyyy_hh_mm_a = "MMM dd, yyyy | hh:mm a";
    public final static String FORMAT_yyyy_MM_dd = "yyyy-MM-dd";

    public String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
