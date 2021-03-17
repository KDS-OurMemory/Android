package com.skts.ourmemory.presenter;

import android.os.SystemClock;

import com.skts.ourmemory.adapter.GridAdapter;
import com.skts.ourmemory.contract.ScheduleContract;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SchedulePresenter implements ScheduleContract.Presenter {
    private final String TAG = SchedulePresenter.class.getSimpleName();

    private ScheduleContract.View mView;

    private long mLastClickTime = 0;

    /*달력*/
    private final int SET_CALENDAR_DATE_START_NUM = 0;
    private final int SET_INIT_START_DAY_NUM = 1;

    public ArrayList<String> mDayList;             // 일 저장할 리스트
    public Calendar mCalendar;                     // 캘린더 변수

    public final Date date;
    public final SimpleDateFormat currentYearFormat;
    public final SimpleDateFormat currentMonthFormat;
    public final SimpleDateFormat currentDayFormat;

    public SchedulePresenter() {
        // 오늘에 날짜를 세팅 해준다
        long now = System.currentTimeMillis();
        date = new Date(now);

        // 년, 월, 일을 따로 저장
        currentYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        currentMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
        currentDayFormat = new SimpleDateFormat("dd", Locale.KOREA);
    }

    @Override
    public void setView(ScheduleContract.View view) {
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
    public void setInit() {
        mDayList = new ArrayList<>();
        mCalendar = Calendar.getInstance();

        // 이번달 1일 무슨 요일인지 판단 mCal.set(Year, Month, Day)
        mCalendar.set(Integer.parseInt(currentYearFormat.format(date)), Integer.parseInt(currentMonthFormat.format(date)) -1, 1);

        int dayNum = mCalendar.get(Calendar.DAY_OF_WEEK);
        // 1일 - 요일 매칭 시키기 위해 공백 add
        for (int i = 1; i < dayNum; i++) {
            mDayList.add("");
        }
        setCalendarDate(mCalendar.get(Calendar.MONTH) + 1);
    }
    
    /**
     * 해당 월에 표시할 일 수 구함
     * 
     * @param month
     * */
    @Override
    public void setCalendarDate(int month) {
        mCalendar.set(Calendar.MONTH, month - 1);

        for (int i = SET_CALENDAR_DATE_START_NUM; i < mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            mDayList.add("" + (i + 1));
        }
    }
}
