package com.skts.ourmemory.presenter;

import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.common.ServerConst;
import com.skts.ourmemory.contract.CalendarAdapterContract;
import com.skts.ourmemory.contract.MyMemoryContract;
import com.skts.ourmemory.model.main.MyMemoryModel;
import com.skts.ourmemory.model.schedule.SchedulePostResult;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.util.MySharedPreferences;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class MyMemoryPresenter implements MyMemoryContract.Presenter {
    private final String TAG = MyMemoryPresenter.class.getSimpleName();

    private final MyMemoryContract.Model mModel;
    private MyMemoryContract.View mView;
    private CalendarAdapterContract.Model mAdapterModel;
    private CalendarAdapterContract.View mAdapterView;

    // RxJava
    private CompositeDisposable mCompositeDisposable;

    private MySharedPreferences mMySharedPreferences;

    private boolean mFragmentHidden;        // 프래그먼트 visible 여부
    private int mThreadCount;               // 스레드 카운트

    // Thread
    private PollingThread mPollingThread;
    private boolean threadFlag;

    public MyMemoryPresenter() {
        mModel = new MyMemoryModel(this);
    }

    @Override
    public void setView(MyMemoryContract.View view) {
        mView = view;
        this.mMySharedPreferences = MySharedPreferences.getInstance(mView.getAppContext());

        // 폴링 데이터
        getPollingData();
    }

    @Override
    public void releaseView() {
        if (mPollingThread != null) {
            threadFlag = false;
        }

        mView = null;
        this.mCompositeDisposable.dispose();
    }

    @Override
    public void setAdapterModel(CalendarAdapterContract.Model adapterModel) {
        this.mAdapterModel = adapterModel;
    }

    @Override
    public void setAdapterView(CalendarAdapterContract.View adapterView) {
        this.mAdapterView = adapterView;
    }

    @Override
    public void getPollingData() {
        threadFlag = true;
        mPollingThread = new PollingThread();
        mPollingThread.start();
    }

    @Override
    public void getData(boolean hidden) {
        mFragmentHidden = hidden;
        if (!hidden) {
            mThreadCount = 0;               // 초기화
            getScheduleData();              // 일정 데이터
        }
    }

    @Override
    public void getScheduleData() {
        mCompositeDisposable = new CompositeDisposable();
        int userId = mMySharedPreferences.getIntExtra(Const.USER_ID);

        mModel.getScheduleListData(userId, mCompositeDisposable);   // 일정 목록 가져오기
    }

    @Override
    public void getScheduleListResult(SchedulePostResult schedulePostResult) {
        if (schedulePostResult == null) {
            mView.showToast("일정 목록 조회 실패. 서버 통신에 실패했습니다. 다시 시도해주세요.");
        } else if (schedulePostResult.getResultCode().equals(ServerConst.SUCCESS)) {
            DebugLog.i(TAG, "일정 목록 조회 성공");
            getCalendarListData(schedulePostResult);
        } else {
            mView.showToast(schedulePostResult.getMessage());
        }
    }

    @Override
    public void getCalendarListData(SchedulePostResult schedulePostResult) {
        mAdapterModel.addItems(schedulePostResult.getResponse());
        mAdapterView.notifyAdapter();

        /*List<SchedulePostResult.ResponseValue> responseValueList = schedulePostResult.getResponse();
        // 오늘에 날짜를 세팅 해준다
        long now = System.currentTimeMillis();
        Date todayDate = new Date(now);

        ArrayList<String> todayList = new ArrayList<>();        // 오늘 일정
        ArrayList<String> nextList = new ArrayList<>();         // 다음 일정

        for (int i = 0; i < responseValueList.size(); i++) {
            String startDate = responseValueList.get(i).getStartDate();
            String endDate = responseValueList.get(i).getEndDate();
            try {
                Date date1 = simpleDateFormat.parse(startDate);     // 시작 날짜
                Date date2 = simpleDateFormat.parse(endDate);       // 종료 날짜
                int compare1 = date1.compareTo(todayDate);
                int compare2 = date2.compareTo(todayDate);
                if (compare1 <= 0 && compare2 >= 0) {               // 시작날 비교값이 작거나 같고, 종료날 비교값이 크거나 같을 경우
                    // 오늘 일정
                    todayList.add(responseValueList.get(i).getName());
                } else if (compare1 > 0) {
                    // 다음 일정
                    nextList.add(responseValueList.get(i).getName());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // 오늘 일정 표시
        mView.showCalendarList(todayList, nextList);*/
    }

    private class PollingThread extends Thread {
        public PollingThread() {
            getScheduleData();           // 일정 리스트 받아오기
        }

        @Override
        public void run() {
            int POLLING_TIME = 300;

            while (threadFlag) {
                mThreadCount++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mThreadCount % POLLING_TIME == 0) {
                    mThreadCount = 0;
                    if (!mFragmentHidden) {                 // 화면이 보여져 있으면
                        getScheduleData();           // 일정 리스트 받아오기
                    }
                }
            }
        }
    }
}
