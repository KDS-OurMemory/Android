package com.skts.ourmemory.presenter;

import com.skts.ourmemory.adapter.FriendListAdapter;
import com.skts.ourmemory.adapter.RequestFriendListAdapter;
import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.common.ServerConst;
import com.skts.ourmemory.contract.FriendContract;
import com.skts.ourmemory.model.BasicResponsePostResult;
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

    private boolean mRequestArrowCollapsible;       // 요청 목록이 접혀져 있는지
    private RequestFriendListAdapter mRequestFriendListAdapter;
    private FriendListAdapter mFriendListAdapter;

    public FriendPresenter() {
        this.mModel = new FriendModel(this);
        this.mRequestFriendListAdapter = new RequestFriendListAdapter();
        this.mFriendListAdapter = new FriendListAdapter();
    }

    @Override
    public boolean isRequestArrowCollapsible() {
        return mRequestArrowCollapsible;
    }

    @Override
    public void setRequestArrowCollapsible(boolean requestArrowCollapsible) {
        this.mRequestArrowCollapsible = requestArrowCollapsible;
    }

    @Override
    public RequestFriendListAdapter getAdapter() {
        return mRequestFriendListAdapter;
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

        // 어댑터 설정
        mView.initAdapter(mRequestFriendListAdapter, mFriendListAdapter);
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
        ArrayList<UserDAO> requestData = new ArrayList<>();         // 친구 요청 리스트
        ArrayList<UserDAO> friendListData = new ArrayList<>();      // 친구 리스트
        int requestCount = 0;

        for (int i = 0; i < friendData.size(); i++) {
            FriendPostResult.ResponseValue responseValue = friendData.get(i);
            String status = responseValue.getStatus();
            if (status.equals(ServerConst.REQUESTED_BY)) {
                // 친구 요청 받은 상태
                UserDAO userDAO = new UserDAO(
                        responseValue.getFriendId(),
                        responseValue.getName(),
                        responseValue.getBirthday(),
                        responseValue.isSolar(),
                        responseValue.isBirthdayOpen(),
                        responseValue.getProfileImageUrl()
                );
                requestData.add(userDAO);
                requestCount++;         // 카운트 증가
            } else if (status.equals(ServerConst.FRIEND)) {
                // 친구
                UserDAO userDAO = new UserDAO(
                        responseValue.getFriendId(),
                        responseValue.getName(),
                        responseValue.getBirthday(),
                        responseValue.isSolar(),
                        responseValue.isBirthdayOpen(),
                        responseValue.getProfileImageUrl()
                );
                friendListData.add(userDAO);
            }
        }

        if (requestCount == 0) {
            // 친구 요청 목록 숨기기
            mView.hideRequestFriend();
        }

        // 친구 요청
        mView.showRequestFriendNumber(requestData.size());

        mRequestFriendListAdapter = new RequestFriendListAdapter(requestData);
        mFriendListAdapter = new FriendListAdapter(friendListData);

        mView.setRequestAdapter(mRequestFriendListAdapter, mFriendListAdapter);
    }

    @Override
    public void requestAcceptFriend(UserDAO userDAO) {
        int userId = mMySharedPreferences.getIntExtra(Const.USER_ID);
        FriendPost friendPost = new FriendPost(userId, userDAO.getUserId());

        mModel.postAcceptFriend(friendPost, mCompositeDisposable, userDAO);
    }

    @Override
    public void getAcceptFriendResult(BasicResponsePostResult basicResponsePostResult, UserDAO userDAO) {
        if (basicResponsePostResult == null) {
            mView.showToast("친구 승인 실패. 서버 통신에 실패했습니다. 다시 시도해주세요.");
        } else if (basicResponsePostResult.getResultCode().equals(ServerConst.SUCCESS)) {
            DebugLog.i(TAG, "친구 승인 성공");
            // 친구 요청 목록에서 제거, 친구 목록에 추가
            setAcceptFriend(userDAO);
        } else {
            mView.showToast(basicResponsePostResult.getMessage());
        }
    }

    @Override
    public void setAcceptFriend(UserDAO userDAO) {
        // 친구 요청 목록에서 제거
        mRequestFriendListAdapter.removeItem(userDAO.getUserId());
        int count = mRequestFriendListAdapter.getItemCount();
        mView.showRequestFriendNumber(count);

        if (count == 0) {
            mView.hideRequestFriend();
        }

        // 친구 목록 추가
        mFriendListAdapter.addItem(userDAO);
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
