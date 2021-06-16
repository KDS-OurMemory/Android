package com.skts.ourmemory.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    //public static final String CALENDAR_HEADER_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String CALENDAR_HEADER_FORMAT = "yyyy-MM";
    public static final String YEAR_FORMAT = "yyyy";
    public static final String MONTH_FORMAT = "MM";
    public static final String DAY_FORMAT = "d";
    public static final String HOUR_FORMAT = "HH";
    public static final String MIN_FORMAT = "mm";
    public static final String SEC_FORMAT = "ss";

    public static String getDate(long date, String pattern) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.ENGLISH);
            Date d = new Date(date);
            return formatter.format(d).toUpperCase();
        } catch (Exception e) {
            return " ";
        }
    }

}