package com.skts.ourmemory.presenter;

import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.common.GlobalApplication;
import com.skts.ourmemory.contract.MainContract;
import com.skts.ourmemory.model.friend.FriendPostResult;
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

    // RxJava
    private CompositeDisposable mCompositeDisposable;
    private MySharedPreferences mMySharedPreferences;

    // Thread
    private PollingThread mPollingThread;
    private boolean threadFlag;
    private long mThreadCount;               // 스레드 카운트

    // Data
    private RoomPostResult mRoomPostResult;
    private SchedulePostResult mSchedulePostResult;
    private FriendPostResult mFriendPostResult;

    public MainPresenter() {
        GlobalApplication globalApplication = new GlobalApplication();
        globalApplication.startThread();
        DebugLog.i(TAG, "전역 스레드 시작");

        mModel = new MainModel(this);
    }

    public RoomPostResult getRoomPostResult() {
        return mRoomPostResult;
    }

    public SchedulePostResult getSchedulePostResult() {
        return mSchedulePostResult;
    }

    public FriendPostResult getFriendPostResult() {
        return mFriendPostResult;
    }

    @Override
    public void setView(MainContract.View view) {
        mView = view;
        mMySharedPreferences = MySharedPreferences.getInstance(mView.getAppContext());

        // 폴링 데이터
        startPollingData();
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
    public int checkFriendRequestCount() {
        return mMySharedPreferences.getIntExtra(Const.FRIEND_REQUEST_COUNT);
    }

    @Override
    public void startPollingData() {
        threadFlag = true;
        mPollingThread = new PollingThread();
        mPollingThread.start();
    }

    @Override
    public void getPollingData() {
        mCompositeDisposable = new CompositeDisposable();
        int userId = mMySharedPreferences.getIntExtra(Const.USER_ID);

        mModel.getRoomListData(userId, mCompositeDisposable);       // 방 목록 가져오기
        mModel.getScheduleListData(userId, mCompositeDisposable);   // 일정 목록 가져오기
        mModel.getFriendListData(userId, mCompositeDisposable);     // 친구 목록 가져오기
    }

    @Override
    public void getRoomListResult(RoomPostResult roomPostResult) {
        mRoomPostResult = roomPostResult;
        mView.showRoomData();
    }

    @Override
    public void getScheduleListResult(SchedulePostResult schedulePostResult) {
        mSchedulePostResult = schedulePostResult;
    }

    @Override
    public void getFriendListResult(FriendPostResult friendPostResult) {
        mFriendPostResult = friendPostResult;
    }

    private class PollingThread extends Thread {
        public PollingThread() {
            getPollingData();           // 폴링 데이터 받아오기
        }

        @Override
        public void run() {
            long POLLING_TIME = 300;

            while (threadFlag) {
                mThreadCount++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mThreadCount % POLLING_TIME == 0) {
                    mThreadCount = 0;
                    getPollingData();           // 폴링 데이터 받아오기
                }
            }
        }
    }
}
