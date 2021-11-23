package com.skts.ourmemory.presenter;

import android.annotation.SuppressLint;
import android.icu.util.Calendar;
import android.icu.util.ChineseCalendar;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.contract.CalendarAdapterContract;
import com.skts.ourmemory.contract.MyMemoryContract;
import com.skts.ourmemory.model.main.MyMemoryModel;
import com.skts.ourmemory.util.MySharedPreferences;

import java.text.SimpleDateFormat;

public class MyMemoryPresenter implements MyMemoryContract.Presenter {
    private final MyMemoryContract.Model mModel;
    private MyMemoryContract.View mView;
    private CalendarAdapterContract.Model mAdapterModel;
    private CalendarAdapterContract.View mAdapterView;
    private MySharedPreferences mMySharedPreferences;

    // Calendar
    private int mYear;
    private int mMonth;
    private int mDay;

    public MyMemoryPresenter() {
        mModel = new MyMemoryModel(this);
    }

    @Override
    public void setView(MyMemoryContract.View view) {
        mView = view;
        mMySharedPreferences = MySharedPreferences.getInstance(mView.getAppContext());
    }

    @Override
    public void releaseView() {
        mView = null;
    }

    @Override
    public void setDate(int year, int month, int day) {
        mYear = year;
        mMonth = month;
        mDay = day;
    }

    @Override
    public int getYear() {
        return mYear;
    }

    @Override
    public int getMonth() {
        return mMonth;
    }

    @Override
    public int getDay() {
        return mDay;
    }

    @Override
    public void setDay(int day) {
        mDay = day;
    }

    @Override
    public void setAdapterModel(CalendarAdapterContract.Model adapterModel) {
        this.mAdapterModel = adapterModel;
    }

    @Override
    public void setAdapterView(CalendarAdapterContract.View adapterView) {
        this.mAdapterView = adapterView;
    }

    /**
     * 음력 날짜를 양력 날짜로 변환
     */
    @Override
    public String convertLunarToSolar() {
        ChineseCalendar calendar = new ChineseCalendar();
        Calendar cal = Calendar.getInstance();

        calendar.set(ChineseCalendar.EXTENDED_YEAR, mYear + 2637);
        calendar.set(ChineseCalendar.MONTH, mMonth - 1);
        calendar.set(ChineseCalendar.DAY_OF_MONTH, mDay);

        cal.setTimeInMillis(calendar.getTimeInMillis());

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(cal.getTime());
    }

    /**
     * 양력 날짜를 음력 날짜로 변환
     */
    @SuppressLint("DefaultLocale")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public String convertSolarToLunar() {
        ChineseCalendar calendar = new ChineseCalendar();
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, mYear);
        cal.set(Calendar.MONTH, mMonth);
        cal.set(Calendar.DAY_OF_MONTH, mDay);

        calendar.setTimeInMillis(cal.getTimeInMillis());

        int y = calendar.get(ChineseCalendar.EXTENDED_YEAR) - 2637;
        int m = calendar.get(ChineseCalendar.MONTH) + 1;
        int d = calendar.get(ChineseCalendar.DAY_OF_MONTH);

        return m + "." + String.format("%02d", d);
    }

    @Override
    public String getBirthDay() {
        return mMySharedPreferences.getStringExtra(Const.USER_BIRTHDAY);
    }

    @Override
    public boolean isBirthDaySolar() {
        return mMySharedPreferences.getBooleanExtra(Const.USER_IS_SOLAR);
    }
}
