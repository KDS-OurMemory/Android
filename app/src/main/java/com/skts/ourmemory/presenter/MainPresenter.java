package com.skts.ourmemory.presenter;

import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.common.GlobalApplication;
import com.skts.ourmemory.common.ServerConst;
import com.skts.ourmemory.contract.MainContract;
import com.skts.ourmemory.model.main.MainModel;
import com.skts.ourmemory.model.room.RoomPostResult;
import com.skts.ourmemory.model.schedule.SchedulePostResult;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.util.MySharedPreferences;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class MainPresenter implements MainContract.Presenter {
    private final String TAG = MainPresenter.class.getSimpleName();

    private final MainContract.Model mModel;
    private MainContract.View mView;
    private MySharedPreferences mMySharedPreferences;

    // RxJava
    private CompositeDisposable mCompositeDisposable;

    // Thread
    private PollingThread mPollingThread;
    private boolean threadFlag;

    public MainPresenter() {
        this.mModel = new MainModel(this);

        GlobalApplication globalApplication = new GlobalApplication();
        globalApplication.startThread();
        DebugLog.i(TAG, "전역 스레드 시작");
    }

    @Override
    public void setView(MainContract.View view) {
        this.mView = view;
        mMySharedPreferences = MySharedPreferences.getInstance(mView.getAppContext());
    }

    @Override
    public void releaseView() {
        if (mPollingThread != null) {
            threadFlag = false;
        }

        this.mView = null;
        this.mCompositeDisposable.dispose();
    }

    @Override
    public void getPollingData() {
        // TODO
        threadFlag = true;
        mPollingThread = new PollingThread();
        mPollingThread.start();
    }

    @Override
    public void getRoomListResult(RoomPostResult roomPostResult) {
        if (roomPostResult == null) {
            mView.showToast("방 목록 조회 실패. 서버 통신에 실패했습니다. 다시 시도해주세요.");
        } else if (roomPostResult.getResultCode().equals(ServerConst.SUCCESS)) {
            DebugLog.i(TAG, "방 목록 조회 성공");
        } else {
            mView.showToast(roomPostResult.getMessage());
        }
    }

    @Override
    public void getScheduleListResult(SchedulePostResult schedulePostResult) {
        if (schedulePostResult == null) {
            mView.showToast("일정 목록 조회 실패. 서버 통신에 실패했습니다. 다시 시도해주세요.");
        } else if (schedulePostResult.getResultCode().equals(ServerConst.SUCCESS)) {
            DebugLog.i(TAG, "일정 목록 조회 성공");
            mView.showCalendarList(schedulePostResult);
        } else {
            mView.showToast(schedulePostResult.getMessage());
        }
    }

    private class PollingThread extends Thread {
        int userId;

        public PollingThread() {
            mCompositeDisposable = new CompositeDisposable();
            userId = mMySharedPreferences.getIntExtra(Const.USER_ID);
            mModel.getRoomListData(userId, mCompositeDisposable);
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
                if (count % POLLING_TIME == 0) {
                    mModel.getRoomListData(userId, mCompositeDisposable);       // 방 목록 가져오기
                    mModel.getScheduleListData(userId, mCompositeDisposable);   // 일정 목록 가져오기
                    count = 0;
                }
            }
        }
    }
}
