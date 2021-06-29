package com.skts.ourmemory.model.calendar;

import android.annotation.SuppressLint;

import com.skts.ourmemory.util.DateUtil;
import com.skts.ourmemory.util.DebugLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Day extends ViewModel {
    private String day;             // 일
    private String month;           // 월
    private final SimpleDateFormat mDayDateFormat;          // 일
    private final SimpleDateFormat mMonthDateFormat;        // 월

    @SuppressLint("SimpleDateFormat")
    public Day() {
        mDayDateFormat = new SimpleDateFormat("dd");
        mMonthDateFormat = new SimpleDateFormat("MM");
    }

    public String getDay() {
        return day;
    }

    public String getMonth() {
        return month;
    }

    public void setCalendar(Calendar calendar) {
        // TODO : 일, 월 데이터 받아와야 함
        day = DateUtil.getDate(calendar.getTimeInMillis(), DateUtil.DAY_FORMAT);
        month = DateUtil.getDate(calendar.getTimeInMillis(), DateUtil.MONTH_FORMAT);
    }

    public String getToday() {
        String today;
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        today = mDayDateFormat.format(date);
        return today;
    }

    public String getStartDay(String startDate) {
        Date date;
        String day = null;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            date = simpleDateFormat.parse(startDate);
            day = mDayDateFormat.format(Objects.requireNonNull(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return day;
    }

    public String getStartMonth(String startDate) {
        Date date;
        String month = null;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            date = simpleDateFormat.parse(startDate);
            month = mMonthDateFormat.format(Objects.requireNonNull(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return month;
    }
}
