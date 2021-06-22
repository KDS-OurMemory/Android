package com.skts.ourmemory.model.calendar;

import android.annotation.SuppressLint;

import com.skts.ourmemory.util.DateUtil;
import com.skts.ourmemory.util.DebugLog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Day extends ViewModel {
    private String day;
    private final SimpleDateFormat simpleDateFormat;

    @SuppressLint("SimpleDateFormat")
    public Day() {
        simpleDateFormat = new SimpleDateFormat("dd");
    }

    public String getDay() {
        return day;
    }

    public void setCalendar(Calendar calendar) {
        day = DateUtil.getDate(calendar.getTimeInMillis(), DateUtil.DAY_FORMAT);
    }

    public String getToday() {
        String today;
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        today = simpleDateFormat.format(date);
        return today;
    }
}
