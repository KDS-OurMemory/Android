package com.skts.ourmemory.presenter;

import com.skts.ourmemory.adapter.RequestFriendListAdapter;
import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.common.ServerConst;
import com.skts.ourmemory.contract.FriendContract;
import com.skts.ourmemory.model.friend.AcceptFriendPostResult;
import com.skts.ourmemory.model.friend.FriendModel;
import com.skts.ourmemory.model.friend.FriendPost;
import com.skts.ourmemory.model.friend.FriendPostResult;
import com.skts.ourmemory.model.user.UserDAO;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.util.MySharedPreferences;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class FriendPresenter implements FriendContract.Presenter {
    private final String TAG = FriendPresenter.class.getSimpleName();

    private final FriendContract.Model mModel;
    private FriendContract.View mView;
    private MySharedPreferences mMySharedPreferences;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    // Thread
    private PollingThread mPollingThread;
    private boolean threadFlag;
    private long mThreadCount;              // 스레드 카운트

    private boolean mRequestArrowExpandable;        // 요청 목록이 접혀져 있는지

    public FriendPresenter() {
        this.mModel = new FriendModel(this);
    }

    @Override
    public boolean isRequestArrowExpandable() {
        return mRequestArrowExpandable;
    }

    @Override
    public void setRequestArrowExpandable(boolean requestArrowExpandable) {
        this.mRequestArrowExpandable = requestArrowExpandable;
    }

    @Override
    public void setView(FriendContract.View view) {
        this.mView = view;
        mMySharedPreferences = MySharedPreferences.getInstance(mView.getAppContext());

        // 폴링 데이터
        startFriendPolling();

        initSet();
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
    public void initSet() {
        // 친구 뱃지 카운트 없애기
        mMySharedPreferences.putIntExtra(Const.FRIEND_REQUEST_COUNT, 0);
    }

    @Override
    public void startFriendPolling() {
        threadFlag = true;
        mPollingThread = new PollingThread();
        mPollingThread.start();
    }

    @Override
    public void getPollingData() {
        mCompositeDisposable = new CompositeDisposable();
        int userId = mMySharedPreferences.getIntExtra(Const.USER_ID);

        mModel.getFriendListData(userId, mCompositeDisposable);     // 친구 목록 가져오기
    }

    @Override
    public void getFriendListResult(FriendPostResult friendPostResult) {
        if (friendPostResult == null) {
            mView.showToast("친구 목록 조회 실패. 서버 통신에 실패했습니다. 다시 시도해주세요.");
        } else if (friendPostResult.getResultCode().equals(ServerConst.SUCCESS)) {
            DebugLog.i(TAG, "친구 목록 조회 성공");
            setFriendData(friendPostResult);
        } else {
            mView.showToast(friendPostResult.getMessage());
        }
    }

    @Override
    public void setFriendData(FriendPostResult friendPostResult) {
        List<FriendPostResult.ResponseValue> friendData = friendPostResult.getResponse();
        if (friendData.isEmpty()) {
            // 친구 목록 없음
            mView.showNoFriend(true);
            return;
        }
        mView.showNoFriend(false);      // 친구 목록 없음 표시 숨기기
        ArrayList<UserDAO> requestData = new ArrayList<>();

        for (int i = 0; i < friendData.size(); i++) {
            FriendPostResult.ResponseValue responseValue = friendData.get(i);
            if (responseValue.getStatus().equals(ServerConst.REQUESTED_BY)) {
                // 친구 요청 받은 상태
                UserDAO userDAO = new UserDAO(
                        responseValue.getFriendId(),
                        responseValue.getName(),
                        responseValue.getBirthday(),
                        responseValue.isSolar(),
                        responseValue.isBirthdayOpen()
                );
                requestData.add(userDAO);
            }
        }

        RequestFriendListAdapter adapter = new RequestFriendListAdapter(requestData);
        mView.setRequestAdapter(adapter);
    }

    @Override
    public void requestAcceptFriend(int friendId) {
        int userId = mMySharedPreferences.getIntExtra(Const.USER_ID);
        FriendPost friendPost = new FriendPost(userId, friendId);

        mModel.postAcceptFriend(friendPost, mCompositeDisposable);
    }

    @Override
    public void getAcceptFriendResult(AcceptFriendPostResult acceptFriendPostResult) {
        if (acceptFriendPostResult == null) {
            mView.showToast("친구 승인 실패. 서버 통신에 실패했습니다. 다시 시도해주세요.");
        } else if (acceptFriendPostResult.getResultCode().equals(ServerConst.SUCCESS)) {
            DebugLog.i(TAG, "친구 승인 조회 성공");
            // TODO 친구 요청 목록에서 제거, 친구 목록에 추가
        } else {
            mView.showToast(acceptFriendPostResult.getMessage());
        }
    }

    private class PollingThread extends Thread {
        public PollingThread() {
            getPollingData();           // 폴링 데이터 받아오기
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
}
