package com.skts.ourmemory.model.calendar;

import android.annotation.SuppressLint;

import com.skts.ourmemory.util.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

public class Day extends ViewModel {
    private String mYear;            // 년
    private String mMonth;           // 월
    private String mDay;             // 일
    private final SimpleDateFormat mMonthDateFormat;        // 월
    private final SimpleDateFormat mDayDateFormat;          // 일

    @SuppressLint("SimpleDateFormat")
    public Day() {
        mDayDateFormat = new SimpleDateFormat(DateUtil.DAY_FORMAT);
        mMonthDateFormat = new SimpleDateFormat(DateUtil.MONTH_FORMAT);
    }

    public String getYear() {
        return mYear;
    }

    public String getMonth() {
        return mMonth;
    }

    public String getDay() {
        return mDay;
    }

    public void setCalendar(Calendar calendar) {
        mYear = DateUtil.getDate(calendar.getTimeInMillis(), DateUtil.YEAR_FORMAT);
        mMonth = DateUtil.getDate(calendar.getTimeInMillis(), DateUtil.MONTH_FORMAT);
        mDay = DateUtil.getDate(calendar.getTimeInMillis(), DateUtil.DAY_FORMAT);
    }

    public String getToday() {
        return DateUtil.getDate(System.currentTimeMillis(), DateUtil.DAY_FORMAT);
    }

    public String getTodayMonth() {
        return DateUtil.getDate(System.currentTimeMillis(), DateUtil.MONTH_FORMAT);
    }

    public String getTodayYear() {
        return DateUtil.getDate(System.currentTimeMillis(), DateUtil.YEAR_FORMAT);
    }

    public String calcMonth(String startDate) {
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

    public String getClickDay(Calendar calendar) {
        return DateUtil.getDate(calendar.getTimeInMillis(), DateUtil.DAY_FORMAT);
    }

    public boolean isHasCalendar(String startDate, String endDate, Calendar calendar) {
        String[] startDay = startDate.split("-|\\s");
        String[] endDay = endDate.split("-|\\s");
        Calendar startCal = new GregorianCalendar(Integer.parseInt(startDay[0]), Integer.parseInt(startDay[1]) - 1, Integer.parseInt(startDay[2]));
        Calendar endCal = new GregorianCalendar(Integer.parseInt(endDay[0]), Integer.parseInt(endDay[1]) - 1, Integer.parseInt(endDay[2]));
        return startCal.compareTo(calendar) <= 0 && endCal.compareTo(calendar) >= 0;
    }

    public int compareDay(String oldStartDate, String newStartDate) {
        int day = 0;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date oldDate = simpleDateFormat.parse(oldStartDate);
            Date newDate = simpleDateFormat.parse(newStartDate);

            day = (int) (oldDate.getTime() - newDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return day;
    }

    public int calcDays(String startDate, String endDate) {
        int day = 0;

        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = simpleDateFormat.parse(startDate);
            Date date2 = simpleDateFormat.parse(endDate);

            day = (int) (((date2.getTime() - date1.getTime()) / 1000) / (24 * 60 * 60));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return day;
    }

    public int getStartDay(String startDate) {
        int day = 0;

        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = simpleDateFormat.parse(startDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            day = calendar.get(Calendar.DAY_OF_MONTH);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return day;
    }

    public int getLastDay(String startDate) {
        int day = 0;

        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = simpleDateFormat.parse(startDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return day;
    }
}
