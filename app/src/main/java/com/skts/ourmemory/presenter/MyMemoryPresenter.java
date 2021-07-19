package com.skts.ourmemory.presenter;

import android.os.SystemClock;

import com.skts.ourmemory.contract.CalendarAdapterContract;
import com.skts.ourmemory.contract.MyMemoryContract;
import com.skts.ourmemory.model.main.MyMemoryModel;

public class MyMemoryPresenter implements MyMemoryContract.Presenter {
    private final MyMemoryContract.Model mModel;
    private MyMemoryContract.View mView;
    private CalendarAdapterContract.Model mAdapterModel;
    private CalendarAdapterContract.View mAdapterView;

    private long mLastClickTime = 0;
    private long mLastClickTime2 = 0;

    // Calendar
    private int mYear;
    private int mMonth;

    public MyMemoryPresenter() {
        mModel = new MyMemoryModel(this);
    }

    @Override
    public void setView(MyMemoryContract.View view) {
        mView = view;
    }

    @Override
    public void releaseView() {
        mView = null;
    }

    @Override
    public boolean isDuplicate() {
        // 중복 발생x
        if (SystemClock.elapsedRealtime() - mLastClickTime > 100) {
            mLastClickTime = SystemClock.elapsedRealtime();
            return false;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        // 중복 발생o
        return true;
    }

    @Override
    public boolean isDuplicate2() {
        // 중복 발생x
        if (SystemClock.elapsedRealtime() - mLastClickTime2 > 100) {
            mLastClickTime2 = SystemClock.elapsedRealtime();
            return false;
        }
        mLastClickTime2 = SystemClock.elapsedRealtime();
        // 중복 발생o
        return true;
    }

    @Override
    public void setYearMonth(int year, int month) {
        mYear = year;
        mMonth = month;
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
    public void setAdapterModel(CalendarAdapterContract.Model adapterModel) {
        this.mAdapterModel = adapterModel;
    }

    @Override
    public void setAdapterView(CalendarAdapterContract.View adapterView) {
        this.mAdapterView = adapterView;
    }
}
