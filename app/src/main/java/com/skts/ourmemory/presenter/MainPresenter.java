package com.skts.ourmemory.presenter;

import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.common.GlobalApplication;
import com.skts.ourmemory.common.ServerConst;
import com.skts.ourmemory.contract.MainContract;
import com.skts.ourmemory.model.BasicResponsePostResult;
import com.skts.ourmemory.model.main.MainModel;
import com.skts.ourmemory.model.room.RoomPostResult;
import com.skts.ourmemory.model.room.RoomResponseValue;
import com.skts.ourmemory.model.schedule.SchedulePostResult;
import com.skts.ourmemory.model.user.MyPagePostResult;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.util.MySharedPreferences;

import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class MainPresenter implements MainContract.Presenter {
    private final String TAG = MainPresenter.class.getSimpleName();

    private final MainContract.Model mModel;
    private MainContract.View mView;
    private MySharedPreferences mMySharedPreferences;
    private CompositeDisposable mCompositeDisposable;

    private long mBackPressTime = 0;

    // Thread
    private PollingThread mPollingThread;
    private boolean threadFlag;
    private long mThreadCount;               // 스레드 카운트

    // Data
    private List<RoomResponseValue> mRoomResponseValue;
    private SchedulePostResult mSchedulePostResult;
    private MyPagePostResult mMyPagePostResult;

    public MainPresenter() {
        GlobalApplication globalApplication = new GlobalApplication();
        globalApplication.startThread();
        DebugLog.i(TAG, "전역 스레드 시작");

        mModel = new MainModel(this);
    }

    public List<RoomResponseValue> getRoomResponseResult() {
        return mRoomResponseValue;
    }

    public SchedulePostResult getSchedulePostResult() {
        return mSchedulePostResult;
    }

    public MyPagePostResult getMyPagePostResult() {
        return mMyPagePostResult;
    }

    @Override
    public void setView(MainContract.View view) {
        mView = view;
        mMySharedPreferences = MySharedPreferences.getInstance(mView.getAppContext());

        // 폴링 데이터
        startPolling();
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
    public int checkAlarmCount() {
        return mMySharedPreferences.getIntExtra(Const.ALARM_COUNT);
    }

    @Override
    public int checkFriendRequestCount() {
        return mMySharedPreferences.getIntExtra(Const.FRIEND_REQUEST_COUNT);
    }

    @Override
    public void startPolling() {
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
    }

    @Override
    public void getMyPageData() {
        mCompositeDisposable = new CompositeDisposable();
        int userId = mMySharedPreferences.getIntExtra(Const.USER_ID);

        mModel.getUserData(userId, mCompositeDisposable);           // 사용자 정보 가져오기
    }

    @Override
    public void getRoomListResult(RoomPostResult roomPostResult) {
        mRoomResponseValue = roomPostResult.getResponseValueList();
        mView.showRoomData();
    }

    public void addRoomList(List<RoomResponseValue> responseValues) {
        for (RoomResponseValue roomResponseValue : responseValues) {
            mRoomResponseValue.add(roomResponseValue);
        }
    }

    @Override
    public void getScheduleListResult(SchedulePostResult schedulePostResult) {
        mSchedulePostResult = schedulePostResult;
        mView.showScheduleData();
    }

    @Override
    public void getMyPageResult(MyPagePostResult myPagePostResult) {
        mMyPagePostResult = myPagePostResult;
        mView.showMyPageData();
    }

    private class PollingThread extends Thread {
        public PollingThread() {
            getPollingData();           // 폴링 데이터 받아오기
            getMyPageData();            // 한 번만
        }

        @Override
        public void run() {
            final long POLLING_TIME = 300;

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

    @Override
    public boolean exitApp() {
        if (System.currentTimeMillis() - mBackPressTime >= 2000) {
            mBackPressTime = System.currentTimeMillis();
            mView.showToast("뒤로 버튼을 한번 더 누르시면 종료됩니다");
            return false;
        } else return System.currentTimeMillis() - mBackPressTime < 2000;
    }

    @Override
    public void deleteRoomData(int roomId) {
        int userId = mMySharedPreferences.getIntExtra(Const.USER_ID);
        mModel.deleteRoomData(roomId, userId, mCompositeDisposable);
    }

    @Override
    public void deleteRoomDataResult(BasicResponsePostResult basicResponsePostResult) {
        if (basicResponsePostResult == null) {
            mView.showToast("방 삭제 실패. 서버 통신에 실패했습니다. 다시 시도해주세요.");
        } else if (basicResponsePostResult.getResultCode().equals(ServerConst.SUCCESS)) {
            DebugLog.i(TAG, "방 삭제 성공");
            mView.showToast("방 삭제 성공, 여기 수정해야됨");
        } else {
            mView.showToast(basicResponsePostResult.getMessage());
        }
    }
}
