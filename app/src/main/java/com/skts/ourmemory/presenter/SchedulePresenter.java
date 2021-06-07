package com.skts.ourmemory.presenter;

import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.common.ServerConst;
import com.skts.ourmemory.contract.ScheduleContract;
import com.skts.ourmemory.model.schedule.ScheduleModel;
import com.skts.ourmemory.model.schedule.SchedulePostResult;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.util.MySharedPreferences;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class SchedulePresenter implements ScheduleContract.Presenter {
    private final String TAG = SchedulePresenter.class.getSimpleName();

    private final ScheduleContract.Model mModel;
    private ScheduleContract.View mView;

    // RxJava
    private CompositeDisposable mCompositeDisposable;

    // Thread
    private ScheduleThread mScheduleThread;
    private boolean threadFlag;

    // 달력
    private final int SET_CALENDAR_DATE_START_NUM = 0;
    private final int SET_INIT_START_DAY_NUM = 1;

    public List<String> mDayList;
    public Calendar mCalendar;                      // 캘린더 변수

    public final Date date;
    public final SimpleDateFormat currentYearFormat;
    public final SimpleDateFormat currentMonthFormat;
    public final SimpleDateFormat currentDayFormat;

    @Override
    public List<String> getDayList() {
        return mDayList;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public SimpleDateFormat getCurrentYearFormat() {
        return currentYearFormat;
    }

    @Override
    public SimpleDateFormat getCurrentMonthFormat() {
        return currentMonthFormat;
    }

    @Override
    public SimpleDateFormat getCurrentDayFormat() {
        return currentDayFormat;
    }

    public SchedulePresenter() {
        this.mModel = new ScheduleModel(this);

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
        if (mScheduleThread != null) {
            threadFlag = false;
        }

        this.mView = null;
        this.mCompositeDisposable.dispose();
    }

    @Override
    public void getPollingData() {
        threadFlag = true;
        mScheduleThread = new ScheduleThread();
        mScheduleThread.start();
    }

    @Override
    public void getScheduleListResult(SchedulePostResult schedulePostResult) {
        if (schedulePostResult == null) {
            mView.showToast("일정 목록 조회 실패. 서버 통신에 실패했습니다. 다시 시도해주세요.");
        } else if (schedulePostResult.getResultCode().equals(ServerConst.SUCCESS)) {
            DebugLog.i(TAG, "일정 목록 조회 성공");
        } else {
            mView.showToast(schedulePostResult.getMessage());
        }
    }

    @Override
    public void setInit() {
        mDayList = new ArrayList<>();
        mCalendar = Calendar.getInstance();

        // 이번달 1일 무슨 요일인지 판단 mCal.set(Year, Month, Day)
        mCalendar.set(Integer.parseInt(currentYearFormat.format(date)), Integer.parseInt(currentMonthFormat.format(date)) - 1, 1);

        int dayNum = mCalendar.get(Calendar.DAY_OF_WEEK);
        // 1일 - 요일 매칭 시키기 위해 공백 add
        for (int i = 1; i < dayNum; i++) {
            mDayList.add("");
        }
        setCalendarDate(mCalendar.get(Calendar.MONTH) + 1);
    }

    /**
     * 해당 월에 표시할 일 수 구함
     */
    @Override
    public void setCalendarDate(int month) {
        mCalendar.set(Calendar.MONTH, month - 1);

        for (int i = SET_CALENDAR_DATE_START_NUM; i < mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            mDayList.add("" + (i + 1));
        }
    }

    private class ScheduleThread extends Thread {
        int userId;

        public ScheduleThread() {
            mCompositeDisposable = new CompositeDisposable();
            MySharedPreferences mySharedPreferences = MySharedPreferences.getInstance(mView.getAppContext());
            userId = mySharedPreferences.getIntExtra(Const.USER_ID);
            mModel.getScheduleListData(userId, mCompositeDisposable);
        }

        @Override
        public void run() {
            int count = 0;
            int POLLING_TIME = 300;

            while (threadFlag) {
                count++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //DebugLog.e("testtt", "" + count);
                if (count % POLLING_TIME == 0) {
                    mModel.getScheduleListData(userId, mCompositeDisposable);   // 일정 목록 가져오기
                    count = 0;
                }
            }
        }
    }
}
