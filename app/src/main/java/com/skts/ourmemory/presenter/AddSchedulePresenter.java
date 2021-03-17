package com.skts.ourmemory.presenter;

import android.annotation.SuppressLint;
import android.os.SystemClock;

import com.skts.ourmemory.contract.AddScheduleContract;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddSchedulePresenter implements AddScheduleContract.Presenter {
    private final String TAG = AddSchedulePresenter.class.getSimpleName();

    private AddScheduleContract.View mView;

    private long mLastClickTime = 0;

    /*생성자*/
    public AddSchedulePresenter() {

    }

    @Override
    public void setView(AddScheduleContract.View view) {
        this.mView = view;
    }

    @Override
    public void releaseView() {
        this.mView = null;
    }

    @Override
    public boolean isDuplicate() {
        // 중복 발생x
        if (SystemClock.elapsedRealtime() - mLastClickTime > 500) {
            mLastClickTime = SystemClock.elapsedRealtime();
            return false;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        // 중복 발생o
        return true;
    }

    @Override
    public String[] initDate() {
        long now = System.currentTimeMillis();
        long startTime = now + (3600 * 1000);       // 시작 시간, 1시간을 더해준다
        long endTime = now + (7200 * 1000);         // 종료 시간, 2시간을 더해준다

        Date startDate = new Date(startTime);
        Date endDate = new Date(endTime);

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd:kk:mm:E");
        String getStartTime = simpleDateFormat.format(startDate);
        String getEndTime = simpleDateFormat.format(endDate);

        return new String[]{getStartTime, getEndTime};
    }
}
